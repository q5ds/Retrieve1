import org.apache.log4j.Logger;
import org.apache.lucene.index.DirectoryReader;

import java.io.IOException;

public class SearchMain {
    private static final Logger log = Logger.getLogger(SearchMain.class);
    private static Searcher searcher;
    private DirectoryReader reader;

    public static void main(String[] args) throws IOException {
        String dir = "D:\\Documents\\信息检索与搜索引擎\\Lucene";
        Searcher search = new Searcher(dir);

        /*System.out.println("url:"+search.getDocCounts("url"));
        System.out.println("title:"+search.getDocCounts("title"));
        System.out.println("date:"+search.getDocCounts("date"));
        System.out.println("content:"+search.getDocCounts("content"));
        System.out.println("tag:"+search.getDocCounts("tag"));*/
        String query = "子女";
        String query2 = "母亲";
        search.boolSearch("title","content",query,query2);
        //search.termSearch("title",query);


        search.destroy();

    }




}
