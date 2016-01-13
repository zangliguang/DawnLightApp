/*
 * @(#)IExceptionHandler.java	 2012-9-18
 *
 * Copyright 2004-2012 Liuzhongnan. 
 * All rights reserved.
 * 
 * LiuZhongnan 81595157@126.com PROPRIETARY/CONFIDENTIAL.
 */

package com.youku.player.apiservice;

/**
 * @author 刘仲男 81595157@qq.com
 * @version $Revision$
 * @Description: TODO 异常处理器
 * @created time 2012-9-18 下午3:55:21
 */
public interface IExceptionHandler {
    /**
     * 获得错误代码
     */
    public int getErrorCode();

    /**
     * 获得具体的错误信息内容
     */
    public String getErrorInfo();
}
