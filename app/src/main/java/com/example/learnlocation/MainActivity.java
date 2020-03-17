package com.example.learnlocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.MapView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.WAKE_LOCK,
//            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
    };

    private MapView mMapView = null;
    private TextView txtPosition = null;

    private LocationClient mLocationClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                StringBuilder currentPosition = new StringBuilder();
                currentPosition.append("纬度:").append(bdLocation.getLatitude()).append("\n");
                currentPosition.append("经度:").append(bdLocation.getLongitude()).append("\n");
                currentPosition.append("定位方式:");
                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                    currentPosition.append("GPS");
                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                    currentPosition.append("网络");
                }
                txtPosition.setText(currentPosition);
            }
        });

        mMapView = findViewById(R.id.mapView);
        txtPosition = findViewById(R.id.txt_position);
        requestPermissions();
    }

    private void requestPermissions() {
        List<String> permissionList = new ArrayList<String>();
        for (String p : permissions) {
            if (ContextCompat.checkSelfPermission(this,p) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(p);
            }
        }

        if (permissionList.size() > 0) {
            ActivityCompat.requestPermissions(this,permissionList.toArray(new String[permissionList.size()]),1);
        } else {
            requestLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序!", Toast.LENGTH_SHORT).show();
                            finish();
                            return ;
                        }
                        requestLocation();
                    }
                }
                break;
        }

    }

    private void requestLocation() {
        mLocationClient.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
