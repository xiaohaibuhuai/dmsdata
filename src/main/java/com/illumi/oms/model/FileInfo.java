package com.illumi.oms.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileInfo implements Serializable {

  /**
   * 
   * @fieldName: serialVersionUID
   * 
   * @fieldType: long
   * 
   * @Description: TODO
   * 
   */
  private static final long serialVersionUID = 1L;

  private final static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");

  private long createTime;

  private String creater;

  private String fileRealName;

  private String fileDisplayName;

  private transient String fileContent;
  
  private String url;
  
  private String editContent;
  
  private String bgUrl;

  public String getCreateTime() {
    return SDF.format(new Date(createTime));
  }

  public long getCreateTimeMillions() {
    return createTime;
  }

  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }

  public String getCreater() {
    return creater;
  }

  public void setCreater(String creater) {
    this.creater = creater;
  }

  public String getFileRealName() {
    return fileRealName;
  }

  public void setFileRealName(String fileRealName) {
    this.fileRealName = fileRealName;
  }

  public String getFileContent() {
    return fileContent;
  }

  public void setFileContent(String fileContent) {
    this.fileContent = fileContent;
  }

  public String getFileDisplayName() {
    return fileDisplayName;
  }

  public void setFileDisplayName(String fileDisplayName) {
    this.fileDisplayName = fileDisplayName;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getEditContent() {
    return editContent;
  }

  public void setEditContent(String editContent) {
    this.editContent = editContent;
  }

  public String getBgUrl() {
    return bgUrl;
  }

  public void setBgUrl(String bgUrl) {
    this.bgUrl = bgUrl;
  }
  
}
