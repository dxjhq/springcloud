package com.hhly.utils.converter;

import com.hhly.utils.ValueUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * 将前台传过来的字符串跟跟枚举的 name 匹配, 匹配不上再跟枚举的 getCode 方法匹配, 最后都不能匹配上就返回 null
 * (默认出错时会抛出 IllegalArgumentException 异常).
 *
 * 此 convert 用于替代 org.springframework.core.convert.support.StringToEnumConverterFactory
 *
 * @see org.springframework.core.convert.support.StringToEnumConverterFactory
 */
public class StringToEnumConverter implements ConverterFactory<String, Enum> {

    @Override
    @SuppressWarnings("unchecked")
    public <E extends Enum> Converter<String, E> getConverter(Class<E> targetType) {
        Class<?> enumType = targetType;
        while (enumType != null && !enumType.isEnum()) {
            enumType = enumType.getSuperclass();
        }
        if (enumType == null) {
            throw new IllegalArgumentException("The target type " + targetType.getName() + " does not refer to an enum");
        }
        return new StringToEnum(enumType);
    }

    private class StringToEnum<E extends Enum> implements Converter<String, E> {

        private final Class<E> enumType;

        public StringToEnum(Class<E> enumType) {
            this.enumType = enumType;
        }

        @Override
        public E convert(String source) {
            return ValueUtil.toEnum(enumType, source);
        }
    }
}
