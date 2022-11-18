package com.rrzui.wiki.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rrzui.wiki.entity.User;
import com.rrzui.wiki.exception.BusinessException;
import com.rrzui.wiki.exception.BusinessExceptionCode;
import com.rrzui.wiki.req.UserLoginReq;
import com.rrzui.wiki.req.UserQueryReq;
import com.rrzui.wiki.req.UserResetPasswordReq;
import com.rrzui.wiki.req.UserSaveReq;
import com.rrzui.wiki.resp.CommonResp;
import com.rrzui.wiki.resp.PageResp;
import com.rrzui.wiki.resp.UserLoginResp;
import com.rrzui.wiki.resp.UserQueryResp;
import com.rrzui.wiki.service.UserService;
import com.rrzui.wiki.utils.CopyUtil;
import com.rrzui.wiki.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @program: 粤嵌项目
 * @description:
 * @author: Ethercat
 * @create: 2022-11-16 16:01
 **/

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SnowFlake snowFlake;

    @GetMapping("/getUserListByPage")
    public CommonResp getUserListByPage(@Valid UserQueryReq req){
        Page<User> page = new Page<>(req.getPage(), req.getSize());

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(req.getLogin_name())){
            wrapper.like("login_name",req.getLogin_name());
        }

        page = userService.page(page,wrapper);
        List<User> list = page.getRecords();

        LOG.info("总行数：{}",page.getTotal()+"");
        LOG.info("总页数：{}",page.getPages()+"");

        List<UserQueryResp> UserResps = CopyUtil.copyList(list, UserQueryResp.class);

        PageResp<UserQueryResp> respPage = new PageResp<>();
        respPage.setTotal(page.getTotal());
        respPage.setList(UserResps);

        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        resp.setContent(respPage);

        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req){
        User user = CopyUtil.copy(req, User.class);

        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

        CommonResp resp = new CommonResp<>();
        //这里是修改
        if (user!=null&&user.getId()!=null){
            //虽然前端已经禁用修改登录名，但是还是要为防止黑客恶意修改，
            // 所以进入这里面将登录名置空，数据库该字段就不回去修改
            user.setLogin_name(null);
            user.setPassword(null);
            userService.saveOrUpdate(user);
            return resp;
        }
        //下面是新增  处理用户名唯一
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("login_name",req.getLogin_name());
        //根据用户名获取对象
        User userDB = userService.getOne(wrapper);
        if (userDB==null){
            //没有重复就新增
            userService.saveOrUpdate(user);
        } else {
            //重复就报出自定义异常
            throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
        }

        return resp;
    }

    @GetMapping("/remove")
    public CommonResp remove(int id){

        userService.removeById(id);

        CommonResp<Object> resp = new CommonResp<>();
        return resp;
    }

    @PostMapping("/resetPassword")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq req){
        User user = CopyUtil.copy(req, User.class);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        userService.updateById(user);


        CommonResp resp = new CommonResp<>();
        return resp;
    }

    @PostMapping("/userLogin")
    public CommonResp userLogin(@Valid @RequestBody UserLoginReq req){
        CommonResp resp = new CommonResp<>();
        //根据用户名查出用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("login_name",req.getLogin_name());
        //根据用户名获取对象
        User userDB = userService.getOne(wrapper);
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        if (ObjectUtils.isEmpty(userDB)){
            //用户名不存在
            LOG.info("用户名不存在{}",req.getLogin_name());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        } else {
            //传递进来的也是经过两次加密的
            if (userDB.getPassword().equals(req.getPassword())){
                //登录成功
                UserLoginResp userLoginResp = CopyUtil.copy(userDB, UserLoginResp.class);
                Long token = snowFlake.nextId();
                LOG.info("UserController.userLogin业务结束，结果：{}",token);
                userLoginResp.setToken(token+"");
                redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(userLoginResp),3600*24, TimeUnit.SECONDS);
                resp.setContent(userLoginResp);
            } else {
                //密码不对
                LOG.info("密码不对，输入密码为{}",req.getPassword(),userDB.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }

        return resp;
    }

    @GetMapping("/logout/{token}")
    public CommonResp logout(@PathVariable String token){

        redisTemplate.delete(token);
        LOG.info("从redis中删除token:{}",token);

        CommonResp<Object> resp = new CommonResp<>();
        return resp;
    }



}
