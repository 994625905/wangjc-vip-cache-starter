package vip.wangjc.cache.builder.rewrite;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import vip.wangjc.cache.builder.AbstractLimitKeyBuilder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * IP限流的key生成器
 * @author wangjc
 * @title: IPLimitKeyBuilderServiceImpl
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 17:05
 */
public class IPLimitKeyBuilder extends AbstractLimitKeyBuilder {

    private static final String UNKNOWN = "unknown";

    @Override
    public String buildKey(MethodInvocation invocation,String jvmProcessId, String localMac, String[] definitionKeys) {

        StringBuffer sb = new StringBuffer();

        /** 本机JVM进程，本地网卡 */
        sb.append(",JVMProcessId[");
        sb.append(jvmProcessId);
        sb.append("],LocalMac[");
        sb.append(localMac);

        /** 限流类型 */
        sb.append("],limitType[IP limiter]@");

        /** 全类名+方法名 */
        Method method = invocation.getMethod();
        sb.append(method.getDeclaringClass().getName());
        sb.append(".");
        sb.append(method.getName());
        sb.append("&");

        /** ip地址 */
        sb.append(this.getIpAddr());

        return sb.toString();
    }


    /**
     * 获取 IP地址
     * 使用 Nginx等反向代理软件， 则不能通过 request.getRemoteAddr()获取 IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，
     * X-Forwarded-For中第一个非 unknown的有效IP字符串，则为真实IP地址
     */
    private String getIpAddr() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}
