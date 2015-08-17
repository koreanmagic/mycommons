package kr.co.koreanmagic.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class CollectionView {
	
	private final static MapView mapView = new MapViewImpl(); 
	
	
	/* map을 문자열로 */
	public static List<String> map(Map<?, ?> map) {
		return map(map, mapView);
	}
	public static List<String> map(Map<?, ?> map, MapView view) {
		List<String> result = new ArrayList<>(map.size());
		for(Entry<?, ?> entry : map.entrySet()) {
			result.add(view.view(entry.getKey(), entry.getValue()));
		}
		return result;
	}
	
	
	/* value가 배열인 map toString()을 map식 toString()으로 */
	public static String mapToString(Map<String, String[]> map) {
		StringBuilder sb = new StringBuilder("{");
		Set<Entry<String, String[]>> entrys = map.entrySet();
		for(Entry<String, String[]> entry : entrys) {
			sb.append(entry.getKey()).append("=").append( StringUtils.join(entry.getValue(), ",")).append("&");
		}
		return sb.delete(sb.length()-1, sb.length()).append("}").toString();
		
	}
	
	
	static interface MapView {
		String view(Object key, Object value);
	}
	
	static class MapViewImpl implements MapView {

		@Override
		public String view(Object key, Object value) {
			return key + " : " + value;
		}
		
	}

}
