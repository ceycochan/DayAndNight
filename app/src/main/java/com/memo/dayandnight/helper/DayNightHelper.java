package com.memo.dayandnight.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.memo.dayandnight.mode.DayNight;

/**
 * Created by lbl on 2017/1/13.
 */

public class DayNightHelper {
    //保存模式的设置
    private final static String FILE_NAME = "settings";
    private final static String MODE = "day_night_mode";

    private SharedPreferences mSharedPreferences;

    public DayNightHelper(Context context) {
        this.mSharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    //保存设置
    public boolean setMode(DayNight mode) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(MODE, mode.getName());
        return editor.commit();
    }

    //日间模式
    public boolean isDay() {
        String mode = mSharedPreferences.getString(MODE, DayNight.DAY.getName());
        if (DayNight.DAY.getName().equals(mode)) {
            return true;
        } else {
            return false;
        }
    }


    //夜间
    public boolean isNight() {
        String mode = mSharedPreferences.getString(MODE, DayNight.DAY.getName());
        if (DayNight.NIGHT.getName().equals(mode)) {
            return true;
        } else {
            return false;
        }
    }

}
