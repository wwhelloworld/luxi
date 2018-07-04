package com.lcsd.luxi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lcsd.luxi.R;


/**
 * Created by wei on 2018/2/1.
 */

public class UpdataDialog extends Dialog {
    private Context context;
    private TextView textView;

    public UpdataDialog(@NonNull Context context) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        setContentView(R.layout.updata_dialog);
        this.context = context;
        setCancelable(false);
        initView();
    }

    private void initView() {
        textView = (TextView) findViewById(R.id.tv_updata_dialog_content);
        findViewById(R.id.tv_updata_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Window window = this.getWindow();//实例
        WindowManager.LayoutParams lp = window.getAttributes();//获取window对象
        window.setGravity(Gravity.CENTER);//对话框弹出的位置
        window.setAttributes(lp);
    }

    public void SetContent(String string) {
        textView.setText(string);
    }
}
