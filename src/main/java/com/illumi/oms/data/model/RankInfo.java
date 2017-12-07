package com.illumi.oms.data.model;

public class RankInfo{
	private int rank;
	private long uuid;
	private String showid;
	private String nickname;
	private Object  change;
	private int isErro;
	
	
	public RankInfo(int rank, long uuid, String showid, String nickname, Object change) {
		super();
		this.rank = rank;
		this.uuid = uuid;
		this.showid = showid;
		this.nickname = nickname;
		this.change = change;
	}
	
	public RankInfo() {}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public String getShowid() {
		return showid;
	}

	public void setShowid(String showid) {
		this.showid = showid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Object getChange() {
		return change;
	}

	public void setChange(Object change) {
		this.change = change;
	}

	public int getIsErro() {
		return isErro;
	}

	public void setIsErro(int isErro) {
		this.isErro = isErro;
	};
	
	
	
	
}
