package com.kevin.mirs.web;

import com.kevin.mirs.dto.MIRSResult;
import com.kevin.mirs.entity.Score;
import com.kevin.mirs.service.ScoreService;
import com.kevin.mirs.service.UserService;
import com.kevin.mirs.vo.UserMovieScore;
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
public class ScoreController {
    @Autowired
    private ScoreService scoreService;

    @RequestMapping(value = "/scores", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "/scores",notes = "添加评分")
    public MIRSResult<Boolean> addScore(@RequestBody Score score,
                                        HttpServletRequest request) {
        // 检查SESSION信息
        if (request.getSession().getAttribute(UserService.USER_ID) == null) {
            return new MIRSResult<Boolean>(false, "请先登录!");
        }

        int id = (int) request.getSession().getAttribute(UserService.USER_ID);
        score.setUid(id);
        int flag = scoreService.addScore(score.getUid(), score.getMid(), score.getScore());
        return new MIRSResult<Boolean>(flag > 0, flag > 0 ? "添加评分成功" : "添加评分失败");
    }

    @RequestMapping(value = "/user/{uid}/score", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "/user/{uid}/score",notes = "查看用户评分记录")
    public MIRSResult<List<UserMovieScore>> getUserScoreHistory(@PathVariable("uid") int uid) {
        List<UserMovieScore> commentList = scoreService.getUserScoreRecord(uid);
        return new MIRSResult<List<UserMovieScore>>(true, commentList);
    }

    @RequestMapping(value = "/{mid}/scores", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "/{mid}/scores",notes = "获取电影评分列表")
    public MIRSResult<List<UserMovieScore>> getMovieComments(@PathVariable("mid") int mid) {
        List<UserMovieScore> commentList = scoreService.getMovieScores(mid);
        return new MIRSResult<List<UserMovieScore>>(true, commentList);
    }

    @RequestMapping(value = "/score/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation(value = "/score/{id}",notes = "更新评分")
    public MIRSResult<Boolean> updateComment(@PathVariable("id") int id,
                                             @RequestBody Score score,
                                             HttpServletRequest request) {
        // 检查SESSION信息
        if (request.getSession().getAttribute(UserService.USER_ID) == null) {
            return new MIRSResult<Boolean>(false, "请先登录!");
        }
        int flag = scoreService.editScore(id, score.getScore());
        return new MIRSResult<Boolean>(flag > 0, flag > 0 ? "更新评分成功" : "更新评分失败");
    }

}
