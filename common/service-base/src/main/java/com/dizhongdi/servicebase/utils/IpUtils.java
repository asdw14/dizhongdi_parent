package com.dizhongdi.servicebase.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

/**
 * ClassName:IpUtils
 * Package:com.dizhongdi.servicebase.utils
 * Description:
 *      ip工具类
 * @Date: 2022/8/28 18:58
 * @Author:dizhongdi
 */
public class IpUtils {
    /**
     * 获取真实ip地址,不返回内网地址
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            if (ip.indexOf (",") > 0) {
                ip = ip.substring (0, ip.indexOf (","));
            }
            if (ip.equals ("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost ();
                } catch (Exception e) {
                    e.printStackTrace ();
                }
                ip = inet.getHostAddress ();
            }
        }

        if (request.getHeader("X-Real-IP") != null && !"".equals(request.getHeader("X-Real-IP")) && !"unknown".equalsIgnoreCase(request.getHeader("X-Real-IP"))) {
            ip = request.getHeader("X-Real-IP");
        }

        if (request.getHeader("Proxy-Client-IP") != null && !"".equals(request.getHeader("Proxy-Client-IP")) && !"unknown".equalsIgnoreCase(request.getHeader("Proxy-Client-IP"))) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (request.getHeader("WL-Proxy-Client-IP") != null && !"".equals(request.getHeader("WL-Proxy-Client-IP")) && !"unknown".equalsIgnoreCase(request.getHeader("WL-Proxy-Client-IP"))) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        System.out.println("用户ip："+ip);
        return ip;
    }
}
