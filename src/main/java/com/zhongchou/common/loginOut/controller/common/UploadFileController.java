package com.zhongchou.common.loginOut.controller.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.util.JsonUtil;

@Controller
public class UploadFileController extends BaseController {
	Logger logger=Logger.getLogger(UploadFileController.class);

	@RequestMapping(value = "/uploadPic.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public  ModelAndView uploadImg(MultipartHttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("UploadFileController.uploadPic  start");

		Map<String,Object> out = new HashMap<String,Object>();
		String picName="";

		//文件框id
        String updateP=request.getParameter("updateP");
		//图片格式
		String picType=request.getParameter("fileExtension");

		//验证图片格式
		if(picType.equals("jpg")||picType.equals("fif")||picType.equals("png")){

			try {
				request.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");

				//新图片名称
				picName = UUID.randomUUID().toString() + System.currentTimeMillis() + "." + picType;
				if(null!=updateP&&!"".equals(updateP)){
					//获取图片
					MultipartFile mf = request.getFile(updateP);

					if(null!=mf||!"".equals(mf)){

						if(mf.getSize() < 1048576){//判断图片不大于1M

							try {
								savePic(mf.getInputStream() , picName);
							} catch (IOException e) {
								//上传图片失败
					        	out.put(Constants.RET_CODE, "002");
					 			out.put(Constants.RET_MSG, "上传图片失败");
					 			JsonUtil.writeJson(response,out);
					 			return null;
							}
						}else{
							//图片大于1M
				        	out.put(Constants.RET_CODE, "004");
				 			out.put(Constants.RET_MSG, "图片大于1M");
				 			JsonUtil.writeJson(response,out);
				 			return null;
						}
					}else{
						//图片不存在
			        	out.put(Constants.RET_CODE, "003");
			 			out.put(Constants.RET_MSG, "图片不存在");
			 			JsonUtil.writeJson(response,out);
			 			return null;
					}
				}else{
					//图片不存在
		        	out.put(Constants.RET_CODE, "003");
		 			out.put(Constants.RET_MSG, "图片不存在");
		 			JsonUtil.writeJson(response,out);
		 			return null;
				}
			} catch (UnsupportedEncodingException e) {
				//上传图片失败
	        	out.put(Constants.RET_CODE, "002");
	 			out.put(Constants.RET_MSG, "上传图片失败");
	 			JsonUtil.writeJson(response,out);
	 			return null;
			}
		}else{

        	//图片文件格式错误
        	out.put(Constants.RET_CODE, "001");
 			out.put(Constants.RET_MSG, "图片文件格式错误");
 			JsonUtil.writeJson(response,out);
 			return null;
         }

		out.put("src", "/upload/image/"+picName);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
     }

	 private void savePic(InputStream inputStream, String fileName) {

         OutputStream os = null;
         try {

        	 //取得根目录路径  
     		 String rootPath=getClass().getResource("/").getFile();
     		 File rootFile = new File(rootPath);
     		 //保存文件的路径
     		 String path = rootFile.getParentFile().getParent() + "/upload/image";

             // 2、保存到临时文件
             // 1K的数据缓冲
             byte[] bs = new byte[1024];
             // 读取到的数据长度
             int len;
             // 输出的文件流保存到本地文件

             File tempFile = new File(path);
             if (!tempFile.exists()) {
                 tempFile.mkdirs();
             }
             os = new FileOutputStream(tempFile.getPath() + File.separator + fileName);
             // 开始读取
             while ((len = inputStream.read(bs)) != -1) {
                 os.write(bs, 0, len);
             }

         } catch (IOException e) {
             e.printStackTrace();
         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             // 完毕，关闭所有链接
             try {
                 os.close();
                 inputStream.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     }

}
