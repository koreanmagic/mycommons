package kr.co.koreanmagic.commons;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

public class BeanUtils {
	
	/*
	 *  디스크립터 맵으로 만들어 보내주기
	 *  Map<getName(), PropertyDescriptor>
	 */
	public static Map<String, PropertyDescriptor> getPropertyDescriptor(Class<?> target) {
		Map<String, PropertyDescriptor> result = new HashMap<>();
		for(PropertyDescriptor discriptor : org.springframework.beans.BeanUtils.getPropertyDescriptors(target)) {
			result.put(discriptor.getName(), discriptor);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static<T> T invokeReadMethod(PropertyDescriptor discriptor, Object target) {
		Method method = discriptor.getReadMethod();
		return (T)ReflectionUtils.invokeMethod(method, target);
	}
	
	public static void invokeWriteMethod(PropertyDescriptor discriptor, Object target, Object...args) {
		Method method = discriptor.getWriteMethod();
		ReflectionUtils.invokeMethod(method, target, args);
	}

	
	
	// web request식으로 된 것 다 뽑아내서 Map으로 돌려주기
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getAttributeMap(Object target) {
		Map<String, Object> map = new HashMap<>();
		Class<?> clazz= target.getClass();
		Method method = null;
		
		method = ReflectionUtils.findMethod(clazz, "getAttributeNames");
		Enumeration<String> names = (Enumeration<String>)ReflectionUtils.invokeMethod(method, target);
		method = ReflectionUtils.findMethod(clazz, "getAttribute", String.class);
		
		String name = null;
		while(names.hasMoreElements()) {
			name = names.nextElement();
			map.put(name, ReflectionUtils.invokeMethod(method, target, name));
		}
		return map;
	}
	
	
}
