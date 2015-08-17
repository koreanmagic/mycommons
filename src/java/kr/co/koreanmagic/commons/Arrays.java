package kr.co.koreanmagic.commons;

import java.lang.reflect.Array;
import java.util.Collection;

public class Arrays {
	
	/* return (T[]) Arrays.copyOf(elementData, size, a.getClass());
     System.arraycopy(elementData, 0, a, 0, size);*/
	
	
	@SuppressWarnings("unchecked")
	public static<T> int roop(Collection<T> col, ArrayLoop<T> loop) {
		T[] array = col.toArray(
					(T[])Array.newInstance(loop.getComponentType(), col.size())
				);
		return roop(array, loop);
	}
	
	public static<T> int roop(T[] array, ArrayLoop<T> loop) {
		int len = array.length;
		for(int i = 0; i < len; i++) {
			
			if(i == 0) { // START
				if(!loop.start(array[i])) break;
			}
			else if(i == len -1) { // END
				if(loop.end(array[i])) break;
			}
			else { // ROOP
				if(loop.roop(array[i], i)) break;
			}
		}
		return len;
	}
	
	/*
	 * 루프의 시작, 중간, 끝을 구분해서 원소들을 넣어준다.
	 * false를 반환하면 루프를 멈춘다.
	 */
	public static interface ArrayLoop<T> {
		Class<T> getComponentType();
		boolean start(T t);
		boolean roop(T t, int index);
		boolean end(T t);
	}

}
