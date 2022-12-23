package com.cmic.ndktest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    int REQUEST_EXTERNAL_STORAGE = 100;
    String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    EditText editText;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences("myssp", 0);
        editor = settings.edit();
        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        editText = findViewById(R.id.content);
        Button saveData = (Button) findViewById(R.id.write);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = editText.getText().toString();
                checkSdReadPermission(new ICheckPermissionListener() {
                    @Override
                    public void hasReadPermission() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                TestReadAndWirte.writeToSd(s, MainActivity.this);
                            }
                        }).start();
                    }
                });
//                editor.putString("sss", s);
//                editor.commit();
//                tv.setText(stringFromJNI());
            }
        });

        Button readData = (Button) findViewById(R.id.read);
        readData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSdReadPermission(new ICheckPermissionListener() {
                    @Override
                    public void hasReadPermission() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String result = TestReadAndWirte.readString(MainActivity.this);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv.setText(result);
                                    }
                                });
                            }
                        }).start();
                    }
                });

            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    void checkSdReadPermission(@NonNull ICheckPermissionListener listener) {
        int permission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            listener.hasReadPermission();
        }
    }

    interface ICheckPermissionListener {
        void hasReadPermission();
    }
}