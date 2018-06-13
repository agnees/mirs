package com.kevin.mirs.service;

import com.kevin.mirs.dao.FriendDao;
import com.kevin.mirs.dao.UserDao;
import com.kevin.mirs.entity.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tanbiao
 * @date created in 18-5-19
 */
@Service
public class FriendService {

    @Autowired
    private FriendDao friendDao;
    @Autowired
    private UserDao userDao;
    public int addFriend(Friend friend){
        //排查ufid是否存在
        if(userDao.getUserPasswordByUserId(friend.getUid()) != null) {
            return friendDao.addFriendRecord(friend.getUid(), friend.getUfid());
        }
        return 0;
    }

    public List<Friend> getFriends(int uid){
        return friendDao.getAllFriendByUid(uid);
    }

    public int unfollowFreind(Friend friend){
        return friendDao.deleteFriend(friend.getUid(),friend.getUfid());
    }
}
