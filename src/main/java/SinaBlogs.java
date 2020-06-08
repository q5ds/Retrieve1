import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;


public class SinaBlogs implements PageProcessor{
    public static String list ="http://blog\\.sina\\.com\\.cn/s/articlelist_1197161814_0_\\s+\\.html";
    public static String post ="http://blog\\.sina\\.com\\.cn/s/blog_\\w+\\.html";


    private static final Logger log = Logger.getLogger(SinaBlogs.class);
    private Site site = Site.me().setRetryTimes(3).setSleepTime(2000).setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0");


    @Override
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().xpath("//div[@class=\"articleList\"]").links().regex(post).all());
        page.addTargetRequests(page.getHtml().links().regex(list).all());
        String HTML = page.getRequest().getUrl();
        System.out.println(HTML);
        if(HTML.startsWith("http://blog.sina.com.cn/s/articlelist_1197161814_0_")){
            log.info("解析博客列表");
            page.setSkip(true);
        }else if(HTML.startsWith("http://blog.sina.com.cn/s/blog_")){
            log.info("解析博客内容");
            String title = page.getHtml().xpath("//div[@class='articalTitle']/h2//text()").get();
            String date = page.getHtml().xpath("//*[@id=\"articlebody\"]//*[@class=\"time SG_txtc\"]").regex("\\((.*)\\)").get();
            List<String> contentList = page.getHtml().xpath("//*[@id=\"sina_keyword_ad_area2\"]").all();
            StringBuilder contentBuilder = new StringBuilder();
            for (String str : contentList) {
                contentBuilder.append(str);
            }
            String content = contentBuilder.toString();

            String tag =page.getHtml().xpath("//*[@id=\"sina_keyword_ad_area\"]/table/tbody/tr/td[1]/h3/a/text()").all().toString();
            String url =page.getUrl().regex("http://blog\\.sina\\.com\\.cn/s/blog_\\w+\\.html").toString();

            Blogs blogs = new Blogs();
            blogs.setTitle(title);
            blogs.setDate(date);
            blogs.setContent(content);
            blogs.setTag(tag);
            blogs.setUrl(url);

            page.putField("title",title);
            page.putField("date", date);
            page.putField("content", content);
            page.putField("tag",tag);
            page.putField("url",url);
        }

    }

    @Override
    public Site getSite() {
        return this.site;
    }


}
