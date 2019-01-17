package gq.cstu.ccs.bankmsg;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static gq.cstu.ccs.bankmsg.SendMessage.BulkSending;

public class MainActivity extends AppCompatActivity {

    public void setStatusBar(boolean navi) {
        // From https://blog.csdn.net/Dovar_66/article/details/52688313
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        if (navi) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN//状态栏不会被隐藏但activity布局会扩展到状态栏所在位置
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION//导航栏不会被隐藏但activity布局会扩展到导航栏所在位置
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setNavigationBarColor(Color.TRANSPARENT);
        } else {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS};
        requestPermissions(permissions, 101);

        while(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Tools.exceptionToast(getApplicationContext(), "No permission \n");
            requestPermissions(permissions, 101);
            finish();
            System.exit(0);
        }

        File dlPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        final String path = dlPath + "/BankMsg.db";

        try {
            if (!Files.exists(Paths.get(path))) {
                CSV.CreateNew(path);
            }
            CSV.Read(path);
        } catch (IOException e) {
            Tools.exceptionSave(getWindow().getDecorView().findViewById(android.R.id.content));
            // Tools.exceptionDialog(getApplicationContext(), "I/O ERROR OCCURRED \n", e.getMessage());
            finish();
        }

        Button buttonExit = findViewById(R.id.buttonExit);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button buttonUserManage = findViewById(R.id.buttonUserManage);
        buttonUserManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserManagerActivity.class));
            }
        });

        Button buttonSendMessage = findViewById(R.id.buttonSendMessage);
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BulkSending(v);
            }
        });

        Button buttonHelp = findViewById(R.id.buttonHelp);
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HelpActivity.class));
            }
        });

        Button buttonAbout = findViewById(R.id.buttonAbout);
        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        });
    }
}
