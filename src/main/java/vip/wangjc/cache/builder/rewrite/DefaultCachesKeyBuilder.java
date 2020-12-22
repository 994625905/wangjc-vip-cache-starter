package vip.wangjc.cache.builder.rewrite;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;
import vip.wangjc.cache.builder.AbstractCachesKeyBuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认的缓存器的key生成器
 * @author wangjc
 * @title: ParamCachesKeyBuilder
 * @projectName wangjc-vip-cache-starter
 * @date 2020/12/21 - 17:11
 */
public class DefaultCachesKeyBuilder extends AbstractCachesKeyBuilder {

    private static final Logger logger = LoggerFactory.getLogger(DefaultCachesKeyBuilder.class);
    /**
     * 记录参数名解析器
     */
    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    /**
     * 表达式解析器
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();

    @Override
    public String buildKey(MethodInvocation invocation, String[] definitionKeys) {
        StringBuffer sb = new StringBuffer();

        /** 全类名+方法名 */
        Method method = invocation.getMethod();
        sb.append(method.getDeclaringClass().getName());
        sb.append(".");
        sb.append(method.getName());
        sb.append("#");

        /** 参数定义 */
        if( definitionKeys.length > 1 || !"".equals(definitionKeys[0]) ){
            sb.append(this.getElDefinitionKey(definitionKeys,method,invocation.getArguments()));
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
            logger.error("caches key analysis faired,please check the reason! keys:[{}]",definitionKeys);
            throw e;
        }
    }

}
