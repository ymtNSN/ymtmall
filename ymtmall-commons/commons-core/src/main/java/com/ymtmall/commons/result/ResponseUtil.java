package com.ymtmall.commons.result;

/**
 * Created by @author yangmingtian on 2019/9/9
 */
public class ResponseUtil<T> {

    private ResponseData<T> responseData;

    public ResponseUtil() {
        responseData = new ResponseData<>();
        responseData.setSuccess(true);
        responseData.setCode(200);
        responseData.setMessage("success");
    }

    public ResponseData<T> setData(T t) {
        this.responseData.setResult(t);
        this.responseData.setSuccess(true);
        this.responseData.setCode(200);
        return this.responseData;
    }

    public ResponseData<T> setData(T t, String msg) {
        this.responseData.setResult(t);
        this.responseData.setSuccess(true);
        this.responseData.setMessage(msg);
        this.responseData.setCode(200);
        return this.responseData;
    }

    public ResponseData<T> setErrorMsg(String msg) {
        this.responseData.setSuccess(false);
        this.responseData.setMessage(msg);
        this.responseData.setCode(500);
        return this.responseData;
    }

    public ResponseData<T> setErrorMsg(Integer code, String msg) {
        this.responseData.setSuccess(false);
        this.responseData.setMessage(msg);
        this.responseData.setCode(500);
        return this.responseData;
    }
}
