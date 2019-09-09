package com.ymtmall.commons.tool.exception;

import com.ymtmall.commons.result.AbstractResponse;

/**
 * Created by @author yangmingtian on 2019/9/9
 */
public class ExceptionUtil {

    /**
     * 描述: 将下层抛出的异常转化为resp返回码
     *
     * @param response response
     * @param e        e
     * @return {@link AbstractResponse}
     * @author yangmingtian
     */
    public static AbstractResponse handlerException4biz(AbstractResponse response, Exception e) throws Exception {
        if (!(e instanceof Exception)) {
            return null;
        }
        if (e instanceof ValidateException) {
            response.setCode(((ValidateException) e).getErrorCode());
            response.setMsg(e.getMessage());
        } else if (e instanceof ProcessException) {
            response.setCode(((ProcessException) e).getErrorCode());
            response.setMsg(e.getMessage());
        } else if (e instanceof BizException) {
            response.setCode(((BizException) e).getErrorCode());
            response.setMsg(e.getMessage());
        } else if (e instanceof Exception) {
            throw e;
        }
        return response;
    }
}
