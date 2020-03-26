package com.nexos.nexos_admin.service.facade;

import com.nexos.nexos_admin.domain.ResultInfo;
import com.nexos.nexos_admin.vo.PreRegisterCheckVO;
import com.nexos.nexos_admin.vo.SysUserLoginVO;
import com.nexos.nexos_admin.vo.SysUserSimpleInfo;

public interface SysUserService {

    /**
     * 系统登录
     * @param sysUserLoginVO
     * @return
     */
    ResultInfo login(SysUserLoginVO sysUserLoginVO);

    /**
     * 退出登录
     * @return
     */
    ResultInfo loginOut(String id);


    /**
     * 用户注册
     * @return
     */
    ResultInfo registerUser(SysUserSimpleInfo sysUserSimpleInfo);

    /**
     *  获取公钥
     * @return
     */
    ResultInfo getPublicKey(String userName);

    /**
     *  注册前,检查字段是否存在
     * @return
     */
    ResultInfo preRegisterCheck(PreRegisterCheckVO checkVO);

}
