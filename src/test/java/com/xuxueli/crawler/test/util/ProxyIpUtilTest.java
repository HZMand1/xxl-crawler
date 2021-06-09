package com.xuxueli.crawler.test.util;

import com.xuxueli.crawler.util.ProxyIpUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

/**
 * proxy ip util test
 *
 * @author xuxueli 2017-11-08 13:35:16
 */
public class ProxyIpUtilTest {

//    public static void main(String[] args) {
//        int code = ProxyIpUtil.checkProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("---", 80)), null);
//        System.out.println(code);
//    }

    public static void main(String[] args) throws IOException {
        List<String> ipList = ProxyIpUtil.getXieQuIpList();
        int i = 0;
        for (String s : ipList) {
            String[] ips = s.split(":");
            String ip = ips[0];
            String port = ips[1];
            int code = ProxyIpUtil.checkProxy(ip,Integer.valueOf(port),null);
            if (code == 200){
                i ++;
                System.out.println(s);
            }
        }
        System.out.println(i);

    }

}
