package com.randioo.randioo_platform_sdk;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.randioo.randioo_platform_sdk.entity.AccountInfo;
import com.randioo.randioo_platform_sdk.exception.AccountErrorException;
import com.randioo.randioo_platform_sdk.exception.AddActiveErrorException;
import com.randioo.randioo_platform_sdk.exception.AddMoneyErrorException;
import com.randioo.randioo_platform_sdk.utils.HttpUtils;

public class RandiooPlatformSdk {
	private Gson gson;
	private TypeAdapter<AccountInfo> accountInfoAdapter;
	private boolean debug;
	private String debugUrl = "http://10.0.51.18/APPadmin";
	private String url = "http://manager.app.randioo.com";
	private String activeProjectName;

	public RandiooPlatformSdk() {
		this.gson = new Gson();
		this.accountInfoAdapter = gson.getAdapter(AccountInfo.class);
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setActiveProjectName(String activeProjectName) {
		this.activeProjectName = activeProjectName;
	}

	public AccountInfo getAccountInfo(String account) throws Exception {
		Map<String, List<String>> params = new HashMap<>();
		params.put("id", Arrays.asList(account));
		params.put("key", Arrays.asList("f4f3f65d6d804d138043fbbd1843d510"));

		String resp = HttpUtils.get((debug ? debugUrl : url) + "/gateway/MaJiang/getMoney.php", params);
		AccountInfo accountInfo = accountInfoAdapter.fromJson(resp);
		if (accountInfo.randiooMoney == -1) {
			throw new AccountErrorException();
		}
		return accountInfo;
	}

	public void addMoney(String account, double money) throws Exception {
		Map<String, List<String>> params = new HashMap<>();
		params.put("id", Arrays.asList(account));
		params.put("key", Arrays.asList("f4f3f65d6d804d138043fbbd1843d510"));
		params.put("money_num", Arrays.asList(String.valueOf(Math.abs(money))));

		params.put("type", Arrays.asList(money > 0 ? "1" : "0"));

		String res = HttpUtils.get((debug ? debugUrl : url) + "/gateway/MaJiang/changeMoney.php", params);
		if (res.equals("0")) {
			throw new AddMoneyErrorException();
		}

	}

	public void addActive(String account) throws Exception {

		Map<String, List<String>> params = new HashMap<>();
		params.put("uid", Arrays.asList(account));
		params.put("key", Arrays.asList("f4f3f65d6d804d138043fbbd1843d510"));
		params.put("app_id", Arrays.asList(activeProjectName));

		String res = HttpUtils.get((debug ? debugUrl : url) + "/gateway/PhpServices/User/activeUser.php", params);
		System.out.println(res);
		if (res.equals("0")) {
			throw new AddActiveErrorException();
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// RandiooPlatformSdk activeUtils = new RandiooPlatformSdk();
		// activeUtils.debug = true ;
		// try {
		// activeUtils.addActive("770", "com.randioo.ddz");
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
}
