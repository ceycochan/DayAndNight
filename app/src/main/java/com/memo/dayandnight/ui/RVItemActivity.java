package com.memo.dayandnight.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.memo.dayandnight.R;
import com.memo.dayandnight.helper.DayNightHelper;

/**
 * Created by lbl on 2017/1/13.
 */

public class RVItemActivity extends AppCompatActivity {

    private DayNightHelper mDayNightHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initTheme();
        setContentView(R.layout.activity_author);
    }

    private void initTheme() {
        if (mDayNightHelper.isDay()) {
            setTheme(R.style.DayTheme);
        } else {
            setTheme(R.style.NightTheme);
        }
    }

    private void initData() {
        mDayNightHelper = new DayNightHelper(this);
    }


}
