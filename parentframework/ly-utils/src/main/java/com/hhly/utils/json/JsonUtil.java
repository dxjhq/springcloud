package com.hhly.utils.json;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.google.common.collect.Lists;

import com.hhly.utils.date.DateFormatType;
import com.hhly.utils.AssembleUtil;
import com.hhly.utils.ValueUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

/**
 * @author wangxianchen
 * @create 2017-11-06
 * @desc 请使用 ResultObject
 */
@Deprecated
public class JsonUtil {

	private static final ObjectMapper DEFAULT = new ObjectMapper();
	/** 只有一些基础规则的 json 映射器. 需要序列化和反序列化时, 就使用这个映射器 */
	private static final ObjectMapper BASIC = new BasicObjectMapper();

	public static ObjectMapper getMapper() {
		return DEFAULT;
	}
	
	/**
	 * 用来渲染给前台的 json 映射器, 定义了一些自定义类的序列化规则, 然而并没有反序列化规则<br>
	 * 所以使用此映射器序列化的 json, 想要反序列化回来调用(toObject toList)时将会不成功
	 */
	public static final ObjectMapper RENDER = new RenderObjectMapper();

	private static class BasicObjectMapper extends ObjectMapper {
		private BasicObjectMapper() {
			super();
			// 日期不用 utc 方式显示(utc 是一个整数值)
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            // 时间格式
            setDateFormat(new SimpleDateFormat(DateFormatType.YYYY_MM_DD_HH_MM_SS.getValue()));
            // 不确定值的枚举返回 null
			configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
			// 不确定的属性项上不要失败, 默认如果失败会抛出 JsonMappingException
			configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			// null 不序列化, 如果想要 空字符串也不序列化, 改成 NON_EMPTY 即可
			setSerializationInclusion(JsonInclude.Include.NON_NULL);

			/**
			 * 序列化 Money 到前端时返回字符串, 在 {@link Money#toString} 上标注
			 * {@link com.fasterxml.jackson.annotation.JsonValue} 同理
			 */
//			registerModule(new SimpleModule().addSerializer(Money.class, new JsonSerializer<Money>() {
//				@Override
//				public void serialize(Money value, JsonGenerator gen, SerializerProvider sp) throws IOException {
//					gen.writeString(value.toString());
//				}
//			}));
			/**
			 * 反序列化一个字符串成 Money. 在 {@link Money#Money(String)} 上标注
			 * {@link com.fasterxml.jackson.annotation.JsonCreator} 同理
			 */
//			registerModule(new SimpleModule().addDeserializer(Money.class, new JsonDeserializer<Money>() {
//				@Override
//				public Money deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
//					return new Money(p.getText());
//				}
//			}));
		}
	}

	private static class RenderObjectMapper extends BasicObjectMapper {
		private RenderObjectMapper() {
			super();
            // 序列化枚举到前端时, 返回 value(没有就使用 name)和 code(没有就使用 ordinal), 前者用来显示, 后者用来前后端传递
            registerModule(new SimpleModule().addSerializer(Enum.class, new JsonSerializer<Enum>() {
                @Override
                public void serialize(Enum value, JsonGenerator gen, SerializerProvider sp) throws IOException {
                    // 枚举没有定义 getValue 就使用 name
                    Object vu = ValueUtil.getMethod(value, "getValue");
                    if (vu == null) vu = value.name();

                    // 枚举没有定义 getCode 就使用 ordinal
                    Object code = ValueUtil.getMethod(value, "getCode");
                    if (code == null) code = value.ordinal();

                    gen.writeObject(AssembleUtil.maps(
							"value", vu,
							"code", code
					));
                }
            }));
			// 序列化 PageList 时只将总条数和当前页的数据返回. 否则可以使用 PageListJsonSerializer 这个现成的实现
			// registerModule(new SimpleModule().addSerializer(new PageListJsonSerializer(this)));
			registerModule(new SimpleModule().addSerializer(PageList.class, new JsonSerializer<PageList>() {
				@Override
				public void serialize(PageList value, JsonGenerator gen, SerializerProvider sp) throws IOException {
					gen.writeObject(AssembleUtil.maps(
                            "items", Lists.newArrayList(value),
                            "total", value.getPaginator().getTotalCount()
                    ));
				}
			}));
		}
	}

