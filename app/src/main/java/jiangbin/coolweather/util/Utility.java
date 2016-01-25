package jiangbin.coolweather.util;

import android.text.TextUtils;

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
                    //将解析出来的数据存储到City表
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
                    county.setcountyCode(array[0]);
                    county.setcountyName(array[1]);
                    county.setcityId(cityId);
                    //将解析出来的数据存储到County表
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }


}















