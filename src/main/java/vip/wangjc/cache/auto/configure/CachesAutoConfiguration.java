package vip.wangjc.cache.auto.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisOperations;
import vip.wangjc.cache.CachesTemplate;
import vip.wangjc.cache.aop.CachesAnnotationAdvisor;
import vip.wangjc.cache.aop.CachesInterceptor;
import vip.wangjc.cache.aop.FlushAnnotationAdvisor;
import vip.wangjc.cache.aop.FlushInterceptor;
import vip.wangjc.cache.client.redis.CacheRedisTemplate;
import vip.wangjc.cache.execute.CachesExecuteFactory;

/**
 * @author wangjc
 * @title: CachesAutoConfiguration
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/22 - 13:54
 */
@Configuration
public class CachesAutoConfiguration {

    /**
     * 配置加载
     */
    @Configuration
    @ConditionalOnClass({RedisOperations.class})
    static class CachesExecuteFactoryConfiguration{
        /**
         * 注入缓存执行器工厂
         * @param cacheRedisTemplate
         * @return
         */
        @Bean
        public CachesExecuteFactory cachesExecuteFactory(CacheRedisTemplate cacheRedisTemplate){
            return new CachesExecuteFactory(cacheRedisTemplate);
        }
    }

    /**
     * 注入缓存器模板
     * @return
     */
    @Bean
    public CachesTemplate cachesTemplate(){
        return new CachesTemplate();
    }

    /**
     * 注入缓存器AOP切面处理
     * @param cachesTemplate
     * @return
     */
    @Bean
    @ConditionalOnBean(CachesTemplate.class)
    public CachesInterceptor cachesInterceptor(CachesTemplate cachesTemplate){
        return new CachesInterceptor(cachesTemplate);
    }

    /**
     * 注入缓存器的aop通知
     * @param cachesInterceptor
     * @return
     */
    @Bean
    @ConditionalOnBean(CachesInterceptor.class)
    public CachesAnnotationAdvisor cachesAnnotationAdvisor(CachesInterceptor cachesInterceptor){
        return new CachesAnnotationAdvisor(cachesInterceptor,299);
    }

    /**
     * 注入刷新的AOP切面处理
     * @param cachesTemplate
     * @return
     */
    @Bean
    @ConditionalOnBean(CachesTemplate.class)
    public FlushInterceptor flushInterceptor(CachesTemplate cachesTemplate){
        return new FlushInterceptor(cachesTemplate);
    }

    /**
     * 注入刷新的aop通知,刷新优先于缓存
     * @param flushInterceptor
     * @return
     */
    @Bean
    @ConditionalOnBean(FlushInterceptor.class)
    public FlushAnnotationAdvisor flushAnnotationAdvisor(FlushInterceptor flushInterceptor){
        return new FlushAnnotationAdvisor(flushInterceptor,298);
    }

}
