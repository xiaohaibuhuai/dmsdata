package com.illumi.oms.common.utils;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.illumi.oms.common.utils.UpYun.FolderItem;
import com.jfinal.ext.plugin.config.ConfigKit;
import com.jfinal.log.Logger;

public class PicUploadUtil {
	
	private static final Logger log = Logger.getLogger(PicUploadUtil.class);

	
	private static final UpYun upyun = new UpYun(ConfigKit.getStr("ops.bucket.name"), ConfigKit.getStr("ops.oper.name"), ConfigKit.getStr("ops.oper.pwd"));

	/**
	 * 
	 * uploadPic(上传原始图片)
	 * (只上传原始图片，不对原始图片处理用此方法)
	 * @param file
	 * @throws Exception
	 * void
	 * @exception
	 */
	public static  void uploadPic(File file) throws Exception {
		

		String fileName = file.getName();
		// 要传到upyun后的文件路径
		String filePath = ConfigKit.getStr("ops.dir.root")+"/" +fileName;

		upyun.setTimeout(10);
		// 设置待上传文件的 Content-MD5 值
		// 如果又拍云服务端收到的文件MD5值与用户设置的不一致，将回报 406 NotAcceptable 错误
		upyun.setContentMD5(UpYun.md5(file));

		// 设置待上传文件的"访问密钥"
		// 注意：
		// 仅支持图片空！，设置密钥后，无法根据原文件URL直接访问，需带URL后面加上（缩略图间隔标志符+密钥）进行访问
		// 举例：
		// 如果缩略图间隔标志符为"!"，密钥为"bac"，上传文件路径为"/folder/test.jpg"，
		// 那么该图片的对外访问地址为：http://空间域名 /folder/test.jpg!bac
		//upyun.setFileSecret("ops");
		
		// 上传文件，并自动创建父级目录（最多10级）
		if(!upyun.writeFile(filePath, file, true)){
			log.error("上传封面图失败："+fileName);
			throw new Exception("上传封面图失败!");
		}

	}
	
	/**
	 * 
	 * uploadPic(上传原始图片)
	 * 文件名前加时间戳
	 * @param file
	 * @throws Exception
	 * void
	 * @exception
	 */
	public static  void uploadPicBytimetamp(File file,long timestamp,String extension) throws Exception {
		

		String fileName = timestamp+"A"+extension;
		// 要传到upyun后的文件路径
		String filePath = ConfigKit.getStr("ops.dir.root")+"/" +fileName;

		upyun.setTimeout(10);
		// 设置待上传文件的 Content-MD5 值
		// 如果又拍云服务端收到的文件MD5值与用户设置的不一致，将回报 406 NotAcceptable 错误
		upyun.setContentMD5(UpYun.md5(file));

		// 设置待上传文件的"访问密钥"
		// 注意：
		// 仅支持图片空！，设置密钥后，无法根据原文件URL直接访问，需带URL后面加上（缩略图间隔标志符+密钥）进行访问
		// 举例：
		// 如果缩略图间隔标志符为"!"，密钥为"bac"，上传文件路径为"/folder/test.jpg"，
		// 那么该图片的对外访问地址为：http://空间域名 /folder/test.jpg!bac
		//upyun.setFileSecret("ops");
		
		// 上传文件，并自动创建父级目录（最多10级）
		if(!upyun.writeFile(filePath, file, true)){
			log.error("上传封面图失败："+fileName);
			throw new Exception("上传封面图失败!");
		}

	}
	
	/**
	 * 上传文件
	 * @param file
	 * @throws Exception
	 */
	public static  void uploadFile(File file,String dirRoot) throws Exception {
		

		String fileName = file.getName();
		// 要传到upyun后的文件路径
		String filePath = dirRoot+"/" +fileName;

		upyun.setTimeout(10);
		// 设置待上传文件的 Content-MD5 值
		// 如果又拍云服务端收到的文件MD5值与用户设置的不一致，将回报 406 NotAcceptable 错误
		upyun.setContentMD5(UpYun.md5(file));

	
		
		// 上传文件，并自动创建父级目录（最多10级）
		if(!upyun.writeFile(filePath, file, true)){
			log.error("上传文件失败："+fileName);
			throw new Exception("上传文件失败!");
		}

	}
	
	
	/**
	 * 
	 * uploadHandlePic(上传图片，此方法会根据params参数对图片进行缩略，旋转，裁剪等处理)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param file
	 * @param params
	 * @throws Exception
	 * void
	 * @exception
	 */
	public static  void uploadHandlePic(File file,Map<String, String> params) throws Exception {
		
		String fileName = file.getName();
		String filePath = ConfigKit.getStr("ops.dir.root")+"/"+params.get("prefix") +fileName;
		upyun.setTimeout(10);
		upyun.setFileSecret("ops");
		if(!upyun.writeFile(filePath, file, true,params)){
			log.error("上传封面图失败："+fileName);
			throw new Exception("上传封面图失败!");
		}

	}
	/**
	 * 
	 * uploadHandlePic(上传图片，此方法会根据params参数对图片进行缩略，旋转，裁剪等处理)
	 * 文件名前加时间戳
	 * @param file
	 * @param params
	 * @throws Exception
	 * void
	 * @exception
	 */
	public static  void uploadHandlePicBytimetamp(File file,Map<String, String> params,long timetamp) throws Exception {
		
		String fileName = timetamp+""+file.getName();
		String filePath = ConfigKit.getStr("ops.dir.root")+"/"+params.get("prefix") +fileName;
		upyun.setTimeout(15);
		upyun.setFileSecret("ops");
		if(!upyun.writeFile(filePath, file, true,params)){
			log.error("上传封面图失败："+fileName);
			throw new Exception("上传封面图失败!");
		}

	}
	
	/**
	 * 获取文件信息
	 * 
	 * @param filePath
	 *            文件路径（包含文件名）
	 * 
	 * @return 文件信息 或 null
	 */
	public static  Map<String, String> getFileInfo(String filePath) {

		try{
			return upyun.getFileInfo(filePath);
		}catch(Exception e){
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 读取目录列表
	 * 
	 * @param path
	 *            目录路径
	 * 
	 * @return List<FolderItem> 或 null
	 */
	public static List<FolderItem> readDir(String path) {
		try{
			return upyun.readDir(path);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
}
