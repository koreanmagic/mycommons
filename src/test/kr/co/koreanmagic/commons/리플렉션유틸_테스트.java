package kr.co.koreanmagic.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class 리플렉션유틸_테스트 {

	@Test
	public void test() throws Exception {
		WorkList target = new WorkList();
		List<String> list = ReflectionView.printMethodByReturnType(WorkList.class, int.class);
		out(list);
	}
	
	
	private static void out(Object obj) {
		if(obj instanceof Collection) {
			outCol((Collection<?>)obj);
			return;
		}
		System.out.println(obj);
	}
	
	private static void outCol(Collection<?> col) {
		for(Object i : col) {
			out(i);
		}
	}

}
