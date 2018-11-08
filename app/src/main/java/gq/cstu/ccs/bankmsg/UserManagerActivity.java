package gq.cstu.ccs.bankmsg;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UserManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manager);
        getSupportActionBar().setTitle("用户管理");

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        requestPermissions(permissions, 101);

        while(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Tools.exceptionToast(getApplicationContext(), "No permission \n");
            requestPermissions(permissions, 101);
            finish();
            System.exit(0);
        }

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        try {
            if (!Files.exists(Paths.get(path + "/BankMsg.db"))) {
                CSV.CreateNew(path + "/BankMsg.db");
            }

            CSV.Read(path + "/BankMsg.db");
        } catch (IOException e) {
            Tools.exceptionDialog(getApplicationContext(), "I/O ERROR OCCURRED \n", e.getMessage());
            finish();
        }

        Button buttonDiscard = findViewById(R.id.buttonDiscard);
        buttonDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final TableLayout tableLayoutUser = findViewById(R.id.TableLayoutUser);

        FloatingActionButton floatingActionButtonAddRow = findViewById(R.id.floatingActionButtonAddRow);
        floatingActionButtonAddRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRow tableRow = new TableRow(UserManagerActivity.this);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                tableRow.setLayoutParams(layoutParams);
                TextView index = new TextView(UserManagerActivity.this);
                index.setText(String.valueOf(tableLayoutUser.getChildCount()));
                index.setId(R.id.info);
                EditText name = new EditText(UserManagerActivity.this);
                name.setText("CrystalComputerStudio");
                EditText gender = new EditText(UserManagerActivity.this);
                gender.setText("Yes");
                EditText phone = new EditText(UserManagerActivity.this);
                phone.setText("0");
                EditText birthday = new EditText(UserManagerActivity.this);
                birthday.setText("0828");
                tableRow.addView(index);
                tableRow.addView(name);
                tableRow.addView(gender);
                tableRow.addView(phone);
                tableRow.addView(birthday);
                tableLayoutUser.addView(tableRow);
            }
        });

        FloatingActionButton floatingActionButtonDelRow = findViewById(R.id.floatingActionButtonDelRow);
        floatingActionButtonDelRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tableLayoutUser.getChildCount() <= 2) return;
                tableLayoutUser.removeViewAt(tableLayoutUser.getChildCount() - 1);
            }
        });

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // implementation for saving
                finish();
            }
        });

        {
            TableRow tableRow = new TableRow(this);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            tableRow.setLayoutParams(layoutParams);
            TextView index = new TextView(this);
            index.setText("Index");
            TextView name = new TextView(this);
            name.setText(CSV.UtilStr.name);
            TextView gender = new TextView(this);
            gender.setText(CSV.UtilStr.gender);
            TextView phone = new TextView(this);
            phone.setText(CSV.UtilStr.phone);
            TextView birthday = new TextView(this);
            birthday.setText(CSV.UtilStr.birthday);
            tableRow.addView(index);
            tableRow.addView(name);
            tableRow.addView(gender);
            tableRow.addView(phone);
            tableRow.addView(birthday);
            tableLayoutUser.addView(tableRow);
        }

        for(int line = 0; line < CSV.guests.length; line++) {
            TableRow tableRow = new TableRow(this);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            tableRow.setLayoutParams(layoutParams);
            TextView index = new TextView(this);
            index.setText(String.valueOf(line + 1));
            index.setId(R.id.info);
            EditText name = new EditText(this);
            name.setText(CSV.guests[line].name);
            EditText gender = new EditText(this);
            gender.setText(CSV.GenderToStringConverter(CSV.guests[line].gender));
            EditText phone = new EditText(this);
            phone.setText(CSV.guests[line].phone);
            EditText birthday = new EditText(this);
            birthday.setText(CSV.guests[line].birthday);
            tableRow.addView(index);
            tableRow.addView(name);
            tableRow.addView(gender);
            tableRow.addView(phone);
            tableRow.addView(birthday);
            tableLayoutUser.addView(tableRow);
        }
    }
}
