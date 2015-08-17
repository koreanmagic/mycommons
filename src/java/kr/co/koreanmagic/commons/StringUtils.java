package kr.co.koreanmagic.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils {
	
	
	// ******** 배열이나 문자List를 받아 그룹핑 ex) "값","값","값" ******** //
	// 콜렉션
	public static String join(String seperator, Collection<?> col) {
		return join(seperator, col.toArray());
	}
	// 오브젝태 배열
	public static String join(String seperator, Object[] args) {
		return join(new StringBuilder(), null, seperator, args).toString();
	}
	
	public static StringBuilder join(StringBuilder sb, String seperator, Object... args) {
		return join(sb, null, seperator, args);
	}
	public static String join(String  enclosed, String seperator, Object... args) {
		return join(new StringBuilder(), enclosed, seperator, Arrays.asList(args)).toString();
	}
	public static StringBuilder join(StringBuilder sb, String  enclosed, String seperator, Object... args) {
		return join(sb, enclosed, seperator, Arrays.asList(args));
	}
	
	public static String join(String enclosed, String seperator, Iterable<Object> iter) {
		return join(new StringBuilder(), enclosed, seperator, iter).toString();
	}
	public static StringBuilder join(StringBuilder sb, String enclosed, String seperator, Iterable<Object> iter) {
		
		int seperatorLength = seperator.length();
		boolean existsEnclosed = enclosed == null || enclosed.length() == 1 ? false : true;
		
		for (Object arg : iter) {
			arg = (arg == null) ? "" : arg; // 값이 null이면 공백값으로 변환
			
			if(existsEnclosed) sb.append(enclosed).append(arg).append(enclosed).append(seperator);
			else sb.append(arg).append(seperator);
		}
		int length = sb.length();
		return sb.delete(length - seperatorLength, length);
	}

	
	public static String join(Object[] array, int start, int len, String sequence) {
		StringBuilder sb = new StringBuilder();
		for(int limit=len-1; start<len; start++) {
			if(start == limit) sb.append(array[start]);
			else sb.append(array[start]).append(sequence);
		}
		return sb.toString();
	}
	
	
	
	//문자에 특정 문자 감싸기  ex) "value"
	public static String enclosed(String enclosed, String string) {
		return enclosed + string + enclosed;
	}
	public static StringBuffer enclosed(String enclosed, StringBuffer buf) {
		return buf.insert(0, enclosed).append(enclosed);
	}
	
	
	
	//문장을 설정한 글자수만큼 쪼개서 배열로 반환
	public static String[] split(String target, int length) {
		/* 파라메터 검증 */
		int target_len = target.length();
		if(target_len < length) return new String[] {target};
		
		/* 배열 갯수 계산 */
		int arraySize = target_len / length;
		arraySize += ((target_len % length) == 0) ? 0 : 1;
		String[] result = new String[arraySize];
		
		int start = 0, end = length;
		for(int i = 0; i < arraySize; i++) {
			if(i == arraySize-1) result[i] = target.substring(start);
			else result[i] = target.substring(start, end);
			start = end;
			end += length;
		}
		return result;
	}
	
	/* CSV 스타일 */
	private static final String SEPARATOR = ",";
	public static String csv(Object[] values) {
		StringBuilder sb = new StringBuilder();
		return addSeparator(sb, values).toString();
	}
	
	private static StringBuilder addSeparator(StringBuilder sb, Object[] values) {
		for(Object value : values) {
			sb.append(value).append(SEPARATOR);
		}
		return sb.delete(sb.length() - SEPARATOR.length(), sb.length());
	}
	
	// Object가 null이면 다른 문장으로 리턴
	public static String nullValue(Object obj) {
		return nullValue(obj, "null");
	}
	public static String nullValue(Object obj, String replace) {
		if(obj == null) return replace;
		return obj.toString();
	}
	
	
	public static boolean hasContent(String s) {
		return (s == null || s.length() == 0) ? false : true; 
	}
	
	
	// "property" ==> "getProperty"
	public static String propertyName(String property, String type) {
		if(property == null || type == null) throw new NullPointerException();
		return type + org.apache.commons.lang3.StringUtils.capitalize(property);
		
	}
	
	//String 특정 위치에 문자 삽입
	public static String insertStr(String target, int pos, String insertStr) {
		StringBuilder sb = new StringBuilder(target);
		return sb.insert(pos, insertStr).toString();
	}
	
	
	
	
	// 두 문자 사이에 있는거 반환 (두 문자 포함  // 정규식)
	public static String betweenStr(String source, String startRegex, String endRegex) {
		return betweenStr(source, 0, startRegex, endRegex, true);
	}
	public static String betweenStr(String source, String startRegex, String endRegex, boolean contains) {
		return betweenStr(source, 0, Pattern.compile(startRegex), Pattern.compile(endRegex), contains);
	}
	public static String betweenStr(String source, Pattern startPattern, Pattern endPattern) {
		return betweenStr(source, 0, startPattern, endPattern, true);
	}
	public static String betweenStr(String source, Pattern startPattern, Pattern endPattern, boolean contains) {
		return betweenStr(source, 0, startPattern, endPattern, contains);
	}
	public static String betweenStr(String source, int startPos, String startRegex, String endRegex, boolean contains) {
		return betweenStr(source, startPos, Pattern.compile(startRegex), Pattern.compile(endRegex), contains);
	}
	public static String betweenStr(String source, int startPos, Pattern startPattern, Pattern endPattern, boolean contains) {
		int start = 0, end = 0, before_start = 0, before_end = 0,
			after_start = 0, after_end = 0;
				
		
		Matcher m = null;
		
		/* 앞쪽 찾기 */
		m = startPattern.matcher(source);
		if(m.find(startPos)) {
			before_start = m.start();
			before_end = m.end();
		}
		
		/* 뒷쪽 찾기 */
		m = endPattern.matcher(source);
		if(m.find(before_end)) {
			after_start = m.start();
			after_end = m.end();
		}
		
		if(contains) {
			start = before_start;
			end = after_end;
		} else {
			start = before_end;
			end = after_start;
		}
			
		
		return source.substring(start, end);
	}
	
	
	// 여러개일때 ** 위와 같이 정규식으로 바꾸자. (2014-10-05 14:33 // 귀찮아서 넘김)
	public static List<String> betweenStrs(String source, String startText, String endText) {
		return betweenStrs(source, 0, startText, endText);
	}
	public static List<String> betweenStrs(String source, int startPos, String startText, String endText) {
		List<String> result = new ArrayList<>();
		int startTextPos, endTextPos,
		startTextLen = startText.length(), endTextLen = endText.length();

		int endPos = -1;
		while((startTextPos = source.indexOf(startText, startPos)) != -1) {
			endTextPos = source.indexOf(endText, startTextPos + startTextLen);
			endPos = endTextPos + endTextLen;
			result.add(source.substring(startTextPos, endPos));
			startPos += (endPos - startPos) + 1;
		}
		return result;
	}
	

	public static String findBetweenStr(StringBuilder buf, String startSeq, String endSeq) {
		int start = buf.indexOf(startSeq) + startSeq.length();
		int end = buf.indexOf(endSeq, start);
		return buf.substring(start, end);
	}
	public static String findBetweenStr(StringBuffer buf, String startSeq, String endSeq) {
		int start = buf.indexOf(startSeq) + startSeq.length();
		int end = buf.indexOf(endSeq, start);
		return buf.substring(start, end);
	}
	public static String findBetweenStr(String buf, String startSeq, String endSeq) {
		int start = buf.indexOf(startSeq) + startSeq.length();
		int end = buf.indexOf(endSeq, start);
		return buf.substring(start, end);
	}
	public static String findBetweenStr(StringBuilder buf, String startSeq, int end) {
		int start = buf.indexOf(startSeq) + startSeq.length();
		return buf.substring(start, start+end);
	}
	public static String findBetweenStr(StringBuffer buf, String startSeq, int end) {
		int start = buf.indexOf(startSeq) + startSeq.length();
		return buf.substring(start, start+end);
	}
	public static String findBetweenStr(String buf, String startSeq, int end) {
		int start = buf.indexOf(startSeq) + startSeq.length();
		return buf.substring(start, start+end);
	}
	
	// 두 문자 사이에 있는거 교체
	public static StringBuilder replaceStr(StringBuilder buf, String startSeq, String endSeq, String str) {
		int start = buf.indexOf(startSeq) + startSeq.length();
		int end = buf.indexOf(endSeq, start);
		return buf.replace(start, end, str);
	}
	public static StringBuffer replaceStr(StringBuffer buf, String startSeq, String endSeq, String str) {
		int start = buf.indexOf(startSeq) + startSeq.length();
		int end = buf.indexOf(endSeq, start);
		return buf.replace(start, end, str);
	}
	public static StringBuffer replaceStr(String buf, String startSeq, String endSeq, String str) {
		return replaceStr(new StringBuffer(buf), startSeq, endSeq, str);
	}
	
	/*
	 * 로그 박스처리
	 */
	public static String stringWrapper(String c, int length, String log) {
		
		final int LOG_LENGTH = log.length();
		
		if(length < LOG_LENGTH)
			length = LOG_LENGTH * 2;
		
		final boolean LOG_IS_ODD = ((LOG_LENGTH % 2) == 0) ? false : true; // 홀수인지 아닌지
		final boolean LEN_IS_ODD = ((length % 2) == 0) ? false : true;

		final int BLANK = 4;
		int i = 0;
		
		StringBuilder builder = new StringBuilder();
		
		for(;i < length;i++) {
			builder.append(c);
		}
		
		final String LINE = builder.toString(); 
		int addLine = ( ( ( LINE.length() - LOG_LENGTH) - BLANK) / 2 );
		
		builder.append("\n");
		for(i=0;i < addLine;i++) {
			builder.append(c);
		}
		builder.append("  ").append(log).append("  ");
		
		// 홀수냐 짝수냐에 따라 보정
		if(!LEN_IS_ODD)			// 박스문자가 짝수이고
			if(LOG_IS_ODD) 		// 로그가 홀수이면
				addLine++; 		// 보정한다.
			
		for(i=0;i < addLine;i++) {
			builder.append(c);
		}
		builder.append("\n")
				.append(LINE);
		
		return builder.toString();
	}
	
}
