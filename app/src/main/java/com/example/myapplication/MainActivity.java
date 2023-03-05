package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.accessibilityservice.GestureDescription;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    int tabs = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("DASHBOARD");
        switching();





    }
    public void helpInstruct(View v){
        TextView instruct = findViewById(R.id.helpList);
     ScrollView sv = findViewById(R.id.helpScroll);
     ImageButton imgbtn = findViewById(R.id.help);
     instruct.setText(getResources().getString(R.string.instructions));
     if (sv.getVisibility()==View.VISIBLE){
         sv.setVisibility(View.GONE);
         imgbtn.setAlpha(0.5f);
     }
     else{sv.setVisibility(View.VISIBLE);
     imgbtn.setAlpha(1f);
     }
    }
    int increment=0;
    public void nextTransact(View v){
        TextView textView2 = findViewById(R.id.textView2);
        ScrollView scrollView = findViewById(R.id.scrollView);
        ScrollView scrollView1 = findViewById(R.id.scrollView1);
        ScrollView scrollView2 = findViewById(R.id.scrollView2);
        ScrollView scrollView3 = findViewById(R.id.scrollView3);
        ScrollView scrollView4 = findViewById(R.id.scrollView4);

        increment++;
        if ((increment < 1) || (increment > 5)) {
            increment = 1;
        }
        if (increment == 1) {
            textView2.setText("All Transactions");
            scrollView.setVisibility(View.VISIBLE);
            scrollView1.setVisibility(View.GONE);
            scrollView2.setVisibility(View.GONE);
            scrollView3.setVisibility(View.GONE);
            scrollView4.setVisibility(View.GONE);
        } else if (increment == 2) {
            textView2.setText("Canteen");
            scrollView.setVisibility(View.GONE);
            scrollView1.setVisibility(View.VISIBLE);
            scrollView2.setVisibility(View.GONE);
            scrollView3.setVisibility(View.GONE);
            scrollView4.setVisibility(View.GONE);
        } else if (increment == 3) {
            textView2.setText("Tuition");
            scrollView.setVisibility(View.GONE);
            scrollView1.setVisibility(View.GONE);
            scrollView2.setVisibility(View.VISIBLE);
            scrollView3.setVisibility(View.GONE);
            scrollView4.setVisibility(View.GONE);
        } else if (increment == 4) {
            textView2.setText("S2S");
            scrollView.setVisibility(View.GONE);
            scrollView1.setVisibility(View.GONE);
            scrollView2.setVisibility(View.GONE);
            scrollView3.setVisibility(View.VISIBLE);
            scrollView4.setVisibility(View.GONE);
        } else if (increment == 5) {
            textView2.setText("Bookstore");
            scrollView.setVisibility(View.GONE);
            scrollView1.setVisibility(View.GONE);
            scrollView2.setVisibility(View.GONE);
            scrollView3.setVisibility(View.GONE);
            scrollView4.setVisibility(View.VISIBLE);
        }
    }
    public void MyQr(View view) {
        Intent intent = new Intent(MainActivity.this, show_qrcode_tab.class);
        Intent intent1 = getIntent();
        String lrn = intent1.getStringExtra("current_user_lrn");
        String name = ((TextView)findViewById(R.id.DashBoardUserName)).getText().toString();
        intent.putExtra("current_user_lrn", lrn);
        intent.putExtra("name", name);
        startActivity(intent);
        Log.d("showed qr tab","successfully shown");
    }

    public void onclickReceivePayment(View view) {
        Intent intent = new Intent(MainActivity.this, confirm_payment_tab.class);
        Intent intent1 = getIntent();
        String lrn = intent1.getStringExtra("current_user_lrn");
        String name = ((TextView)findViewById(R.id.DashBoardUserName)).getText().toString();
        intent.putExtra("current_user_lrn", lrn);
        intent.putExtra("name", name);
        startActivity(intent);
    }
    public void AllProfiles(View view) {
        Intent intent = new Intent(MainActivity.this, user_profiles.class);
        startActivity(intent);
        Log.d("profile tab","successfully shown");
    }
    public void switching(){

        SharedPreferences preferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        //get the transaction history

        Intent rec = new Intent(MainActivity.this, receipt_tab.class);

        Intent i = getIntent();
        String name = i.getStringExtra("name");
        ((TextView)findViewById(R.id.DashBoardUserName)).setText(name);
        //Getting The Image from User
        ImageView dp = findViewById(R.id.ProfilePicture);
        ImageView qr = findViewById(R.id.clickQr);
        TextView category = findViewById(R.id.lrn2);

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

        String history_string;
        String[] array_history;
        DecimalFormat decimalFormat = new DecimalFormat("₱###,###.##");
        int x=0;
        int k = 0;
        ImageButton imgbtn = findViewById(R.id.nextTransact);
        // Build the multiline text by appending each line to a StringBuilder
        StringBuilder sb = new StringBuilder();
        SpannableStringBuilder ssb;
        StringBuilder sb1 = new StringBuilder();
        SpannableStringBuilder ssb1;
        StringBuilder sb2 = new StringBuilder();
        SpannableStringBuilder ssb2;
        StringBuilder sb3 = new StringBuilder();
        SpannableStringBuilder ssb3;
        StringBuilder sb4 = new StringBuilder();
        SpannableStringBuilder ssb4;
        TextView textView, textView1, textView2,textView3, textView4;
        switch (name){
            case ("CANTEEN CASHIER"):
                category.setText("Building-A Canteen");
                dp.setImageResource(R.drawable.cashier);
                qr.setImageResource(R.drawable.cashier_lrn);
                ((TextView)findViewById(R.id.cashAvailale)).setText(decimalFormat.format(cashier_balance));
                imgbtn.setVisibility(View.GONE);
                history_string = preferences.getString("cashier_history", "");
//--------------------0
                textView = findViewById(R.id.arrayList);
                array_history = history_string.split(",");
                k = array_history.length;
                for ( x = 0; x < array_history.length; x++) {
                    k--;
                    sb.append(array_history[k]+"\n\n");
                }
                textView.setText(sb.toString());
                // Create a SpannableStringBuilder from the text
                ssb = new SpannableStringBuilder(textView.getText());
                // Add a ClickableSpan to each line
                for (int a = 0; a < array_history.length; a++) {
                    final String line = array_history[a];
                    final int start = sb.indexOf(line);
                    final int end = start + line.length();
                    ssb.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            // Do something when the line is clicked (e.g. show a Toast)
                            Intent intent1 = getIntent();
                            String lrn = intent1.getStringExtra("current_user_lrn");
                            String name = ((TextView)findViewById(R.id.DashBoardUserName)).getText().toString();
                            rec.putExtra("current_user_lrn", lrn);
                            rec.putExtra("name", name);
                            rec.putExtra("receipt_details", line);
                            startActivity(rec);
                            }
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(Color.WHITE);
                            ds.setUnderlineText(false);
                        }
                    }, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
