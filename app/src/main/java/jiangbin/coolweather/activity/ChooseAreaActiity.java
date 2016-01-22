package jiangbin.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jiangbin.coolweather.model.City;
import jiangbin.coolweather.model.CoolWeatherDB;
import jiangbin.coolweather.model.County;
import jiangbin.coolweather.model.Province;

/**
 * Created by Administrator on 2016/1/22 0022.
 */
public class ChooseAreaActiity extends Activity {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private CoolWeatherDB coolWeatherDB;
    private List<String> dataList = new ArrayList<String>();
    /**
     * 省列表
     */
    private List<Province> provinceList;
    /**
     * 市列表
     */
    private List<City> cityList;
    /**
     * 县列表
     */
    private List<County> cityList;
    /**
     * 选中的省份
     */
    private Province selectedProvince;
    /**
     * 选中的城市
     */
    private City selectedCity;
    /**
     * 当前选中的级别
     */
    private int currentLevel;
}
