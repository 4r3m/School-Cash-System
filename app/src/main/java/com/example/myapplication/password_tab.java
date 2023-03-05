package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class password_tab extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_tab);
        Intent get = getIntent();
        String name = get.getStringExtra("name");
        TextView user = findViewById(R.id.usernamed);
        ImageView img = findViewById(R.id.profileIcon);
        user.setText(name);

        int image = get.getIntExtra("image", 0);
        img.setImageResource(image);
    }
    public void login(View view){
        Intent get = getIntent();
        String password =get.getStringExtra("password");
        String lrn =get.getStringExtra("current_user_lrn");
        EditText pass = findViewById(R.id.password);
        Intent i = new Intent(this, MainActivity.class);

        TextView user = findViewById(R.id.usernamed);
        if(pass.getText().toString().equals(password)){
            i.putExtra("name", user.getText());
            i.putExtra("current_user_lrn", lrn);
            startActivity(i);
        }else {
            Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show();
        }
    }
}