// Set the modified text with ClickableSpans to the TextView
                textView.setText(ssb);
// Make sure the TextView is clickable
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case "SCHOOL ACCOUNTANT":
                category.setText("Registrar Accountant");
                dp.setImageResource(R.drawable.accountant);
                qr.setImageResource(R.drawable.accountant_lrn);
                ((TextView)findViewById(R.id.cashAvailale)).setText(decimalFormat.format(accountant_balance));
                imgbtn.setVisibility(View.GONE);
                history_string = preferences.getString("accountant_history", "");

//--------------------0
                textView = findViewById(R.id.arrayList);
                array_history = history_string.split(",");
                k = array_history.length;
                for ( x = 0; x < array_history.length; x++) {
                    k--;
                    sb.append(array_history[k]+"\n\n");
                }

                textView.setText(sb.toString());
                // Create a SpannableStringBuilder from the text
                ssb = new SpannableStringBuilder(textView.getText());
                // Add a ClickableSpan to each line
                for (int a = 0; a < array_history.length; a++) {
                    final String line = array_history[a];
                    final int start = sb.indexOf(line);
                    final int end = start + line.length();
                    ssb.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            // Do something when the line is clicked (e.g. show a Toast)
                            Intent intent1 = getIntent();
                            String lrn = intent1.getStringExtra("current_user_lrn");
                            String name = ((TextView)findViewById(R.id.DashBoardUserName)).getText().toString();
                            rec.putExtra("current_user_lrn", lrn);
                            rec.putExtra("name", name);
                            rec.putExtra("receipt_details", line);
                            startActivity(rec);
                        }
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(Color.WHITE);
                            ds.setUnderlineText(false);
                        }
                    }, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
