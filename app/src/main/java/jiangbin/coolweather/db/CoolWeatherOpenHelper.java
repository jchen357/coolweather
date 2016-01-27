package jiangbin.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/1/22 0022.
 * SQLiteOpenHelper用于数据库的创建和版本更新
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

    /**
     * Province表建表语言
     * @jiang
     */
    public static final String CREATE_PROVINCE = "create table Province ("
            + "id integer primary key autoincrement, "
            + "province_name text, "
            + "province_code text) ";

    /**
     * City表建表语言
     * @jiang
     */
    public static final String CREATE_CITY = "create table City ("
            + "id integer primary key autoincrement, "
            + "city_name text, "
            + "city_code text, "
            + "province_id integer)";

    /**
     * Country表建表语句
     * @jiang
     */
    public static final String CREATE_COUNTY = "create table County ("
            + "id integer primary key autoincrement, "
            + "county_name text, "
            + "county_code text, "
            + "city_id integer)";

    public CoolWeatherOpenHelper(Context context, String name,
                                 SQLiteDatabase.CursorFactory factory, int version) {
        /* 调用父类SQLiteOpenHelper()的构造函数 */
        super(context, name, factory, version);
    }

    /**
     * 如果用户第一次使用该程序，系统会自动调用onCreate(SQLiteDatabase db)来初始化底层数据库
     * @jiang
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        /* 执行SQL建表语句 */
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
