package com.example.felipe.aedesmap;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import com.example.felipe.aedesmap.MAP.ClusteringMap;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    private BaseDAO dao;
    private double lat;
    private double lng;
    private LocationListener lListerner;
    private LocationManager locationManager;

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
    }

    public void onCreateCamera(View v){

        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imagem = new File(picDir,"foto.jpg");

        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagem));
        startActivity(camera);

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



    public void onClickOpenMap() {
        startActivity(new Intent(this, ClusteringMap.class));
    }

    public void onClickAddPosition() {
        insertPosition();
    }

    public void onClickGetPosition() {
        getGPSposition(locationManager, lListerner);
        Toast.makeText(getBaseContext(),"Procurando posição",Toast.LENGTH_LONG).show();
    }

    public void insertPosition() {

        Calendar dateAtual = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:00.0");

        dao = new BaseDAO(this);
        SQLiteDatabase db = dao.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //db.execSQL("delete from " + dao.TBL);
        long result = 0;

        cv.put(dao.LGN, lng);
        cv.put(dao.LAT, lat);
        cv.put(dao.DATA_IN, format.format(dateAtual.getTime()));
        result = db.insert(dao.TBL, null, cv);

        if (result == -1) {
            Toast.makeText(this, "Nao foi salvo", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(this, "Ponto salvo", Toast.LENGTH_LONG).show();

        db.close();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
        }

    }

    public void getGPSposition(LocationManager locationManager, LocationListener lListerner){
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        lListerner = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                TextView positionTela = (TextView) findViewById(R.id.tfLatLng);
                lat = location.getLatitude();
                lng = location.getLongitude();
                positionTela.setText(lat + " | " + lng);

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
        }

        else{
            Toast.makeText(this,"nada",Toast.LENGTH_SHORT);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
