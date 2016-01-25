package jiangbin.coolweather.model;

/**
 * Created by Administrator on 2016/1/22 0022.
 */
public class County {
    private int id;
    private String countyName;
    private String countyCode;
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getcountyName() {
        return countyName;
    }

    public void setcountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getcountyCode() { return countyCode; }

    public void setcountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public int getcityId() {
        return cityId;
    }

    public void setcityId(int cityId) {
        this.cityId = cityId;
    }
}