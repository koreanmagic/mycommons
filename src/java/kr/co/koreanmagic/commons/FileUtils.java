package kr.co.koreanmagic.commons;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.Iterator;

public class FileUtils {

	
	
	public static Path cutTailPath(Path target, Path root) {
		if(!target.startsWith(root)) throw new RuntimeException("루트패스가 일치하지 않습니다.");
		int count = root.getNameCount();
		return target.subpath(count, target.getNameCount());
	}
	
	
	/*
	 * Target : G:/a/b/c
	 * Source : G:/1/2/3/4
	 * SourcePath : G:/1/2/3/4/5/6
	 * 
	 * Target ==> G:/a/b/c/5/6
	 */
	public static Path relativePathToPath(Path target, Path sourceFrom, Path sourceTo) throws IOException {
		Path relativePath = sourceFrom.relativize(sourceTo);
		if(relativePath.startsWith(".."))
			throw new IOException("Path.resolve()를 이용해 상대경로를 만드는데 ../가 발견되었습니다.");
		return target.resolve(relativePath);
	}
	
	/*
	 *  뒤에서부터 끊어서 서브 경로를 준다
	 *  ("g:/a/b/c", 2) --> "b/c"
	 */
	public static Path lastIndexSubPath(Path target, int index) {
		int count = target.getNameCount();
		if(!(count > index)) return target;
		return target.subpath(count - index, count);
	}
	
	
	/*
	 *  Path 객체와 복제할 BasicFileAttributes 객체를 넣어주면, Path객체의 생성, 액세스, 수정일을 Attributes객체의 설정값으로 바꿔준다.
	 *  반환되는 BasicFileAttributeView는 수정된 Path(target) 객체의 BasicFileAttributeView이다.
	 */
	public static BasicFileAttributeView setFileTimes(Path target, Path source) throws IOException {
		return setFileTimes(target, getBasicFileAttributeView(source));
	}
	public static BasicFileAttributeView setFileTimes(Path target, BasicFileAttributeView sourceView) throws IOException {
		return setFileTimes(target, sourceView.readAttributes());
	}
	public static BasicFileAttributeView setFileTimes(Path target, BasicFileAttributes sourceAttrs) throws IOException {
		BasicFileAttributeView view = null;
		view = Files.getFileAttributeView(target, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
		view.setTimes(sourceAttrs.lastModifiedTime(), sourceAttrs.lastAccessTime(), sourceAttrs.creationTime());
		return view;
	}
	
	// BasicFileAttributeView 객체 구하기. 불필요한 코딩을 없애기 위한 메서드
	public static BasicFileAttributeView getBasicFileAttributeView(Path path, LinkOption...opts) {
		BasicFileAttributeView view = null;
		view = Files.getFileAttributeView(path, BasicFileAttributeView.class, opts);
		return view;
	}	
	
	
	// 파일명에 못쓰는 문자 지우기
	private static final String INVALID_CHAR = "[\\\\/:\\*\\?\\\"\\<\\>\\|]" ;
	public static String removeEscape(String fileName) {
		return removeEscape(fileName, "");
	}
	public static String removeEscape(String fileName, String replace) {
		return fileName.replaceAll(INVALID_CHAR, replace);
	}
	
	

	private static final String GLOB_PATTERN = "*\\[[0-9]*\\]\\.";
	/*
	 * <중복 파일, 파일명 만들어주기.>
	 * create는 파일을 생성할지 말지를 선택하는 플래그값.
	 * 파일을 실제 만들지 않을 경우, 다중스레드 작업에서 파일명이 중복될 여지가 있다.
	 * 알아보기만 하고 리턴할 경우, 다른 스레스가 같은 파일명을 얻어갈 수 있기 때문. 
	 */
	public final static Path overlapNumbering(Path root) {
		return overlapNumbering(root, false);
	}
	public final static Path overlapNumbering(Path root, boolean create) {
		if(!Files.exists(root, LinkOption.NOFOLLOW_LINKS)) {
			if(create) return createFile(root);
			else return root;
		}
		Path parentPath = root.getParent();
		String[] separate = FileUtils.separateFileName(root.getFileName().toString());
		String name = separate[0];
		String type = separate[1];
		String glob = changeGlobChar(name) + GLOB_PATTERN + type;
		int count = 1;
		try(DirectoryStream<Path> stream = Files.newDirectoryStream(parentPath, glob);) {
			Iterator<Path> iter = stream.iterator();
			while(iter.hasNext()) {
				iter.next();
				count++;
			}
		} catch (IOException e) {
			//일단은 런타임 에러를 발생시키자!
			throw new RuntimeException(e);
		}
		
		Path result = parentPath.resolve(name + "[" + count + "]." + type);
		return create ? createFile(result) : result;
	}
	// 파일 생성에 실패하면 런타임 에러 발생. 추후에 다른 방식으로 처리해보자!
	private final static Path createFile(Path filePath) {
		try {
			return Files.createFile(filePath);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * 파일명에 글롭 메타문자가 들어가있으면,
	 * 파일명을 이용해 글롭 패턴 검색시, 특정 메타문자로 인식해 검색에 오류가 생길 수 있다.
	 * 이를 방지하기 위해 글롭 메타 문자에는 역슬래시를 붙여준다. 
	 */
	private static final String[] GLOB_META_CHAR = {"\\[", "\\]", "\\{", "\\}"};
	public final static String changeGlobChar(String str) {
		for(String meta : GLOB_META_CHAR) {
			str = str.replaceAll(meta, "\\" + meta);
		}
		return str;
	}

	
	/*
	 * 컬렉션을 받아서 특정 파일에 덮어써준다.
	 */
	public static void writeLines(Collection<String> list, Path saveFile) throws Exception {
		try(BufferedWriter bw = Files.newBufferedWriter(saveFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
			for(String l : list) {
				bw.append(l).append("\r\n");
			}
			bw.flush();
		}
	}
	
	
	// 파일 확장자 추출
	public static String getFileType(String fileName) {
		return separateFileName(fileName)[1];
	}
	// 파일명과 확장자를 분리한다.
	public static String[] separateFileName(String fileName) {
		int index = fileName.lastIndexOf(".");
		return new String[]{
						fileName.substring(0, index),
						fileName.substring(index + 1, fileName.length())
					};
	}

}