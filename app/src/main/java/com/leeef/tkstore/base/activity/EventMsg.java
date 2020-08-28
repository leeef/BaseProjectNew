package com.leeef.tkstore.base.activity;

/**
 * @Description:
 * @Author: leeeef
 * @CreateDate: 2020/8/28 13:50
 */
public class EventMsg {
    public MsgCode code;
    public Object obj;

    public EventMsg(MsgCode code) {
        this.code = code;
    }

    public EventMsg(MsgCode code, Object obj) {
        this.code = code;
        this.obj = obj;
    }

}