package vip.wangjc.cache.limit.builder.rewrite;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;
import vip.wangjc.cache.limit.builder.AbstractLimitKeyBuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 参数限流的key生成器
 * @author wangjc
 * @title: ParamLimitKeyBuilderServiceImpl
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 17:11
 */
public class ParamLimitKeyBuilder extends AbstractLimitKeyBuilder {

    /**
     * 记录参数名解析器
     */
    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    /**
     * 表达式解析器
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();

    @Override
    public String buildKey(MethodInvocation invocation,String jvmProcessId, String localMac, String[] definitionKeys) {
        StringBuffer sb = new StringBuffer();

        /** 本机JVM进程，本地网卡 */
        sb.append(",JVMProcessId[");
        sb.append(jvmProcessId);
        sb.append("],LocalMac[");
        sb.append(localMac);

        /** 限流类型 */
        sb.append("],limitType[param limiter]@");

        /** 全类名+方法名 */
        Method method = invocation.getMethod();
        sb.append(method.getDeclaringClass().getName());
        sb.append(".");
        sb.append(method.getName());
        sb.append("#");

        /** 参数定义 */
        if( definitionKeys.length > 1 || !"".equals(definitionKeys[0]) ){
            sb.append(getElDefinitionKey(definitionKeys,method,invocation.getArguments()));
        }
        return sb.toString();
    }


    /**
     * 获取EL表达式值
     * @param definitionKeys
     * @param method
     * @param parameterValues
     * @return
     */
    private String getElDefinitionKey(String[] definitionKeys, Method method, Object[] parameterValues){
        try {
            EvaluationContext context = new MethodBasedEvaluationContext(null,method,parameterValues,NAME_DISCOVERER);

            /** 格式化获取值 */
            List<String> list = new ArrayList<>(definitionKeys.length);
            for(String key:definitionKeys){
                if(key != null && !key.isEmpty()){
                    list.add(PARSER.parseExpression(key).getValue(context).toString());
                }
            }
            return StringUtils.collectionToDelimitedString(list,".","","");
        }catch (Exception e){
            return StringUtils.collectionToDelimitedString(Arrays.asList(definitionKeys),".","","");
        }
    }

}
