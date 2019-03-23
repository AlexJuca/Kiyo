package io.github.alexjuca.kiyo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private KiyoListener kiyoListener = new KiyoListener() {
        @Override
        public void onPermissionAccepted(int response) {
            call();
        }

        @Override
        public void onPermissionDenied(int response) {

        }

        @Override
        public void onShouldShowRequestPermissionRationale() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button call = findViewById(R.id.button);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });

        Kiyo.with(this).withView(findViewById(android.R.id.content)).
                withPermission(Manifest.permission.CALL_PHONE).
                withReason("Make a call").withListener(kiyoListener).verify();
    }

    @SuppressLint("MissingPermission")
    public void call() {
        Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel: 994590923" + Uri.encode("#")));
        startActivity(call);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Kiyo.with(this).withListener(kiyoListener).onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}