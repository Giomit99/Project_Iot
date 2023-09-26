package com.iot.DoveStudio.BroadcastReceiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import com.iot.DoveStudio.Service.BluetoothScan;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private final String TAG= "MyBroadcastReceiver";
    BluetoothScan bluetoothScan= null;
    BluetoothAdapter bluetoothAdapter= null;
    LocationManager locationManager= null;

    public MyBroadcastReceiver(BluetoothScan bluetoothScan, BluetoothAdapter bluetoothAdapter, LocationManager locationManager){
        this.bluetoothScan= bluetoothScan;
        this.bluetoothAdapter= bluetoothAdapter;
        this.locationManager= locationManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action != null) {
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int bluetoothState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (bluetoothState) {
                    case BluetoothAdapter.STATE_OFF:
                        // Bluetooth è stato disattivato
                        Log.i(TAG, "Bluetooth disattivato");
                        bluetoothScan.stopScan();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        // Bluetooth è stato attivato
                        Log.i(TAG, "Bluetooth attivato");
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            bluetoothScan.startScan();
                        }
                        break;
                }
            } else if (action.equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (isGpsEnabled) {
                    // GPS è stato attivato
                    Log.i(TAG, "GPS attivato");
                    if(bluetoothAdapter.isEnabled())
                        bluetoothScan.startScan();
                }
                else {
                    // GPS è stato disattivato
                    Log.i(TAG, "GPS disattivato");
                    bluetoothScan.stopScan();
                }
            }
        }
    }

    public void stop(Context context) {
        context.unregisterReceiver(this);
    }
}
