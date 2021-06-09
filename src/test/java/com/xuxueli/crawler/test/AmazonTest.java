package com.xuxueli.crawler.test;

import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.annotation.PageFieldSelect;
import com.xuxueli.crawler.annotation.PageSelect;
import com.xuxueli.crawler.conf.XxlCrawlerConf;
import com.xuxueli.crawler.parser.PageParser;
import com.xuxueli.crawler.proxy.ProxyMaker;
import com.xuxueli.crawler.proxy.strategy.RoundProxyMaker;
import com.xuxueli.crawler.util.ProxyIpUtil;
import lombok.Data;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AmazonTest {
    private static Logger logger = LoggerFactory.getLogger(AmazonTest.class);

    @Data
    @PageSelect(cssQuery = "#search .s-desktop-width-max .s-desktop-content .s-matching-dir .sg-col-inner >span .s-main-slot .s-result-item")
    public static class PageVo {

        @PageFieldSelect(cssQuery = "a-text-normal", selectType = XxlCrawlerConf.SelectType.TEXT)
        private String goodsName;

        @PageFieldSelect(cssQuery = "td:eq(1)", selectType = XxlCrawlerConf.SelectType.TEXT)
        private int port;
    }

    public static void main(String[] args) throws IOException {

        // 代理池
        List<String> proxyList = ProxyIpUtil.getXieQuIpList();
        String[] ips = proxyList.get(0).split(":");
        String ip = ips[0];
        Integer port = Integer.valueOf(ips[1]);

        // 设置代理池
        ProxyMaker proxyMaker = new RoundProxyMaker()
                .addProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port)));

        // 参数
//        Map<String, String> paramMap = new HashMap<>();
//        paramMap.put("i", "specialty-aps");
//        paramMap.put("bbn", "16225009011");
//        paramMap.put("rh", "n:!16225009011,n:281407");
//        paramMap.put("language", "zh");
//        paramMap.put("ref", "sr_pg_1");

        // 构造爬虫
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("https://www.amazon.com/-/zh/s?i=electronics-intl-ship&bbn=16225009011&rh=n%3A16225009011%2Cn%3A281407%2Cp_36%3A1253504011&dc&page=1&language=zh&qid=1622957649&rnid=386442011&ref=sr_pg_1")
//                .setParamMap(paramMap)
                .setWhiteUrlRegexs("https://www.amazon.com/-/zh/s?i=electronics-intl-ship&bbn=16225009011&rh=n%3A16225009011%2Cn%3A281407%2Cp_36%3A1253504011&dc&page=1&language=zh&qid=\\d{5}&rnid=386442011&ref=sr_pg_\\d+")
                .setUserAgent(XxlCrawlerConf.USER_AGENT_EDGE_MY)
                .setThreadCount(1)
                .setIfPost(false)
                .setProxyMaker(proxyMaker)
                .setPageParser(new PageParser<XxlCrawlerTest05.PageVo>() {
                    @Override
                    public void parse(Document html, Element pageVoElement, XxlCrawlerTest05.PageVo pageVo) {
                        if (pageVo.getPort() == 0) {
                            return;
                        }

//                        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(pageVo.getIp(), pageVo.getPort()));
//                        if (ProxyIpUtil.checkProxy(proxy, null) == 200) {
//                            proxyPool.add(pageVo);
//                            logger.info("proxy pool size : {}, new proxy: {}", proxyPool.size(), pageVo);
//                        }

                    }
                })
                .build();

        // 启动
        crawler.start(true);


    }
}
