package com.nexos.nexos_admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nexos.nexos_admin.constant.Constant;
import com.nexos.nexos_admin.exception.BusinessResponseCode;
import com.nexos.nexos_admin.exception.BussinessException;
import com.nexos.nexos_admin.service.facade.RedisService;
import com.nexos.nexos_admin.vo.ResultInfo;
import com.nexos.nexos_admin.enums.SysUserLock;
import com.nexos.nexos_admin.enums.SysUserStatus;
import com.nexos.nexos_admin.shiro.ShiroProperties;
import com.nexos.nexos_admin.util.JwtUtil;
import com.nexos.nexos_admin.mapper.SysUserMapper;
import com.nexos.nexos_admin.po.SysUser;
import com.nexos.nexos_admin.service.facade.SysUserService;
import com.nexos.nexos_admin.util.MD5Util;
import com.nexos.nexos_admin.util.RSAEncrypt;
import com.nexos.nexos_admin.util.SaltGenerator;
import com.nexos.nexos_admin.vo.PreRegisterCheckVO;
import com.nexos.nexos_admin.vo.SysUserLoginVO;
import com.nexos.nexos_admin.vo.SysUserSimpleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: nexos_admin
 * @description:
 * @author: afsun
 * @create: 2020-03-26 13:45
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ShiroProperties shiroProperties;

    @Autowired
    RedisService redisService;

    private static Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);


    @Override
    public ResultInfo login(SysUserLoginVO sysUserLoginVO) {
        ResultInfo resultInfo = ResultInfo.newInstance();
        String userName = sysUserLoginVO.getUserName();
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", sysUserLoginVO.getUserName());
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        if (sysUser == null) {
            throw new BussinessException(BusinessResponseCode.ACCOUNT_UNFIND);
        }
        if (SysUserStatus.ACCOUNT_INVALID.equals(sysUser.getDeleted())) {
            throw new BussinessException(BusinessResponseCode.USER_INVALID);
        }
        if (SysUserLock.ACCOUNT_LOCK.equals(sysUser.getStatus())) {
            throw new BussinessException(BusinessResponseCode.USER_LOCK);
        }
        String originPwd = sysUserLoginVO.getPassword();
        String dbPwd = sysUser.getPwd();
        String salt = sysUser.getSalt();
        if (!dbPwd.equals(MD5Util.encode(originPwd, salt))) {
            throw new BussinessException(BusinessResponseCode.PWD_ERROR);
        }
        Map userMap = new HashMap();
        userMap.put(Constant.TOKEN_ROLE, "");
        userMap.put(Constant.TOKEN_PERMISSION, "");
        userMap.put(Constant.TOKEN_CREATIE_TIME, new Date().getTime());
        String token = JwtUtil.generateToken("nexos", sysUser.getId(), userMap, shiroProperties.getSecret(), shiroProperties.getExpireTime());
        resultInfo.setSuccess(true);
        resultInfo.setResult(token);
        resultInfo.setCode("0000");
        redisService.delete(Constant.REFRESH_KEY+sysUserLoginVO.getUserName());
        return resultInfo;

    }

    @Override
    public ResultInfo loginOut(String id) {
        return null;
    }

    @Override
    public ResultInfo registerUser(SysUserSimpleInfo sysUserSimpleInfo) {
        ResultInfo resultInfo = ResultInfo.newInstance();

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserSimpleInfo, sysUser);
        try {
            // 获取到原始密码
            String originPwd = sysUserSimpleInfo.getPwd();
            // 生产随机salt 进行MD5加密
            String salt = SaltGenerator.generator();
            sysUser.setSalt(salt);
            String password = MD5Util.encode(originPwd, salt);
            sysUser.setPwd(password);
            sysUserMapper.insert(sysUser);
            resultInfo.setSuccess(true);
            return resultInfo;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new BussinessException(BusinessResponseCode.SYSTEM_BUSY);
        }
    }

    @Override
    public ResultInfo getPublicKey(String userName) {
        ResultInfo resultInfo = ResultInfo.newInstance();
        Map<String, String> keyMap = null;
        try {
            keyMap = RSAEncrypt.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String privateKey = keyMap.get("private");
        String publicKey = keyMap.get("public");
        resultInfo.setResult(publicKey);
        // 存入redis中
        redisTemplate.opsForValue().set("LoginPublicKey:" + userName, privateKey, 18000, TimeUnit.SECONDS);
        return resultInfo;
    }

    @Override
    public ResultInfo preRegisterCheck(PreRegisterCheckVO checkVO) {
        ResultInfo resultInfo = ResultInfo.newInstance();
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        QueryWrapper<SysUser> query = queryWrapper.eq("user_name", checkVO.getUserName()).or().
                eq("nick_name", checkVO.getNickName()).or().
                eq("email", checkVO.getEmail());
        SysUser sysUserfromDb = sysUserMapper.selectOne(query);
        if (sysUserfromDb != null) {
            if (checkVO.getUserName().equals(sysUserfromDb.getUserName())) {
                resultInfo.setResultDesc("注册失败,此用户名已经存在");
            } else if (checkVO.getNickName().equals(sysUserfromDb.getNickName())) {
                resultInfo.setResultDesc("注册失败,昵称已经存在");
            } else {
                resultInfo.setResultDesc("注册失败,邮箱已经被注册");
            }
            resultInfo.setSuccess(false);
            return resultInfo;
        }
        resultInfo.setSuccess(true);
        return resultInfo;
    }
}