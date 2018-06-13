package com.kevin.mirs.web;

import com.google.common.collect.Lists;
import com.kevin.mirs.dto.MIRSResult;
import com.kevin.mirs.entity.Friend;
import com.kevin.mirs.service.FriendService;
import com.kevin.mirs.service.RecommendFriendService;
import com.kevin.mirs.service.UserService;
import com.kevin.mirs.vo.UserProfile;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author tanbiao
 * @date created in 18-5-19
 */

@Controller
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private RecommendFriendService recommendFriendService;



    @RequestMapping(value = "/{uid}/{ufid}",method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation(value ="/{uid}/{ufid}",notes = "关注好友uid为用户id，ufid为好友id")
    public MIRSResult<Boolean> followFriend(@PathVariable("uid") int uid,
                                            @PathVariable("ufid") int ufid,
                                           HttpServletRequest request){
        Friend friend = new Friend();
        friend.setUfid(ufid);
        friend.setUid(uid);
        int flag = friendService.addFriend(friend);
        return new MIRSResult<Boolean>(flag != 0 ,"添加好友成功");
    }

    @RequestMapping(value = "/{uid}" , method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value ="/{uid}" , notes = "获取好友列表")
    public MIRSResult<List<Friend>> allFriends(@PathVariable("uid") int uid){
        List<Friend> friends = friendService.getFriends(uid);
        MIRSResult<List<Friend>> result = new MIRSResult<List<Friend>>(true,friends);
        return result;
    }

    @RequestMapping(value = "/{uid}/{ufid}",method = RequestMethod.DELETE)
    @ResponseBody
    @ApiOperation(value = "/{uid}/{ufid}",notes = "取消关注好友")
    public MIRSResult<Boolean> unfollowFriend(@PathVariable("uid") int uid,
                                            @PathVariable("ufid") int ufid,
                                            HttpServletRequest request){
        Friend friend = new Friend();
        friend.setUid(uid);
        friend.setUfid(ufid);
        friendService.unfollowFreind(friend);
        return new MIRSResult<Boolean>(true,"删除好友成功");
    }

    @RequestMapping(value = "/recommendation",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "/recommendation")
    public MIRSResult<List<UserProfile>> recommendFriends(HttpServletRequest request){
        Integer uid = (Integer) request.getSession().getAttribute(UserService.USER_ID);
        List<UserProfile> userProfiles = Lists.newArrayList();
        if(uid == null){
            return new MIRSResult<List<UserProfile>>(false,"未登录");
        }
        userProfiles =recommendFriendService.getRecommendFriends(uid);
        return new MIRSResult<List<UserProfile>>(true,userProfiles);
    }
}

