package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;

public class MapActivity extends AppCompatActivity {
    MapView map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE));
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(10.0);
        org.osmdroid.util.GeoPoint startpoint = new org.osmdroid.util.GeoPoint(32.0151, 34.7795);
        mapController.setCenter((IGeoPoint) startpoint);

    }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_map, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == R.id.exit_map) {
                showExitDialog();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        // Add this method to show the exit confirmation dialog
        private void showExitDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit the app?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finishAffinity(); // Exit the app
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss(); // Dismiss the dialog, stay in the app
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
