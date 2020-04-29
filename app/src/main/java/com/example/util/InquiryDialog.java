package com.example.util;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.example.alonemusic.R;

public class InquiryDialog {

    /* @setIcon 设置对话框图标
     * @setTitle 设置对话框标题
     * @setMessage 设置对话框消息提示
     * setXXX方法返回Dialog对象，因此可以链式设置属性
     */
    public static void showNormalDialog(Context context, String message){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setIcon(R.drawable.warning);
        normalDialog.setTitle("询问");
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        // 显示
        normalDialog.show();
    }
}