// Set the modified text with ClickableSpans to the TextView
                textView.setText(ssb);
// Make sure the TextView is clickable
                textView.setMovementMethod(LinkMovementMethod.getInstance());

                break;
            case "BOOKSTORE":
                category.setText("Bookstore Cashier");
                dp.setImageResource(R.drawable.bookstore);
                qr.setImageResource(R.drawable.bookstore_lrn);
                ((TextView)findViewById(R.id.cashAvailale)).setText(decimalFormat.format(bookstore_balance));
                imgbtn.setVisibility(View.GONE);
                history_string = preferences.getString("bookstore_history", "");

//--------------------0
                textView = findViewById(R.id.arrayList);
                array_history = history_string.split(",");
                k = array_history.length;
                for ( x = 0; x < array_history.length; x++) {
                    k--;
                    sb.append(array_history[k]+"\n\n");
                }

                textView.setText(sb.toString());
                // Create a SpannableStringBuilder from the text
                ssb = new SpannableStringBuilder(textView.getText());
                // Add a ClickableSpan to each line
                for (int a = 0; a < array_history.length; a++) {
                    final String line = array_history[a];
                    final int start = sb.indexOf(line);
                    final int end = start + line.length();
                    ssb.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            // Do something when the line is clicked (e.g. show a Toast)
                            Intent intent1 = getIntent();
                            String lrn = intent1.getStringExtra("current_user_lrn");
                            String name = ((TextView)findViewById(R.id.DashBoardUserName)).getText().toString();
                            rec.putExtra("current_user_lrn", lrn);
                            rec.putExtra("name", name);
                            rec.putExtra("receipt_details", line);
                            startActivity(rec);
                        }
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(Color.WHITE);
                            ds.setUnderlineText(false);
                        }
                    }, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
// Set the modified text with ClickableSpans to the TextView
                textView.setText(ssb);
// Make sure the TextView is clickable
                textView.setMovementMethod(LinkMovementMethod.getInstance());

                break;
            case "STUDENT ONE":

                category.setText("Grade - Section");
                dp.setImageResource(R.drawable.student1);
                qr.setImageResource(R.drawable.student1_lrn);
                ((TextView)findViewById(R.id.cashAvailale)).setText(decimalFormat.format(student1_balance));

                history_string = preferences.getString("student1_history", "");
//--------------------0
                textView = findViewById(R.id.arrayList);
                array_history = history_string.split(",");
                k = array_history.length;
                for ( x = 0; x < array_history.length; x++) {
                    k--;
                    sb.append(array_history[k]+"\n\n");
                }

                textView.setText(sb.toString());
                // Create a SpannableStringBuilder from the text
                ssb = new SpannableStringBuilder(textView.getText());
                // Add a ClickableSpan to each line
                for (int a = 0; a < array_history.length; a++) {
                    final String line = array_history[a];
                    final int start = sb.indexOf(line);
                    final int end = start + line.length();
                    ssb.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            // Do something when the line is clicked (e.g. show a Toast)
                            Intent intent1 = getIntent();
                            String lrn = intent1.getStringExtra("current_user_lrn");
                            String name = ((TextView)findViewById(R.id.DashBoardUserName)).getText().toString();
                            rec.putExtra("current_user_lrn", lrn);
                            rec.putExtra("name", name);
                            rec.putExtra("receipt_details", line);
                            startActivity(rec);
                        }
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(Color.WHITE);
                            ds.setUnderlineText(false);
                        }
                    }, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
// Set the modified text with ClickableSpans to the TextView
                textView.setText(ssb);
// Make sure the TextView is clickable
                textView.setMovementMethod(LinkMovementMethod.getInstance());


                history_string = preferences.getString("student1_history1", "");
