# wangjc-vip-cache-starter
涵盖Redis五种数据类型的所有操作（增删改查），栈的压弹，队列操作；基于缓存的声明式限流器

# 一、简介

- 为了更好的利用缓存，wangjc-vip-cache-starter致力于将平时使用的缓存组件，进行单独的设计封装，然后以service的方式将bean提供出来（eg：RedisService）。提供声明式限流器（@Limiter），对指定的方法函数进行限流（单位时间内的访问次数），可供选择的限流方式有：请求IP、请求参数、默认（无条件限流）。提供声明式的缓存器和缓存刷新机制（@Caches，@Flush	）…………

- PS:目前可供选择的缓存客户端只有redis。后期考虑集成其他的，下面以redis为例介绍

------------

# 二、功能说明

## 1、提供自动注入的RedisService服务

- 并且提供redis五种数据类型（string，hash，set，list，zset）的所有增删改查，压栈，弹栈……

  ```java
    public interface RedisService {
        /**
         * 获取专属的RedisTemplate（单独构造）
         * @return
         */
        CacheRedisTemplate getCacheRedisTemplate();
        //================================================String================================================
        /**
         * 设置值，
         * @param key
         * @param value：实体的话需要序列化
         */
        void set(String key,Object value);
        /**
         * 设置值，过期时间，
         * @param key
         * @param value
         * @param expire：精确到秒
         */
        void set(String key, Object value, long expire);
        …………………………以下的hash，list，set……皆省略
        //================================================公共方法================================================
        /**
         * 根据key删除
         * @param key
         * @return
         */
        Boolean del(String key);
        /**
         * 根据key批量删除
         * @param key
         */
        void del(String[] key);
        …………………………    省略
    }
  ```

  ## 2、提供声明式缓存器和刷新缓存

  ### 缓存器

- 作用于方法，根据制定的key生成器，来优先读取缓存，缓存取不到再读方法的返回值，为了避免雪崩缓存，默认的过期时间是当前线程随机数【12h-24h】。key生成器和expire生成器都可以自定义，各自继承对应的抽象类即可。默认的keyBuilder是：方法全类名#EL表达式的参数名

  ```java
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Caches {
        /**
         * 缓存的客户端类型，当前只支持redis
         * @return
         */
        CacheClientType clientType() default CacheClientType.REDIS_TEMPLATE;
        /**
         * 缓存器的key，支持EL表达式
         * @return
         */
        String[] keys() default {};
        /**
         * 缓存器的key构建器
         * @return
         */
        Class<? extends AbstractCachesKeyBuilder> keyBuilder() default DefaultCachesKeyBuilder.class;
        /**
         * 过期时间的生成器，默认为随机
         * @return
         */
        Class<? extends AbstractCachesExpireBuilder> expireBuilder() default RandomCachesExpireBuilder.class;
    }
  ```

  ### 刷新缓存

- 作用于方法，根据制定的key生成器，优先执行方法的修改逻辑，然后在返回新值刷新缓存。由于方法名的不同，如果使用刷新缓存注解的话，就必须自定义key的构造规则了，继承AbstractCachesKeyBuilder重写buildKey（）方法即可。

  ```java
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Flush {
        /**
         * 限流的客户端类型，当前只支持redis
         * @return
         */
        CacheClientType clientType() default CacheClientType.REDIS_TEMPLATE;
        /**
         * 缓存器的key，支持EL表达式
         * @return
         */
        String[] keys() default {};
        /**
         * 缓存器的key构建器
         * @return
         */
        Class<? extends AbstractCachesKeyBuilder> keyBuilder() default DefaultCachesKeyBuilder.class;
    }
  ```

  ## 3、提供声明式限流器

