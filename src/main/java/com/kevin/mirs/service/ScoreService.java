package com.kevin.mirs.service;

import com.google.common.collect.Lists;
import com.kevin.mirs.dao.MovieDao;
import com.kevin.mirs.dao.ScoreDao;
import com.kevin.mirs.dao.UserDao;
import com.kevin.mirs.entity.Movie;
import com.kevin.mirs.entity.Score;
import com.kevin.mirs.entity.User;
import com.kevin.mirs.vo.UserMovieScore;
import com.kevin.mirs.vo.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author tanbiao
 * @date created in 18-5-21
 */
@Service
public class ScoreService {
    @Autowired
    private ScoreDao scoreDao;
    @Autowired
    private MovieDao movieDao;

    @Autowired
    private UserDao userDao;
    @Autowired
    private RecommendService recommendService;
    private  static ExecutorService executorService = Executors.newFixedThreadPool(5);
    public int addScore(int uid,int mid,int score){

        Score  s = Score.getInstance(uid,mid,score);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                recommendService.getRealTimeRecommendedMovies(uid);

            }
        });
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                recommendService.getRealTimeRecommendedFriends(uid);

            }
        });

        return scoreDao.insertScore(s);
    }

    public List<UserMovieScore> getMovieScores(int mid){
        List<Score> scores =  scoreDao.selectByMid(mid);
        List<UserMovieScore> userMovieScores = Lists.newArrayList();
        for(Score score : scores){
            UserProfile user = userDao.getUserProfileByUserId(score.getUid());
            UserMovieScore userMovieScore = UserMovieScore.getInstanceByUsreName(user.getUsername(),score);
            userMovieScores.add(userMovieScore);
        }
        return userMovieScores;
    }

    public List<UserMovieScore> getUserScoreRecord(int uid){
        List<Score> scores = scoreDao.selectByUid(uid);
        List<UserMovieScore> userMovieScores = Lists.newArrayList();
        for(Score score : scores){
            Movie movie = movieDao.getMovieById(score.getMid());
            UserMovieScore userMovieScore = UserMovieScore.getInstanceByMovieName(movie.getName(),score);
            userMovieScores.add(userMovieScore);
        }
        return userMovieScores;
    }

    public int editScore(int id,int score) {
        Score score1 = scoreDao.selectById(id);
        if (score1 != null) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    recommendService.getRealTimeRecommendedMovies(score1.getUid());

                }
            });
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    recommendService.getRealTimeRecommendedFriends(score1.getUid());
                }
            });
            score1.setScore(score);
            return scoreDao.updateScore(score1);

        }
        return 0;
    }
}
