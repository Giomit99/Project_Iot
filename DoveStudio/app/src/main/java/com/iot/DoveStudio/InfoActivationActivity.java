package com.iot.DoveStudio;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class InfoActivationActivity extends AppCompatActivity {
    private final String TAG= "InfoActivationActivity";
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_ENABLE_BT = 1; //Costante per bluetooth
    private static final int REQUEST_ENABLE_LOCATION = 1;
    Button button= null;
    BluetoothManager bluetoothManager= null;
    BluetoothAdapter bluetoothAdapter= null;
    LocationManager locationManager= null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_activation);

        button= findViewById(R.id.bttInfoActivity);

        supportBluetooth();

        istanceInfo();

        verifyBluetooht();

        autorizzazioneGPS();

        verifyLocation();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(InfoActivationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /******************************************************************************************/
    private void supportBluetooth() {
        //1.Verifica se il dispositivo supporta BLE
        Context context = getApplicationContext();
        PackageManager packageManager = context.getPackageManager();

        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
            Log.i(TAG, "Il dispositivo non supporta BLE");  // Il dispositivo non supporta BLE
        else
            Log.i(TAG, "Il dispositivo supporta BLE");
    }

    private void istanceInfo() {
        //2.Ottieni un'istanza di BluetoothAdapter
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    private void verifyBluetooht() {
        //3. Assicurati che il Bluetooth sia attivo
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            // Il Bluetooth non è abilitato, richiedi all'utente di abilitarlo
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private void verifyLocation(){
        //4. Attivazione GPS
        boolean isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isLocationEnabled) {
            // L'accesso alla posizione non è abilitato, richiedi all'utente di attivarlo
            Intent enableLocationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(enableLocationIntent, REQUEST_ENABLE_LOCATION);
        }
    }

    private void autorizzazioneGPS(){
        // Controlla se l'autorizzazione GPS è già concessa
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Se l'autorizzazione non è concessa, richiedi l'autorizzazione
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
        // Altrimenti l'autorizzazione è già concessa, puoi avviare la tua logica basata sulla posizione qui
    }
}
