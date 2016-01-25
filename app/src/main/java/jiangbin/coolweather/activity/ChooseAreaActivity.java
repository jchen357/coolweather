package jiangbin.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jiangbin.coolweather.R;
import jiangbin.coolweather.model.City;
import jiangbin.coolweather.model.CoolWeatherDB;
import jiangbin.coolweather.model.County;
import jiangbin.coolweather.model.Province;
import jiangbin.coolweather.util.HttpCallbackListener;
import jiangbin.coolweather.util.HttpUtil;
import jiangbin.coolweather.util.Utility;

/**
 * Created by Administrator on 2016/1/22 0022.
 */
public class ChooseAreaActivity extends Activity {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private CoolWeatherDB coolWeatherDB;
    /*定义String类型的ArrayList*/
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
    private List<County> countyList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*继承父类activity的onCreate方法*/
        super.onCreate(savedInstanceState);
        //-----------------------------------------------------
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //
        if (prefs.getBoolean("city_selected", false)) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        //-----------------------------------------------------
        /*设置窗口风格*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*设置内容为layout文件下的choose_area.xml*/
        setContentView(R.layout.choose_area);
        /*对应于choose_area.xml中声明的id为list_view的listView和id为title_text的titleText*/
        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);
        /**
         * ArrayAdapter:通常用于将List数组或List集合的多个值包装成多个列表项
         * ArrayAdapter(Context, textViewResourcedId, 数组或List)
         * SimpleAdapter:可用于将List集合的多个对象包装成多个列表项
         * <--------------------------------------->
         * Context  访问整个Android应用的接口
         * textViewResourcedId  资源ID
         * 数组或List 提供数据
         * <--------------------------------------->
         * android.R.layout.simple_list_item_1安卓自带的适配器布局
         * _1在使用适配器时，只需要提供一个文本类型的数据
         * _2需要两个
         */
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        /*为ListView设置adapter*/
        listView.setAdapter(adapter);
        coolWeatherDB = CoolWeatherDB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
                if (currentLevel == LEVEL_PROVINCE) {
                    /**
                     * 将List<Province> provinceList的索引（是一个Province对象）传给selectedProvince
                     */
                    selectedProvince = provinceList.get(index);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(index);
                    queryCounties();
                    //---------------------------------------
                } else if (currentLevel == LEVEL_COUNTY) {
                    String countyCode = countyList.get(index).getcountyCode();
                    Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
                    intent.putExtra("county_code", countyCode);
                    startActivity(intent);
                    finish();
                    //---------------------------------------
                }
            }
        });
        /*默认遍历省*/
        queryProvinces();
    }

    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryProvinces() {
        provinceList = coolWeatherDB.loadProvince();
        if (provinceList.size() > 0) {
            /*初始化dataList*/
            dataList.clear();
            /*foreach写法*/
            for (Province province : provinceList) {
                /*因为是在遍历省名，所以用getProvinceName()得到省名添加进dataList进行显示*/
                dataList.add(province.getProvinceName());
            }
            /*notifyDataSetChanged():当传入的View或者dataList发生变化，进行更新*/
            adapter.notifyDataSetChanged();
            /**
             * 设置当前选定的项目。如果在触摸模式中，
             * 项目不会被选择，但它仍然会被适当地定位。
             * 如果指定的选择位置小于0，则在位置0处的项将被选择
             */
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        } else {
            /*传入代号和类型*/
            queryFromServer(null, "province");
        }
    }

    /**
     * 查询省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询
     * 大部分与queryProvinces()类似，着重分析不同
     */
    private void queryCities() {
        /*需要传入省级的代号*/
        cityList = coolWeatherDB.loadCities(selectedProvince.getId());
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        } else {
            /*传入省级代号和市级类型*/
            queryFromServer(selectedProvince.getProvinceCode(), "city");
        }
    }

    /**
     * 查询市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCounties() {
        /*需要传入市级的代号*/
        countyList = coolWeatherDB.loadCounty(selectedCity.getId());
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getcountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currentLevel = LEVEL_CITY;
        } else {
            /*传入市级代号和市级类型*/
            queryFromServer(selectedCity.getCityCode(), "county");
        }
    }

    /**
     * 根据传入的代号和类型从服务器上查询省市县数据
     */
    private void queryFromServer(final String code, final String type) {
        String address;
        /*当code为空指针或者长度为0时返回TRUE*/
        if (!TextUtils.isEmpty(code)) {
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        } else {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        /*显示加载对话框*/
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvincesResponse(coolWeatherDB, response);
                } else if ("city".equals(type)) {
                    result = Utility.handleCitiesResponse(coolWeatherDB, response, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountiesResponse(coolWeatherDB, response, selectedCity.getId());
                }
                if (result) {
                    /*通过runOnUiThread()方法到主线程处理逻辑*/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /*数据接收以及解析完成，关闭加载对话框*/
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                /*通过runOnUiThread()方法到主线程处理逻辑*/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*数据接收以及解析没有完成，关闭加载对话框*/
                        closeProgressDialog();
                        /*显示加载失败的提示框*/
                        Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     *  关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * 捕捉back按键，根据当前的级别来判断，此时应该返回市列表、省列表、还是直接退出
     */
    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_COUNTY) {
            queryCities();
        } else if (currentLevel == LEVEL_CITY) {
            queryProvinces();
        } else {
            finish();
        }
    }
}



















