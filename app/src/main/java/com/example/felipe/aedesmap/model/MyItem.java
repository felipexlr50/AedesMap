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

package com.example.felipe.aedesmap.model;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;


public class MyItem  implements ClusterItem {

    private final LatLng mPosition;
    private Bitmap image;


    public MyItem(double lat, double lng, Bitmap image) {
        this.setImage(image);
        mPosition = new LatLng(lat, lng);
    }

    public MyItem(double lat, double lng, String image) {
        this.setImage(convertImageString64(image));
        mPosition = new LatLng(lat, lng);
    }

    public MyItem(double lat, double lng) {

        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
         return mPosition;
    }


    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    private Bitmap convertImageString64(String imgString){
        //imgString = imgString.replace("\n","");
        //imgString = imgString.replace("\\","");

        byte[] byteArray = Base64.decode(imgString,Base64.URL_SAFE|Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
    }
}
