package com.ymtmall.commons.result;

import java.io.Serializable;

/**
 * Created by @author yangmingtian on 2019/9/9
 */
public abstract class AbstractResponse implements Serializable {
    private static final long serialVersionUID = 3458321185282131163L;

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
