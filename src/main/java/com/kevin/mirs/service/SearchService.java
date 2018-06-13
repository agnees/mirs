package com.kevin.mirs.service;


import com.kevin.mirs.dao.MovieDao;
import com.kevin.mirs.enums.MovieColumnEnum;
import com.kevin.mirs.vo.SimpleMovie;
import com.kevin.mirs.vo.SuggestionMovie;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

@Service
public class SearchService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String[] DEFAULT_FIELDS = {
            MovieColumnEnum.ID.getName(),
            MovieColumnEnum.NAME.getName(),
            MovieColumnEnum.RELEASE_YEAR.getName(),
            MovieColumnEnum.DIRECTORS.getName(),
            MovieColumnEnum.SCREENWRITERS.getName(),
            MovieColumnEnum.ACTORS.getName(),
            MovieColumnEnum.TYPES.getName(),
            MovieColumnEnum.ORIGIN_PLACE.getName(),
            MovieColumnEnum.LANGUAGES.getName(),
            MovieColumnEnum.ANOTHER_NAMES.getName(),
            MovieColumnEnum.COVER_LINK.getName(),
            MovieColumnEnum.SYNOPSIS.getName()
    };

    private final int DEFAULT_LIMIT = 6;

    @Resource
    Analyzer analyzer;

    @Resource
    IndexSearcher indexSearcher;

    @Resource
    MovieDao movieDao;


    /**
     * 按照指定条件查询电影信息
     */
    public ArrayList<SuggestionMovie> getSuggestionMovies(String keyword, int limit) {

        logger.info("--------------------getSuggestionMovies--------------------");

        int searchLimit = (limit > 0) ? limit : DEFAULT_LIMIT;

        ArrayList<SuggestionMovie> suggestionMovies = new ArrayList<SuggestionMovie>();

        try {
            QueryParser queryParser = new MultiFieldQueryParser(DEFAULT_FIELDS, analyzer);
            Query query = queryParser.parse(keyword);
            ScoreDoc[] scoreDocs = indexSearcher.search(query, searchLimit).scoreDocs;
            for (ScoreDoc scoreDoc : scoreDocs) {
                Document document = indexSearcher.doc(scoreDoc.doc);
                System.out.println("编号为" + document.get("id") + "号的电影得分是" + scoreDoc.score);

                // 从数据库获取
                //SuggestionMovie s = movieDao.getSuggestedMovie(Integer.parseInt(document.get("id")));

                //直接获取
                SuggestionMovie s = new SuggestionMovie(
                        Integer.parseInt(document.get(MovieColumnEnum.ID.getName())),
                        document.get(MovieColumnEnum.NAME.getName()),
                        document.get(MovieColumnEnum.COVER_LINK.getName()));

                suggestionMovies.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(suggestionMovies);
        return suggestionMovies;
    }

    /**
     * 根据分类搜索电影
     * @param keywords
     * @param type
     * @param sort
     * @param limit
     * @param offset
     * @return
     */
    public ArrayList<SimpleMovie> searchMoives(String keywords, int type, int sort, int limit, int offset) {

        logger.info("--------------------searchMoives--------------------");

        ArrayList<SimpleMovie> results = new ArrayList<>();
        String column = MovieColumnEnum.columnOf(type);
        String orderBy = MovieColumnEnum.columnOf(sort);

        String low = "";
        String high = "";


        // TODO: 2016/12/12 校验合法性，避免SQL注入

        try {
            if(column.equals("name")) {
                results = movieDao.getMoviesIncludeMovieName(keywords, orderBy, limit, offset);
            } else if (column.equals("another_names")) {
                results = movieDao.getMoviesIncludeAnotherName(keywords, orderBy, limit, offset);
            } else if (column.equals("directors")) {
                results = movieDao.getMoviesIncludeDirector(keywords, orderBy, limit, offset);
            } else if (column.equals("screenwriters")) {
                results = movieDao.getMoviesIncludeScreenwriter(keywords, orderBy, limit, offset);
            } else if (column.equals("actors")) {
                results = movieDao.getMoviesIncludeActor(keywords, orderBy, limit, offset);
            } else if (column.equals("types")) {
                results = movieDao.getMoviesIncludeType(keywords, orderBy, limit, offset);
            } else if (column.equals("origin_place")) {
                results = movieDao.getMoviesIncludeOriginPlace(keywords, orderBy, limit, offset);
            } else if (column.equals("languages")) {
                results = movieDao.getMoviesIncludeLanguage(keywords, orderBy, limit, offset);
            } else if (column.equals("release_year")) {
                results = movieDao.getMoviesByReleaseYear(keywords, orderBy, limit, offset);
            } else if (column.equals("douban_rating")) {
                String[] score = keywords.split("-");
                low = high = score[0];
                if (score.length == 2) {
                    high = score[1];
                }
                results = movieDao.getMoviesByDoubanRating(low, high, orderBy, limit, offset);
            } else if (column.equals("imdb_rating")) {
                String[] score = keywords.split("-");
                low = high = score[0];
                if (score.length == 2) {
                    high = score[1];
                }
                results = movieDao.getMoviesByDoubanRating(low, high, orderBy, limit, offset);
            } else if (column.equals("awards")) {
                results = movieDao.getMoviesIncludeAward(keywords, orderBy, limit, offset);
            } else {
                results = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            results = null;
        }

        return results;
    }


}