    private static final ObjectMapper NOTHING = new SubRenderObjectMapper();
    private static class SubRenderObjectMapper extends RenderObjectMapper {
        private SubRenderObjectMapper() {
            super();
            registerModule(new SimpleModule().addSerializer(PageList.class, new JsonSerializer<PageList>() {
                @Override
                public void serialize(PageList value, JsonGenerator gen, SerializerProvider sp) throws IOException {
                    gen.writeObject(value.toArray());
                }
            }));
        }
    }

	/** 对象转换成 json 字符串 */
	public static String toJson(Object obj) {
		return toJson(BASIC, obj);
	}

	private static String toJson(ObjectMapper om, Object obj) {
		try {
			return om.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException("object(" + obj + ") to json exception.", e);
		}
	}

	/** 对象转换成 json 字符串, 主要用来渲染到前台 */
	public static String toRender(Object obj) {
		return toJson(RENDER, obj);
	}

	public static String toJsonNoPage(Object obj) {
        return toJson(NOTHING, obj);
	}

	/** 将 json 字符串转换为对象 */
	public static <T> T toObject(String json, Class<T> clazz) {
		try {
			return BASIC.readValue(json, clazz);
		} catch (Exception e) {
			throw new RuntimeException("json (" + json + ") to object(" + clazz.getName() + ") exception", e);
		}
	}

