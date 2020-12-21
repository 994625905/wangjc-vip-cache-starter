package vip.wangjc.cache.auto.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import vip.wangjc.cache.limit.LimitTemplate;
import vip.wangjc.cache.limit.aop.LimitAnnotationAdvisor;
import vip.wangjc.cache.limit.aop.LimitInterceptor;
import vip.wangjc.cache.limit.execute.LimitExecuteFactory;

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
         * @param redisTemplate
         * @return
         */
        @Bean
        @Order(299)
        public LimitExecuteFactory limitExecuteFactory(RedisTemplate redisTemplate){
            return new LimitExecuteFactory(redisTemplate);
        }
    }

    /**
     * 注入限流器的模板
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public LimitTemplate limitTemplate(){
        return new LimitTemplate();
    }

    /**
     * 注入限流器的aop切面处理
     * @param limitTemplate
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public LimitInterceptor limitInterceptor(LimitTemplate limitTemplate){
        return new LimitInterceptor(limitTemplate);
    }

    /**
     * 注入限流器的aop通知
     * @param limitInterceptor
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public LimitAnnotationAdvisor limitAnnotationAdvisor(LimitInterceptor limitInterceptor){
        return new LimitAnnotationAdvisor(limitInterceptor,399);
    }

}
