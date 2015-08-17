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
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class IoUtil {
	
	
	
	/*
	 * Path --> Buffered 객체 반환
	 */
	public static final BufferedWriter getWriter(Path path) throws IOException {
		return getWriter(path, "euc-kr");
	}
	public static final BufferedWriter getWriter(Path path, String cs) throws IOException {
		return getWriter(getOutputStream(path), cs);
	}
	public static final BufferedReader getReader(Path path) throws IOException {
		return getReader(path, "euc-kr");
	}
	public static final BufferedReader getReader(Path path, String cs) throws IOException {
		return getReader(getInputStream(path), cs);
	}
	

	/*
	 * ByteStream --> Buffered 객체 반환
	 */
	public static final BufferedWriter getWriter(OutputStream out) throws IOException {
		return getWriter(out, "euc-kr");
	}
	public static final BufferedWriter getWriter(OutputStream out, String cs) throws IOException {
		return new BufferedWriter(new OutputStreamWriter(out, Charset.forName(cs)));
	}
	public static final BufferedReader getReader(InputStream in) throws IOException {
		return getReader(in, "euc-kr");
	}
	public static final BufferedReader getReader(InputStream in, String cs) throws IOException {
		return new BufferedReader(new InputStreamReader(in, Charset.forName(cs)));
	}
	
	
	/*
	 * PrintWriter 객체 반환
	 */
	public static final PrintWriter getPrintWriter(Path path) throws IOException {
		return getPrintWriter(path, "euc-kr");
	}
	public static final PrintWriter getPrintWriter(Path path, String cs) throws IOException {
		return new PrintWriter(path.toFile(), cs);
	}
	public static final PrintWriter getPrintWriter(OutputStream out) throws IOException {
		return getPrintWriter(out, "euc-kr");
	}
	public static final PrintWriter getPrintWriter(OutputStream out, String cs) throws IOException {
		return new PrintWriter(new OutputStreamWriter(out, Charset.forName(cs)));
	}
	
	
	public static final OutputStream getOutputStream(Path path) throws IOException {
		return Files.newOutputStream(path);
	}
	public static final InputStream getInputStream(Path path)  throws IOException {
		return Files.newInputStream(path);
	}
	
	public static final FileChannel getRandomChannel(Path path) throws IOException {
		return new RandomAccessFile(path.toFile(), "rw").getChannel();
	}
	public static final FileChannel getReadChannel(Path path) throws IOException {
		return new FileInputStream(path.toFile()).getChannel();
	}
	public static final FileChannel getWriteChannel(Path path) throws IOException {
		return new FileOutputStream(path.toFile()).getChannel();
	}
	
	public static final ByteBuffer getBuffer(int size) {
		return ByteBuffer.allocateDirect(size);
	}

}