//--------------------1
                textView1 = findViewById(R.id.arrayList1);
                array_history = history_string.split(",");
                k = array_history.length;
                for ( x = 0; x < array_history.length; x++) {
                    k--;
                    sb1.append(array_history[k]+"\n\n");
                }

                textView1.setText(sb1.toString());
                // Create a SpannableStringBuilder from the text
                ssb1 = new SpannableStringBuilder(textView1.getText());
                // Add a ClickableSpan to each line
                for (int a = 0; a < array_history.length; a++) {
                    final String line = array_history[a];
                    final int start = sb1.indexOf(line);
                    final int end = start + line.length();
                    ssb1.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            // Do something when the line is clicked (e.g. show a Toast)
                            Intent intent1 = getIntent();
                            String lrn = intent1.getStringExtra("current_user_lrn");
                            String name = ((TextView)findViewById(R.id.DashBoardUserName)).getText().toString();
                            rec.putExtra("current_user_lrn", lrn);
                            rec.putExtra("name", name);
                            rec.putExtra("receipt_details", line);
                            startActivity(rec);
                        }
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(Color.WHITE);
                            ds.setUnderlineText(false);
                        }
                    }, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
// Set the modified text with ClickableSpans to the TextView
                textView1.setText(ssb1);
// Make sure the TextView is clickable
                textView1.setMovementMethod(LinkMovementMethod.getInstance());

                history_string = preferences.getString("student1_history2", "");
//--------------------2
                textView2 = findViewById(R.id.arrayList2);
                array_history = history_string.split(",");
                k = array_history.length;
                for ( x = 0; x < array_history.length; x++) {
                    k--;
                    sb2.append(array_history[k]+"\n\n");
                }

                textView2.setText(sb2.toString());
                // Create a SpannableStringBuilder from the text
                ssb2 = new SpannableStringBuilder(textView2.getText());
                // Add a ClickableSpan to each line
                for (int a = 0; a < array_history.length; a++) {
                    final String line = array_history[a];
                    final int start = sb2.indexOf(line);
                    final int end = start + line.length();
                    ssb2.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            // Do something when the line is clicked (e.g. show a Toast)
                            Intent intent1 = getIntent();
                            String lrn = intent1.getStringExtra("current_user_lrn");
                            String name = ((TextView)findViewById(R.id.DashBoardUserName)).getText().toString();
                            rec.putExtra("current_user_lrn", lrn);
                            rec.putExtra("name", name);
                            rec.putExtra("receipt_details", line);
                            startActivity(rec);
                        }
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(Color.WHITE);
                            ds.setUnderlineText(false);
                        }
                    }, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
// Set the modified text with ClickableSpans to the TextView
                textView2.setText(ssb2);
// Make sure the TextView is clickable
                textView2.setMovementMethod(LinkMovementMethod.getInstance());
                history_string = preferences.getString("student1_history3", "");
//--------------------3
                textView3 = findViewById(R.id.arrayList3);
                array_history = history_string.split(",");
                k = array_history.length;
                for ( x = 0; x < array_history.length; x++) {
                    k--;
                    sb3.append(array_history[k]+"\n\n");
                }
                textView3.setText(sb3.toString());
                // Create a SpannableStringBuilder from the text
                ssb3 = new SpannableStringBuilder(textView3.getText());
                // Add a ClickableSpan to each line
                for (int a = 0; a < array_history.length; a++) {
                    final String line = array_history[a];
                    final int start = sb3.indexOf(line);
                    final int end = start + line.length();
                    ssb3.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            // Do something when the line is clicked (e.g. show a Toast)
                            Intent intent1 = getIntent();
                            String lrn = intent1.getStringExtra("current_user_lrn");
                            String name = ((TextView)findViewById(R.id.DashBoardUserName)).getText().toString();
                            rec.putExtra("current_user_lrn", lrn);
                            rec.putExtra("name", name);
                            rec.putExtra("receipt_details", line);
                            startActivity(rec);
                        }
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(Color.WHITE);
                            ds.setUnderlineText(false);
                        }
                    }, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
// Set the modified text with ClickableSpans to the TextView
                textView3.setText(ssb3);
