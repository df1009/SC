package com.zhongchou.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.poi.ss.formula.functions.T;
import org.bouncycastle.util.encoders.Base64;

import com.google.gson.Gson;
import com.yanshang.util.StringUtils;

public class SerializeUtil {
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
		}
		return null;
	}

	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
		}
		return null;
	}
	public static String serializeString(Object object) {
		return Base64.toBase64String(serialize(object));
	}
	public static Object unserializeString(String str) {
		if(StringUtils.isEmpty(str)){
			return null;
		}
		return unserialize(Base64.decode(str));
	}
	public static String getGson(Object object) {
		Gson gson = new Gson();
		return gson.toJson(object);
	}
	public static Object setGson(String json, Class<T> classOfT) {
		Gson gson = new Gson();
		return gson.fromJson(json,classOfT);
	}

}