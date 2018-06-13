package com.kevin.config;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.lionsoul.jcseg.analyzer.v5x.JcsegAnalyzer5X;
import org.lionsoul.jcseg.tokenizer.core.JcsegTaskConfig;
import org.springframework.context.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


@Configuration
@Lazy
public class  LuceneConfig {

    String WIN_INDEX_PATH = "c:temp/mirs/indexes/";
//    String DEFAULT_INDEX_PATH = "/usr/mirs/indexes/";
    String DEFAULT_INDEX_PATH = "/home/tb/mirs/indexes/";

    @Bean
    public Analyzer analyzer() {
        System.out.println("------------初始化analyzer-------------");
//        如果需要使用特定的分词算法，可通过构造函数来指定：
//        Analyzer analyzer = new ChineseWordAnalyzer(SegmentationAlgorithm.FullSegmentation);
//        如不指定，默认使用双向最大匹配算法：SegmentationAlgorithm.BidirectionalMaximumMatching
//        可用的分词算法参见枚举类：SegmentationAlgorithm
//        return new ChineseWordAnalyzer();
//        return new StandardAnalyzer();
        return new JcsegAnalyzer5X(JcsegTaskConfig.COMPLEX_MODE);
    }


    @Bean(name = "directory")
    public Directory directory() throws IOException {
        System.out.println("--------------初始化directory----------");
        String indexPath;
        if(System.getProperty("os.name").substring(0, 3).equals("Win")){
            indexPath = WIN_INDEX_PATH;
        } else {
            indexPath = DEFAULT_INDEX_PATH;
        }
        Path path = Paths.get(indexPath);
        return FSDirectory.open(path);
    }


    private IndexWriterConfig config() {
        return new IndexWriterConfig(analyzer());
    }


    @Bean(name = "indexWriter")
    @DependsOn("directory")
    public IndexWriter indexWriter() throws IOException {
        System.out.println("--------------初始化indexWriter----------");
        IndexWriter indexWriter = new IndexWriter(directory(), config());
        return indexWriter;
    }


    private IndexReader indexReader() throws IOException {
        return DirectoryReader.open(directory());
    }


    @Bean
    @DependsOn("indexWriter")
    public IndexSearcher indexSearcher() throws IOException {
        System.out.println("--------------初始化indexSearcher----------");
        return new IndexSearcher(indexReader());
    }
}
