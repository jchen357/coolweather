package jiangbin.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jiangbin.coolweather.model.City;
import jiangbin.coolweather.model.CoolWeatherDB;
import jiangbin.coolweather.model.County;
import jiangbin.coolweather.model.Province;

/**
 * Created by Administrator on 2016/1/22 0022.
 * 服务器返回的省市县数据都是“代号|城市，代号|城市”这种格式的
 */
public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     */
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,
                                                               String response) {
        if (!TextUtils.isEmpty(response)) {
            /*按逗号分隔，变成{代号|城市}的数组*/
            String[] allprovinces = response.split(",");
            if (allprovinces != null && allprovinces.length > 0) {
                for (String p : allprovinces) {
                    /*再按单竖线分隔，分成代号和城市*/
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    /*将解析出来的数据存储到Province表*/
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,
                                                            String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0) {
                for (String c : allCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    /*将解析出来的数据存储到City表*/
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,
                                                 String response ,int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(",");
            if (allCounties != null && allCounties.length > 0) {
                for (String c : allCounties) {
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    /*将解析出来的数据存储到County表*/
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析服务器返回的JSON数据，并将解析出的数据存储到本地
     */
    public static void handleWeatherResponse(Context context, String response) {
        try {
            /**
             * {
             *  "weatherinfo":
             *      {
             *          "city":"昆山",
             *          "cityid":"101190404"
             *          "temp1":"21C"
             *          ...
             *      }
             * }
             **************************************************************
             *
             * JSON数据格式
             * object =
             * {
             *      propertyName1 : propertyValue1 ,
             *      propertyName2 : propertyValue2 ,
             *      ...
             * }
             */
            Log.d("Tag","handleWeatherResponse");
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            Log.d("Tag", "<JSON>" +cityName+temp1);
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
        } catch (JSONException e) {
            Log.d("Tag","JSONException");
            e.printStackTrace();
        }
    }

    /**
     * 将服务器返回的所有天气信息存储到SharedPreferences文件
     */
    public static void saveWeatherInfo(Context context, String cityName, String weatherCode,
                       String temp1, String temp2, String weatherDesp, String publishTime) {
        /**
         * SimpleDateFormat
         * y-年  M-月  d-天  Locale.CHINA-定位在中国
         */
        Log.d("Tag", "saveWeatherInfo" + cityName + temp1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        /**
         * SharedPreferences
         * 保存的数据主要是简单类型的key-value对，本身没有写入数据的能力
         * 通过内部的edit()方法获取Editor对象
         */
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        /**
         * putString(putBoolean):存入指定key对应的数据
         */
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        /**
         * format():把Date格式的数据转换为String型
         */
        editor.putString("current_date", sdf.format(new Date()));
        /**
         * commit():提交修改
         */
        editor.commit();
    }
}

