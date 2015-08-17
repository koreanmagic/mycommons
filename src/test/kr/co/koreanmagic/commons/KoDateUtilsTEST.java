package kr.co.koreanmagic.commons;

import java.sql.Timestamp;
import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;

public class KoDateUtilsTEST {

	
	@Test
	public void test() {
		DateTime dt = new DateTime("2013-05-06");
		DateTime dt2 = new DateTime("2013-05-07");
		
		Timestamp date = new Timestamp(dt.toDate().getTime());
		Timestamp date1 = new Timestamp(dt2.toDate().getTime());
		Timestamp date2 = new Timestamp(new Date().getTime());
		out(date.equals(date1));
		out(date1.equals(date2));
		out(date.equals(date2));
		
		out("음냐 : " + DateUtils.getSqlStyle(date));
		out("음냐 : " + DateUtils.getSqlStyle(date1));
		out("음냐 : " + DateUtils.getSqlStyle(date2));
	}
	
	private void out(Object obj) {
		System.out.println(obj);
	}
	
	

}
