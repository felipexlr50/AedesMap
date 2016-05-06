/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.felipe.aedesmap.MAP;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.example.felipe.aedesmap.DAO.BaseDAO;
import com.example.felipe.aedesmap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.example.felipe.aedesmap.model.MyItem;



import java.io.InputStream;
import java.util.List;

/**
 * Simple activity demonstrating ClusterManager.
 */
public class ClusteringMap extends BaseDemoActivity {
    private BaseDAO dao;

    private ClusterManager<MyItem> mClusterManager;

    //private Context context;
    //private GoogleMap mMap;

   /* public ClusteringMap(Context context, GoogleMap mMap){
        this.context = context;
        this.mMap = mMap;
    }*/

    public ClusteringMap(){

    }

    @Override
    public void startDemo() {
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-23.086057, -47.212235), 14));

        mClusterManager = new ClusterManager<MyItem>(this, getMap());
        getMap().setOnCameraChangeListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);


        addItens();

    }

    private void addItens(){
        double lat;
        double lng;

        dao = new BaseDAO(this);
        SQLiteDatabase db = dao.getWritableDatabase();

        String querry = "select * from position";

        Cursor c = db.rawQuery(querry,null);

        if(c.moveToFirst()){
            do {
                lat = c.getDouble(c.getColumnIndex(dao.LAT));
                lng = c.getDouble(c.getColumnIndex(dao.LGN));
                Log.d("banco", lat + " - " + lng);
                MyItem itens = new MyItem(lat, lng);
                mClusterManager.addItem(itens);
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        /*for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            MyItem offsetItem = new MyItem(lat, lng);
            mClusterManager.addItem(offsetItem);
        }*/

    }
}