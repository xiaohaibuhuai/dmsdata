package com.illumi.oms.cheat.model.Json;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class PlayTimes implements Serializable {

  /**
   * 
   * @fieldName: serialVersionUID
   * 
   * @fieldType: long
   * 
   * @Description: TODO
   * 
   */
  private static final long serialVersionUID = 6484460641407747978L;

  @SerializedName("uuid")
  private String uuid;

  @SerializedName("play_times")
  private String playTimes;

  @SerializedName("record_times")
  private String recordTimes;

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getPlayTimes() {
    return playTimes;
  }

  public void setPlayTimes(String playTimes) {
    this.playTimes = playTimes;
  }

  public String getRecordTimes() {
    return recordTimes;
  }

  public void setRecordTimes(String recordTimes) {
    this.recordTimes = recordTimes;
  }

}
