package com.illumi.oms.data.model;

import java.util.List;
import java.util.Map;

public class SinglePokerResponse {
  
	private String roomName;
	private String roomid;
	private String handid;
	
 
	private List<SinglePokerItemInfo> before;
	
	//Flop发牌
	private String flopPokerDes;
	private List<SinglePokerItemInfo> flop;
	
	//Turn发牌
	private String turnPokerDes;
	private List<SinglePokerItemInfo> turn;
	
	//River发牌
	private String riverPokerDes;
	private List<SinglePokerItemInfo> river;
	
	//结算
	private String showDownDes ="结算";
	private List<SinglePokerItemInfo> result;
	
	//手牌
	private String handPokerDes ="手牌";
	private List<SinglePokerItemInfo> handPoker;
	
	
	
	
	
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getRoomid() {
		return roomid;
	}
	public void setRoomid(String roomid) {
		this.roomid = roomid;
	}
	public String getHandid() {
		return handid;
	}
	public void setHandid(String handid) {
		this.handid = handid;
	}
	public List<SinglePokerItemInfo> getBefore() {
		return before;
	}
	public void setBefore(List<SinglePokerItemInfo> before) {
		this.before = before;
	}

	public List<SinglePokerItemInfo> getFlop() {
		return flop;
	}
	public void setFlop(List<SinglePokerItemInfo> flop) {
		this.flop = flop;
	}

	
	public List<SinglePokerItemInfo> getTurn() {
		return turn;
	}
	public void setTurn(List<SinglePokerItemInfo> turn) {
		this.turn = turn;
	}
	public String getFlopPokerDes() {
		return flopPokerDes;
	}
	public void setFlopPokerDes(String flopPokerDes) {
		this.flopPokerDes = flopPokerDes;
	}
	public String getTurnPokerDes() {
		return turnPokerDes;
	}
	public void setTurnPokerDes(String turnPokerDes) {
		this.turnPokerDes = turnPokerDes;
	}
	public String getRiverPokerDes() {
		return riverPokerDes;
	}
	public void setRiverPokerDes(String riverPokerDes) {
		this.riverPokerDes = riverPokerDes;
	}
	public List<SinglePokerItemInfo> getRiver() {
		return river;
	}
	public void setRiver(List<SinglePokerItemInfo> river) {
		this.river = river;
	}
	public List<SinglePokerItemInfo> getResult() {
		return result;
	}
	public void setResult(List<SinglePokerItemInfo> result) {
		this.result = result;
	}
	public List<SinglePokerItemInfo> getHandPoker() {
		return handPoker;
	}
	public void setHandPoker(List<SinglePokerItemInfo> handPoker) {
		this.handPoker = handPoker;
	}
	public String getShowDownDes() {
		return showDownDes;
	}
	public void setShowDownDes(String showDownDes) {
		this.showDownDes = showDownDes;
	}
	public String getHandPokerDes() {
		return handPokerDes;
	}
	public void setHandPokerDes(String handPokerDes) {
		this.handPokerDes = handPokerDes;
	}
	
	
	
	
	
	
}
