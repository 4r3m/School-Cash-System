package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class set_balance_tab extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_balance_tab);
    }
    public void OnClickSet(View v){
        Intent i = new Intent(this, user_profiles.class);

        SharedPreferences preferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String cashier = ((EditText)findViewById(R.id.set1)).getText().toString();
        String accountant = ((EditText)findViewById(R.id.set2)).getText().toString();
        String student1 = ((EditText)findViewById(R.id.set3)).getText().toString();
        String student2 = ((EditText)findViewById(R.id.set4)).getText().toString();
        String bookstore = ((EditText)findViewById(R.id.set5)).getText().toString();

        editor.putString("cashier_balance",    cashier);
        editor.putString("accountant_balance", accountant);
        editor.putString("student1_balance",   student1);
        editor.putString("student2_balance",   student2);
        editor.putString("bookstore_balance",   bookstore);

        editor.putString("cashier_history", "");
        editor.putString("accountant_history", "");
        editor.putString("student1_history", "");
        editor.putString("student2_history", "");
        editor.putString("bookstore_history", "");


        editor.putString("student1_history1", "");
        editor.putString("student1_history2", "");
        editor.putString("student1_history3", "");
        editor.putString("student1_history4", "");

        editor.putString("student2_history1", "");
        editor.putString("student2_history2", "");
        editor.putString("student2_history3", "");
        editor.putString("student2_history4", "");
        editor.putString("reference", "0");
        editor.apply();
        startActivity(i);
    }
}