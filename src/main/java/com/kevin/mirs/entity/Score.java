package com.kevin.mirs.entity;

import java.util.Date;

/**
 * @author tanbiao
 * @date created in 18-5-21
 */
public class Score {
    private Integer id;
    private Integer uid;
    private Integer mid;
    private Integer score;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
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

    public static Score getInstance(int id,int score){
        Score score1 = new Score();
        score1.setId(id);
        score1.setScore(score);
        score1.setUpdateTime(new Date());
        return score1;
    }

    public static Score getInstance(int uid,int mid,int score){
        Score s = new Score();
        s.setMid(mid);
        s.setUid(uid);
        s.setScore(score);
        s.setUpdateTime(new Date());
        return s;
    }
}
