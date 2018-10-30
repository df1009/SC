package com.zhongchou.common.util;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.wicket.util.file.File;

public class UploadImageUtil {
	private static final String[] IMAGE_TYPE = {".jpg",".jpeg",".gif",".png",".bmp"};
	private static final int IMAGESIZE = 1048576;


	public static Map<String,String> uploadImage(String image,String path,String uploadPath,String pictureName) throws Exception{

		String header = "data:image/png;base64,";
		Map<String,String> outParam = new HashMap<String, String>();
		if (image.indexOf(header) != 0) {
			return null;
		}
		Boolean isImage = false;// 判断后缀格式是否为图片格式
		String imageType = "." +header.substring(11, 14);
		for (String type : IMAGE_TYPE) {
			if (imageType.equals(type)) {
				isImage = true;
			}
		}
		if (!isImage) {
			outParam.put("ret_code", "001");
			 outParam.put("ret_msg", "上传的文件类型不是图片类型");
			 outParam.put("imagePath", "");
			 return outParam;
		}
		// 去掉头部
		image = image.substring(header.length());
		String imgFilePath = path+File.separator+pictureName + imageType;
		String backPath = uploadPath+pictureName + imageType;
		File newFile = new File(imgFilePath);
		if(newFile.exists()){
			newFile.delete();
		}
		// 写入磁盘
		byte[] decoder = Base64.decodeBase64(image);
		if (decoder.length > IMAGESIZE) {
			 outParam.put("ret_code", "002");
			 outParam.put("ret_msg", "上传的图片不能超过1M");
			 outParam.put("imagePath", "");
			 return outParam;
		}
		FileOutputStream out = new FileOutputStream(imgFilePath);
		out.write(decoder);
		out.close();
		outParam.put("ret_code", "000");
		outParam.put("ret_msg", "上传的图片成功");
		outParam.put("imagePath", backPath);
		return outParam;
	}
}
