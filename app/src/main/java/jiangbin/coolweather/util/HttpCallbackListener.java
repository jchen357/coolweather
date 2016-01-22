package jiangbin.coolweather.util;

/**
 * Created by Administrator on 2016/1/22 0022.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}