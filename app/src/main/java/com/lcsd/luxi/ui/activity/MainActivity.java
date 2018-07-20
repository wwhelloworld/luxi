package com.lcsd.luxi.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.lcsd.luxi.R;
import com.lcsd.luxi.permissions.PerUtils;
import com.lcsd.luxi.permissions.PerimissionsCallback;
import com.lcsd.luxi.permissions.PermissionEnum;
import com.lcsd.luxi.permissions.PermissionManager;
import com.lcsd.luxi.ui.fragment.Fragment1;
import com.lcsd.luxi.ui.fragment.Fragment2;
import com.lcsd.luxi.ui.fragment.Fragment3;
import com.lcsd.luxi.ui.fragment.Fragment4;
import com.lcsd.luxi.utils.UpdateManager;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Context mContext;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Fragment1 mFragment1;
    private Fragment2 mFragment2;
    private Fragment3 mFragment3;
    private Fragment4 mFragment4;
    public  static LinearLayout mLl3;


    @BindView(R.id.ll_01)
    LinearLayout mLl1;
    @BindView(R.id.ll_02)
    LinearLayout mLl2;
//    @BindView(R.id.ll_03)
//    LinearLayout mLl3;
    @BindView(R.id.ll_04)
    LinearLayout mLl4;

    @BindView(R.id.iv_01)
    ImageView mIv1;
    @BindView(R.id.iv_02)
    ImageView mIv2;
    @BindView(R.id.iv_03)
    ImageView mIv3;
    @BindView(R.id.iv_04)
    ImageView mIv4;

    @BindView(R.id.tv_01)
    TextView mTv1;
    @BindView(R.id.tv_02)
    TextView mTv2;
    @BindView(R.id.tv_03)
    TextView mTv3;
    @BindView(R.id.tv_04)
    TextView mTv4;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();

    }


    @Override
    protected void initData() {
        mLl3=findViewById(R.id.ll_03);
        mContext = this;
        mFragmentManager = getFragmentManager();
        showFragment(1);
        ImmersionBar.with(this).statusBarColorInt(getResources().getColor(R.color.color_red_h)).init();

        //权限

        PermissionManager
                .with(mContext)
                .tag(1861)
                .permission(PermissionEnum.READ_EXTERNAL_STORAGE, PermissionEnum.WRITE_EXTERNAL_STORAGE)
                .callback(new PerimissionsCallback() {
                    @Override
                    public void onGranted(ArrayList<PermissionEnum> grantedList) {
                        //权限通过
                        new UpdateManager(mContext);

                    }

                    @Override
                    public void onDenied(ArrayList<PermissionEnum> deniedList) {
                        PermissionDenied(deniedList);
                    }
                }).checkAsk();
    }

    @Override
    protected void setListener() {
        mLl1.setOnClickListener(this);
        mLl2.setOnClickListener(this);
        mLl3.setOnClickListener(this);
        mLl4.setOnClickListener(this);
    }

    /**
     * 显示fragment
     */
    private void showFragment(int index) {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        hideFragment(mFragmentTransaction);/*想要显示一个fragment,先隐藏所有fragment，防止重叠*/
        switch (index) {
            case 1:
                /*如果fragment1已经存在则将其显示出来*/

                if (mFragment1 != null)
                    mFragmentTransaction.show(mFragment1);

                    /*否则是第一次切换则添加fragment1，注意添加后是会显示出来的，replace方法也是先remove后add*/
                else {
                    mFragment1 = new Fragment1();
                    mFragmentTransaction.add(R.id.fragment_controller, mFragment1);
                }
                break;
            case 2:

                if (mFragment2 != null)
                    mFragmentTransaction.show(mFragment2);
                else {
                    mFragment2 = new Fragment2();
                    mFragmentTransaction.add(R.id.fragment_controller, mFragment2);

                }
                break;
            case 3:
                if (mFragment3 != null)
                    mFragmentTransaction.show(mFragment3);
                else {
                    mFragment3 = new Fragment3();
                    mFragmentTransaction.add(R.id.fragment_controller, mFragment3);
                }
                break;
            case 4:
                if (mFragment4 != null)
                    mFragmentTransaction.show(mFragment4);
                else {
                    mFragment4 = new Fragment4();
                    mFragmentTransaction.add(R.id.fragment_controller, mFragment4);
                }
                break;
        }
        mFragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 隐藏fragment
     */
    private void hideFragment(FragmentTransaction ft) {
        if (mFragment1 != null)
            ft.hide(mFragment1);
        if (mFragment2 != null)
            ft.hide(mFragment2);
        if (mFragment3 != null)
            ft.hide(mFragment3);
        if (mFragment4 != null)
            ft.hide(mFragment4);
    }

    @Override
    public void onClick(View v) {
        mTv1.setTextColor(getResources().getColor(R.color.light_black1));
        mTv2.setTextColor(getResources().getColor(R.color.light_black1));
        mTv3.setTextColor(getResources().getColor(R.color.light_black1));
        mTv4.setTextColor(getResources().getColor(R.color.light_black1));
        mIv1.setBackgroundResource(R.drawable.img_bottom_firstpage);
        mIv2.setBackgroundResource(R.drawable.img_bottom_xinwen);
        mIv3.setBackgroundResource(R.drawable.img_bottom_zhibo);
        mIv4.setBackgroundResource(R.drawable.img_bottom_fuwu);
        switch (v.getId()) {
            case R.id.ll_01:
                mIv1.setBackgroundResource(R.drawable.img_bottom_firstpages);
                ImmersionBar.with(this).statusBarDarkFont(false).init();
                ImmersionBar.with(this).statusBarColorInt(getResources().getColor(R.color.color_red_h)).init();
                showFragment(1);
                mTv1.setTextColor(getResources().getColor(R.color.orange));
                break;
            case R.id.ll_02:
                mIv2.setBackgroundResource(R.drawable.img_bottom_xinwens);
                ImmersionBar.with(this).statusBarDarkFont(true, 0.3f).init();
                ImmersionBar.with(this).statusBarColorInt(getResources().getColor(R.color.transparent)).init();
                showFragment(2);
                mTv2.setTextColor(getResources().getColor(R.color.orange));
                break;
            case R.id.ll_03:
                mIv3.setBackgroundResource(R.drawable.img_bottom_zhibos);
                ImmersionBar.with(this).statusBarDarkFont(false).init();
                ImmersionBar.with(this).statusBarColorInt(getResources().getColor(R.color.transparent)).init();

                showFragment(3);
                mTv3.setTextColor(getResources().getColor(R.color.orange));
                break;
            case R.id.ll_04:
                mIv4.setBackgroundResource(R.drawable.img_bottom_fuwus);
                ImmersionBar.with(this).statusBarDarkFont(true, 0.3f).init();
                ImmersionBar.with(this).statusBarColorInt(getResources().getColor(R.color.transparent)).init();

                showFragment(4);
                mTv4.setTextColor(getResources().getColor(R.color.orange));
                break;
        }
    }


    private void PermissionDenied(final ArrayList<PermissionEnum> permissionsDenied) {
        StringBuilder msgCN = new StringBuilder();
        for (int i = 0; i < permissionsDenied.size(); i++) {
            if (i == permissionsDenied.size() - 1) {
                msgCN.append(permissionsDenied.get(i).getName_cn());
            } else {
                msgCN.append(permissionsDenied.get(i).getName_cn() + ",");
            }
        }
        if (mContext == null) {
            return;
        }
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setMessage(String.format(mContext.getResources().getString(R.string.permission_explain), msgCN.toString()))
                .setCancelable(false)
                .setPositiveButton(R.string.per_setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PerUtils.openApplicationSettings(mContext, R.class.getPackage().getName());
                    }
                })
                .setNegativeButton(R.string.per_cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        alertDialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handleResult(requestCode, permissions, grantResults);
    }


    /**
     * 返回键两次退出程序
     */

    private long mExitTime;

    @SuppressLint("WrongConstant")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Fragment3.zb_player != null && Fragment3.zb_player.isFullscreen()) {
            //防止音量键导致返回竖屏
            if (Fragment3.zb_player.handleVolumeKey(keyCode)) {
                return true;
            } else {
                Fragment3.zb_player.onBackPressed();
            }
            return false;

        } else {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
