package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class receipt_tab extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_tab);
        Intent intent = getIntent();

        TextView ref = findViewById(R.id.ref_no);
        TextView amount = findViewById(R.id.amount);
        TextView receive = findViewById(R.id.sent_received);
        TextView from = findViewById(R.id.from_to);
        TextView user = findViewById(R.id.user_names);
        TextView org= findViewById(R.id.orgs);
        TextView date= findViewById(R.id.dates);

        String receipt_details = intent.getStringExtra("receipt_details");


        String[] stringArray = receipt_details.split(" ");
//reference

        int reference = Integer.parseInt(stringArray[15]);
        ref.setText(String.format("%012d",reference));

//balance
        amount.setText(stringArray[2]);
//receive
        receive.setText(stringArray[0]);
//from
        from.setText(stringArray[5]);
//user and org
        switch (stringArray[6]){
            case "401647156237":
                user.setText("CANTEEN CASHIER");
                org.setText("Building-A Canteen");
                break;
            case "107631100121":
                user.setText("SCHOOL ACCOUNTANT");
                org.setText("Registrar Staff");
                break;
            case "401647150221":
                user.setText("STUDENT ONE");
                org.setText("Grade 10 - Lotus");

                break;
            case "107622100091":
                user.setText("STUDENT TWO");
                org.setText("Grade 12 - Euclid");
                break;
            case "117218060115":
                user.setText("BOOKSTORE");
                org.setText("Bookstore Personnel");
                break;
            default:
                break;
        }
//date
        date.setText(stringArray[10]+" "+stringArray[11]+" "+stringArray[12]);

    }
    public void download(View view) {

        captureActivityAndSaveToGallery();
    }
    private static final int REQUEST_CODE_PERMISSION = 100;

    // Method for capturing activity and saving the image

    private void captureActivityAndSaveToGallery() {
        Button invi = findViewById(R.id.download1);
        // Check if permission for writing to external storage is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if it's not granted yet
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        } else {
            invi.setVisibility(View.INVISIBLE);
            // Create a Bitmap of the activity's content view
            View view = getWindow().getDecorView().getRootView();
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);

            // Save the bitmap to the gallery
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "CAPTURE_" + timeStamp + ".jpg";
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/Camera/";

            File file = new File(directory);
            if (!file.exists()) {
                file.mkdirs();
            }
            File imageFile = new File(directory + imageFileName);

            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
                fos.getFD().sync();
                MediaScannerConnection.scanFile(this, new String[]{imageFile.toString()}, null, null);
                Toast.makeText(this, "Image saved to gallery.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to save image to gallery.", Toast.LENGTH_SHORT).show();
            }
        }
        invi.setVisibility(View.VISIBLE);
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, capture the activity and save to gallery
                captureActivityAndSaveToGallery();
            } else {
                // Permission denied, show a message
                Toast.makeText(this, "Permission denied. Cannot save image to gallery.", Toast.LENGTH_SHORT).show();
            }
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
                    Intent i = new Intent(receipt_tab.this,MainActivity.class);
                    i.putExtra("current_user_lrn", lrn);
                    i.putExtra("name", name);
                    startActivity(i);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}