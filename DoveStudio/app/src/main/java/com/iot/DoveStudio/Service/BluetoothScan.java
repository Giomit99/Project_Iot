package com.iot.DoveStudio.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.iot.DoveStudio.BroadcastReceiver.MyBroadcastReceiver;
import com.iot.DoveStudio.MyThread.Mythread;
import com.iot.DoveStudio.R;

import java.util.List;

public class BluetoothScan extends Service {
/*******************************************************************/
    private final String TAG = BluetoothScan.class.getName();
    private final String startBeaconName = "SH-A";
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private LocationManager locationManager;
    private BluetoothLeScanner bluetoothLeScanner;
    private boolean isScanning;
    private ScanCallback scanCallback;
    private Handler handler;
    private MyBroadcastReceiver broadcastReceiver= null;

    private void istance() {
        //2.Ottieni un'istanza di BluetoothAdapter
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        handler= new Handler();
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");

        super.onCreate();
        istance();

        //Inizializzo il broadcast receiver
        broadcastReceiver= new MyBroadcastReceiver(this, bluetoothAdapter, locationManager);

        IntentFilter bluetoothFilter = new IntentFilter();
        bluetoothFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(broadcastReceiver, bluetoothFilter);

        IntentFilter gpsFilter = new IntentFilter();
        gpsFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        registerReceiver(broadcastReceiver, gpsFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStart");

        addNotification();
        startScan();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "OnDestroy");
        super.onDestroy();
        //stopScan();
        broadcastReceiver.stop(this);
    }

    @SuppressLint("MissingPermission")
    public void startScan() {
        Log.i(TAG, "startScan");

        if (!isScanning) {
            Log.i(TAG, "startScan and !isScanning");

            ScanSettings settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                    .build();

            scanCallback = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    Log.i(TAG, "onScanResult");

                    BluetoothDevice bluetoothDevice = result.getDevice();
                    if (bluetoothDevice != null) {
                        if (bluetoothDevice.getName()!= null && bluetoothDevice.getName().contains(startBeaconName)) {
                            Log.i(TAG, bluetoothDevice.getAddress());
                            int rssi= result.getRssi();
                            if(rssi > -60) {
                                Mythread mythread = new Mythread();
                                mythread.setTypeMessage(bluetoothDevice.getName());
                                Thread thread = new Thread(mythread);
                                thread.start();
                                try {
                                    thread.join();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                Log.i(TAG, mythread.getMessage());
                            }
                        }
                    }
                }

                @Override
                public void onBatchScanResults(List<ScanResult> results) {
                    for (ScanResult result : results) {
                        BluetoothDevice device = result.getDevice();
                        String deviceName = device.getName();
                        String deviceAddress = device.getAddress();
                        // Puoi gestire i dispositivi trovati qui
                        Log.d(TAG, "Dispositivo trovato: " + deviceName + " (" + deviceAddress + ")");
                    }
                }

                @Override
                public void onScanFailed(int errorCode) {
                    Log.e(TAG, "La scansione Bluetooth LE è fallita con codice di errore: " + errorCode);
                }
            };

            bluetoothLeScanner.startScan(null, settings, scanCallback);
            isScanning = true;
        }
    }

    @SuppressLint("MissingPermission")
    public void stopScan() {
        Log.i(TAG, "stopScan");

        if (isScanning) {
            bluetoothLeScanner.stopScan(scanCallback);
            isScanning = false;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final String NOTIFICATION_CHANNEL_ID = "MyForeroundService_ID";
    private void addNotification() {
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_ID,
                    NotificationManager.IMPORTANCE_LOW
            );
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
        Notification.Builder notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setContentText("MyForegroundService is running")
                    .setContentTitle("Service is active")
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.ic_launcher_background);
        }

        startForeground(1001, notification.build());
    }
}