- 作用于方法，限流类型分为三种：请求IP、请求参数、默认（无条件限流），前两者都是针对特定的规则限流（恶意刷单，爬虫接口……），后者默认针对当前方法做全局限流，表示单位时间内该方法只能被允许访问的次数，以缓存组件来充当计数器，默认为redis（LUA脚本计数）。提供限流成功后的执行策略，这个一般需要自定义执行策略，继承AbstractLimitStrategy重写strategy（）方法即可（eg：一个错误码提示，或者是一个错误页面提示），默认是在控制台打印日志。

  ```java
    @Target({ElementType.METHOD})  
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Limiter {
        /**
         * 限流类型，默认为普通方法限流
         * @return
         */
        LimitType type() default LimitType.NORMAL;
        /**
         * 限流成功后的策略，
         * @return
         */
        Class<? extends AbstractLimitStrategy> strategy() default DefaultLimitStrategy.class;
        /**
         * 限流的客户端类型，当前只支持redis
         * @return
         */
        CacheClientType clientType() default CacheClientType.REDIS_TEMPLATE;
        /**
         * 限流的key值，跟type有关（只在LimitType.PARAM有效）
         * @return
         */
        String[] key() default {};
        /**
         * 时间范围，单位秒
         * @return
         */
        int period() default 10;
        /**
         * 限制访问次数，（默认10秒钟之内只能访问10次）
         * @return
         */
        int count() default 10;
    }
  ```

------------

# 三、代码结构说明

- 包结构前缀统一为：vip.wangjc.cache。然后下面根据职责细分：注解，aop扫描，自动配置，key构建器，实体，异常，执行器，失败策略，工具……其中，key构建器，执行器，后置策略，都支持自定义。自定义方式为重写对应的抽象类，然后在注解中指定。

