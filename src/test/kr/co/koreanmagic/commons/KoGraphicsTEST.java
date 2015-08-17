package kr.co.koreanmagic.commons;

import static org.junit.Assert.*;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

import org.junit.Test;

public class KoGraphicsTEST {

	@Test
	public void test() throws Exception {
		//KoGraphics.thumbnail("g:/test2.jpg", "g:/test2-thumb.jpg", 100);
		Thumbnails.of("g:/test2.jpg").height(100).outputFormat("jpg").toFiles(Rename.SUFFIX_HYPHEN_THUMBNAIL);
		//System.out.println((double)((int)3/4));
	}

}
