package vip.wangjc.cache.aop;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import vip.wangjc.cache.annotation.Flush;

/**
 * 缓存器的aop通知
 * @author wangjc
 * @title: CachesAnnotationAdvisor
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 19:29
 */
public class FlushAnnotationAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {


    private final Advice advice;

    private final Pointcut pointcut = AnnotationMatchingPointcut.forMethodAnnotation(Flush.class);

    public FlushAnnotationAdvisor(FlushInterceptor interceptor, int order){
        this.advice = interceptor;
        setOrder(order);
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if(this.advice instanceof BeanFactoryAware){
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }
}
