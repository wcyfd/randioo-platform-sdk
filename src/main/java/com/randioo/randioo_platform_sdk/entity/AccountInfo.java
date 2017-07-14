package com.randioo.randioo_platform_sdk.entity;

import com.google.gson.annotations.SerializedName;

public class AccountInfo {
	@SerializedName("randioo_money")
	public int randiooMoney;
	@SerializedName("nickname")
	public String nickName;
	@SerializedName("headimgurl")
	public String headImgUrl;
	@SerializedName("sex")
	public int sex;

	@Override
	public String toString() {
		return "randiooMoney=" + randiooMoney + ",nickName=" + nickName + ",headimgurl=" + headImgUrl + ",sex=" + sex;
	}
}
