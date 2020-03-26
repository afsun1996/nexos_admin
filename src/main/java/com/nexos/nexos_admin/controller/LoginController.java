package com.nexos.nexos_admin.controller;

import com.nexos.nexos_admin.domain.ResultInfo;
import com.nexos.nexos_admin.service.facade.SysUserService;
import com.nexos.nexos_admin.vo.PreRegisterCheckVO;
import com.nexos.nexos_admin.vo.SysUserLoginVO;
import com.nexos.nexos_admin.vo.SysUserSimpleInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @program: nexos_admin
 * @description:
 * @author: afsun
 * @create: 2020-03-26 12:10
 */
@RestController
@Api("登录模块")
@RequestMapping("/sys")
public class LoginController  {

    @Autowired
    SysUserService sysUserService;

    @PostMapping("/login")
    @ApiOperation("登录系统")
    public ResultInfo loginIn(@RequestBody @Valid SysUserLoginVO sysUserLoginVO){
        return sysUserService.login(sysUserLoginVO);
    }

    @GetMapping("/getKey")
    @ApiOperation("登录时获取秘钥加密")
    public ResultInfo getPublicKey(@RequestParam String userName){
        return sysUserService.getPublicKey(userName);
    }

    @PostMapping("/registerUser")
    @ApiOperation("注册用户")
    public ResultInfo registerUser(@RequestBody @Valid SysUserSimpleInfo sysUserSimpleInfo){
        return sysUserService.registerUser(sysUserSimpleInfo);
    }

    @GetMapping("/preRegisterCheck")
    @ApiOperation("注册前检查,是否重复")
    public ResultInfo preRegisterCheck(@RequestBody @Valid PreRegisterCheckVO checkVO){
        return sysUserService.preRegisterCheck(checkVO);
    }




}