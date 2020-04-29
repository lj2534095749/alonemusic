package com.example.alonemusic.test;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alonemusic.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Created by Adminstrator on 2016/10/8.
 */
public class FileIOActivity extends AppCompatActivity {
    //声明两个文本框
    private EditText editFileIn, editFileOut;
    //声明两个按钮
    private Button btnRead, btnWrite;
    //指定文件名
    final String FILE_NAME = "qstIO.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_io);
        Log.d("FileIO", "FileIOActivity");
        //获取两个文本框
        editFileIn = findViewById(R.id.editFileIn);
        editFileOut = findViewById(R.id.editFileOut);

        //获取两个按钮
        btnRead = findViewById(R.id.btnRead);
        btnWrite = findViewById(R.id.btnWrite);

        //绑定btnRead按钮的事件监听器
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //读取指定文件中的内容，并在editFileIn文本框中显示出来
                editFileIn.setText(read());
            }
        });
        //绑定btnWrite按钮的事件监听器
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //将edit1中得内容写入文件中
                write(editFileOut.getText().toString());
                editFileOut.setText("");
            }
        });
    }

    private String read() {
        try {
            //打开文件输入流
            FileInputStream fis = openFileInput(FILE_NAME);
            byte[] buff = new byte[1024];
            int hasRead = 0;
            StringBuilder sb = new StringBuilder("");
            //读取文件内容
            while ((hasRead = fis.read(buff)) > 0) {
                sb.append(new String(buff, 0, hasRead));
            }
            //关闭文件输入流
            fis.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void write(String content) {
        try {
            //以追加模式打开文件输入流
            FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_APPEND);
            //将FileOutputStream包装成PrintStream
            PrintStream ps = new PrintStream(fos);
            //输出文件内容
            ps.println(content);
            ps.close();
            //使用Toast显示保存成功
            Toast.makeText(FileIOActivity.this, "保存成功", Toast.LENGTH_LONG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
