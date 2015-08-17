package kr.co.koreanmagic.commons;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class TestUtil {
	
	
	
	
	static Logger logger = Logger.getLogger("test");
	
	
	private static final ThreadLocal<Long> localTotalTime = new ThreadLocal<>();
	private static ThreadLocal<List<Long>> localTotalTimeList;
	private static long time;
	
	static final long start() {
		if(localTotalTimeList == null) {
			localTotalTimeList = new ThreadLocal<>();
			localTotalTimeList.set(new ArrayList<Long>());
		}
		TestUtil.time = System.currentTimeMillis();
		return time;
	}
	
	static final long end() {
		long time = System.currentTimeMillis();
		return time - TestUtil.time;
	}
	
	private static final DecimalFormat form  = new DecimalFormat("0.###");
	
	static final String globalEnd() {
		String log = "[";
		long time = System.currentTimeMillis() - TestUtil.time;
		long totalTime = getLocalTime(time);
		return log + "]";
		
	}
	
	private static final long getLocalTime(long lastEndTime) {
		Long localTime = localTotalTime.get(); // 토탈 시간 합계
		if(localTime == null) { // null이면 처음 기록하는 상황
			localTime = lastEndTime;
			localTotalTime.set(localTime); // 마지막 시간으로 설정.
		}
		else localTime += lastEndTime; 
		
		localTotalTime.set(localTime);
		return localTime;
	}
	
	
	

}
