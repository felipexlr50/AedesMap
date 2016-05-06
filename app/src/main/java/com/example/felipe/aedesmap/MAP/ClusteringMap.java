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

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.felipe.aedesmap.DAO.BaseDAO;
import com.example.felipe.aedesmap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.example.felipe.aedesmap.model.MyItem;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple activity demonstrating ClusterManager.
 */
public class ClusteringMap extends BaseDemoActivity implements ClusterManager.OnClusterClickListener<MyItem>
        , ClusterManager.OnClusterInfoWindowClickListener<MyItem>
        , ClusterManager.OnClusterItemClickListener<MyItem>
        , ClusterManager.OnClusterItemInfoWindowClickListener<MyItem> {

    private BaseDAO dao;
    private ClusterManager<MyItem> mClusterManager;


    private class CustomIconRenderer extends DefaultClusterRenderer<MyItem> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        //private final int mDimension;

        public CustomIconRenderer() {
            super(getApplicationContext(), getMap(), mClusterManager);

            mImageView = new ImageView(getApplicationContext());
            //mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            //mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            //int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            //mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);

        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem myItem, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            mImageView.setImageResource(R.drawable.mosquitoicon);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.mosquitoicon));

        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }

    }

    @Override
    public boolean onClusterClick(Cluster<MyItem> cluster) {
        // Show a toast with some info when the cluster is clicked.

        Toast.makeText(this, cluster.getSize() + " (including " + "teste2" + ")", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<MyItem> cluster) {
        // Does nothing, but you could go to a list of the users.
    }

    @Override
    public boolean onClusterItemClick(MyItem item) {
        // Does nothing, but you could go into the user's profile page, for example.
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(MyItem item) {
        // Does nothing, but you could go into the user's profile page, for example.
    }

    @Override
    public void startDemo() {
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-23.086057, -47.212235), 14));
        mClusterManager = new ClusterManager<MyItem>(this, getMap());
        mClusterManager.setRenderer(new CustomIconRenderer());

        mClusterManager = new ClusterManager<MyItem>(this, getMap());
        getMap().setOnCameraChangeListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        mClusterManager.cluster();

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
                MyItem itens = new MyItem(lat, lng, R.drawable.mosquitoicon);
                mClusterManager.addItem(itens);
            }while(c.moveToNext());
        }
        c.close();
        db.close();
    }


}