package com.kevin.mirs.web;

import com.kevin.mirs.dto.MIRSResult;
import com.kevin.mirs.entity.Comment;
import com.kevin.mirs.service.CommentService;
import com.kevin.mirs.service.UserService;
import com.kevin.mirs.vo.UserMovieComment;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author tanbiao
 * @date created in 18-5-22
 */

@Controller
@RequestMapping("/movie")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/comments",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "/comments",notes = "添加评论")
    public MIRSResult<Boolean> addComment(@RequestBody Comment comment,
                                          HttpServletRequest request){
        // 检查SESSION信息
        if (request.getSession().getAttribute(UserService.USER_ID) == null) {
            return new MIRSResult<Boolean>(false, "请先登录!");
        }

        int id = (int) request.getSession().getAttribute(UserService.USER_ID);
        comment.setUid(id);
        int flag = commentService.addComment(comment.getUid(),comment.getMid(),comment.getContent());
        return new MIRSResult<Boolean>(flag > 0 ,flag > 0 ? "添加成功":"添加失败");
    }

    @RequestMapping(value = "/user/{uid}/comment",method = RequestMethod.GET )
    @ResponseBody
    @ApiOperation(value = "/user/{uid}/comment",notes = "获取用户评论记录")
    public MIRSResult<List<UserMovieComment>> getUserComments(@PathVariable("uid") int uid){
        List<UserMovieComment> commentList = commentService.getUserMovieComments(uid);
        return new MIRSResult<List<UserMovieComment>>(true,commentList);
    }

    @RequestMapping(value = "/{mid}/comments" ,method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "/{mid}/comments",notes = "获取电影的评论")
    public MIRSResult<List<UserMovieComment>> getMovieComments(@PathVariable("mid") int mid){
        List<UserMovieComment> commentList = commentService.getMovieComments(mid);
        return new MIRSResult<List<UserMovieComment>>(true,commentList);
    }

    @RequestMapping(value = "/comment/{id}",method = {RequestMethod.PUT,RequestMethod.OPTIONS})
    @ResponseBody
    @ApiOperation(value ="/comment/{id}" ,notes = "修改评论")
    public MIRSResult<Boolean> updateComment(@PathVariable("id")int id,@RequestBody Comment comment,
                                            HttpServletRequest request){
        // 检查SESSION信息
        if (request.getSession().getAttribute(UserService.USER_ID) == null) {
            return new MIRSResult<Boolean>(false, "请先登录!");
        }

        int uid = (int) request.getSession().getAttribute(UserService.USER_ID);
        comment.setUid(uid);
        comment.setId(id);
        int flag = commentService.updateComment(comment);
        return new MIRSResult<Boolean>(flag > 0,flag > 0 ? "更新评论成功":"更新评论失败");
    }

    @RequestMapping(value = "/comment/{id}",method = {RequestMethod.DELETE,RequestMethod.OPTIONS})
    @ResponseBody
    @ApiOperation(value = "/comment/{id}",notes = "删除评论")
    public MIRSResult<Boolean> deleteComment(@PathVariable("id")int id,
                                             HttpServletRequest request){
        // 检查SESSION信息
        if (request.getSession().getAttribute(UserService.USER_ID) == null) {
            return new MIRSResult<Boolean>(false, "请先登录!");
        }
        int flag = commentService.delteComment(id);
        return new MIRSResult<Boolean>(flag > 0,flag > 0 ? "删除评论成功":"删除评论失败");
    }

}
