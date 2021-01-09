package vip.wangjc.cache.auto.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisOperations;
import vip.wangjc.cache.LimitTemplate;
import vip.wangjc.cache.aop.LimitAnnotationAdvisor;
import vip.wangjc.cache.aop.LimitInterceptor;
import vip.wangjc.cache.builder.factory.LimitKeyBuilderFactory;
import vip.wangjc.cache.client.redis.CacheRedisTemplate;
import vip.wangjc.cache.execute.factory.LimitExecuteFactory;

/**
 * 限流的自动配置
 * @author wangjc
 * @title: LimitAutoConfiguration
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 19:13
 */
@Configuration
public class LimitAutoConfiguration {

    /**
     * 配置限流执行器的构建工厂配置
     */
    @Configuration
    @ConditionalOnClass(RedisOperations.class)
    static class limitExecuteFactoryConfiguration{
        /**
         * 注入限流执行器的构建工厂
         * @param cacheRedisTemplate
         * @return
         */
        @Bean
        public LimitExecuteFactory limitExecuteFactory(CacheRedisTemplate cacheRedisTemplate){
            return new LimitExecuteFactory(cacheRedisTemplate);
        }
    }

    /**
     * 注入限流key生成器的构建工厂
     * @return
     */
    @Bean
    public LimitKeyBuilderFactory limitKeyBuilderFactory(){
        return new LimitKeyBuilderFactory();
    }

    /**
     * 注入限流器的模板
     * @return
     */
    @Bean
    public LimitTemplate limitTemplate(){
        return new LimitTemplate();
    }

    /**
     * 注入限流器的aop切面处理
     * @param limitTemplate
     * @return
     */
    @Bean
    @ConditionalOnBean(LimitTemplate.class)
    public LimitInterceptor limitInterceptor(LimitTemplate limitTemplate){
        return new LimitInterceptor(limitTemplate);
    }

    /**
     * 注入限流器的aop通知
     * @param limitInterceptor
     * @return
     */
    @Bean
    @ConditionalOnBean(LimitInterceptor.class)
    public LimitAnnotationAdvisor limitAnnotationAdvisor(LimitInterceptor limitInterceptor){
        return new LimitAnnotationAdvisor(limitInterceptor,399);
    }

}
