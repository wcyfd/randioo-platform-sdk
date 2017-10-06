package com.randioo.randioo_platform_sdk;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.randioo.randioo_platform_sdk.entity.AccountInfo;
import com.randioo.randioo_platform_sdk.entity.RoundOverEntity;
import com.randioo.randioo_platform_sdk.exception.AccountErrorException;
import com.randioo.randioo_platform_sdk.exception.AddActiveErrorException;
import com.randioo.randioo_platform_sdk.exception.AddMoneyErrorException;
import com.randioo.randioo_platform_sdk.utils.HttpUtils;

public class RandiooPlatformSdk {

    Logger logger = LoggerFactory.getLogger(RandiooPlatformSdk.class);

    private Gson gson;
    private TypeAdapter<AccountInfo> accountInfoAdapter;
    private String url /* = "http://manager.app.randioo.com" */;
    private String activeProjectName;
    private String accountUrl /* = url + "/gateway/MaJiang/getMoney.php" */;
    private String moneyUrl /* = url + "/gateway/MaJiang/changeMoney.php" */;
    private String activeUrl /*
                              * = url +
                              * "/gateway/PhpServices/User/activeUser.php"
                              */;
    private String roundOverUrl /*
                                 * = url +
                                 * "/Public/Admin/UserOnline/national_Information.php"
                                 */;

    public RandiooPlatformSdk() {
        this.gson = new Gson();
        this.accountInfoAdapter = gson.getAdapter(AccountInfo.class);
    }

    public void setActiveProjectName(String activeProjectName) {
        this.activeProjectName = activeProjectName;
    }

    public void setAccountUrl(String accountUrl) {
        this.accountUrl = accountUrl;
    }

    public void setMoneyUrl(String moneyUrl) {
        this.moneyUrl = moneyUrl;
    }

    public void setActiveUrl(String activeUrl) {
        this.activeUrl = activeUrl;
    }

    public void setRoundOverUrl(String roundOverUrl) {
        this.roundOverUrl = roundOverUrl;
    }

    public AccountInfo getAccountInfo(String account) throws Exception {
        Map<String, List<String>> params = new HashMap<>();
        params.put("id", Arrays.asList(account));
        params.put("key", Arrays.asList("f4f3f65d6d804d138043fbbd1843d510"));

        logger.info("accountInfo {}", params);
        String resp = HttpUtils.get(accountUrl, params);
        AccountInfo accountInfo = accountInfoAdapter.fromJson(resp);
        if (accountInfo.randiooMoney == -1) {
            throw new AccountErrorException();
        }
        return accountInfo;
    }

    public void addMoney(String account, double money) throws Exception {
        logger.info("燃点币变化 account={} money={}", account, money);
        Map<String, List<String>> params = new HashMap<>();
        params.put("id", Arrays.asList(account));
        params.put("key", Arrays.asList("f4f3f65d6d804d138043fbbd1843d510"));
        params.put("money_num", Arrays.asList(String.valueOf(Math.abs(money))));

        params.put("type", Arrays.asList(money > 0 ? "1" : "0"));

        logger.info("addMoney {}", params);
        String res = HttpUtils.get(moneyUrl, params);
        if (res.charAt(0) == '0') {
            throw new AddMoneyErrorException();
        }

    }

    public void addActive(String account) throws Exception {

        Map<String, List<String>> params = new HashMap<>();
        params.put("uid", Arrays.asList(account));
        params.put("key", Arrays.asList("f4f3f65d6d804d138043fbbd1843d510"));
        params.put("app_id", Arrays.asList(activeProjectName));

        String res = HttpUtils.get(activeUrl, params);

        logger.info("addActive {}", params);

        System.out.println(res);
        if (res.charAt(0) == '0') {
            throw new AddActiveErrorException();
        }

    }

    public void logRoundOver(RoundOverEntity roundOverEntity) {
        Map<String, List<String>> params = new HashMap<>();
        params.put("uid", Arrays.asList(roundOverEntity.account));
        params.put("nickname", Arrays.asList(roundOverEntity.name));
        params.put("app_id", Arrays.asList(roundOverEntity.appId));
        params.put("integral", Arrays.asList(roundOverEntity.point + ""));
        params.put("is_win", Arrays.asList((roundOverEntity.win ? 1 : 0) + ""));
        params.put("start_time", Arrays.asList(roundOverEntity.startTime + ""));
        params.put("end_time", Arrays.asList(roundOverEntity.endTime + ""));
        params.put("room_id", Arrays.asList(roundOverEntity.roomId));

        try {
            logger.info("logRoundOver {}  {}", roundOverUrl, params);

            String result = HttpUtils.post(roundOverUrl, params);
            logger.info("logRoundOver {} response {}", params, result);
        } catch (Exception e) {
            e.printStackTrace();
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
