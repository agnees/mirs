package com.kevin.mirs.service;

import com.google.common.collect.Lists;
import com.kevin.mirs.dao.CommentDao;
import com.kevin.mirs.dao.MovieDao;
import com.kevin.mirs.dao.UserDao;
import com.kevin.mirs.entity.Comment;
import com.kevin.mirs.entity.Movie;
import com.kevin.mirs.entity.User;
import com.kevin.mirs.vo.UserMovieComment;
import com.kevin.mirs.vo.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

/**
 * @author tanbiao
 * @date created in 18-5-21
 */
@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MovieDao movieDao;

    public int addComment(int uid,int mid,String content){
        Comment comment = Comment.getInstance(uid,mid,content);
        return commentDao.insertComment(comment);
    }

    public List<UserMovieComment> getMovieComments(int mid){
        List<Comment> comments =  commentDao.selectByMid(mid);
        List<UserMovieComment> userMovieComments = Lists.newArrayList();
        for(Comment comment : comments){
            UserProfile userProfile = userDao.getUserProfileByUserId(comment.getUid());
            UserMovieComment userMovieComment = UserMovieComment.getInstance(userProfile.getUsername(),comment);
            userMovieComments.add(userMovieComment);

        }
        return userMovieComments;
    }

    public List<UserMovieComment> getUserMovieComments(int uid){
        List<Comment> comments = commentDao.selectByUid(uid);
        List<UserMovieComment> userMovieComments = Lists.newArrayList();
        for(Comment comment : comments){
            Movie movie = movieDao.getMovieById(comment.getMid());
            if(movie == null){
                continue;
            }
            UserMovieComment userMovieComment = UserMovieComment.getInstanceByMovieName(movie.getName(),comment);
            userMovieComments.add(userMovieComment);

        }
        return userMovieComments;
    }

    public int updateComment(Comment comment){
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setUpdateTime(new Date());
        return commentDao.updateComment(comment);
    }

    public int delteComment(int id){
        return commentDao.updateCommentStatus(id);
    }
}
