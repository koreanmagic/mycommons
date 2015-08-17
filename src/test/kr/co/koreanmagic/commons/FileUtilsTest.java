package kr.co.koreanmagic.commons;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

public class FileUtilsTest {

	Logger logger = Logger.getLogger("test");
	
	
	@Test
	public void 짜투리경로만_돌려주기() {
		String expected = "dir3\\dir4\\file.txt";
		
		Path targetPath = Paths.get("G:/dir0/dir1/dir2/dir3/dir4/file.txt");
		Path rootPath = Paths.get("G:/dir0/dir1/dir2");
		Path result = FileUtils.cutTailPath(targetPath, rootPath);
		
		assertThat(expected, is(result.toString()));
	}
	
	@Test
	public void 파일명에_못쓰는_문자_바꾸기() {
		String name = "학\\/\\교종이@||magi.txt";
		out(FileUtils.removeEscape(name, "_"));
	}
	
	
	@Test(expected=RuntimeException.class)
	public void 짜투리경로만_돌려주기_에러() {
		String expected = "dir3\\dir4\\file.txt";
		
		Path targetPath = Paths.get("G:/dir0/dir1/dir2/dir3/dir4/file.txt");
		Path rootPath = Paths.get("G:/dir0/dir1/dir21");  // 경로가 다름!!
		Path result = FileUtils.cutTailPath(targetPath, rootPath);
		
		assertThat(expected, is(result.toString()));
	}
	
	@Test
	public void TEST_relativePathtoPath() throws Exception {
		Path target = Paths.get("G:/target");
		Path expected = Paths.get("G:/target/3/4/5");
		
		Path sourceFrom = Paths.get("G:/source/1/2");
		Path sourceTo = Paths.get("G:/source/1/2/3/4/5");
		
		Path test = sourceFrom.relativize(sourceTo); 
		
		Path result = FileUtils.relativePathToPath(target, sourceFrom, sourceTo);
		assertThat(result, is(expected));
		
	}
	
	@Test
	public void 확장자() {
		
		String file = "/name/nmae/한컴기획_명함(대표)_92-52mm.pdf";
		Path path = Paths.get(file);
		
		out(path.toFile().toString());
		
	}
	
	@Test
	public void 확장자_점포함() {
		String file = "한컴기획_명함(대표)_92-52mm.pdf";
		assertThat(FileUtils.getFileType(file), is("pdf"));
	}
	
	
	private void out(Object obj) {
		System.out.println(obj);
	}

}
