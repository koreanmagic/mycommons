package kr.co.koreanmagic.commons;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

public class ReflectionView {
	
	private static MethodFormat methodFormat = new MethodFormatImpl();
	private static final String[] EXCLUDE_METHOD = {"wait", "notify", "notifyAll"};
	
	
	// 메소드 실행
	public static Object invokeMethod(Method method, Object target) {
		return invokeMethod(method, target, new Object[0]); // spring 소스에서 이렇게 0개 배열로 넘기고 있음.
	}
	public static Object invokeMethod(Method method, Object target, Object...args) {
		try {
			return ReflectionUtils.invokeJdbcMethod(method, target, args);
		} catch (Exception e) {
			throw new RuntimeException(e); 
		}
	}
	
	
	// 메소드 명 출력
	public static List<String> printMethods(Class<?> clazz) {
		return printMethods(clazz, methodFormat);
	}
	public static List<String> printMethods(Class<?> clazz, MethodFormat format) {
		Method[] methods = clazz.getDeclaredMethods();
		return writeMethod(methods, format);
	}
	
	
	/* 리턴 타입으로 메소드 찾기 */
	public static List<String> printMethodByReturnType(Class<?> clazz, Class<?> returnType) {
		return printMethodByReturnType(clazz, returnType, methodFormat);
	}
	public static List<String> printMethodByReturnType(Class<?> clazz, Class<?> returnType, MethodFormat format) {
		return writeMethod(findMethodByReturnType(clazz, returnType), format);
	}
	
	/* 리턴 타입으로 메소드 찾기 */
	public static List<Method> findMethodByReturnType(Class<?> clazz, final Class<?> returnType) {
		return selecting(clazz, new Selector() {
			public boolean match(Method method) {
				return method.getReturnType().isAssignableFrom(returnType);
			}
		});
	}
	
	
	/* 인자 타입으로 메소드 찾기 */
	public static List<String> printMethodByArgs(Class<?> clazz, Class<?>[] params) {
		return printMethodByArgs(clazz, params, methodFormat);
	}
	public static List<String> printMethodByArgs(Class<?> clazz, Class<?>[] params, MethodFormat format) {
		return writeMethod(findMethodByArgs(clazz, params), format);
	}
	/* 인자 타입으로 메소드 찾기 */
	public static List<Method> findMethodByArgs(Class<?> clazz, final Class<?>[] params) {
		return selecting(clazz, new Selector() {
			public boolean match(Method method) {
				return Arrays.deepEquals(method.getParameterTypes(), params);
			}
		});
		
	}
	
	
	// 리턴타입과 아큐타입에 맞는 모든 메소드 실행
	public static Map<String, Object> printMethodReturnValues(Object target, final Class<?> returnType, final Object[] args) {
		return printMethodReturnValues(target, returnType, args, methodFormat);
	}
	public static Map<String, Object> printMethodReturnValues(Object target, final Class<?> returnType, final Object[] args, MethodFormat format) {
		Class<?>[] params = null;
		// 아큐타입을 골라내기 위해서, 인자에서 클래스 추출
		if(!(args == null) && args.length > 0) {
			params = new Class<?>[args.length];
			for(int i = 0; i < args.length; i++) {
				params[i] = args[i].getClass();
			}
		} else {
			params = new Class<?>[0];
		}
		return printMethodReturnValues(target, returnType, args, params, format);
	}
	private static Map<String, Object> printMethodReturnValues(Object target, final Class<?> returnType,
														final Object[] args, final Class<?>[] argsType, MethodFormat format) {
		
		// 아큐타입과 리턴타입이 맞는 메소드 추출
		List<Method> list = selecting(target.getClass(), new Selector() {
			public boolean match(Method method) {
				if(method.getReturnType().equals(returnType))
					if(Arrays.deepEquals(method.getParameterTypes(), argsType))
						return true;
				return false;
			}
		});
		return putInvokeValue(list, target, args);
		
		/*
		Map<String, Object> result = new HashMap<>();
		int err = 0;
		// 메소드 실행
		try {
			for(Method method : list) {
				result.put(writeMethod(method, format), ReflectionUtils.invokeMethod(method, target, args));
			}
		} catch (Exception e) {
			result.put("error" + err++, "error");
		}
		return result;*/
	}
	
	
	
