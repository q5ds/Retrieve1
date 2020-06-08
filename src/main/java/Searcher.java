import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;


public class Searcher {

    private static final Logger log = Logger.getLogger(Searcher.class);

    private IndexSearcher searcher;

    private DirectoryReader reader;


    public Searcher(String dir){
        try {
            this.reader = DirectoryReader.open(FSDirectory.open(Paths.get(dir)));
            this.searcher = new IndexSearcher(reader);
            log.info("IndexSearcher初始化成功");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("IndexSearcher初始化失败");
        }
    }

    public int getDocCounts(String field){
        if(reader!=null){
            try {
                return reader.getDocCount(field);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("读取索引过程中出现异常");
                return -1;
            }
        }
        return -1;
    }

    public void boolSearch(String field,String field1,String query,String query1)throws IOException {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        TermQuery tq1 = new TermQuery(new Term(field, query));
        TermQuery tq2 = new TermQuery(new Term(field1, query1));
        builder.add(tq1, BooleanClause.Occur.MUST);
        builder.add(tq2, BooleanClause.Occur.MUST);
        Query q = builder.build();
        TopDocs docs = (TopDocs) searcher.search(q, 10);
        ScoreDoc[] hits = docs.scoreDocs;
        for (ScoreDoc doc : hits) {
            Document d = searcher.doc(doc.doc);
            System.out.println("["+doc.doc +','+doc.score +"]:"+d.get("url") +","+d.get("title") + d.get("tag"));
        }
    }
    public void termSearch(String field,String query)throws IOException{
        TermQuery tq = new TermQuery(new Term(field,query));
        TopDocs docs = (TopDocs) searcher.search(tq,10);
        ScoreDoc[] hits = docs.scoreDocs;
        for (ScoreDoc doc : hits){
            Document d = searcher.doc(doc.doc);
            System.out.println("["+doc.doc+','+doc.score+"]:"+d.get("url")+"/"+d.get("title"));

        }
    }



    public void destroy() {
        if (this.reader != null) {
            try {
                this.reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error("销毁搜索器时占用的错误", e);
            }
        }
    }



}
