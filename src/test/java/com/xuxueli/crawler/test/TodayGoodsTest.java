package com.xuxueli.crawler.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.parser.PageParser;
import com.xuxueli.crawler.proxy.ProxyMaker;
import com.xuxueli.crawler.proxy.strategy.RoundProxyMaker;
import com.xuxueli.crawler.util.ProxyIpUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  今日特价的分类和品种信息获取
 */
public class TodayGoodsTest {
    private static Logger logger = LoggerFactory.getLogger(TodayGoodsTest.class);

    public static void main(String[] args) throws IOException {

        // 代理池
        List<String> proxyList = ProxyIpUtil.getXieQuIpList();
        String[] ips = proxyList.get(0).split(":");
        String ip = ips[0];
        Integer port = Integer.valueOf(ips[1]);

        // 设置代理池
        ProxyMaker proxyMaker = new RoundProxyMaker()
                .addProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port)));

        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("https://www.amazon.com/-/zh/international-sales-offers/b/?ie=UTF8&node=15529609011&ref_=nav_cs_gb_intl_52df97a2eee74206a8343034e85cd058")
                .setWhiteUrlRegexs("https://www.amazon.com/-/zh/international-sales-offers/b/?.*")
                .setThreadCount(3)
                .setProxyMaker(proxyMaker)
                .setPageParser(new PageParser<Object>() {
                    @Override
                    public void parse(Document html, Element pageVoElement, Object pageVo) {

                        // 文件信息
                        String htmlData = html.html().replaceAll("[\\s\\t\\n\\r]", "");
                        String regex = "\"categories\":(.*?),\"urls";
                        Pattern r = Pattern.compile(regex);

                        // 现在创建 matcher 对象
                        Matcher m = r.matcher(htmlData);
                        if (m.find()) {
                            String temp = m.group(1);
                            JSONArray jsonArray = JSON.parseArray(temp);
                            for (Object o : jsonArray) {
                                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(o));
                                System.out.println("分类名称：" + jsonObject.getString("category") + "===" + "分类ID：" + jsonObject.getString("nodeId"));
                            }
                        }
                    }
                })
                .build();
        crawler.start(true);
    }
}
