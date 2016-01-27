package jiangbin.coolweather.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import jiangbin.coolweather.db.CoolWeatherOpenHelper;

/**
 * Created by Administrator on 2016/1/22 0022.
 */
public class CoolWeatherDB {

    /**
     * 数据库名
     */
    public static final String DB_NAME = "cool_weather";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static CoolWeatherDB coolWeatherDB;

    private SQLiteDatabase db;

    /**
     * 将构造方法私有化
     *
     */
    private CoolWeatherDB(Context context) {
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
        /**
         * 一旦得到SQLiteOpenHelper对象之后，可以使用getWritableDatabase()
         * 或getReadableDatabase()方法来获取一个用于操作数据库的SQLiteDatabase()实例
         * */
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取CoolWeatherDB的实例
     * 提供了一个getInstance方法来获取CoolWeatherDB的实例
     * 这样就可以保证全局范围内只会有一个CoolWeatherDB的实例
     */
    public synchronized static CoolWeatherDB getInstance(Context context) {
        if (coolWeatherDB == null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    /**
     * 将province实例存储到数据库
     *
     *
     */
    public void saveProvince(Province province) {
        if (province != null) {
            /**
             * ContentValues只能存储基本类型的数据，像string，int之类的，
             * 不能存储对象这种东西，而HashTable却可以存储对象
             */
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            db.insert("Province", null, values);
        }
    }

    /**
     * 从数据库读取全国所有的省份信息
     *
     * Cursor query():对指定数据进行查询
     */
    public List<Province> loadProvince() {
        List<Province> list  = new ArrayList<Province>();
        /**
         * Cursor query(String table, String[] columns, String selection,
         * String[] selectionArgs, String groupBy, String having,String orderBy)
         * 表名，列要求，where子句——行要求，where子句——对应的条件值，
         * 分组方式，having方式，排序方式
         */
        Cursor cursor = db
                .query("Province",null,null,null,null,null,null);
        /* Cursor moveToFirst():记录指针移动到第一行，移动成功返回TRUE */
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                /**
                 * 查询了 http://www.android-doc.com/reference/android/database/Cursor.html
                 * String getColumnIndex(String columnName),返回指定列的名称，如果不存在返回-1
                 * int getInt (int columnIndex),传入目标列的索性，返回索引的整型值
                 */
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            } while (cursor.moveToNext()); /*记录指针移动到下一个数据，移动成功返回TRUE*/
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 将实例City存储到数据库
     */
    public void saveCity(City city) {
        if (city != null) {

            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_id", city.getProvinceId());
            db.insert("City", null, values);
        }
    }

    /**
     * 从数据库读取某省下所有的城市信息
     */
    public List<City> loadCities(int provinceId) {
        List<City> list = new ArrayList<City>();
        /**
         * Cursor query(String table, String[] columns, String selection,
         * String[] selectionArgs, String groupBy, String having,String orderBy)
         * 表名，列要求，where子句——行要求，where子句——对应的条件值，
         * 分组方式，having方式，排序方式
         */
        Cursor cursor = db.query("City", null, "province_id = ?",
                new String[] { String.valueOf(provinceId) }, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 将County实例存储到数据库
     */
    public void saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put("county_name", county.getCountyName());
            values.put("county_code", county.getCountyCode());
            values.put("city_id", county.getCityId());
            db.insert("County", null, values);
        }
    }

    /**
     * 从数据库读取某城市下所有的县信息
     */
    public List<County> loadCounty(int cityId) {
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("County", null, "city_id = ?",
                new String[] { String.valueOf(cityId) }, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
                list.add(county);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }







}
