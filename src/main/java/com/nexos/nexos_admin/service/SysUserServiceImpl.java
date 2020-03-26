package com.nexos.nexos_admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nexos.nexos_admin.domain.ResultInfo;
import com.nexos.nexos_admin.enums.SysUserStatus;
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
import org.springframework.util.StringUtils;

import java.security.NoSuchAlgorithmException;
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

    private static Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);


    @Override
    public ResultInfo login(SysUserLoginVO sysUserLoginVO) {
        ResultInfo resultInfo = ResultInfo.newInstance();
        String userName = sysUserLoginVO.getUserName();
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",sysUserLoginVO.getUserName());
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        if (sysUser == null){
            resultInfo.setSuccess(false);
            resultInfo.setResultDesc("登录失败,无此用户");
            return resultInfo;
        }
        if (SysUserStatus.ACCOUNT_INVALID.equals(sysUser.getDeleted())){
            resultInfo.setSuccess(false);
            resultInfo.setResultDesc("登录失败,用户已经被删除");
            return resultInfo;
        }
        if (SysUserStatus.ACCOUNT_LOCK.equals(sysUser.getStatus())){
            resultInfo.setSuccess(false);
            resultInfo.setResultDesc("登录失败,用户被锁");
            return resultInfo;
        }
        // 获取加密后的密码 用私密解码
        String password = sysUserLoginVO.getPassword();
        String privateKey = (String) redisTemplate.opsForValue().get("LoginPublicKey:" + userName);
        if (StringUtils.isEmpty(privateKey)){
            resultInfo.setSuccess(false);
            resultInfo.setResultDesc("登录失败,请先获取公钥");
            return resultInfo;
        }
        try {
            String originPwd = RSAEncrypt.decrypt(password, privateKey);
            String salt = sysUser.getSalt();
            if (!password.equals(MD5Util.encode(originPwd, salt))){
                resultInfo.setSuccess(false);
                resultInfo.setResultDesc("登录失败,密码不正确");
                return resultInfo;
            }
            // 登录成功
            redisTemplate.delete("LoginPublicKey:" + userName);
            String token = JwtUtil.generateToken("nexos", userName, null, "", 256000);
            resultInfo.setSuccess(true);
            resultInfo.setResult(token);
            resultInfo.setCode("0000");
            return resultInfo;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultInfo loginOut(String id) {
        return null;
    }

    @Override
    public ResultInfo registerUser(SysUserSimpleInfo sysUserSimpleInfo) {
        ResultInfo resultInfo = ResultInfo.newInstance();
        String privateKey = (String) redisTemplate.opsForValue().get("LoginPublicKey:" + sysUserSimpleInfo.getUserName());
        if (StringUtils.isEmpty(privateKey)){
            resultInfo.setSuccess(false);
            resultInfo.setResultDesc("登录失败,请先获取公钥");
            return resultInfo;
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserSimpleInfo,sysUser);
        try {
            // 获取到原始密码
            String originPwd = RSAEncrypt.decrypt(sysUserSimpleInfo.getPwd(), privateKey);
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
            throw new RuntimeException(e);
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
        redisTemplate.opsForValue().set("LoginPublicKey:"+userName,privateKey,18000, TimeUnit.SECONDS);
        return resultInfo;
    }

    @Override
    public ResultInfo preRegisterCheck(PreRegisterCheckVO checkVO) {
        ResultInfo resultInfo = ResultInfo.newInstance();
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        QueryWrapper<SysUser> query = queryWrapper.eq("user_name", checkVO.getUserName()).or().
                eq("nick_name",checkVO.getNickName()).or().
                eq("email",checkVO.getEmail());
        SysUser sysUserfromDb = sysUserMapper.selectOne(query);
        if (sysUserfromDb != null){
            if (checkVO.getUserName().equals(sysUserfromDb.getUserName())){
                resultInfo.setResultDesc("注册失败,此用户名已经存在");
            }else if (checkVO.getNickName().equals(sysUserfromDb.getNickName())){
                resultInfo.setResultDesc("注册失败,昵称已经存在");
            }else{
                resultInfo.setResultDesc("注册失败,邮箱已经被注册");
            }
            resultInfo.setSuccess(false);
            return resultInfo;
        }
        resultInfo.setSuccess(true);
        return resultInfo;
    }
}