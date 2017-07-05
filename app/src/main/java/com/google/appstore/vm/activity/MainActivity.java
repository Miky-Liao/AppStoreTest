package com.google.appstore.vm.activity;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.appstore.R;
import com.google.appstore.util.PermissionUtil;
import com.google.appstore.util.UIUtils;
import com.google.appstore.vm.fragment.FragmentFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //针对6.0以上手机申请权限
        PermissionUtil.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        initView();
    }

    private void initView() {
        Toolbar toobar = (Toolbar) findViewById(R.id.content_tb);
        setSupportActionBar(toobar);   //使用toolbar替换ActionBar
        //Toolbar和DrawerLayout联动到一起
        mDrawerLayout = (DrawerLayout) findViewById(R.id.menu_dl);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toobar, R.string.open, R.string.close);   //关联DrawerLayout和Toolbar
        toggle.syncState();     //同步状态

        mDrawerLayout.addDrawerListener(toggle);     //让Toolbar菜单按钮跟随Menu变化
        NavigationView navigationView = (NavigationView) findViewById(R.id.menu_nv);
        navigationView.setNavigationItemSelectedListener(this);     //设置菜单项目点击监听

        TabLayout tabLayout = (TabLayout) findViewById(R.id.content_tl);
        ViewPager viewPager = (ViewPager) findViewById(R.id.main_content_vp);

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);    //与ViewPager进行绑定
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        mDrawerLayout.closeDrawers();
        return false;
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private final String[] mTitles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            mTitles = UIUtils.getStrings(R.array.tab_title);    //获得每个fragment的标题
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public int getCount() {
            if (mTitles != null) {
                return mTitles.length;
            }
            return 0;
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentFactory.createFragment(position);
        }

    }
}