- service包是对外提供的缓存服务，当前只支持redis，后续考虑集成其他的组件。

  ## 结构如下：

  ```java
  ├─src
  	│  ├─main
  	│  │  ├─java
  	│  │  │  └─vip
  	│  │  │      └─wangjc	
  	│  │  │          └─cache
  	│  │  │              │  CachesTemplate.java （缓存的模板方法提供）
  	│  │  │              │  LimitTemplate.java （限流器的模板方法提供）
  	│  │  │              │
  	│  │  │              ├─annotation
  	│  │  │              │      Caches.java （注解：缓存器）
  	│  │  │              │      Flush.java （注解：刷新缓存）
  	│  │  │              │      Limiter.java （注解：限流器）
  	│  │  │              │
  	│  │  │              ├─aop
  	│  │  │              │      CachesAnnotationAdvisor.java （缓存器的aop通知）
  	│  │  │              │      CachesInterceptor.java （缓存器的切面处理）
  	│  │  │              │      FlushAnnotationAdvisor.java （刷新缓存的aop通知）
  	│  │  │              │      FlushInterceptor.java （刷新缓存的切面处理）
  	│  │  │              │      LimitAnnotationAdvisor.java （限流器的aop通知）
  	│  │  │              │      LimitInterceptor.java （限流器的切面处理）
  	│  │  │              │
  	│  │  │              ├─auto
  	│  │  │              │  └─configure
  	│  │  │              │          CachesAutoConfiguration.java （缓存器的自动加载配置）
  	│  │  │              │          LimitAutoConfiguration.java （限流器的自动加载配置）
  	│  │  │              │          RedisAutoConfiguration.java （Redis的自动加载配置）
  	│  │  │              │
  	│  │  │              ├─builder
  	│  │  │              │  │  AbstractCachesExpireBuilder.java （抽象类：缓存器的过期时间生成器）
  	│  │  │              │  │  AbstractCachesKeyBuilder.java （抽象类：缓存器的key生成器）
  	│  │  │              │  │  AbstractLimitKeyBuilder.java （抽象类：限流器的key生成器）
  	│  │  │              │  │  LimitKeyBuilderFactory.java （限流器的key生成器工厂）
  	│  │  │              │  │
  	│  │  │              │  └─rewrite
  	│  │  │              │          DefaultCachesKeyBuilder.java （默认的缓存器key生成器）
  	│  │  │              │          IPLimitKeyBuilder.java （IP限流key生成器）
  	│  │  │              │          NormalLimitKeyBuilder.java （默认限流key生成器）
  	│  │  │              │          ParamLimitKeyBuilder.java （请求参数限流key生成器）
  	│  │  │              │          RandomCachesExpireBuilder.java （默认的缓存器过期时间生成器：线程随机数）
  	│  │  │              │
    	│  │  │              ├─client
    	│  │  │              │  └─redis
    	│  │  │              │          CacheRedisTemplate.java （构造缓存专属的RedisTemplate模板）
    	│  │  │              │
  	│  │  │              ├─entity
  	│  │  │              │      CacheClientType.java （枚举：缓存的客户端类型：redis，memcache，……）
  	│  │  │              │      LimitEntity.java （限流的实体）
  	│  │  │              │      LimitType.java （枚举：限流的类型）
  	│  │  │              │
  	│  │  │              ├─execute
  	│  │  │              │  │  AbstractCachesExecute.java （抽象类：缓存器执行器）
  	│  │  │              │  │  AbstractLimitExecute.java （抽象类：限流器执行器）
  	│  │  │              │  │  CachesExecuteFactory.java （缓存器执行器生成工厂）
  	│  │  │              │  │  LimitExecuteFactory.java （限流器执行器生成工厂）
  	│  │  │              │  │
  	│  │  │              │  └─rewrite
  	│  │  │              │          RedisCachesExecute.java (redis缓存器)
  	│  │  │              │          RedisLimitExecute.java （redis限流器）
  	│  │  │              │
  	│  │  │              ├─service
  	│  │  │              │  │  RedisService.java （接口：redis服务）
  	│  │  │              │  │
  	│  │  │              │  └─impl
  	│  │  │              │          RedisServiceImpl.java （redis服务实现类）
  	│  │  │              │
  	│  │  │              ├─strategy
  	│  │  │              │  │  AbstractLimitStrategy.java （抽象类：限流的后置策略）
  	│  │  │              │  │
  	│  │  │              │  └─rewrite
  	│  │  │              │          DefaultLimitStrategy.java （默认限流后置策略）
  	│  │  │              │
  	│  │  │              └─util
  	│  │  │                      CacheUtil.java （工具类）
  	│  │  │
  	│  │  └─resources
  	│  │      └─META-INF
  	│  │              spring.factories （springboot的自动化配置支持）
  ```

  UML图如下：
  ![wangjc-vip-cache-starter类图](http://www.wangjc.vip/group1/M00/00/01/rBAAD1_h4CCAXLzcAATOX4ifB0c788.png "wangjc-vip-cache-starter类图")

------------

# 四、使用方式

## 1、添加依赖

- 可实时查看maven中央仓库release的最新版本

  ```xml
  		<dependency>
    			<groupId>org.springframework.boot</groupId>
    			<artifactId>spring-boot-starter-data-redis</artifactId>
    			<optional>true</optional>
    		</dependency>
  		
    		<dependency>
    			<groupId>vip.wangjc</groupId>
    			<artifactId>wangjc-vip-cache-starter</artifactId>
    			<version>查看最新版本</version>
    		</dependency>
  ```

  

  ## 2、配置文件
  ```properties
    spring.redis.host=127.0.0.1
    #需要就放开注释
    #spring.redis.password=697295
    spring.redis.timeout=5000
    spring.redis.port=6379
    #我默认使用4号槽
    #spring.redis.database=3
  ```

  ## 3、直接使用

  注入不确定存在的bean，可以带个参数required = false，如下所示，不一定引入了redis

  ```java
        @Autowired(required = false)
        private RedisService redisService;
        @Autowired
        private TestService testService;
        @RequestMapping(value = "set")
        public Object set(String value){
            redisService.set("wangjc",value);
            return true;
        }
        @RequestMapping(value = "cache")
        @Limiter
        public Object cache(String key){
            String value = testService.value(key);
            return value;
        }
        @RequestMapping(value = "get")
        @Limiter(strategy = defineStrategy.class)
        public Object get(String key){
            String value = this.redisService.get(key, String.class);
            return value;
        }
  ```

  ```java
    @Component
    public class TestService {
        /**
         * 测试缓存器
         * @return
         */
        @Caches(keys = {"#key"})
        public String value(String key){
            String value = "我想我很适合，当一个歌颂者……";
            System.out.println(value);
            return value;
        }
    }
  ```

- 如果想测试Flush的话，为了保证与Caches注解的key一致性，需要自定义keyBuilder，我就不做演示了。
  详情代码信息请前往开源平台，然后顺手给个star呗！
