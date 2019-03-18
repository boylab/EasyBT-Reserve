package com.microape.example;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_SuportBT, btn_Permissions, btn_Function;
    private CheckBox cb_SuportBT, cb_Permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_SuportBT = findViewById(R.id.btn_SuportBT);
        btn_Permissions = findViewById(R.id.btn_Permissions);
        btn_Function = findViewById(R.id.btn_Function);

        cb_SuportBT = findViewById(R.id.cb_SuportBT);
        cb_Permissions = findViewById(R.id.cb_Permissions);

        btn_Permissions.setOnClickListener(this);
        btn_Function.setOnClickListener(this);

        boolean isSuportWiFi = getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);
        btn_SuportBT.setEnabled(isSuportWiFi);
        cb_SuportBT.setChecked(isSuportWiFi);
        btn_Permissions.setClickable(isSuportWiFi);
        btn_Function.setClickable(isSuportWiFi);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.btn_Permissions:
                MainActivityPermissionsDispatcher.onNeedsLocationWithCheck(this);
                break;
            case  R.id.btn_Function:
                funcTest();
                break;
        }
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onNeedsLocation() {
        cb_Permissions.setChecked(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onShowLocation(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(R.string.show_location_msg)
                .setPositiveButton(R.string.show_location_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.show_location_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onLocationDenied() {
        cb_Permissions.setChecked(false);
        Toast.makeText(this, R.string.show_location_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onLocationNeverAsk() {
        cb_Permissions.setChecked(false);
        Toast.makeText(this, R.string.show_location_neverask, Toast.LENGTH_SHORT).show();
    }

    private void funcTest() {
        if (!cb_SuportBT.isChecked()) {
            Toast.makeText(this, R.string.location_non_suport, Toast.LENGTH_SHORT).show();
        } else if (!cb_Permissions.isChecked()) {
            Toast.makeText(this, R.string.location_non_permission, Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this, BTFuncActivity.class);
        startActivity(intent);
    }
}
