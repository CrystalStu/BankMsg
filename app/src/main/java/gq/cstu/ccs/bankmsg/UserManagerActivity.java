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

        File dlPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        final String path = dlPath + "/BankMsg.db";

        try {
            if (!Files.exists(Paths.get(path))) {
                CSV.CreateNew(path);
            }
            CSV.Read(path);
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
                final TableRow tableRow = new TableRow(UserManagerActivity.this);
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
                FloatingActionButton floatingActionButtonDel = new FloatingActionButton(UserManagerActivity.this);
                floatingActionButtonDel.setImageResource(R.drawable.ic_delete_black_24dp);
                floatingActionButtonDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tableLayoutUser.removeView(tableRow);
                    }
                });
                tableRow.addView(index);
                tableRow.addView(name);
                tableRow.addView(gender);
                tableRow.addView(phone);
                tableRow.addView(birthday);
                tableRow.addView(floatingActionButtonDel);
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
                String totalStr = new String();
                for(int currentIndex = 1; currentIndex < tableLayoutUser.getChildCount(); currentIndex++) {
                    TableRow currentRow = (TableRow) tableLayoutUser.getChildAt(currentIndex);
                    EditText name = (EditText) currentRow.getChildAt(1);
                    EditText gender = (EditText) currentRow.getChildAt(2);
                    EditText phone = (EditText) currentRow.getChildAt(3);
                    EditText birthday = (EditText) currentRow.getChildAt(4);
                    String nameStr = name.getText().toString();
                    String genderStr = gender.getText().toString();
                    String phoneStr = phone.getText().toString();
                    String birthdayStr = birthday.getText().toString();
                    if(nameStr.contains("|BRK|") || nameStr.contains(",")) {
                        Tools.exceptionToast(getApplicationContext(), "Invalid Format");
                        //Tools.exceptionDialog(getApplicationContext(), "Invalid Format", "Name cannot contain | or ,.");
                        return;
                    }
                    if(genderStr == "Yes" || genderStr == "No") {
                        Tools.exceptionToast(getApplicationContext(), "Invalid Format");
                        //Tools.exceptionDialog(getApplicationContext(), "Invalid Format", "Gender must be Yes or No.");
                        return;
                    }
                    char[] phoneArray = phoneStr.toCharArray();
                    for(int currentIndexOfPhone = 0; currentIndexOfPhone < phoneStr.length(); currentIndexOfPhone++) {
                        if(phoneArray[currentIndexOfPhone] < '0' || phoneArray[currentIndexOfPhone] > '9') {
                            Tools.exceptionToast(getApplicationContext(), "Invalid Format");
                            //Tools.exceptionDialog(getApplicationContext(), "Invalid Format", "Phone must be only numbers.");
                            return;
                        }
                    }
                    if(birthdayStr.length() != 4) {
                        Tools.exceptionToast(getApplicationContext(), "Invalid Format");
                        //Tools.exceptionDialog(getApplicationContext(), "Invalid Format", "Birthday must be 4 numbers (MM-DD).");
                        return;
                    }
                    String conclusionStr = nameStr + "|BRK|" + genderStr + "|BRK|" + phoneStr + "|BRK|" + birthdayStr + '\n';
                    totalStr += conclusionStr;
                }
                try {
                    CSV.Write(getApplicationContext(), path, totalStr);
                } catch (IOException e) {
                    Tools.exceptionDialog(getApplicationContext(), "I/O ERROR OCCURRED \n", e.getMessage());
                    finish();
                }
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
            TextView del = new TextView(this);
            del.setText("Delete");
            tableRow.addView(index);
            tableRow.addView(name);
            tableRow.addView(gender);
            tableRow.addView(phone);
            tableRow.addView(birthday);
            tableRow.addView(del);
            tableLayoutUser.addView(tableRow);
        }

        for(int line = 0; line < CSV.guests.length; line++) {
            final TableRow tableRow = new TableRow(this);
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
            FloatingActionButton floatingActionButtonDel = new FloatingActionButton(this);
            floatingActionButtonDel.setImageResource(R.drawable.ic_delete_black_24dp);
            floatingActionButtonDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tableLayoutUser.removeView(tableRow);
                }
            });
            tableRow.addView(index);
            tableRow.addView(name);
            tableRow.addView(gender);
            tableRow.addView(phone);
            tableRow.addView(birthday);
            if(line != 0) tableRow.addView(floatingActionButtonDel);
            tableLayoutUser.addView(tableRow);
        }
    }
}
