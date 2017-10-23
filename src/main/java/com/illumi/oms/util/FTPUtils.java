package com.illumi.oms.util;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.jfinal.log.Logger;

public class FTPUtils {

  private final static Logger logger = Logger.getLogger(FTPUtils.class);

  private final static String USER = "imgops/pokerimg";

  private final static String PASSWORD = "1lumiimgops";

  private final static String HOST = "v0.ftp.upyun.com";

  private final static int PORT = 21;

  public static FTPClient getFtpClient() {
    FTPClient client = new FTPClient();
    try {
      client.connect(HOST, PORT);
      client.enterLocalPassiveMode();
      client.setFileType(FTP.BINARY_FILE_TYPE);
      if (client.login(USER, PASSWORD)) {
        showServerReply(client);
        if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
          return null;
        }
      }
    } catch (SocketException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return client;
  }

  private static void showServerReply(FTPClient ftpClient) {
    String[] replies = ftpClient.getReplyStrings();
    if (replies != null && replies.length > 0) {
      for (String aReply : replies) {
        logger.debug("SERVER: " + aReply);
      }
    }
  }
}
