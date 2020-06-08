

import lombok.extern.log4j.Log4j;
import us.codecraft.webmagic.Spider;


@Log4j
public class LuceneMain{
    public static void main(String[] args) {
        log.info("crawler starting...");
        SinaBlogs sina = new SinaBlogs();
        Spider spider = Spider.create(sina);


        try {
            spider.addPipeline(new LucenePipeline("D:\\Documents\\信息检索与搜索引擎\\Lucene"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        for(int i=1;i<3;i++){
            String Page = "http://blog.sina.com.cn/s/articlelist_1197161814_0_"+i+".html";
            spider.thread(20);
            spider.addUrl(Page);
            spider.run();
        }
    }
}