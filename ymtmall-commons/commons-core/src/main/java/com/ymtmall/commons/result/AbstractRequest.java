package com.ymtmall.commons.result;

import java.io.Serializable;

/**
 * Created by @author yangmingtian on 2019/9/9
 */
public abstract class AbstractRequest implements Serializable {

    private static final long serialVersionUID = 3050525437741303575L;

    public abstract void requestCheck();

    @Override
    public String toString() {
        return "AbstractRequest{}";
    }
}
