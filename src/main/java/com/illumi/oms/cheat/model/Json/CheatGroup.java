package com.illumi.oms.cheat.model.Json;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class CheatGroup implements Serializable {

  /**
   * 
   * @fieldName: serialVersionUID
   * 
   * @fieldType: long
   * 
   * @Description: TODO
   * 
   */
  private static final long serialVersionUID = -5009046814529697561L;

  @SerializedName("groups")
  private String GroupId;

  private String uuid;

  public String getGroupId() {
    return GroupId;
  }

  public void setGroupId(String groupId) {
    GroupId = groupId;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
  
}
