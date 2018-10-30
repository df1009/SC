package com.zhongchou.common.base;

import java.util.LinkedHashMap;
import java.util.Map;

public class SCMap extends LinkedHashMap {
	Map scValueMap = null;

	public SCMap(){
		super();
	}

	public SCMap(Map head,Map body){
		if(scValueMap == null){
			scValueMap = new LinkedHashMap();
			scValueMap.put("head", head);
			scValueMap.put("body", body);
		}
	}

	public LinkedHashMap getMap(){
		return (LinkedHashMap)scValueMap;
	}

	public void setValue(String key,String value){
		super.put(key, value);
	}
}
