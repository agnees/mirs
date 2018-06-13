package com.kevin.mirs.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kevin.mirs.entity.Comment;

import java.util.Date;

/**
 * @author tanbiao
 * @date created in 18-5-21
 */
public class UserMovieComment {
    private int uid;
    private int mid;
    private String userName;
    private String movieName;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date updateTime;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public static UserMovieComment getInstance(String userName, Comment comment){
        UserMovieComment userMovieComment = new UserMovieComment();
        userMovieComment.setUserName(userName);
        userMovieComment.setContent(comment.getContent());
        userMovieComment.setUpdateTime(comment.getUpdateTime());
        userMovieComment.setMid(comment.getMid());
        userMovieComment.setUid(comment.getUid());
        return userMovieComment;
    }

    public static UserMovieComment getInstanceByMovieName(String movieName, Comment comment){
        UserMovieComment userMovieComment = new UserMovieComment();
        userMovieComment.setMovieName(movieName);
        userMovieComment.setContent(comment.getContent());
        userMovieComment.setUpdateTime(comment.getUpdateTime());
        userMovieComment.setUid(comment.getUid());
        userMovieComment.setMid(comment.getMid());
        return userMovieComment;
    }
}
