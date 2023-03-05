package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class user_profiles extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profiles);

    }

    boolean methodExecuted = false;


    public void OnClickProfile(View v){

        Intent i = new Intent(this, password_tab.class);
        String name = "";
        SharedPreferences preferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("cashier_lrn", "401647156237");
        editor.putString("accountant_lrn", "107631100121");
        editor.putString("student1_lrn", "401647150221");
        editor.putString("student2_lrn", "107622100091");
        editor.putString("bookstore_lrn", "117218060115");
        editor.commit();

        switch (v.getId()){
            case R.id.profile01:
                name = ((TextView)findViewById(R.id.profile_one)).getText().toString();
                i.putExtra("name", name);
                i.putExtra("current_user_lrn", "401647156237");
                i.putExtra("password", "cashier123");
                i.putExtra("image", R.drawable.cashier);
                startActivity(i);
                break;
            case R.id.profile02:
                name = ((TextView)findViewById(R.id.profile_two)).getText().toString();
                i.putExtra("name", name);
                i.putExtra("current_user_lrn", "107631100121");
                i.putExtra("password", "accountant123");
                i.putExtra("image", R.drawable.accountant);
                startActivity(i);
                break;
            case R.id.profile03:
                name = ((TextView)findViewById(R.id.profile_three)).getText().toString();
                i.putExtra("name", name);
                i.putExtra("current_user_lrn", "401647150221");
                i.putExtra("password", "student1123");
                i.putExtra("image", R.drawable.student1);
                startActivity(i);
                break;
            case R.id.profile04:
                name = ((TextView)findViewById(R.id.profile_four)).getText().toString();
                i.putExtra("name", name);
                i.putExtra("current_user_lrn", "107622100091");
                i.putExtra("password", "student2123");
                i.putExtra("image", R.drawable.student2);
                startActivity(i);
                break;
            case R.id.profile05:
                name = ((TextView)findViewById(R.id.profile_five)).getText().toString();
                i.putExtra("name", name);
                i.putExtra("current_user_lrn", "117218060115");
                i.putExtra("password", "bookstore123");
                i.putExtra("image", R.drawable.bookstore);
                startActivity(i);
                break;
            default:
                break;

        }

    }
    public void OnClickSetBal(View v) {
        Intent i = new Intent(this, set_balance_tab.class);
        startActivity(i);

    }

}