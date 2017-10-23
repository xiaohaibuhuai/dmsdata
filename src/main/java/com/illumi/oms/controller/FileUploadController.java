package com.illumi.oms.controller;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
import com.illumi.oms.common.utils.PicUploadUtil;
import com.illumi.oms.common.utils.StringUtil;
import com.illumi.oms.common.utils.UpYun.PARAMS;
import com.illumi.oms.model.RaceMtt;
import com.jayqqaa12.jbase.jfinal.ext.ctrl.EasyuiController;
import com.jfinal.ext.plugin.config.ConfigKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Logger;
import com.jfinal.upload.UploadFile;

@ControllerBind(controllerKey = "/file/upload")
public class FileUploadController extends EasyuiController<RaceMtt> {

    private static final Logger log = Logger.getLogger(FileUploadController.class);
    public void imageUpload() {
        log.info(StringUtil.report(this.keepModel(this.getClass())));
        // 最大上传10M的图片
        UploadFile uploadFile = getFile(getPara("imgFile"), "", 10 * 1024 * 1024, "UTF-8");
        // 异步上传时，无法通过uploadFile.getFileName()获取文件名
        String fileName = getPara("fileName");
        String[] arr = fileName.split("\\\\");
        if (arr.length > 1) {
            fileName = arr[arr.length - 1];
        }
        // 异步上传时，无法通过File source = uploadFile.getFile();获取文件
        // File source = new File(uploadFile.getUploadPath() + fileName); //
        // 获取临时文件对象
        long timetamp = System.currentTimeMillis();
        File source = new File(uploadFile.getSaveDirectory() + fileName);
        String extension = "";
        if (fileName.lastIndexOf(".") > 0) {
            extension = fileName.substring(fileName.lastIndexOf("."));
        }
        JSONObject json = new JSONObject();
        if (".png".equals(extension) || ".jpg".equals(extension) || ".gif".equals(extension)
                || ".jpeg".equals(extension) || ".bmp".equals(extension)) {
            try {
                PicUploadUtil.uploadPicBytimetamp(source, timetamp, extension);
                // 设置缩略图的参数
                Map<String, String> params = new HashMap<String, String>();
                // 设置缩略图类型，必须搭配缩略图参数值（KEY_VALUE）使用，否则无效
                params.put(PARAMS.KEY_X_GMKERL_TYPE.getValue(), PARAMS.VALUE_FIX_BOTH.getValue());
                // 设置缩略图参数值，必须搭配缩略图类型（KEY_TYPE）使用，否则无效
                params.put(PARAMS.KEY_X_GMKERL_VALUE.getValue(), "150x150");
                // 设置缩略图的质量，默认 95
                params.put(PARAMS.KEY_X_GMKERL_QUALITY.getValue(), "95");
                // 设置缩略图的锐化，默认锐化（true）
                params.put(PARAMS.KEY_X_GMKERL_UNSHARP.getValue(), "true");
                // 若在 upyun 后台配置过缩略图版本号，则可以设置缩略图的版本名称
                // 注意：只有存在缩略图版本名称，才会按照配置参数制作缩略图，否则无效
                params.put("prefix", "small_");
                // 上传缩略图
                PicUploadUtil.uploadHandlePicBytimetamp(source, params, timetamp);
                json.put("error", 0);
                json.put("src", ConfigKit.getStr("ops.img.url") + ConfigKit.getStr("ops.dir.root")
                        + "/" + timetamp + "A" + extension); // 相对地址，显示图片用
                source.delete();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                json.put("error", 1);
                json.put("message", "上传出现错误，请稍后再上传");
            } catch (Exception e) {
                json.put("error", 1);
                json.put("message", "文件写入服务器出现错误，请稍后再上传");
            }
        } else {
            source.delete();
            json.put("error", 1);
            json.put("message", "只允许上传png,jpg,jpeg,gif,bmp类型的图片文件");
        }
        renderJson(json.toJSONString());
    }
}
