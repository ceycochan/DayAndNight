package com.memo.dayandnight.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.memo.dayandnight.R;
import com.memo.dayandnight.adapter.SimpleChanAdapter;
import com.memo.dayandnight.helper.DayNightHelper;
import com.memo.dayandnight.mode.DayNight;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 夜间模式实现方案
 */

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    //SP保存当前的主题设置
    private final static String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private LinearLayout mHeaderLayout;
    private DayNightHelper mDayNightHelper;

    private List<RelativeLayout> mLayoutList;
    private List<CheckBox> mCheckBoxList;
    private List<TextView> mTextViewList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        initData();
        initTheme();
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new SimpleChanAdapter());

        mHeaderLayout = (LinearLayout) findViewById(R.id.header_layout);

        mLayoutList = new ArrayList<>(); // 布局容器 (容纳不同模式下的不同布局)
        mLayoutList.add((RelativeLayout) findViewById(R.id.jianshu_layout));
        mLayoutList.add((RelativeLayout) findViewById(R.id.zhihu_layout));

        // 布局容器
        mTextViewList = new ArrayList<>();

        mTextViewList.add((TextView) findViewById(R.id.tv_jianshu));
        mTextViewList.add((TextView) findViewById(R.id.tv_zhihu));

        mCheckBoxList = new ArrayList<>();  // cb容器

        CheckBox ckbJianshu = (CheckBox) findViewById(R.id.ckb_jianshu);
        ckbJianshu.setOnCheckedChangeListener(this);
        mCheckBoxList.add(ckbJianshu);
        CheckBox ckbZhihu = (CheckBox) findViewById(R.id.ckb_zhihu);
        ckbZhihu.setOnCheckedChangeListener(this);
        mCheckBoxList.add(ckbZhihu);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int viewId = compoundButton.getId();
        if (viewId == R.id.ckb_jianshu) {
            changeThemeByJianShu();
        } else if (viewId == R.id.ckb_zhihu) {
            changeThemeByZhiHu();
        }
    }

    private void initData() {
        mDayNightHelper = new DayNightHelper(this);
    }

    private void initTheme() {  // 初始化theme
        if (mDayNightHelper.isDay()) {
            setTheme(R.style.DayTheme);  // setTheme @ source code
        } else {
            setTheme(R.style.NightTheme);
        }
    }


    /**
     * 切换主题
     */
    private void toggleThemeSetting() {
        if (mDayNightHelper.isDay()) {
            mDayNightHelper.setMode(DayNight.NIGHT);
            setTheme(R.style.NightTheme);
        } else {
            mDayNightHelper.setMode(DayNight.DAY);
            setTheme(R.style.DayTheme);
        }
    }

    /**
     * 使用简书的实现套路来切换夜间主题
     */
    private void changeThemeByJianShu() {
        toggleThemeSetting();
        refreshUI();
    }

    /**
     * 使用知乎的实现套路来切换夜间主题
     */
    private void changeThemeByZhiHu() {
        showAnimation();
        toggleThemeSetting();
        refreshUI();
    }


    /**
     * 核心代码:主题切换需要从刷新UI
     */
    private void refreshUI() {
        TypedValue background = new TypedValue();
        TypedValue textColor = new TypedValue();

        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.clockBackground, background, true); // boolean returned
        theme.resolveAttribute(R.attr.clockTextColor, textColor, true);

        mHeaderLayout.setBackgroundResource(background.resourceId);

        for (RelativeLayout layout : mLayoutList) {
            layout.setBackgroundResource(background.resourceId);
        }
        for (CheckBox checkBox : mCheckBoxList) {
            checkBox.setBackgroundResource(background.resourceId);
        }
        for (TextView textView : mTextViewList) {
            textView.setBackgroundResource(background.resourceId);
        }

        Resources resources = getResources();
        for (TextView textView : mTextViewList) {
            textView.setTextColor(resources.getColor(textColor.resourceId));
        }

        /**
         * 配置RecyclerView的mode切换
         */
        int childCount = mRecyclerView.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            ViewGroup childView = (ViewGroup) mRecyclerView.getChildAt(childIndex);
            childView.setBackgroundResource(background.resourceId);

            View infoLayout = childView.findViewById(R.id.info_layout);
            infoLayout.setBackgroundResource(background.resourceId);

            TextView nickName = (TextView) childView.findViewById(R.id.tv_nickname);
            nickName.setBackgroundResource(background.resourceId);
            nickName.setTextColor(resources.getColor(textColor.resourceId));

            TextView motto = (TextView) childView.findViewById(R.id.tv_motto);
            motto.setBackgroundResource(background.resourceId);
            motto.setTextColor(resources.getColor(textColor.resourceId));
        }

        /**
         * 让RecyclerView缓存在Pool中的item失效  @ Java反射
         */

        Class<RecyclerView> recyclerViewClass = RecyclerView.class;
        try {
            Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");  // mRecycler负责
            declaredField.setAccessible(true);
            Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName()).getDeclaredMethod("clear", (Class<?>[]) new Class[0]);  // (Class<?>[]) 一定要有 否则模式切换后滑动RecyclerView的item不完全切换
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(declaredField.get(mRecyclerView), new Object[0]);
            RecyclerView.RecycledViewPool recycledViewPool = mRecyclerView.getRecycledViewPool();
            recycledViewPool.clear();
        } catch (NoSuchFieldException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        refreshStatusBar();
    }

    private void refreshStatusBar() {
        if (Build.VERSION.SDK_INT>=21){
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            getWindow().setStatusBarColor(getResources().getColor(typedValue.resourceId));
        }
    }

    /**
     * 模式切换动画
     *
     */

    private void showAnimation() {
        final View decorView = getWindow().getDecorView();
        Bitmap cacheBitmap = getCacheBitmapFromView(decorView);
        if (decorView instanceof ViewGroup && cacheBitmap != null) {
            final View view = new View(this);
            view.setBackground(new BitmapDrawable(getResources(), cacheBitmap));
            ViewGroup.LayoutParams layoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) decorView).addView(view, layoutParam);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            objectAnimator.setDuration(300);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ((ViewGroup) decorView).removeView(view);
                }
            });
            objectAnimator.start();
        }
    }

    /**
     * 获取某个View的缓存视图
     */

    private Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true; //运行后新加final
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache(); //运行后新加final
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }

}
