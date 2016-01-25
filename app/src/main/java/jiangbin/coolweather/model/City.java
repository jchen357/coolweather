package jiangbin.coolweather.model;

/**
 * Created by Administrator on 2016/1/22 0022.
 */
public class City {
    private int id;
    private String cityName;
    private String cityCode;
    private int provinceId;

    /**
     * 设置id的get()和set()方法
     * id为
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}