// Make sure the TextView is clickable
                textView3.setMovementMethod(LinkMovementMethod.getInstance());
                //-----------------------4
                history_string = preferences.getString("student1_history4", "");
                textView4 = findViewById(R.id.arrayList4);
                array_history = history_string.split(",");
                k = array_history.length;
                for ( x = 0; x < array_history.length; x++) {
                    k--;
                    sb4.append(array_history[k]+"\n\n");
                }

                textView4.setText(sb4.toString());
                // Create a SpannableStringBuilder from the text
                ssb4 = new SpannableStringBuilder(textView4.getText());
                // Add a ClickableSpan to each line
                for (int a = 0; a < array_history.length; a++) {
                    final String line = array_history[a];
                    final int start = sb4.indexOf(line);
                    final int end = start + line.length();
                    ssb4.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            // Do something when the line is clicked (e.g. show a Toast)
                            Intent intent1 = getIntent();
                            String lrn = intent1.getStringExtra("current_user_lrn");
                            String name = ((TextView)findViewById(R.id.DashBoardUserName)).getText().toString();
                            rec.putExtra("current_user_lrn", lrn);
                            rec.putExtra("name", name);
                            rec.putExtra("receipt_details", line);
                            startActivity(rec);
                        }
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(Color.WHITE);
                            ds.setUnderlineText(false);
                        }
                    }, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
// Set the modified text with ClickableSpans to the TextView
                textView4.setText(ssb4);
// Make sure the TextView is clickable
                textView4.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case "STUDENT TWO":

                category.setText("Grade - Section");
                dp.setImageResource(R.drawable.student2);
                qr.setImageResource(R.drawable.student2_lrn);
                ((TextView)findViewById(R.id.cashAvailale)).setText(decimalFormat.format(student2_balance));

                history_string = preferences.getString("student2_history", "");

//--------------------0
                textView = findViewById(R.id.arrayList);
                array_history = history_string.split(",");
                k = array_history.length;
                for ( x = 0; x < array_history.length; x++) {
                    k--;
                    sb.append(array_history[k]+"\n\n");
                }

                textView.setText(sb.toString());
                // Create a SpannableStringBuilder from the text
                ssb = new SpannableStringBuilder(textView.getText());
                // Add a ClickableSpan to each line
                for (int a = 0; a < array_history.length; a++) {
                    final String line = array_history[a];
                    final int start = sb.indexOf(line);
                    final int end = start + line.length();
                    ssb.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            // Do something when the line is clicked (e.g. show a Toast)
                            Intent intent1 = getIntent();
                            String lrn = intent1.getStringExtra("current_user_lrn");
                            String name = ((TextView)findViewById(R.id.DashBoardUserName)).getText().toString();
                            rec.putExtra("current_user_lrn", lrn);
                            rec.putExtra("name", name);
                            rec.putExtra("receipt_details", line);
                            startActivity(rec);
                        }
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(Color.WHITE);
                            ds.setUnderlineText(false);
                        }
                    }, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }

// Set the modified text with ClickableSpans to the TextView
                textView.setText(ssb);
// Make sure the TextView is clickable
                textView.setMovementMethod(LinkMovementMethod.getInstance());

                history_string = preferences.getString("student2_history1", "");
//--------------------1
                textView1 = findViewById(R.id.arrayList1);
                array_history = history_string.split(",");
                k = array_history.length;
                for ( x = 0; x < array_history.length; x++) {
                    k--;
                    sb1.append(array_history[k]+"\n\n");
                }

                textView1.setText(sb1.toString());
                // Create a SpannableStringBuilder from the text
                ssb1 = new SpannableStringBuilder(textView1.getText());
                // Add a ClickableSpan to each line
                for (int a = 0; a < array_history.length; a++) {
                    final String line = array_history[a];
                    final int start = sb1.indexOf(line);
                    final int end = start + line.length();
                    ssb1.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            // Do something when the line is clicked (e.g. show a Toast)
                            Intent intent1 = getIntent();
                            String lrn = intent1.getStringExtra("current_user_lrn");
                            String name = ((TextView)findViewById(R.id.DashBoardUserName)).getText().toString();
                            rec.putExtra("current_user_lrn", lrn);
                            rec.putExtra("name", name);
                            rec.putExtra("receipt_details", line);
                            startActivity(rec);
                        }
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(Color.WHITE);
                            ds.setUnderlineText(false);
                        }
                    }, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
// Set the modified text with ClickableSpans to the TextView
                textView1.setText(ssb1);
// Make sure the TextView is clickable
                textView1.setMovementMethod(LinkMovementMethod.getInstance());

                history_string = preferences.getString("student2_history2", "");
