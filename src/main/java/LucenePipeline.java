import com.hankcs.lucene.HanLPIndexAnalyzer;
import org.apache.log4j.Logger;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;


import java.io.IOException;
import java.nio.file.Paths;


public class LucenePipeline implements Pipeline {
    private static final Logger log = Logger.getLogger(LucenePipeline.class);

    private IndexWriter writer;

    public LucenePipeline(String  dir){
        HanLPIndexAnalyzer analyzer = new HanLPIndexAnalyzer();
        Directory index;
        try {
            index = FSDirectory.open(Paths.get(dir));
            IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
            writer = new IndexWriter(index, writerConfig);
            log.info("索引初始化完成，索引目录为:"+dir);

        } catch (IOException e) {
            e.printStackTrace();
            log.error("无法初始化索引，请检查提供的索引目录是否可用:"+dir);
            writer = null;
        }
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println("get page: " + resultItems.getRequest().getUrl());
        Blogs blogs = new Blogs();


        blogs.setTitle(resultItems.get("title"));
        blogs.setDate(resultItems.get("date"));
        blogs.setContent(resultItems.get("content"));
        blogs.setTag(resultItems.get("tag"));
        blogs.setUrl(resultItems.get("url"));
        //resultItems.get("date");
        //resultItems.get("content");
        //resultItems.get("tag");
        //resultItems.get("url");
        /*System.out.println("tag----"+blogs.getTag());
        System.out.println("title----"+blogs.getTitle());
        System.out.println("tag----"+blogs.getContent());
        System.out.println("tag----"+blogs.getDate());
        System.out.println("tag----"+blogs.getUrl());*/
        //System.out.println("tag----"+resultItems.get("tag"));

        addBlogs(blogs);
    }

    public boolean addBlogs(Blogs blogs){
        if(writer==null){
            return false;
        }
        Document doc = new Document();
        /*SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = null;
        try{
            d = format.parse(blogs.getDate());
            d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        if(blogs.getTitle()==null){
            return false;
        }
        doc.add(new TextField("title",blogs.getTitle(), Field.Store.YES));
        doc.add(new TextField("content",blogs.getContent(),Field.Store.YES));
        doc.add(new StringField("url", blogs.getUrl(),Field.Store.YES));
        //doc.add(new LongPoint("date",d.getTime()));
        doc.add(new StringField("date",blogs.getDate(),Field.Store.YES));
        doc.add(new StoredField("tag",blogs.getTag()));

        try {
            writer.updateDocument(new Term("url", blogs.getUrl()), doc);
            writer.commit();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("构建索引失败");
            return false;
        }
    }







    public void destroy(){
        if(writer == null)
            return;
        try{
            log.info("索引关闭");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.info("关闭索引失败");
        }
    }

}
