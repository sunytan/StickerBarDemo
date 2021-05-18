package com.example.stickerbardemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private AppBarLayout appBarLayout;
    private CoordinatorLayout coordinatorLayout;
    boolean canScroll = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusBarColor(Color.TRANSPARENT);
        recyclerView = findViewById(R.id.ry);
        appBarLayout = findViewById(R.id.app_bar);

        coordinatorLayout = findViewById(R.id.root_ly);
        floatingActionButton = findViewById(R.id.float_btn);
        setFloatButtonColor(canScroll ? Color.GREEN:Color.RED);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canScroll = !canScroll;
                setCanScroll(canScroll);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        MyAdapter myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        ArrayList<Integer> datas = new ArrayList<>(30);
        for (int i = 0; i < 30; i++) {
            datas.add(i);
        }
        myAdapter.setDatas(datas);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d(TAG, "onOffsetChanged: "+verticalOffset);
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    Log.d(TAG, "onOffsetChanged: closed");
                }
            }
        });
    }

    private void setCanScroll(boolean canScroll) {
//        第一种实现禁止滑动的方式
//        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams();
//        MyBehavior behavior = (MyBehavior) lp.getBehavior();
//        behavior.setCanScroll(canScroll);
        recyclerView.setNestedScrollingEnabled(canScroll);
    }

    private void setStatusBarColor(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }
    }

    private void invokeSetNestedScrollAccepted(boolean accept){
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior)lp.getBehavior();
        try {
            Method method = lp.getClass().getDeclaredMethod("setNestedScrollAccepted",new Class[]{int.class,boolean.class});
            method.setAccessible(true);
            method.invoke(lp,0,accept);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void setFloatButtonColor(int color){
        ColorStateList colorStateList = ColorStateList.valueOf(color);
        floatingActionButton.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
        floatingActionButton.setBackgroundTintList(colorStateList);
    }


}