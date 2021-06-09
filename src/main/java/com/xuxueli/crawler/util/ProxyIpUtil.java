package com.xuxueli.crawler.util;

import com.xuxueli.crawler.conf.XxlCrawlerConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * proxy ip util
 *
 * @author xuxueli 2017-11-08 13:06:55
 */
public class ProxyIpUtil {
    private static Logger logger = LoggerFactory.getLogger(ProxyIpUtil.class);

    /**
     * check proxy
     *
     * @param proxy
     * @param validSite
     * @return int
     */
    public static int checkProxy(Proxy proxy, String validSite) {
        try {
            URL url = new URL(validSite != null ? validSite : XxlCrawlerConf.SITE_BAIDU);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
            httpURLConnection.setRequestProperty("User-Agent", XxlCrawlerConf.USER_AGENT_CHROME);
            httpURLConnection.setConnectTimeout(XxlCrawlerConf.TIMEOUT_MILLIS_DEFAULT);
            httpURLConnection.setReadTimeout(XxlCrawlerConf.TIMEOUT_MILLIS_DEFAULT);

            httpURLConnection.connect();
            int statusCode = httpURLConnection.getResponseCode();
            return statusCode;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return -2;
        }
    }

    public static int checkProxy(String host,Integer port, String validSite) {
        try {
            // 创建代理服务器
            InetSocketAddress addr = new InetSocketAddress(host, port);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
            // 访问目标网页
            URL url = new URL(validSite != null ? validSite : XxlCrawlerConf.SITE_BAIDU);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
            httpURLConnection.setConnectTimeout(XxlCrawlerConf.TIMEOUT_MILLIS_DEFAULT);
            httpURLConnection.setReadTimeout(XxlCrawlerConf.TIMEOUT_MILLIS_DEFAULT);
            httpURLConnection.connect();
            int statusCode = httpURLConnection.getResponseCode();
            return statusCode;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return -2;
        }
    }

    /**
     * 将输入流转换成字符串
     *
     * @param inStream
     * @return
     * @throws IOException
     */
    public static String IO2String(InputStream inStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inStream.read(buffer)) != -1) {
            result.write(buffer, 0, len);
        }
        String str = result.toString(StandardCharsets.UTF_8.name());
        return str;
    }

    /**
     * check proxy, repeat 3 times
     *
     * @param proxy
     * @param validSite
     * @return int
     */
    public static int checkProxyRepeat(Proxy proxy, String validSite) {
        for (int i = 0; i < 3; i++) {
            int statusCode = checkProxy(proxy, validSite);
            if (statusCode > 0) {
                return statusCode;
            }
        }
        return -2;
    }

    public static List<String> getXieQuIpList() throws IOException {
        String path = "http://api.xiequ.cn/VAD/GetIp.aspx?act=get&uid=68334&vkey=50E8A85F5151EEAC09188F144F68A8BE&num=20&time=30&plat=1&re=0&type=0&so=1&ow=1&spl=1&addr=&db=1";// 要获得html页面内容的地址

        URL url = new URL(path);// 创建url对象

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 打开连接
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        conn.setRequestProperty("contentType", "GBK"); // 设置url中文参数编码

        conn.setConnectTimeout(5 * 1000);// 请求的时间

        conn.setRequestMethod("GET");// 请求方式

        InputStream inStream = conn.getInputStream();
        // readLesoSysXML(inStream);

        BufferedReader in = new BufferedReader(new InputStreamReader(inStream, "GBK"));
        StringBuffer buffer = new StringBuffer();
        List<String> ipp = new ArrayList<String>();
        String line = "";
        // 读取获取到内容的最后一行,写入
        while ((line = in.readLine()) != null) {
            buffer.append(line);
            ipp.add(line);
        }
        List<String> ipList = new ArrayList<>();
        for (String s : ipp) {
            String[] ips = s.split(":");
            String ip = ips[0];
            String port = ips[1];
            int code = ProxyIpUtil.checkProxy(ip,Integer.valueOf(port),null);
            if (code == 200){
                ipList.add(s);
            }
        }
        return ipList;
    }

}