//--------------------2
                textView2 = findViewById(R.id.arrayList2);
                array_history = history_string.split(",");
                k = array_history.length;
                for ( x = 0; x < array_history.length; x++) {
                    k--;
                    sb2.append(array_history[k]+"\n\n");
                }

                textView2.setText(sb2.toString());
                // Create a SpannableStringBuilder from the text
                ssb2 = new SpannableStringBuilder(textView2.getText());
                // Add a ClickableSpan to each line
                for (int a = 0; a < array_history.length; a++) {
                    final String line = array_history[a];
                    final int start = sb2.indexOf(line);
                    final int end = start + line.length();
                    ssb2.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            // Do something when the line is clicked (e.g. show a Toast)
                            Intent intent1 = getIntent();
                            String lrn = intent1.getStringExtra("current_user_lrn");
                            String name = ((TextView)findViewById(R.id.DashBoardUserName)).getText().toString();
                            rec.putExtra("current_user_lrn", lrn);
                            rec.putExtra("name", name);
                            rec.putExtra("receipt_details", line);
                            startActivity(rec);
                        }
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(Color.WHITE);
                            ds.setUnderlineText(false);
                        }
                    }, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
// Set the modified text with ClickableSpans to the TextView
                textView2.setText(ssb2);
// Make sure the TextView is clickable
                textView2.setMovementMethod(LinkMovementMethod.getInstance());

                history_string = preferences.getString("student2_history3", "");

//--------------------3
                textView3 = findViewById(R.id.arrayList3);
                array_history = history_string.split(",");
                k = array_history.length;
                for ( x = 0; x < array_history.length; x++) {
                    k--;
                    sb3.append(array_history[k]+"\n\n");
                }

                textView3.setText(sb3.toString());
                // Create a SpannableStringBuilder from the text
                ssb3 = new SpannableStringBuilder(textView3.getText());
                // Add a ClickableSpan to each line
                for (int a = 0; a < array_history.length; a++) {
                    final String line = array_history[a];
                    final int start = sb3.indexOf(line);
                    final int end = start + line.length();
                    ssb3.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            // Do something when the line is clicked (e.g. show a Toast)
                            Intent intent1 = getIntent();
                            String lrn = intent1.getStringExtra("current_user_lrn");
                            String name = ((TextView)findViewById(R.id.DashBoardUserName)).getText().toString();
                            rec.putExtra("current_user_lrn", lrn);
                            rec.putExtra("name", name);
                            rec.putExtra("receipt_details", line);
                            startActivity(rec);
                        }
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(Color.WHITE);
                            ds.setUnderlineText(false);
                        }
                    }, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
// Set the modified text with ClickableSpans to the TextView
                textView3.setText(ssb3);
// Make sure the TextView is clickable
                textView3.setMovementMethod(LinkMovementMethod.getInstance());
//-----------------------4
                history_string = preferences.getString("student2_history4", "");
                textView4 = findViewById(R.id.arrayList4);
                array_history = history_string.split(",");
                k = array_history.length;
                for ( x = 0; x < array_history.length; x++) {
                    k--;
                    sb4.append(array_history[k]+"\n\n");
                }

                textView4.setText(sb4.toString());
                // Create a SpannableStringBuilder from the text
                ssb4 = new SpannableStringBuilder(textView4.getText());
                // Add a ClickableSpan to each line
                for (int a = 0; a < array_history.length; a++) {
                    final String line = array_history[a];
                    final int start = sb4.indexOf(line);
                    final int end = start + line.length();
                    ssb4.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            // Do something when the line is clicked (e.g. show a Toast)
                            Intent intent1 = getIntent();
                            String lrn = intent1.getStringExtra("current_user_lrn");
                            String name = ((TextView)findViewById(R.id.DashBoardUserName)).getText().toString();
                            rec.putExtra("current_user_lrn", lrn);
                            rec.putExtra("name", name);
                            rec.putExtra("receipt_details", line);
                            startActivity(rec);
                        }
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(Color.WHITE);
                            ds.setUnderlineText(false);
                        }
                    }, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
// Set the modified text with ClickableSpans to the TextView
                textView4.setText(ssb4);
// Make sure the TextView is clickable
                textView4.setMovementMethod(LinkMovementMethod.getInstance());
                break;
            default:
                break;
        }
    }

}