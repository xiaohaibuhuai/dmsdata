package com.illumi.oms.cheat.model.Json;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.illumi.oms.cheat.utils.StringUtil;

public class CheatInfo implements Serializable {

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


  @SerializedName("user_01")
  private String user01;

  @SerializedName("user_02")
  private String user02;

  @SerializedName("score")
  private String score;

  @SerializedName("times_sames_in_month")
  private int sameTimes;

  public String getUser01() {
    return user01;
  }

  public void setUser01(String user01) {
    this.user01 = user01;
  }

  public String getUser02() {
    return user02;
  }

  public void setUser02(String user02) {
    this.user02 = user02;
  }

  public String getScore() {
    return score;
  }

  public void setScore(String score) {
    this.score = score;
  }

  public int getSameTimes() {
    return sameTimes;
  }

  public void setSameTimes(int sameTimes) {
    this.sameTimes = sameTimes;
  }

  public boolean contains(String uuid) {
    return StringUtil.equals(uuid, user01) || StringUtil.equals(uuid, user02);
  }

  public String getPartner(String uuid) {
    if (contains(uuid)) {
      if (StringUtil.equals(uuid, user01)) {
        return user02;
      } else if (StringUtil.equals(uuid, user02)) {
        return user01;
      } else {
        return null;
      }
    } else {
      return null;
    }
  }
}
