package kr.co.koreanmagic.commons;

import java.util.Scanner;

public class Utils {
	
	public static void timeout() {
		Scanner scan = new Scanner(System.in);
		String line = null;
		
		System.out.print("any key press -->");
		
		line = scan.next();
		if(line.contains("q"))
			throw new RuntimeException("입력글자 : " + line);
	}

}
