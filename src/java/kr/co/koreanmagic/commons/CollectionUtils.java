package kr.co.koreanmagic.commons;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CollectionUtils {
	
	@SuppressWarnings("unchecked")
	public static<T> Set<T> collection(T...ts) {
		return new HashSet<T>(Arrays.asList(ts));
	}

}
