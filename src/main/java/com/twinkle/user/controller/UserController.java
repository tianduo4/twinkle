package com.twinkle.user.controller;

import com.twinkle.user.repository.model.User;
import com.twinkle.user.repository.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: 会员服务
 *
 * @author ：King
 * @date ：2019/1/12 10:15
 */
@Api(value = "UserController", description = "会员接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //    @RequestMapping(method = RequestMethod.POST,value = "/insert")
//    public void insert(User user){
//        userService.insert(user);
//    }
//    @RequestMapping(method = RequestMethod.POST,value = "/update/{id}")
//    public void update(@RequestParam User user){
//        userService.update(user);
//    }
//
//    @RequestMapping(method = RequestMethod.GET,value = "/delete/{id}")
//    public void delete(@PathVariable("id")int id){
//        userService.delete(id);
//    }
    @ApiOperation(value = "会员查询", notes = "会员查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "会员Id", required = true, dataType = "int")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/select")
    public User select(@PathVariable("id") int id) {
        return userService.selectById(id);
    }

//    @AccessLimit(perSecond=1,timeOut = 500)//5秒钟生成一个令牌
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/public/select")
    public User publicSelect(@PathVariable("id") int id) {
        return userService.selectById(id);
    }

//    @RequestMapping(method = RequestMethod.GET,value = "/selectAll/{pageNum}/{pageSize}")
//    public List<User> selectAll(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize){
//        return userService.selectAll(pageNum,pageSize);
//
}
