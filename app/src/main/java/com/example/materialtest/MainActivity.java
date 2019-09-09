package com.example.materialtest;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerlayout;
    //水果类数据集fruits
    private Fruit[] fruits={new Fruit("Apple",R.drawable.apple),new Fruit("Banana",R.drawable.banana),
        new Fruit("Orange",R.drawable.orange),new Fruit("Watermelon",R.drawable.watermelon),
        new Fruit("Grape",R.drawable.grape),new Fruit("Pineapple",R.drawable.pineapple),
        new Fruit("Pear",R.drawable.pear),new Fruit("Strawberry",R.drawable.strawberry),
        new Fruit("Cherry",R.drawable.cherry),new Fruit("Lemon",R.drawable.lemon)};
    //使用RecyclerView展示数据，需要数据队列和适配器
    private List<Fruit> fruitList=new ArrayList<>();
    private FruitAdapter adapter;
    //使用swipeRefreshLayout实现下拉刷新
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //使用Toolbar代替ActionBar，记得使用无ActionBar主题
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //使用DrawerLayout实现滑动菜单功能
        mDrawerlayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBar actionBar=getSupportActionBar();
        //通过修改默认返回按钮的样式和功能实现侧边菜单按钮
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);//显示导航按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_launcher_foreground);//设置导航按钮图标
        }
        //给NavigationView处理菜单项的点击事件
        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_call);//将Call菜单项设置为默认选中
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            //当用户点击菜单项时，会回调onNavigationItemSelected()方法，在此方法中处理相应逻辑
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_call:
                        Toast.makeText(MainActivity.this,"You click nav_call",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_friends:
                        Toast.makeText(MainActivity.this,"You click nav_friends",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_location:
                        Toast.makeText(MainActivity.this,"You click nav_location",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_mail:
                        Toast.makeText(MainActivity.this,"You click nav_mail",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_task:
                        Toast.makeText(MainActivity.this,"You click nav_task",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        mDrawerlayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });
        //给悬浮按钮FloatingActionButton添加点击事件监听
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //使用Snackbar实现提示功能，比Toast多一个按钮的点击事件
                //为了解决使用Snackbar提示遮挡悬浮按钮，可使用CoordinatorLayout作为父布局，此时会自动偏移从而不会被遮挡
                Snackbar.make(v,"Data added",Snackbar.LENGTH_SHORT)
                        .setAction("Undo",new View.OnClickListener(){//添加一个动作用于用户交互
                            @Override
                            public void onClick(View view){
                                Toast.makeText(MainActivity.this,"Data restored",Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
        //使用RecyclerView展示数据
        initFruits();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);//使用网格布局，这里指定每行两列
        recyclerView.setLayoutManager(layoutManager);
        adapter=new FruitAdapter(fruitList);//使用适配器进行数据匹配
        recyclerView.setAdapter(adapter);
        //实现下拉刷新逻辑
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);//设置下拉刷新进度条的颜色
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();
            }
        });
    }
    //更新数据
    private void refreshFruits(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                //切换到主线程更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initFruits();
                        adapter.notifyDataSetChanged();//更新数据集并刷新显示
                        swipeRefresh.setRefreshing(false);//通知刷新事件结束，隐藏刷新进度条
                    }
                });
            }
        }).start();
    }
    //初始化水果列表数据
    private void initFruits(){
        fruitList.clear();
        for(int i=0;i<49;i++){
            //获取随机数，从数据集里随机获取信息
            Random random=new Random();
            int index=random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }
    //加载菜单文件，true表示显示菜单
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    //为菜单按钮设计响应逻辑
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home://修改默认返回按钮的功能
                mDrawerlayout.openDrawer(GravityCompat.START);//调用openDrawer()方法显示菜单
                break;
            case R.id.backup:
                Toast.makeText(this,"You click Backup",Toast.LENGTH_SHORT).show();
                break;
            case R.id.add:
                Toast.makeText(this,"You click Add",Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(this,"You click Setting",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
}
