package com.illumi.dms.common.utils;

import com.illumi.oms.common.utils.ValidateObjectUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.node.Node;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ESUtils {
    private static Log logger = LogFactory.getLog(ESUtils.class);
    private static String HOST=""; //集群地址
    private static String PORT=""; //集群端口
    private static String CLUSTER_NAME="";  //集群名称
    private static String TRANSPORT_SINFF="true"; //客户端是否探测整个集群的状态
    private static TransportClient client;
    static {
        try {
            Properties properties = new Properties();
            //properties.load(new FileInputStream());
            System.out.print("--------------------"+System.getProperty("user.dir"));
            Resource resource = new ClassPathResource("es.properties");
            System.out.print(resource.getFile().getPath());
            properties.load(resource.getInputStream());
            HOST=properties.getProperty("es.host");
            PORT=properties.getProperty("es.port");
            CLUSTER_NAME=properties.getProperty("es.cluster.name");
            TRANSPORT_SINFF=properties.getProperty("es.transport.sinff");
            logger.info(String.format("ClusterName:%s; Host:%s; Port:%s; TransportSinff:%s",CLUSTER_NAME,HOST,PORT,TRANSPORT_SINFF));
            System.out.print(String.format("ClusterName:%s; Host:%s; Port:%s; TransportSinff:%s",CLUSTER_NAME,HOST,PORT,TRANSPORT_SINFF));
        }catch (IOException ex){
            logger.error("es资源文件没找到",ex);
        }


    }

    public  static TransportClient  getClient(){
        try{
            if(ValidateObjectUtil.isBlank(client)){
                synchronized (client){
                    if(ValidateObjectUtil.isBlank(client)){
                        init();
                        return client;
                    }else{
                        return client;
                    }
                }
            }else{
                return  client;
            }
        }catch (Exception ex) {
            ex.printStackTrace();
            logger.error("es client exception", ex);
            return null; //getClient();
        }
    }

    public static void init(){

    }

}
