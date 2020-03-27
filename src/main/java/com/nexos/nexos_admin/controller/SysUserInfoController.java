package com.nexos.nexos_admin.controller;

import com.nexos.nexos_admin.vo.ResultInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: nexos_admin
 * @description:
 * @author: afsun
 * @create: 2020-03-27 12:00
 */
@RestController
@Api(tags = "个人信息中心")
@Slf4j
public class SysUserInfoController {


    @GetMapping("/userInfo")
    @ApiOperation("获取个人信息")
    public ResultInfo getUserInfo(@RequestParam String userId){
        ResultInfo resultInfo = ResultInfo.newInstance();
        return resultInfo;
    }

}