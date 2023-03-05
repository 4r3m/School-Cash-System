package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.qrcode.encoder.QRCode;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class confirm_payment_tab extends AppCompatActivity {
    Button btn_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);
        btn_scan = findViewById(R.id.btn_scan);

        EditText editText = findViewById(R.id.editTextNumberDecimal);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    btn_scan.setEnabled(true);
                } else {
                    btn_scan.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btn_scan.setOnClickListener(v->{
        scanCode();
        }
        );

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                Intent intent1 = getIntent();
                String lrn = intent1.getStringExtra("current_user_lrn");
                String name = intent1.getStringExtra("name");
                if (upIntent != null) {
                    Intent i = new Intent(confirm_payment_tab.this,MainActivity.class);
                    i.putExtra("current_user_lrn", lrn);
                    i.putExtra("name", name);
                    startActivity(i);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash On");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barlauncher.launch(options);


    }
    ActivityResultLauncher<ScanOptions> barlauncher = registerForActivityResult(new ScanContract(),result ->{

        if(result.getContents() != null) {
            String deduct = ((EditText)findViewById(R.id.editTextNumberDecimal)).getText().toString();
            SharedPreferences preferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            Intent intent = new Intent(confirm_payment_tab.this, MainActivity.class);
            Intent i = getIntent();
            String current_user_lrn = i.getStringExtra("current_user_lrn");

            String cashier = preferences.getString("cashier_balance", "");
            double cashier_balance = Double.parseDouble(cashier);
            String accountant = preferences.getString("accountant_balance", "");
            double accountant_balance = Double.parseDouble(accountant);
            String student1 = preferences.getString("student1_balance", "");
            double student1_balance = Double.parseDouble(student1);
            String student2 = preferences.getString("student2_balance", "");
            double student2_balance = Double.parseDouble(student2);
            String bookstore = preferences.getString("bookstore_balance", "");
            double bookstore_balance = Double.parseDouble(bookstore);

            double deducted = Double.parseDouble(deduct);

            String cashier_lrn = preferences.getString("cashier_lrn", "");
            String accountant_lrn = preferences.getString("accountant_lrn", "");
            String student1_lrn = preferences.getString("student1_lrn", "");
            String student2_lrn = preferences.getString("student2_lrn", "");
            String bookstore_lrn = preferences.getString("bookstore_lrn", "");

            String history;
            String[] stringArray;
            ArrayList<String> arrayList;
            String get_string;

            Calendar calendar = Calendar.getInstance();
            Date current_date = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            String date = dateFormat.format(current_date);

            //prohibits scanning own qr
            if(current_user_lrn.equals(result.getContents())){
                AlertDialog.Builder builder = new AlertDialog.Builder(confirm_payment_tab.this);
                builder.setTitle("Confirmed");
                builder.setMessage("Invalid: Own QR Code");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String name = i.getStringExtra("name");
                        intent.putExtra("name", name);
                        intent.putExtra("current_user_lrn", current_user_lrn);
                    }
                }).show();
            }
            //prohibits scanning not included qr codes
            else if((!result.getContents().equals(cashier_lrn))&&(!result.getContents().equals(accountant_lrn))&&
                    (!result.getContents().equals(bookstore_lrn))
                    &&(!result.getContents().equals(student1_lrn))&&(!result.getContents().equals(student2_lrn))){
                AlertDialog.Builder builder = new AlertDialog.Builder(confirm_payment_tab.this);
                builder.setTitle("Confirmed");
                builder.setMessage("Invalid: User Not Found");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String name = i.getStringExtra("name");
                        intent.putExtra("name", name);
                        intent.putExtra("current_user_lrn", current_user_lrn);
                    }
                }).show();
            }
            //executions if the qr is valid
            else{

                //deduction to other user
                //cashier logic
                if (result.getContents().equals(cashier_lrn)) {
                    //prohibits not enough balance
                    if(cashier_balance<deducted){
                        AlertDialog.Builder builder = new AlertDialog.Builder(confirm_payment_tab.this);
                        builder.setTitle("Confirmed");
                        builder.setMessage("Invalid: Not Enough Balance");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }
                    //if valid amount
                    else{

                        //variables for the reference system
                        String references = preferences.getString("reference", "");
                        int intref = Integer.parseInt(references);
                        int addAll = intref+1;
                        String putAll = String.format("%012d",addAll);
                        editor.putString("reference", putAll );

                        cashier_balance = cashier_balance - deducted;
                        editor.putString("cashier_balance", String.valueOf(cashier_balance));
                        editor.putString("result", cashier_lrn);

                        get_string = preferences.getString("cashier_history", "");
                        stringArray = get_string.split(",");

                        arrayList = new ArrayList<>(Arrays.asList(stringArray));
                        arrayList.add("SENT - ₱" + deduct + "   to: " + current_user_lrn + " \n   " + date+ " Ref No: "+ putAll);

                        history = String.join(",", arrayList);
                        intent.putExtra("name", "CANTEEN CASHIER");
                        intent.putExtra("current_user_lrn", "401647156237");
                        editor.putString("cashier_history", history);
                        editor.apply();

                        switch (current_user_lrn) {
                            case "107631100121":
                                accountant_balance = accountant_balance + deducted;
                                editor.putString("accountant_balance", String.valueOf(accountant_balance));
                                editor.putString("result", accountant_lrn);

                                get_string = preferences.getString("accountant_history", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                intent.putExtra("name", "SCHOOL ACCOUNTANT");
                                intent.putExtra("current_user_lrn", "107631100121");
                                editor.putString("accountant_history", history);
                                editor.apply();
                                break;
                            case "117218060115":
                                bookstore_balance = bookstore_balance + deducted;
                                editor.putString("bookstore_balance", String.valueOf(bookstore_balance));
                                editor.putString("result", bookstore_lrn);

                                get_string = preferences.getString("bookstore_history", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                intent.putExtra("name", "BOOKSTORE");
                                intent.putExtra("current_user_lrn", "117218060115");
                                editor.putString("bookstore_history", history);
                                editor.apply();
                                break;
                            case "401647150221":
                                student1_balance = student1_balance + deducted;
                                editor.putString("student1_balance", String.valueOf(student1_balance));
                                editor.putString("result", student1_lrn);

                                get_string = preferences.getString("student1_history", "");
                                stringArray = get_string.split(",");
                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                intent.putExtra("name", "STUDENT ONE");
                                intent.putExtra("current_user_lrn", "401647150221");
                                editor.putString("student1_history", history);
                                //-------------------
                                get_string = preferences.getString("student1_history1", "");
                                stringArray = get_string.split(",");
                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   to: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);
                                history = String.join(",", arrayList);
                                editor.putString("student1_history1", history);
                                editor.apply();
                                break;
                            case "107622100091":
                                student2_balance = student2_balance + deducted;
                                editor.putString("student2_balance", String.valueOf(student2_balance));
                                editor.putString("result", student2_lrn);
                                get_string = preferences.getString("student2_history", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                intent.putExtra("name", "STUDENT TWO");
                                intent.putExtra("current_user_lrn", "107622100091");
                                editor.putString("student2_history", history);
                                //-------------------
                                get_string = preferences.getString("student2_history1", "");
                                stringArray = get_string.split(",");
                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);
                                history = String.join(",", arrayList);
                                editor.putString("student2_history1", history);
                                editor.apply();
                                break;
                            default:
                                break;
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(confirm_payment_tab.this);
                        builder.setTitle("Confirmed");
                        builder.setMessage("₱" + deduct + " is deducted to: CASHIER ID: " + result.getContents());
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                editor.putString("deduct", String.valueOf(deduct));
                                startActivity(intent);
                            }
                        }).show();
                    }
                }
                //accountant logic
                else if (result.getContents().equals(accountant_lrn)) {
                    if(accountant_balance<deducted){
                        AlertDialog.Builder builder = new AlertDialog.Builder(confirm_payment_tab.this);
                        builder.setTitle("Confirmed");
                        builder.setMessage("Invalid: Not Enough Balance");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }else {
                        //variables for the reference system
                        String references = preferences.getString("reference", "");
                        int intref = Integer.parseInt(references);
                        int addAll = intref+1;
                        String putAll = String.format("%012d",addAll);
                        editor.putString("reference", putAll );

                        accountant_balance = accountant_balance - deducted;
                        editor.putString("accountant_balance", String.valueOf(accountant_balance));
                        editor.putString("result", accountant_lrn);

                        get_string = preferences.getString("accountant_history", "");
                        stringArray = get_string.split(",");

                        arrayList = new ArrayList<>(Arrays.asList(stringArray));
                        arrayList.add("SENT - ₱" + deduct + "   to: " + current_user_lrn + " \n   " + date+ " Ref No: "+ putAll);

                        history = String.join(",", arrayList);
                        intent.putExtra("name", "SCHOOL ACCOUNTANT");
                        intent.putExtra("current_user_lrn", "107631100121");
                        editor.putString("accountant_history", history);
                        editor.apply();

                        switch (current_user_lrn) {
                            case "401647156237":

                                cashier_balance = cashier_balance + deducted;
                                editor.putString("cashier_balance", String.valueOf(cashier_balance));
                                editor.putString("result", cashier_lrn);

                                get_string = preferences.getString("cashier_history", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                intent.putExtra("name", "CANTEEN CASHIER");
                                intent.putExtra("current_user_lrn", "401647156237");
                                editor.putString("cashier_history", history);
                                editor.apply();
                                break;
                            case "117218060115":
                                bookstore_balance = bookstore_balance + deducted;
                                editor.putString("bookstore_balance", String.valueOf(bookstore_balance));
                                editor.putString("result", bookstore_lrn);

                                get_string = preferences.getString("bookstore_history", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                intent.putExtra("name", "BOOKSTORE");
                                intent.putExtra("current_user_lrn", "117218060115");
                                editor.putString("bookstore_history", history);
                                editor.apply();
                                break;
                            case "401647150221":
                                student1_balance = student1_balance + deducted;
                                editor.putString("student1_balance", String.valueOf(student1_balance));
                                editor.putString("result", student1_lrn);
                                get_string = preferences.getString("student1_history", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                intent.putExtra("name", "STUDENT ONE");
                                intent.putExtra("current_user_lrn", "401647150221");
                                editor.putString("student1_history", history);
                                //-------------------
                                get_string = preferences.getString("student1_history2", "");
                                stringArray = get_string.split(",");
                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   to: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);
                                history = String.join(",", arrayList);
                                editor.putString("student1_history2", history);
                                editor.apply();
                                break;
                            case "107622100091":
                                student2_balance = student2_balance + deducted;
                                editor.putString("student2_balance", String.valueOf(student2_balance));
                                editor.putString("result", student2_lrn);
                                get_string = preferences.getString("student2_history", "");
                                stringArray = get_string.split(",");


                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                intent.putExtra("name", "STUDENT TWO");
                                intent.putExtra("current_user_lrn", "107622100091");
                                editor.putString("student2_history", history);
                                editor.putString("student2_history2", history);
                                //-------------------
                                get_string = preferences.getString("student2_history2", "");
                                stringArray = get_string.split(",");
                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   to: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);
                                history = String.join(",", arrayList);
                                editor.putString("student2_history2", history);
                                editor.apply();
                                break;
                            default:
                                break;
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(confirm_payment_tab.this);
                        builder.setTitle("Confirmed");
                        builder.setMessage("₱" + deduct + " is deducted to: ACCOUNTANT ID: " + result.getContents());
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                editor.putString("deduct", String.valueOf(deduct));
                                startActivity(intent);
                            }
                        }).show();
                    }
                }
                //bookstore logic
                else if (result.getContents().equals(bookstore_lrn)) {
                    if(bookstore_balance<deducted){
                        AlertDialog.Builder builder = new AlertDialog.Builder(confirm_payment_tab.this);
                        builder.setTitle("Confirmed");
                        builder.setMessage("Invalid: Not Enough Balance");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }else {
                        //variables for the reference system
                        String references = preferences.getString("reference", "");
                        int intref = Integer.parseInt(references);
                        int addAll = intref+1;
                        String putAll = String.format("%012d",addAll);
                        editor.putString("reference", putAll );

                        bookstore_balance = bookstore_balance - deducted;
                        editor.putString("bookstore_balance", String.valueOf(bookstore_balance));
                        editor.putString("result", bookstore_lrn);

                        get_string = preferences.getString("bookstore_history", "");
                        stringArray = get_string.split(",");

                        arrayList = new ArrayList<>(Arrays.asList(stringArray));
                        arrayList.add("SENT - ₱" + deduct + "   to: " + current_user_lrn + " \n   " + date+ " Ref No: "+ putAll);

                        history = String.join(",", arrayList);
                        intent.putExtra("name", "BOOKSTORE");
                        intent.putExtra("current_user_lrn", "117218060115");
                        editor.putString("bookstore_history", history);
                        editor.apply();

                        switch (current_user_lrn) {
                            case "401647156237":

                                cashier_balance = cashier_balance + deducted;
                                editor.putString("cashier_balance", String.valueOf(cashier_balance));
                                editor.putString("result", cashier_lrn);

                                get_string = preferences.getString("cashier_history", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                intent.putExtra("name", "CANTEEN CASHIER");
                                intent.putExtra("current_user_lrn", "401647156237");
                                editor.putString("cashier_history", history);
                                editor.apply();
                                break;
                            case "107631100121":
                                accountant_balance = accountant_balance + deducted;
                                editor.putString("accountant_balance", String.valueOf(accountant_balance));
                                editor.putString("result", accountant_lrn);

                                get_string = preferences.getString("accountant_history", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                intent.putExtra("name", "SCHOOL ACCOUNTANT");
                                intent.putExtra("current_user_lrn", "107631100121");
                                editor.putString("accountant_history", history);
                                editor.apply();
                                break;
                            case "401647150221":
                                student1_balance = student1_balance + deducted;
                                editor.putString("student1_balance", String.valueOf(student1_balance));
                                editor.putString("result", student1_lrn);
                                get_string = preferences.getString("student1_history", "");
                                stringArray = get_string.split(",");


                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                intent.putExtra("name", "STUDENT ONE");
                                intent.putExtra("current_user_lrn", "401647150221");
                                editor.putString("student1_history", history);
                                //-------------------
                                get_string = preferences.getString("student1_history4", "");
                                stringArray = get_string.split(",");
                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   to: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);
                                history = String.join(",", arrayList);
                                editor.putString("student1_history4", history);
                                editor.apply();
                                break;
                            case "107622100091":
                                student2_balance = student2_balance + deducted;
                                editor.putString("student2_balance", String.valueOf(student2_balance));
                                editor.putString("result", student2_lrn);
                                get_string = preferences.getString("student2_history", "");
                                stringArray = get_string.split(",");


                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                intent.putExtra("name", "STUDENT TWO");
                                intent.putExtra("current_user_lrn", "107622100091");
                                editor.putString("student2_history", history);
                                //-------------------
                                get_string = preferences.getString("student2_history4", "");
                                stringArray = get_string.split(",");
                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   to: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);
                                history = String.join(",", arrayList);
                                editor.putString("student2_history4", history);
                                editor.apply();
                                break;
                            default:
                                break;
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(confirm_payment_tab.this);
                        builder.setTitle("Confirmed");
                        builder.setMessage("₱" + deduct + " is deducted to: ACCOUNTANT ID: " + result.getContents());
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                editor.putString("deduct", String.valueOf(deduct));
                                startActivity(intent);
                            }
                        }).show();
                    }
                }
                //student1 logic
                else if (result.getContents().equals(student1_lrn)) {
                    if(student1_balance<deducted){
                        AlertDialog.Builder builder = new AlertDialog.Builder(confirm_payment_tab.this);
                        builder.setTitle("Confirmed");
                        builder.setMessage("Invalid: Not Enough Balance");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }else {
                        //variables for the reference system
                        String references = preferences.getString("reference", "");
                        int intref = Integer.parseInt(references);
                        int addAll = intref+1;
                        String putAll = String.format("%012d",addAll);
                        editor.putString("reference", putAll );

                        student1_balance = student1_balance - deducted;
                        editor.putString("student1_balance", String.valueOf(student1_balance));
                        editor.putString("result", student1_lrn);
                        get_string = preferences.getString("student1_history", "");
                        stringArray = get_string.split(",");
                        arrayList = new ArrayList<>(Arrays.asList(stringArray));
                        arrayList.add("SENT - ₱" + deduct + "   to: " + current_user_lrn + " \n   " + date+ " Ref No: "+ putAll);
                        history = String.join(",", arrayList);
                        editor.putString("student1_history", history);
                        intent.putExtra("name", "STUDENT ONE");
                        intent.putExtra("current_user_lrn", "401647150221");
                        editor.apply();

                        switch (current_user_lrn) {
                            case "401647156237":

                                cashier_balance = cashier_balance + deducted;
                                editor.putString("cashier_balance", String.valueOf(cashier_balance));
                                editor.putString("result", cashier_lrn);

                                get_string = preferences.getString("cashier_history", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                intent.putExtra("name", "CANTEEN CASHIER");
                                intent.putExtra("current_user_lrn", "401647156237");
                                editor.putString("cashier_history", history);
                                //-----------------------
                                get_string = preferences.getString("student1_history1", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("SENT - ₱" + deduct + "   to: " + current_user_lrn + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                editor.putString("student1_history1", history);
                                editor.apply();
                                break;
                            case "107631100121":
                                accountant_balance = accountant_balance + deducted;
                                editor.putString("accountant_balance", String.valueOf(accountant_balance));
                                editor.putString("result", accountant_lrn);

                                get_string = preferences.getString("accountant_history", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                intent.putExtra("name", "SCHOOL ACCOUNTANT");
                                intent.putExtra("current_user_lrn", "107631100121");
                                editor.putString("accountant_history", history);

                                //-----------------------
                                get_string = preferences.getString("student1_history2", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("SENT - ₱" + deduct + "   to: " + current_user_lrn + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                editor.putString("student1_history2", history);
                                editor.apply();
                                break;
                            case "117218060115":
                                bookstore_balance = bookstore_balance + deducted;
                                editor.putString("bookstore_balance", String.valueOf(bookstore_balance));
                                editor.putString("result", bookstore_lrn);

                                get_string = preferences.getString("bookstore_history", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                intent.putExtra("name", "BOOKSTORE");
                                intent.putExtra("current_user_lrn", "117218060115");
                                editor.putString("bookstore_history", history);

                                //-----------------------
                                get_string = preferences.getString("student1_history4", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("SENT - ₱" + deduct + "   to: " + current_user_lrn + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                editor.putString("student1_history4", history);
                                editor.apply();
                                break;
                            case "107622100091":
                                student2_balance = student2_balance + deducted;
                                editor.putString("student2_balance", String.valueOf(student2_balance));
                                editor.putString("result", student2_lrn);
                                get_string = preferences.getString("student2_history", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                intent.putExtra("name", "STUDENT TWO");
                                intent.putExtra("current_user_lrn", "107622100091");
                                editor.putString("student2_history", history);
                                //-----------------------
                                get_string = preferences.getString("student2_history3", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                editor.putString("student2_history3", history);
                                //-----------------------
                                get_string = preferences.getString("student1_history3", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("SENT - ₱" + deduct + "   to: " + current_user_lrn + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                editor.putString("student1_history3", history);
                                editor.apply();
                                break;
                            default:
                                break;
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(confirm_payment_tab.this);
                        builder.setTitle("Confirmed");
                        builder.setMessage("₱" + deduct + " is deducted to: STUDENT ID: " + result.getContents());
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                editor.putString("deduct", String.valueOf(deduct));
                                startActivity(intent);
                            }
                        }).show();
                    }
                }
                //student2 logic
                else if (result.getContents().equals(student2_lrn)) {
                    if(student2_balance<deducted){
                        AlertDialog.Builder builder = new AlertDialog.Builder(confirm_payment_tab.this);
                        builder.setTitle("Confirmed");
                        builder.setMessage("Invalid: Not Enough Balance");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }else {
                        //variables for the reference system
                        String references = preferences.getString("reference", "");
                        int intref = Integer.parseInt(references);
                        int addAll = intref+1;
                        String putAll = String.format("%012d",addAll);
                        editor.putString("reference", putAll );

                        student2_balance = student2_balance - deducted;
                        editor.putString("student2_balance", String.valueOf(student2_balance));
                        editor.putString("result", student2_lrn);
                        get_string = preferences.getString("student2_history", "");
                        stringArray = get_string.split(",");

                        arrayList = new ArrayList<>(Arrays.asList(stringArray));
                        arrayList.add("SENT - ₱" + deduct + "   to: " + current_user_lrn + " \n   " + date+ " Ref No: "+ putAll);

                        history = String.join(",", arrayList);
                        intent.putExtra("name", "STUDENT TWO");
                        intent.putExtra("current_user_lrn", "107622100091");
                        editor.putString("student2_history", history);

                        switch (current_user_lrn) {
                            case "401647156237":

                                cashier_balance = cashier_balance + deducted;
                                editor.putString("cashier_balance", String.valueOf(cashier_balance));
                                editor.putString("result", cashier_lrn);

                                get_string = preferences.getString("cashier_history", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                intent.putExtra("name", "CANTEEN CASHIER");
                                intent.putExtra("current_user_lrn", "401647156237");
                                editor.putString("cashier_history", history);
                                editor.apply();
//-----------------------
                                get_string = preferences.getString("student2_history1", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("SENT - ₱" + deduct + "   to: " + current_user_lrn + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                editor.putString("student2_history1", history);
                                editor.apply();
                                break;
                            case "107631100121":
                                accountant_balance = accountant_balance + deducted;
                                editor.putString("accountant_balance", String.valueOf(accountant_balance));
                                editor.putString("result", accountant_lrn);

                                get_string = preferences.getString("accountant_history", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                intent.putExtra("name", "SCHOOL ACCOUNTANT");
                                intent.putExtra("current_user_lrn", "107631100121");
                                editor.putString("accountant_history", history);

                                //-----------------------
                                get_string = preferences.getString("student2_history2", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("SENT + ₱" + deduct + "   from: " + current_user_lrn + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                editor.putString("student2_history2", history);
                                editor.apply();
                                break;
                            case "117218060115":
                                bookstore_balance = bookstore_balance + deducted;
                                editor.putString("bookstore_balance", String.valueOf(bookstore_balance));
                                editor.putString("result", bookstore_lrn);

                                get_string = preferences.getString("bookstore_history", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                intent.putExtra("name", "BOOKSTORE");
                                intent.putExtra("current_user_lrn", "117218060115");
                                editor.putString("bookstore_history", history);

                                //-----------------------
                                get_string = preferences.getString("student2_history4", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("SENT - ₱" + deduct + "   to: " + current_user_lrn + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                editor.putString("student2_history4", history);
                                editor.apply();
                                break;
                            case "401647150221":
                                student1_balance = student1_balance + deducted;
                                editor.putString("student1_balance", String.valueOf(student1_balance));
                                editor.putString("result", student1_lrn);
                                get_string = preferences.getString("student1_history", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                intent.putExtra("name", "STUDENT ONE");
                                intent.putExtra("current_user_lrn", "401647150221");
                                editor.putString("student1_history", history);

                                //-----------------------
                                get_string = preferences.getString("student2_history3", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("SENT - ₱" + deduct + "   to: " + current_user_lrn + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                editor.putString("student2_history3", history);
                                //-----------------------
                                get_string = preferences.getString("student1_history3", "");
                                stringArray = get_string.split(",");

                                arrayList = new ArrayList<>(Arrays.asList(stringArray));
                                arrayList.add("RECEIVED + ₱" + deduct + "   from: " + result.getContents() + " \n   " + date+ " Ref No: "+ putAll);

                                history = String.join(",", arrayList);
                                editor.putString("student1_history3", history);
                                editor.apply();
                                break;
                            default:
                                break;
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(confirm_payment_tab.this);
                        builder.setTitle("Confirmed");
                        builder.setMessage("₱" + deduct + " is deducted to: STUDENT ID: " + result.getContents());
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                editor.putString("deduct", String.valueOf(deduct));
                                startActivity(intent);
                            }
                        }).show();
                    }

            }

            }

            ((EditText)findViewById(R.id.editTextNumberDecimal)).setText("");}
    });

}