package com.kevin.mirs.service;

import com.google.common.collect.Lists;
import com.kevin.mirs.dao.UserDao;
import com.kevin.mirs.dao.UserRecommendedFriendsDao;
import com.kevin.mirs.vo.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tanbiao
 * @date created in 18-5-30
 */
@Service
public class RecommendFriendService {

    @Autowired
    private UserRecommendedFriendsDao userRecommendedFriendsDao;

    @Autowired
    private UserDao userDao;

    public List<UserProfile> getRecommendFriends(int uid){
        Integer[] ids = userRecommendedFriendsDao.getUserRecommendedFriends(uid);
        List<UserProfile> userProfiles = Lists.newArrayList();
        for(Integer id : ids){
            userProfiles.add(userDao.getUserProfileByUserId(id));
        }
        return userProfiles;
    }
}
