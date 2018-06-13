package com.kevin.mirs.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kevin.mirs.entity.Score;

import java.util.Date;

/**
 * @author tanbiao
 * @date created in 18-5-21
 */
public class UserMovieScore {

    private int mid;
    private int uid;
    private String userName;
    private String movieName;
    private Integer score;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date updateTime;


    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public static UserMovieScore getInstanceByUsreName(String userName, Score score){
        UserMovieScore userMovieScore = new UserMovieScore();
        userMovieScore.setUserName(userName);
        userMovieScore.setScore(score.getScore());
        userMovieScore.setUpdateTime(score.getUpdateTime());
        userMovieScore.setMid(score.getMid());
        userMovieScore.setUid(score.getUid());
        return userMovieScore;
    }

    public static UserMovieScore getInstanceByMovieName(String movieName,Score score){
        UserMovieScore userMovieScore = new UserMovieScore();
        userMovieScore.setMovieName(movieName);
        userMovieScore.setScore(score.getScore());
        userMovieScore.setUpdateTime(score.getUpdateTime());
        userMovieScore.setMid(score.getMid());
        userMovieScore.setUid(score.getUid());
        return userMovieScore;
    }
}
