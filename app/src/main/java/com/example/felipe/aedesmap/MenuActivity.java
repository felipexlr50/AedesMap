package com.example.felipe.aedesmap;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felipe.aedesmap.DAO.BaseDAO;
import com.example.felipe.aedesmap.DAO.ImageDAO;
import com.example.felipe.aedesmap.MAP.ClusteringMap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private BaseDAO dao;
    private ImageDAO imageDAO;
    private double lat;
    private double lng;
    private LocationListener lListerner;
    private LocationManager locationManager;
    private boolean isGPSSignalOn=false;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private int nada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        gpsMessage();

    }

    public void onCreateCamera(View v){
       //if(lat!=0 || lng!=0) {
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
       // }
      //  else{
            //Toast.makeText(MenuActivity.this, this.getString(R.string.cameraWarning), Toast.LENGTH_SHORT).show();
       // }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            imageDAO = new ImageDAO(this);
            Calendar dateAtual = Calendar.getInstance();
            SQLiteDatabase db = imageDAO.getWritableDatabase();

            int result = 0;
            DateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:00.0");

            if (data.getExtras().isEmpty()) {
                Toast.makeText(MenuActivity.this, this.getString(R.string.imageCanceled), Toast.LENGTH_SHORT).show();
            } else {
                if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

                    Bitmap photo = (Bitmap) data.getExtras().get("data");

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    if (photo != null) {
                        photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);

                    }

                    byte[] byteArray = stream.toByteArray();
                    Log.d("Byte", byteArray.toString());


                    result = imageDAO.insert(byteArray, lat, lng, format.format(dateAtual.getTime()), db);

                    if (result == -1) {
                        Toast.makeText(this, this.getString(R.string.imageNotSaved), Toast.LENGTH_LONG).show();
                        Log.d("byte", "Imagem nao salva");
                    } else
                        insertPosition();
                    Toast.makeText(this, this.getString(R.string.imageSaved), Toast.LENGTH_LONG).show();
                    Log.d("byte", "Imagem salva");


                    db.close();


                }
            }
        }

        catch(NullPointerException exception){
            exception.printStackTrace();
            Toast.makeText(MenuActivity.this, this.getString(R.string.imageCanceled), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickOpenInfo(){
        startActivity(new Intent(this,InfoActivity.class));
    }

    public void onClickOpenMap() {
        startActivity(new Intent(this, ClusteringMap.class));
    }

    public void onClickAddPosition() {
        insertPosition();
    }

    public void onClickGetPosition() {
        getGPSposition(locationManager, lListerner);
        //Toast.makeText(getBaseContext(),"Procurando posição",Toast.LENGTH_LONG).show();
        Log.d("boolean",isGPSSignalOn+"");
        if(getLat()==0 || getLng()==0){
            isGPSSignalOn=false;
        }
        else{
            isGPSSignalOn=true;
        }

        if(!isGPSSignalOn){
            Toast.makeText(MenuActivity.this, "Procurando Posição...", Toast.LENGTH_SHORT).show();
        }
    }

    public void insertPosition() {

        Calendar dateAtual = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:00.0");

        dao = new BaseDAO(this);
        SQLiteDatabase db = dao.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //db.execSQL("delete from " + dao.TBL);
        long result = 0;

        if(getLat() ==0|| getLng() ==0){
            Toast.makeText(MenuActivity.this, this.getString(R.string.noGPSSignal), Toast.LENGTH_SHORT).show();
        }

        else if((getLat() >90|| getLat() <-90)||(getLng() >180|| getLng() <-180)){
            Toast.makeText(MenuActivity.this, this.getString(R.string.wrongGPSValue), Toast.LENGTH_SHORT).show();
        }

        else {
            cv.put(dao.LGN, getLng());
            cv.put(dao.LAT, getLat());
            cv.put(dao.DATA_IN, format.format(dateAtual.getTime()));
            result = db.insert(dao.TBL, null, cv);

            if (result == -1) {
                Toast.makeText(this, this.getString(R.string.positionNotsaved), Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(this, this.getString(R.string.positionSaved), Toast.LENGTH_LONG).show();

            db.close();
        }

    }

    public void getGPSposition(LocationManager locationManager, LocationListener lListerner){
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        lListerner = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                TextView positionTela = (TextView) findViewById(R.id.tfLatLng);
                setLat(location.getLatitude());
                setLng(location.getLongitude());
                positionTela.setText(getLat() + " | " + getLng());
                Log.d("boolean",isGPSSignalOn+"");

                if(getLat()==0 || getLng()==0){
                    isGPSSignalOn=false;
                }
                else{
                    isGPSSignalOn=true;
                }

                if(!isGPSSignalOn){
                    Toast.makeText(MenuActivity.this, "Procurando Posição...", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.MAPS_RECEIVE)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.MAPS_RECEIVE)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, lListerner);




    }


    private void gpsMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(this.getString(R.string.gpsWarning2));

        builder.setMessage(this.getString(R.string.gpsWarning3))
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onClickGetPosition();
                        dialog.cancel();
                    }
                });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add) {
            onClickAddPosition();


        } else if (id == R.id.nav_mapa) {

            onClickOpenMap();
            Log.d("menu","teste2");

        } else if(id == R.id.nav_getPos){

            onClickGetPosition();

        } else if(id == R.id.nav_info){

            onClickOpenInfo();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
