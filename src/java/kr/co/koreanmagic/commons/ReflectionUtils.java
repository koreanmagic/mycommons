package kr.co.koreanmagic.commons;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ReflectionUtils {
	
	@SuppressWarnings("unchecked")
	public static<T> T cast(Object obj) {
		return (T)obj;
	}
	
	
	/*
	 *	결정된 제네릭 타입을 가지고 Class<T>를 가지고 온다.  
	 */
	@SuppressWarnings("unchecked")
	public static<T> Class<T> getActualType(Class<?> clazz, int paramPosition) {
		ParameterizedType type = (ParameterizedType)clazz.getGenericSuperclass();
		return (Class<T>)type.getActualTypeArguments()[paramPosition];
	}
	
	
	/*
	 * setter getter메소드만 추출해준다
	 * [name, getName()], [tel, getTel()] ... 
	 */
	public static Map<String, Method> getterMethodMap(Class<?> clazz) {
		return methodMap(clazz, "get.*", x -> {
			return StringUtils.uncapitalize(x.substring(3, x.length()));
		});
	}
	
	public static Map<String, Method> setterMethodMap(Class<?> clazz) {
		return methodMap(clazz, "set.*", x -> {
			return StringUtils.uncapitalize(x.substring(3, x.length()));
		});
	}
	
	/*
	 * getter나 setter 메서드를 심플네임으로 맵을 만들어준다.
	 * param1 : 메소드를 추출할 클래스
	 * param2 : 메소드명을 판별할 정규식
	 * param3 : map의 key로 사용될 메소드명을 한번 가공
	 * 
	 */
	public static Map<String, Method> methodMap(Class<?> clazz, String regex, Function<String, String> lambda) {
		Pattern pattern = Pattern.compile(regex);
		Matcher m = null;
		Map<String, Method> map = new HashMap<>();
		String methodName = null;
		
		try {
		
			for(Method method : clazz.getMethods()) {
				
				methodName = method.getName();
				m = pattern.matcher(methodName);
				if(m.matches()) {
					map.put(lambda.apply(methodName), method);
				}
			}
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return map;
	}

}
