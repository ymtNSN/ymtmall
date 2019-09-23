package com.ymtmall.commons.tool.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by @author yangmingtian on 2019/9/19
 */
public class TradeNoUtils {

    public static String generateTradeNo() {
        Date now = new Date();
        String date = new SimpleDateFormat("yyyyMMdd").format(now);
        String seconds = new SimpleDateFormat("HHmmss").format(now);
        String millSeconds = new SimpleDateFormat("SSS").format(now);
        return date + "00001000" + getTwo() + "00" + seconds + getTwo() + millSeconds;
    }

    private static String getTwo() {
        Random rad = new Random();

        String result = rad.nextInt(100) + "";

        if (result.length() == 1) {
            result = "0" + result;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(generateTradeNo());
    }

}
