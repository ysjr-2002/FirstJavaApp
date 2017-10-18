package com.mydata;

/**
 * Created by ysj on 2017/10/17.
 */

public interface HttpCallbackListener {


    void onFinished(String response);

    void onError(Exception ex);
}
