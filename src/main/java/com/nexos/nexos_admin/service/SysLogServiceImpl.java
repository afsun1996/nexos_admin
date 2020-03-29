package com.nexos.nexos_admin.service;    /**
 * Created by 孙爱飞 on 2020/3/27.
 */

import com.nexos.nexos_admin.mapper.SysLogMapper;
import com.nexos.nexos_admin.po.SysLog;
import com.nexos.nexos_admin.service.facade.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *@description: //TODO
 *@author: your name
 *@create: 2020-03-27 20:33
 *@version: 1.0
 */
@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    SysLogMapper sysLogMapper;


    @Override
//    @Async
    public int saveSyslog(SysLog sysLog) {
       return  sysLogMapper.insert(sysLog);
    }
}
