package com.dizhongdi.servicemsm.service.impl;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.dizhongdi.servicemsm.service.MsmService;
import com.dizhongdi.servicemsm.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:MsmServiceImpl
 * Package:com.dizhongdi.servicemsm.service.impl
 * Description:
 *
 * @Date: 2022/7/7 23:35
 * @Author:dizhongdi
 */
@Service
public class MsmServiceImpl implements MsmService {
        //容联云发送手机验证码
        @Override
        public boolean send(String phone, String templateCode,String code) {
            //生产环境请求地址：app.cloopen.com
            String serverIp = "app.cloopen.com";
            //请求端口
            String serverPort = "8883";
            //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
            String accountSId = "8aaf07087a331dc7017b0aaa2bde4088";
            String accountToken = "70955fe6dc7542d789e5e6cd082c8a29";
            //请使用管理控制台中已创建应用的APPID
            String appId = "8aaf070881ad8ad40181d9483d3907ea";
            CCPRestSmsSDK sdk = new CCPRestSmsSDK();
            sdk.init(serverIp, serverPort);
            sdk.setAccount(accountSId, accountToken);
            sdk.setAppId(appId);
            sdk.setBodyType(BodyType.Type_JSON);
            //发送短信至手机号
            String to = phone;
            //短信模板
            String templateId= templateCode;
            //验证码，分钟内到期

            String[] datas = {code,"5"};
            HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas);
            if("000000".equals(result.get("statusCode"))){
                //正常返回输出data包体信息（map）
                return true;

//            HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
//            Set<String> keySet = data.keySet();
//            for(String key:keySet){
//                Object object = data.get(key);
//                System.out.println(key +" = "+object);
//            }
            }else{
                //异常返回输出错误码和错误信息
                System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
                return true;
            }
        }

    @Override
    public String getCode() {
        int random = (int) (Math.random() * 1000000);
        System.out.println(random);
        String code = String.format("%06d", random);
        System.out.println(code);
        return code;
    }

//    public String aliSend(String mobile){
//
//        String host = "https://gyytz.market.alicloudapi.com";
//        String path = "/sms/smsSend";
//        String method = "POST";
//        String appcode = "e6fc0352fb9d4063afeb3c9fbf69c933";
//        Map<String, String> headers = new HashMap<String, String>();
//        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
//        headers.put("Authorization", "APPCODE " + appcode);
//        Map<String, String> querys = new HashMap<String, String>();
//        querys.put("mobile", mobile);
//        String params = "**code**:" + getCode() + ",**minute**:5";
//        querys.put("param", params);
//        querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
//        querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");
//        Map<String, String> bodys = new HashMap<String, String>();
//
//        try {
//            /**
//             * 重要提示如下:
//             * HttpUtils请从
//             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
//             * 下载
//             *
//             * 相应的依赖请参照
//             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
//             */
//            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
//            System.out.println(response.toString());
//            //获取response的body
//            System.out.println(EntityUtils.toString(response.getEntity()));
//            return response.toString();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "error";
//        }
//    }

    public boolean aliSend(String mobile,String code){
        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "POST";
        String appcode = "e6fc0352fb9d4063afeb3c9fbf69c933";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        String content = "code:"+ code + ",expire_at:5";
        bodys.put("content", content);
        bodys.put("phone_number", mobile);
        bodys.put("template_id", "TPL_0001");

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            System.out.println(EntityUtils.toString(response.getEntity()));
//            re = EntityUtils.toString(response.getEntity());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
