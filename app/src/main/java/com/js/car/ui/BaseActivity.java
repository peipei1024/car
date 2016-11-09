package com.js.car.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.js.car.MainActivity;
import com.js.car.R;
import com.js.car.app.ActivityCollector;
import com.js.car.fragment.home.HomeFragment;
import com.js.car.fragment.MyFragment;
import com.js.car.fragment.NewFragment;
import com.js.car.fragment.news.widget.NewsListFragment;
import com.js.car.utils.IntentUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
* Class name :BaseActivity
*
* Version information :
*
* Describe ：
*
* Author ：裴徐泽
*
* Created by pei on 2016-5-3.
*
*/
public class BaseActivity extends FragmentActivity {

    @Bind(R.id.id_home_image)
    ImageView idHomeImage;
    @Bind(R.id.id_home_text)
    TextView idHomeText;
    @Bind(R.id.id_home_view)
    LinearLayout idHomeView;
    @Bind(R.id.id_message_image)
    ImageView idMessageImage;
    @Bind(R.id.id_message_text)
    TextView idMessageText;
    @Bind(R.id.id_message_view)
    LinearLayout idMessageView;
    @Bind(R.id.id_my_image)
    ImageView idMyImage;
    @Bind(R.id.id_my_text)
    TextView idMyText;
    @Bind(R.id.id_my_view)
    LinearLayout idMyView;
    @Bind(R.id.control_fab)
    ImageView controlFab;


    private HomeFragment homeFragment;
    private NewsListFragment newFragment;
    private MyFragment myFragment;
    private FragmentManager manager;
    private int[] res_unselect = {R.mipmap.icon_home_unselect, R.mipmap.icon_news_unselect, R.mipmap.icon_my_unselect};
    private int[] res_select = {R.mipmap.icon_home_select, R.mipmap.icon_news_select, R.mipmap.icon_my_select};
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);

        //处理“内存重启”
        if (savedInstanceState != null) {
            manager = getSupportFragmentManager();
            List<android.support.v4.app.Fragment> fragmentList = manager.getFragments();
            for (android.support.v4.app.Fragment fragment : fragmentList) {
                if (fragment instanceof HomeFragment) {
                    homeFragment = (HomeFragment) fragment;
                } else if (fragment instanceof NewFragment) {
                    newFragment = (NewsListFragment) fragment;
                } else if (fragment instanceof MyFragment) {
                    myFragment = (MyFragment) fragment;
                }

            }
            // 解决重叠问题
            manager.beginTransaction().show(homeFragment).hide(newFragment).hide(myFragment).commit();
        }else{
            // 正常时
            homeFragment = new HomeFragment();
            newFragment = new NewsListFragment();
            myFragment = new MyFragment();

            manager = getSupportFragmentManager();
            manager.beginTransaction().add(R.id.id_fragment, homeFragment, homeFragment.getClass().getName())
                    .add(R.id.id_fragment, newFragment, newFragment.getClass().getName())
                    .add(R.id.id_fragment, myFragment, myFragment.getClass().getName()).
                    show(homeFragment).hide(newFragment).hide(myFragment).commit();
        }
    }


    @OnClick({R.id.id_home_view, R.id.id_message_view, R.id.id_my_view,R.id.control_fab})
    public void onClick(View view) {
        init3Button();
        FragmentTransaction f = manager.beginTransaction();
        f.commit();
        switch (view.getId()) {
            case R.id.id_home_view:
                f.show(homeFragment).hide(newFragment).hide(myFragment);
                idHomeText.setTextColor(getResources().getColor(R.color.colorMain));
                idHomeImage.setImageResource(res_select[0]);
                break;
            case R.id.id_message_view:
                f.show(newFragment).hide(homeFragment).hide(myFragment);
                idMessageText.setTextColor(getResources().getColor(R.color.colorMain));
                idMessageImage.setImageResource(res_select[1]);
                break;
            case R.id.id_my_view:
                f.show(myFragment).hide(newFragment).hide(homeFragment);
                idMyText.setTextColor(getResources().getColor(R.color.colorMain));
                idMyImage.setImageResource(res_select[2]);
                break;
            case R.id.control_fab:
               // IntentUtils.doIntent(BaseActivity.this, com.wm.remusic.activity.MainActivity.class);
                break;
        }
    }



    /**
     * 初始化四个选项卡,进入没有选中状态
     */
    private void init3Button(){
        idHomeText.setTextColor(getResources().getColor(R.color.colorTextMain));
        idMessageText.setTextColor(getResources().getColor(R.color.colorTextMain));
        idMyText.setTextColor(getResources().getColor(R.color.colorTextMain));

        idHomeImage.setImageResource(res_unselect[0]);
        idMessageImage.setImageResource(res_unselect[1]);
        idMyImage.setImageResource(res_unselect[2]);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

}
