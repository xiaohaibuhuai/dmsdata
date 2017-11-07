package com.illumi.oms.cheat.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class StringUtil extends StringUtils {

  private static final String letterChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";


  /**
   * 参数为返回随机数的长度
   *
   * @param length 需要生成的字符串长度
   * @return
   */
  public static String generateString(int length) {
    StringBuffer sb = new StringBuffer();
    Random random = new Random();
    for (int i = 0; i < length; i++) {
      sb.append(letterChar.charAt(random.nextInt(letterChar.length())));
    }
    return sb.toString();
  }

  /**
   * 将字符串型的json数据转换为List<map>返回
   *
   * @return
   */
  // public static List<Map> getMapFromJsonString(String jsonString) {
  // List<Map> list = JsonUtil.getListMapFromJsonString(jsonString);
  // return list;
  // }

  public static boolean isNullOrEmpty(String value) {
    if (value != null)
      return value.length() == 0;
    return true;
  }

  public static String getPathFileName(String filePath, boolean... includeType) {
    if (isNullOrEmpty(filePath))
      return null;
    int i = filePath.lastIndexOf("\\");
    if (i == -1) {
      i = filePath.lastIndexOf("/");
    }
    int j = filePath.lastIndexOf(".");
    if (i == -1 || j == -1)
      return null;
    if (includeType.length > 0) {
      return filePath.substring(i + 1);
    }
    return filePath.substring(i + 1, j);
  }

  public static boolean isIn(String substring, String[] source) {
    if (source == null || source.length == 0) {
      return false;
    }
    for (int i = 0; i < source.length; i++) {
      String aSource = source[i];
      if (aSource.equals(substring)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 去除前后空格，如果为空则返回空字符串
   * 
   * @param value
   * @return
   */
  public static String getStringValue(String value) {
    if (null == value || "".equals(value.trim()))
      return "";
    return value.trim();
  }

  /*
   * mysql 分页
   */
  public static String forPaginate(int pageNumber, int pageSize, String sql) {
    int offset = pageSize * (pageNumber - 1);
    StringBuilder ret = new StringBuilder();
    ret.append(sql);
    ret.append(" limit ").append(offset).append(", ").append(pageSize); // limit can use one or two
                                                                        // '?' to pass paras
    return ret.toString();
  }

  /*
   * 截取过长字段
   */
  public static String FixedString(String value, int number) {
    if (value != null && !"".equals(value)) {
      if (value.length() > number) {
        value = value.substring(0, number) + "...";
      }
      return value.trim();
    } else {
      return "";
    }
  }

  /*
   * 时间戳转可视日期
   */
  public static String timeStampToDate(long datetime, String defaultValue) {
    String date = "0";
    try {
      if (datetime != 0) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = sdf.format(new Date(datetime));
      } else {
        date = defaultValue;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return date;
  }

  public static String timeStampToDate(long datetime, int fixed, String defaultValue) {
    String date = "0";
    try {
      if (datetime != 0) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = sdf.format(new Date(datetime));
        date = date.substring(0, fixed);
      } else {
        date = defaultValue;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return date;
  }

  public static String timeStampToDate(long datetime) {
    String date = "0";
    try {
      if (datetime != 0) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = sdf.format(new Date(datetime));
      } else {
        date = "未加入过牌局";
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return date;
  }

  public static String loginTimeStampToDate(long datetime, String defaultValue) {
    String date = "0";
    try {
      if (datetime != 0) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = sdf.format(new Date(datetime));
      } else {
        date = defaultValue;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return date;
  }

  public static String loginTimeStampToDate(long datetime, int fixed, String defaultValue) {
    String date = "0";
    try {
      if (datetime != 0) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = sdf.format(new Date(datetime));
        date = date.substring(0, fixed);
      } else {
        date = defaultValue;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return date;
  }

  public static String loginTimeStampToDate(long datetime) {
    String date = "0";
    try {
      if (datetime != 0) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = sdf.format(new Date(datetime));
      } else {
        date = "未登录过游戏";
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return date;
  }

  /*
   * 时间戳转可视日期
   */
  public static String timeStampToDateNormal(long datetime) {
    String date = "0";
    try {
      if (datetime != 0) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = sdf.format(new Date(datetime));
      } else {
        date = "";
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return date;
  }

  /*
   * 时间戳转可视日期
   */
  public static String timeStampToDateNormal(long datetime, int fixed) {
    String date = "0";
    try {
      if (datetime != 0) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = sdf.format(new Date(datetime));
      } else {
        date = "";
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return date.substring(0, fixed);
  }

  /*
   * 获取当前时间
   */
  public static String getNowTime() {
    String date = "0";
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      date = sdf.format(new Date());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return date;
  }

  /*
   * 
   */
  public static String division(long i, long j) {
    DecimalFormat df = new DecimalFormat("0.0");
    return df.format((float) i / j);
  }
}
