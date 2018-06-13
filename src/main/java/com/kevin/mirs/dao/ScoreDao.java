package com.kevin.mirs.dao;

import com.kevin.mirs.entity.Score;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author tanbiao
 * @date created in 18-5-21
 */
@Repository
public interface ScoreDao {

    List<Score> selectByMid(@Param("mid") int mid);

    List<Score> selectByUid(@Param("uid") int uid);

    Score selectById(@Param("id") int id);

    int updateScore(@Param("score") Score score);

    int insertScore(@Param("score") Score score);

}
