package com.hhly.utils.web;

import com.hhly.api.model.RenderDomain;
import com.hhly.utils.AssembleUtil;
import com.hhly.utils.ValueUtil;
import com.hhly.utils.LogUtil;
import com.hhly.utils.MoneyUtil;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.Map;
/**
 * @author pengchao
 * @create 2017-12-26
 * @desc
 */
/** 项目里的视图渲染解析器, 这里主要是为了在上下文中注入一些公用类 */
public class RenderViewResolver extends FreeMarkerViewResolver {

    /** 静态资源用到的版本号 */
    private static String version = ValueUtil.random(6);

    private static final BeansWrapper BEANS_WRAPPER = new BeansWrapperBuilder(Configuration.getVersion()).build();
    private static final TemplateHashModel STATIC_HASH_MODEL = BEANS_WRAPPER.getStaticModels();
    private static final TemplateHashModel ENUM_HASH_MODEL = BEANS_WRAPPER.getEnumModels();

    /** 一些全局的工具类 */
    private static final Class[] CLASSES = new Class[] {
            AssembleUtil.class, ValueUtil.class, MoneyUtil.class, com.hhly.utils.date.DateUtil.class, Render.class, RequestUtil.class
    };

    /** 构造器只加载一次 */
    public RenderViewResolver() {
        super();

        // Map<String, Object> context = Maps.newHashMap();
        // 使用下面这一句后, 页面上使用 ${C["...date.DateUtil"].now()}. 太长了! 使用下面的 putClass 来替代
        // context.put("C", STATIC_HASH_MODEL);
        // 使用下面这一句后, 页面上使用 ${E["...enums.OrderStatus"].Create} 获取枚举. 太长了! 使用下面的 putEnum 来替代
        // context.put("E", ENUM_HASH_MODEL);

        // 把工具类放入渲染的上下文中
        putClass(CLASSES);
    }
    /** 把「是否是线上 」和「域名」放入渲染的全局上下文中. 只加载一次 */
    public RenderViewResolver putVariable(boolean online, RenderDomain domain) {
        setAttributesMap(AssembleUtil.maps("online", online, "domain", domain));
        return this;
    }
    /** 把类放入渲染的全局上下文中. 只加载一次 */
    public RenderViewResolver putClass(Class<?>... classes) {
        if (AssembleUtil.isNotEmpty(classes)) {
            Map<String, Object> context = AssembleUtil.maps();
            for (Class<?> clazz : classes) {
                String clazzName = clazz.getName();
                try {
                    context.put(clazz.getSimpleName(), STATIC_HASH_MODEL.get(clazzName));
                } catch (TemplateModelException e) {
                    if (LogUtil.ROOT_LOG.isErrorEnabled())
                        LogUtil.ROOT_LOG.error("add class(" + clazzName + ") in Render context exception", e);
                }
            }
            setAttributesMap(context);
        }
        return this;
    }
    /**
     * <pre>
     * 把枚举放入渲染的上下文中. 只加载一次
     *
     * 假定要渲染的地方上下文中有一个 user 对象, 并且里面有 gender 这个枚举. GenderEnum 可以直接拿过来用:
     * &lt;#list GenderEnum?values as gender&gt;
     *   &lt;label>
     *     &lt;input type="radio" value="${gender.code}"&lt;#if user.gender == gender> checked="checked"&lt;/#if>>
     *     ${gender.getValue()}
     *   &lt;/label>
     * &lt;/#list&gt;
     * </pre>
     */
    public RenderViewResolver putEnum(Class<?>... enums) {
        if (AssembleUtil.isNotEmpty(enums)) {
            Map<String, Object> context = AssembleUtil.maps();
            for (Class<?> clazz : enums) {
                if (clazz.isEnum()) {
                    String clazzName = clazz.getName();
                    try {
                        context.put(clazz.getSimpleName() + "Enum", ENUM_HASH_MODEL.get(clazz.getName()));
                    } catch (TemplateModelException e) {
                        if (LogUtil.ROOT_LOG.isErrorEnabled())
                            LogUtil.ROOT_LOG.error("add enum(" + clazzName + ") in Render context exception", e);
                    }
                }
            }
            setAttributesMap(context);
        }
        return this;
    }

    @Override
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        setAttributesMap(AssembleUtil.maps("version", version));
        return super.buildView(viewName);
    }

    public static String changeVersion() {
        version = ValueUtil.random(6);
        return version;
    }
    public static String getVersion() {
        return version;
    }
}