    /** 将 json 字符串转换为对象, 当转换异常时, 返回 null */
    public static <T> T toObjectNil(String json, Class<T> clazz) {
        try {
            return BASIC.readValue(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /** 将 json 字符串转换为对象(默认规则), 此转换非常严谨, 如果类中少了属性会失败而不是给空值. 当转换异常时, 返回 null */
    public static <T> T toObjectWithNil(String json, Class<T> clazz) {
        try {
            return DEFAULT.readValue(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /** 将 json 字符串转换为指定的数组列表 */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        try {
            return BASIC.readValue(json, BASIC.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            throw new RuntimeException("json(" + json + ") to list(" + clazz.getName() + ") exception.", e);
        }
    }
    /** 将 json 字符串转换为指定的数组列表 */
    public static <T> List<T> toListNil(String json, Class<T> clazz) {
        try {
            return BASIC.readValue(json, BASIC.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            return null;
        }
    }

	// ========== 返回自定义属性 ==========

	private static final String CUSTOM_FILTER = "customFilter";

	@JsonFilter(CUSTOM_FILTER)
	private static class CustomFilterMixin {
	}

	/**
     * <pre>
	 * 只输出传入的属性, 支持级联! 参考:
	 * https://github.com/Antibrumm/jackson-antpathfilter<br>
	 * 此方法是为了输出 json 字符串, 在 controller 中应该调用 {@link #toObjectWithField}
	 *
	 * public class User {
	 * 	Long id;
	 * 	String name;
	 * 	String password;
	 * 	Msg info;
	 * }
	 * public class Msg {
	 * 	Long id;
	 * 	String name;
	 * }
	 *
	 * User user = new User(123l, "ruby", "encrypt-code", new Msg(890l, "abc123"));
	 *
	 * // 输出 {"id":123,"name":"ruby","password":"encrypt-code","info":{"id":890,"name":"abc123"}}
	 * toJsonWithField(user);
	 *
	 * // 输出 {"name":"ruby","info":{}}
	 * toJsonWithField(user, "name", "info");
	 *
	 * // 输出 {"name":"ruby"}
	 * toJsonWithField(user, "name", "info.name");
	 *
	 * // 输出 {"name":"ruby","info":{"name":"abc123"}}
	 * toJsonWithField(user, "name", "phone", "info", "info.name");
	 *
	 * // 输出 {"id":123,"name":"ruby","password":"encrypt-code","info":{}}
	 * toJsonWithField(user, "*");
	 *
	 * // 输出 {"id":123,"name":"ruby","info":{}} <span style="color:red;">星号代表全部, 感叹(!) 和 减号(-) 都能排除属性</span>
	 * toJsonWithField(user, "*", "!password");
	 *
	 * // 输出 {"id":123,"name":"ruby","info":{"id":890,"name":"abc123"}}
	 * toJsonWithField(user, "**", "!password");
	 *
	 * // 输出 {"id":123,"name":"ruby","info":{"name":"abc123"}}
	 * toJsonWithField(user, "**", "!password", "-info.id");
	 * </pre>
	 * 
	 * @see #toJson(java.lang.Object)
	 */
	public static String toJsonWithField(Object obj, String... fields) {
		if (obj == null)
			return null;

		return toRender(toObjectWithField(obj, fields));
	}

	/** 基于 spring mvc 的设置, 当前方法返回的对象会被 {@link #toRender} 渲染后才会返回到前台 */
	public static Object toObjectWithField(Object obj, String... fields) {
		if (obj == null)
			return null;

		if (obj instanceof PageList) {
			// pageList 只操作里面的 List
			return customField((PageList) obj, fields);
		} else {
            String json = customField(obj, fields);
            // 返回使用 Object 将会是一个 LinkedHashMap 与原对象无关, 如果返回原对象, 对象上有默认值也将会被序列化
            // return (obj instanceof List) ? toList(json, Object.class) : toObject(json, Object.class);
            if (obj instanceof List) {
                // 将过滤好的字符串「反序列化」成一个 List 并返回
                if (AssembleUtil.isEmpty((List) obj)) {
                    return toList(json, Object.class);
                } else {
                    return toList(json, ((List) obj).iterator().next().getClass());
                }
            } else {
                // 将过滤好的字符串「反序列化」成一个 Object 并返回
                return toObject(json, obj.getClass());
            }
		}
	}

	/** 如果传入的是 List, 按照传入的参数进行过滤后再次返回 List */
	public static <T> List<T> toListWithField(List<T> list, String... fields) {
		if (AssembleUtil.isEmpty(list))
			return Collections.<T>emptyList();

		if (list instanceof PageList) {
			// pageList 只操作里面的 List
			return customField((PageList) list, fields);
		} else {
			// 将过滤好的字符串「反序列化」成 List 并返回
			return toList(customField(list, fields), (Class<T>) list.iterator().next().getClass());
		}
	}

	private static PageList customField(PageList pageList, String... fields) {
		if (AssembleUtil.isEmpty(pageList))
			return pageList;

		String json = customField(AssembleUtil.lists(pageList.toArray()), fields);
		// 将上面的字符串反序列化成一个 list
		List list = toList(json, pageList.iterator().next().getClass());

		// 将 pageList 清空并将上面的 list 重新添加进来
		pageList.clear();
		pageList.addAll(list);
		return pageList;
	}

	/** 将对象过滤掉相关属性并序列化成一个字符串返回 */
	private static String customField(Object obj, String... fields) {
		// 构建一个专门用来过滤字段的映射器
		ObjectMapper om = new BasicObjectMapper();
		// 过滤属性时会改变映射器的一些内部信息, 因此要每次都实例化一个映射器. 随之而来的代价就是性能会稍差一点
		om.addMixIn(Object.class, CustomFilterMixin.class);
		om.setFilterProvider(new SimpleFilterProvider().addFilter(CUSTOM_FILTER, new AntPathPropertyFilter(fields)));

		// 使用此映射器序列化对象成一个字符串
		return toJson(om, obj);
	}
}
