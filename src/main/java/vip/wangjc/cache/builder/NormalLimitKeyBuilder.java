package vip.wangjc.cache.builder;

import org.aopalliance.intercept.MethodInvocation;
import vip.wangjc.cache.builder.abstracts.AbstractLimitKeyBuilder;

import java.lang.reflect.Method;

/**
 * 普通限流的key生成器
 * @author wangjc
 * @title: NormalLimitKeyBuilderServiceImpl
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 17:09
 */
public class NormalLimitKeyBuilder extends AbstractLimitKeyBuilder {

    @Override
    public String buildKey(MethodInvocation invocation,String jvmProcessId, String localMac, String[] definitionKeys) {

        StringBuffer sb = new StringBuffer();

        /** 本机JVM进程，本地网卡 */
        sb.append(",JVMProcessId[");
        sb.append(jvmProcessId);
        sb.append("],LocalMac[");
        sb.append(localMac);

        /** 限流类型 */
        sb.append("],limitType[normal limiter]@");

        /** 全类名+方法名 */
        Method method = invocation.getMethod();
        sb.append(method.getDeclaringClass().getName());
        sb.append(".");
        sb.append(method.getName());
        return sb.toString();
    }
}
