package com.chestnut.haoweilai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chestnut.haoweilai.common.R;
import com.chestnut.haoweilai.entity.User;
import com.chestnut.haoweilai.service.UserService;
import com.chestnut.haoweilai.util.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 发送手机短信验证码
     * @param request
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(HttpServletRequest request, @RequestBody User user) {
        // 获取手机号
        String phone = user.getPhone();
        if (!StringUtils.isNotEmpty(phone)) {
            return R.error("短信发送失败，手机号为空");
        }
        // 生成验证码
        String code = ValidateCodeUtils.generateValidateCode(4).toString();
        log.info("code = {}", code);
        // 发送短信 （若要发送短信则取消注释下行代码，并更改com.chestnut.reggie.util.SMSUtils.sendMessage方法）
//        SMSUtils.sendMessage("阿里云短信测试", "SMS_154950909", phone, code);
        // 生成验证码保存至redis中，设置有效期5min
        redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
        return R.success("短信发送成功");
    }

    /**
     * 用户登录
     * @param request
     * @param map
     * @return
     */
    @PostMapping("/login")
    public R<User> login(HttpServletRequest request, @RequestBody Map map) {
        // 获取手机号
        String phone = map.get("phone").toString();
        // 获取验证码
        String code = map.get("code").toString();
        // 获取存储在redis中的验证码
        Object codeInRedis = redisTemplate.opsForValue().get(phone);
        // 比对验证码
        if (codeInRedis != null && codeInRedis.equals(code)) {
            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            lqw.eq(User::getPhone, phone);
            User user = userService.getOne(lqw);
            // 若为新用户则添加进数据库
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            // 登录成功，id存入redis中
            redisTemplate.delete(phone);
            redisTemplate.opsForValue().set("user", user.getId(), 1, TimeUnit.DAYS);
//            request.getSession().setAttribute("user", user.getId());
            return R.success(user);
        }
        // 登录失败
        return R.error("手机号或验证码错误");
    }

    /**
     * 用户退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        // 清除session中的id
        request.getSession().removeAttribute("user");
        return R.success("用户退出成功");
    }
}
