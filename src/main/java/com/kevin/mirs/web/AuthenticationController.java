package com.kevin.mirs.web;


import com.google.code.kaptcha.Constants;
import com.kevin.mirs.dao.EmailVerifyDao;
import com.kevin.mirs.dao.RegisterSessionDao;
import com.kevin.mirs.dto.MIRSResult;
import com.kevin.mirs.entity.User;
import com.kevin.mirs.enums.EVStatusEnum;
import com.kevin.mirs.enums.RSStatusEnum;
import com.kevin.mirs.service.UserService;
import com.kevin.mirs.utils.FormatUtils;
import com.kevin.mirs.utils.IPUtils;
import com.kevin.mirs.vo.*;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;

@Controller
@RequestMapping("/authentication")
public class AuthenticationController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    UserService userService;

    @Resource
    RegisterSessionDao RSDao;

    @Resource
    EmailVerifyDao EVDao;

    @ResponseBody
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    @ApiOperation(value = "/token", notes = "登录，返回用户一个Token")
    public MIRSResult<UserProfile> doLogin(@RequestBody LoginUser loginUser,
                                           HttpServletRequest request) {
//        Subject currentUser = SecurityUtils.getSubject();
//
//        // 如果已经登录，则转入登录成功的页面，防止继续登录
//        if (currentUser.isAuthenticated()) {
//            return "";
//        }
        logger.info("--------------------POST:/authorization/token--------------------");

        System.out.println(loginUser);
        int userId = 0;
        String original = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);

        if (!loginUser.getCaptcha().equals(original)) {
            return new MIRSResult<>(false, "验证码错误！");
        }
        if (FormatUtils.emailFormat(loginUser.getUsername())) {
            userId = userService.checkPasswordByUserEmail(loginUser.getUsername(), loginUser.getPassword());
        } else {
            userId = userService.checkPasswordByUsername(loginUser.getUsername(), loginUser.getPassword());
        }
        if (userId != 0) {
            String token = "";
            // 将用户ID存储在SESSION中
            request.getSession().setAttribute(UserService.USER_ID, userId);
            UserProfile userProfile = userService.getUserProfileByUserId(userId);

            // 更新用户上次登录信息
            String ip = IPUtils.getIpAddr(request);
            userService.updateUserLoginInfoByUserId(userId, ip);

            return new MIRSResult<>(true, userProfile);
        } else {
            return new MIRSResult<>(false, "用户名或密码错误！");
        }

    }

    @ResponseBody
    @RequestMapping(value = "/account", method = RequestMethod.POST)
    @ApiOperation(value = "/account", notes = "注册一个账号")
    public MIRSResult<UserProfile> doRegister(@RequestBody RegisterUser registerUser,
                                               HttpServletRequest request) {
        logger.info("--------------------POST:/authorization/account--------------------");

        // 验证SESSION
        if (request.getSession().getAttribute(UserService.VERIFICATION) == null) {
            return new MIRSResult<>(false, "还没有发送验证邮件!");
        }

        String ip = IPUtils.getIpAddr(request);

        String originVerification = (String) request.getSession().getAttribute(UserService.VERIFICATION);

        if (!originVerification.equals(registerUser.getVerification())) {
            return new MIRSResult<>(false, "验证码不正确!");
        }

        // 判断注册是否超时
        Timestamp expireTime = RSDao.getExpireTimeByEmail(registerUser.getEmail());
        if (expireTime.getTime() < new Date().getTime()) {
            RSDao.updateStatusByEmail(registerUser.getEmail(), RSStatusEnum.EXPIRED.getStatus());
            EVDao.updateStatusByEmail(registerUser.getEmail(), EVStatusEnum.EXPIRED.getStatus());
            return new MIRSResult<>(false, "注册超时了!");
        }


        System.out.println(registerUser);

        User user = userService.addUer(
                registerUser.getUsername(),
                registerUser.getPassword(),
                registerUser.getEmail(),
                registerUser.getVerification(),
                ip);

        if (user == null) {
            return new MIRSResult<>(false, "注册失败！");
        }

        request.getSession().setAttribute(UserService.USER_ID, user.getId());


        // TODO 增加token
        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getEmail(), "", "", "", "");
        System.out.println(userProfile);

        // 修改注册信息表的状态
        RSDao.updateStatusByEmail(user.getEmail(), RSStatusEnum.SUCCESS.getStatus());

        // 修改邮件认证表的状态
        EVDao.updateStatusByEmail(user.getEmail(), EVStatusEnum.SUCCESS.getStatus());

        return new MIRSResult<>(true, userProfile);
    }

}
