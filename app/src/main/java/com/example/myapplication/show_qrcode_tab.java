package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class show_qrcode_tab extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qrcode_tab);
        Intent i = getIntent();
        String lrn = i.getStringExtra("current_user_lrn");
        String name = i.getStringExtra("name");
        TextView uname= findViewById(R.id.user_name);
        ImageView qr = findViewById(R.id.my_qrcode1);
        qr.setImageResource(R.drawable.cashier_lrn);

        TextView grade = findViewById(R.id.GradeSection2);
        ((TextView)findViewById(R.id.lrn2)).setText(lrn);
        uname.setText(name);

        switch (lrn){
            case "401647156237":
                qr.setImageResource(R.drawable.cashier_lrn);
                grade.setText("Building-A Canteen");
                break;
            case "107631100121":
                qr.setImageResource(R.drawable.accountant_lrn);
                grade.setText("School Accountant");
                break;
            case "401647150221":
                qr.setImageResource(R.drawable.student1_lrn);
                grade.setText("Grade-Section");
                break;
            case "107622100091":
                qr.setImageResource(R.drawable.student2_lrn);
                grade.setText("Grade-Section");
                break;
            case "117218060115":
                qr.setImageResource(R.drawable.bookstore_lrn);
                grade.setText("Bookstore Cashier");
                break;
        }
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
                    Intent i = new Intent(show_qrcode_tab.this,MainActivity.class);
                    i.putExtra("current_user_lrn", lrn);
                    i.putExtra("name", name);
                    startActivity(i);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}