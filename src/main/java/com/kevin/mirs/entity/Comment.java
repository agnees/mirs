package com.kevin.mirs.entity;

import org.springframework.web.util.HtmlUtils;

import java.util.Date;

/**
 * @author tanbiao
 * @date created in 18-5-21
 */
public class Comment {

    private Integer id;
    private Integer uid;
    private Integer mid;
    private String content;
    private Integer isDelete;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public static Comment getInstance(int uid,int mid,String content){
        Comment comment = new Comment();
        comment.setMid(mid);
        comment.setUid(uid);
        comment.setIsDelete(0);
        comment.setContent(HtmlUtils.htmlEscape(content));
        comment.setUpdateTime(new Date());
        return comment;
    }
}