	public static Map<String, Object> noArgAllMethod(Object target) {
		List<Method> methods = selecting(target.getClass(), new Selector() {
			@Override
			public boolean match(Method method) {
				if(method.getParameterTypes().length == 0) // 파라미터가 없으면 결정
					return true;
				else
					return false;
			}
		});
		return putInvokeValue(methods, target);
	}
	
	// 메소드들을 실행시켜서 이름과 값을 맵에 담아줌
	private static Map<String, Object> putInvokeValue(Iterable<Method> methods, Object target, Object...args) {
		Map<String, Object> map = new HashMap<>();
		Method currentMethod = null;
		try {
			for(Method method : methods) {
				currentMethod = method;
				currentMethod.setAccessible(true);
				map.put(Modifier.toString(currentMethod.getModifiers()) + " " + currentMethod.getName(), currentMethod.invoke(target, args));
			} 
		} catch (Exception e) {
			throw new RuntimeException("현재 메소드 : " + currentMethod, e);
		}
		return map;
	}
	
	
	
	private static List<Method> selecting(Class<?> clazz, Selector selector) {
		Method[] methods = clazz.getMethods();
		List<Method> list = new ArrayList<>();
		for(Method method : methods) {
			if(!isExcludeMethod(method))
			if(selector.match(method)) list.add(method);
		}
		return list;
	}
	
	
	// 프리미티브 타입 변환
	public static Class<?> convertPrimitiveType(Class<?> clazz) {
		throw new RuntimeException("구현하세요!");
	}
	
	private static Class<?> convertPrimitive(Class<?> clazz) {
		if(clazz == boolean.class) return Boolean.class;
		if(clazz == byte.class) return Byte.class;
		if(clazz == short.class) return Short.class;
		if(clazz == char.class) return Character.class;
		if(clazz == int.class) return Integer.class;
		if(clazz == long.class) return Long.class;
		if(clazz == float.class) return Float.class;
		if(clazz == double.class) return Double.class;
		else
			return null;
	}
	
	private static Class<?> convertWrap(Class<?> clazz) {
		if(clazz == Boolean.class) return boolean.class;
		if(clazz == Byte.class) return byte.class;
		if(clazz == Short.class) return short.class;
		if(clazz == Character.class) return char.class;
		if(clazz == Integer.class) return int.class;
		if(clazz == Long.class) return long.class;
		if(clazz == Float.class) return float.class;
		if(clazz == Double.class) return double.class;
		else
			return null;
	}
	
	
	private static List<String> writeMethod(Method[] list, MethodFormat format) {
		List<String> result = new ArrayList<>(list.length);
		for(Method method : list) {
			if(!isExcludeMethod(method))
				result.add(writeMethod(method, format));
		}
		return result;
	}
	
	private static List<String> writeMethod(List<Method> list, MethodFormat format) {
		List<String> result = new ArrayList<>(list.size());
		for(Method method : list) {
			if(!isExcludeMethod(method))
				result.add(writeMethod(method, format));
		}
		return result;
	}
	
	private static boolean isExcludeMethod(Method method) {
		for(String methodName : EXCLUDE_METHOD) {
			if(method.getName().toLowerCase().contains(methodName))
				return true;
		}
		return false;
	}
	
	private static String writeMethod(Method method, MethodFormat format) {
		return format.format(
								Modifier.toString(method.getModifiers()),
								method.getReturnType().getSimpleName(),
								method.getName(),
								method.toString()
							);
	}
	
	
	
	
	static interface Selector {
		boolean match(Method method);
	}
	
	static interface MethodFormat {
		String format(String modifier, String returnType, String methodName, String signature);
	}
	
	static class MethodFormatImpl implements MethodFormat {

		public String format(String modifier, String returnType, String methodName, String signature) {
			return String.format("[%s] {%s} %s>>", modifier, returnType, methodName);
		}
		
	}
	
}
