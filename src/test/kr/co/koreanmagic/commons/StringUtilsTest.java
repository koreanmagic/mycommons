package kr.co.koreanmagic.commons;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void enclosed() {
		String result = "+value1+///+value2+///+value3+";
		String test = StringUtils.join("+", "///", "value1", "value2", "value3");
		
		assertThat(result, is(test));
	}
	
	@Test
	public void enclosed2() {
		String value = "value";
		assertThat(StringUtils.enclosed("+", value), is("+value+"));
	}
	
	@Test
	public void enclosed3() {
		StringBuffer buf = new StringBuffer("value");
		assertThat(StringUtils.enclosed("\"", buf).toString(), is("\"value\""));
	}

	
	
	@Test
	public void en() {
		List<String> list = new ArrayList<>();
		list.add("a");
		list.add("b");
		list.add("c");
		
		String test = StringUtils.join("+", "|", list);
		
		assertThat(test, is("+a+|+b+|+c+"));
	}
	

	
	@Test
	public void getter() {
		String test = "property";
		String result = StringUtils.propertyName(test, "get");
		
		assertThat(result, is("getProperty"));
	}
	
	@Test
	public void join2() {
		String[] a = {"1", "2", "3", "4"};
		out(StringUtils.join(a, 1, 3, "/"));
	}
	
	private void out(Object obj) {
		System.out.println(obj);
	}

}
