import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import org.apache.log4j.Logger;



public class SinaBlogsMain {
    private static final Logger log = Logger.getLogger(SinaBlogs.class);
    private Site site = Site.me().setRetryTimes(3).setSleepTime(2000);


    public static void main(String[] args){
        log.info("新浪博客爬虫启动");
        for(int i=1;i<=3;i++){

            String Page = "http://blog.sina.com.cn/s/articlelist_1197161814_0_"+ i +".html";
            SinaBlogs sina = new SinaBlogs();
            Spider spider = Spider.create(sina);
            spider.addPipeline(new JsonFilePipeline("D:\\Documents\\信息检索与搜索引擎\\Exp1"));
            spider.thread(20);
            spider.addUrl(Page);
            spider.run();

        }

    }
}
