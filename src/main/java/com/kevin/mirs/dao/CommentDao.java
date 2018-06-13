package com.kevin.mirs.dao;

import com.kevin.mirs.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author tanbiao
 * @date created in 18-5-21
 */
@Repository
public interface CommentDao {

    List<Comment> selectByMid(@Param("mid") int mid);

    List<Comment> selectByUid(@Param("uid") int uid);

    Comment selectById(@Param("id") int id);

    int updateComment(@Param("comment") Comment comment);

    int updateCommentStatus(@Param("id") int id);

    int insertComment(@Param("comment") Comment comment);
}
