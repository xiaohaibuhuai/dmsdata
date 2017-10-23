package com.illumi.oms.service;
import com.alibaba.dubbo.rpc.protocol.protobuf.ProtobufController;
import com.google.protobuf.ServiceException;
import com.jfinal.log.Logger;
import com.texaspoker.moment.TexasPokerECMSDubbo.ECMSService;
import com.texaspoker.moment.TexasPokerECMSDubbo.UserLoginRequest;
import com.texaspoker.moment.TexasPokerECMSDubbo;

public class DubboService {
	private static final Logger log = Logger.getLogger(DubboService.class);

	private ProtobufController pbController ;
   	//add by zhangp
   	private ECMSService.BlockingInterface pokerHistoryService;
   	//用户登录
   	public int userLogin(String account,String md5Pwd){
   		int result = 1;
   		try {
   	   		TexasPokerECMSDubbo.UserLoginRequest request = UserLoginRequest.newBuilder().setSID(account)
   	   																					.setSPWDMD5(md5Pwd)
   	   																					.build();
			TexasPokerECMSDubbo.UserLoginResponse response = pokerHistoryService.userLogin(pbController, request);
			if(response!=null){
				result = response.getIErrorCode();
				log.info("UserLoginResponse response.getIErrorCode():"+response.getIErrorCode());
				if(result == 0 && response.getStUserBaseInfoNet() != null){
					result = (int) response.getStUserBaseInfoNet().getUuid();
				}
			}else{
				result = 2;
				log.error("UserLoginResponse is null:"+pbController.errorText());
			}
		} catch (ServiceException e) {
			result = 2;
			log.error("调用户登录接口异常"+e.getMessage());
		} catch (Exception e){
			result = 2;
			log.error("用户登录异常"+e.getMessage());
		}
   		return result;
   	}

	public ProtobufController getPbController() {
		return pbController;
	}

	public void setPbController(ProtobufController pbController) {
		this.pbController = pbController;
	}
	
	public ECMSService.BlockingInterface getPokerHistoryService() {
		return pokerHistoryService;
	}
	public void setPokerHistoryService(ECMSService.BlockingInterface pokerHistoryService) {
		this.pokerHistoryService = pokerHistoryService;
	}	
 	
}
