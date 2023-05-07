package io.github.alexjuca.kiyoSample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import io.github.alexjuca.kiyo.Kiyo;
import io.github.alexjuca.kiyo.KiyoListener;

public class MainActivity extends AppCompatActivity {
    private final KiyoListener kiyoListener = new KiyoListener() {
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

        final AppCompatButton call = findViewById(R.id.button);
        call.setOnClickListener(v -> call());

        Kiyo.with(this).withPermission(Manifest.permission.CALL_PHONE).withListener(kiyoListener).verify();
    }

    @SuppressLint("MissingPermission")
    public void call() {
        try {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel: 994590923" + Uri.encode("#"))));
        } catch (Exception exception) {
            Log.i("Permission Error", exception.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Kiyo.with(this).withListener(kiyoListener).onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
