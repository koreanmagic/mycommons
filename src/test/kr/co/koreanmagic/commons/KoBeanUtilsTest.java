package kr.co.koreanmagic.commons;

import java.lang.reflect.Constructor;

import org.junit.Test;

public class KoBeanUtilsTest {
	
	//public static final Class<Integer>  TYPE = (Class<Integer>) Class.getPrimitiveClass("int");
	
	@Test
	public void test() throws Exception {
		
		//Bean bean = KoBeanUtils.putValue(new Bean(), "name", "asdf");
		//out(bean);
		Class<Integer> c = Integer.class;
		Integer i = new Integer(10);
		
		Constructor<Integer> con = Integer.class.getConstructor(int.class);
		Integer result = (Integer)con.newInstance(10);
	}
	
	
	public void tt(Object i) {
		out(i.getClass().isPrimitive());
	}
	
	
	private void out(Object obj) {
		System.out.println(obj);
	}
	
	class Bean {
		private String name;
		private int old;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getOld() {
			return old;
		}
		public void setOld(int old) {
			this.old = old;
		}
		
		public void setOld(Integer old) {
			
		}
		
		@Override
		public String toString() {
			return name + " : " + old;
		}
	}
}
