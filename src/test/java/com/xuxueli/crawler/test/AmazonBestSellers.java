package com.xuxueli.crawler.test;

import com.xuxueli.crawler.XxlCrawler;
import com.xuxueli.crawler.annotation.PageFieldSelect;
import com.xuxueli.crawler.annotation.PageSelect;
import com.xuxueli.crawler.conf.XxlCrawlerConf;
import com.xuxueli.crawler.parser.PageParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AmazonBestSellers {
    private static Logger logger = LoggerFactory.getLogger(TodayGoodsTest.class);


    @PageSelect(cssQuery = "#zg_browseRoot > ul > li")
    public static class PageVo {

        @PageFieldSelect(cssQuery = "a", selectType = XxlCrawlerConf.SelectType.TEXT)
        private String category;

        @PageFieldSelect(cssQuery = "a", selectType = XxlCrawlerConf.SelectType.ATTR, selectVal = "abs:href")
        private String url;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }


    }

    public static void main(String[] args) throws IOException {

        // 代理池
//        List<String> proxyList = ProxyIpUtil.getXieQuIpList();
//        String[] ips = proxyList.get(0).split(":");
//        String ip = ips[0];
//        Integer port = Integer.valueOf(ips[1]);
//
//        // 设置代理池
//        ProxyMaker proxyMaker = new RoundProxyMaker()
//                .addProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port)));

        XxlCrawler crawler = new XxlCrawler.Builder()
                .setUrls("https://www.amazon.com/gp/bestsellers/?ref_=nav_cs_bestsellers_8a080d3d7b55497ea1bdd97b7cff8b7b")
                .setWhiteUrlRegexs("https://www.amazon.com/gp/bestsellers/?.*")
                .setThreadCount(3)
//                .setProxyMaker(proxyMaker)
                .setPageParser(new PageParser<PageVo>() {
                    @Override
                    public void parse(Document html, Element pageVoElement, PageVo pageVo) {
                        System.out.println("主类目：" + pageVo.getCategory() + "子类目路径：" + pageVo.getUrl());

                    }
                })
                .build();
        crawler.start(true);
    }
}
