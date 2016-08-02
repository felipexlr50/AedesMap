/*
package com.example.felipe.aedesmap;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.felipe.aedesmap.DAO.ImageDAO;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class GraphAcitivity extends AppCompatActivity {

    private String ano;
    private String mes;
    private int ocorrencias;
    private java.util.Date date;

    private Viewport viewport;

    private BarGraphSeries<DataPoint> series;

    private GraphView graphView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_graph_acitivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        graphView = (GraphView) findViewById(R.id.graph);
        series = new BarGraphSeries<DataPoint>();
        graphView.addSeries(series);
        series.setColor(Color.parseColor("#d64949"));
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);
        series.setSpacing(50);
        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));

        graphView.getGridLabelRenderer().setNumHorizontalLabels(3);

        graphView.getGridLabelRenderer().setVerticalAxisTitle(this.getString(R.string.occurrence));
        graphView.setTitle(this.getString(R.string.graphTitle));


       // graphView.getGridLabelRenderer().setNumHorizontalLabels(3);
        ImageDAO imageDAO = new ImageDAO(this);
        SQLiteDatabase db = imageDAO.getWritableDatabase();
        Cursor cursor = imageDAO.getOccurrenceByMonth();

        if(cursor.getColumnCount()<3){
            graphView.getGridLabelRenderer().setNumHorizontalLabels(cursor.getColumnCount());
        }
        else {
            graphView.getGridLabelRenderer().setNumHorizontalLabels(3);
        }
        cursor.close();
        db.close();
        getFromDB(this);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }



    public void getFromDB(Context context){

        ImageDAO imageDAO = new ImageDAO(context);
        SQLiteDatabase db = imageDAO.getWritableDatabase();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Cursor cursor = imageDAO.getOccurrenceByMonth();
        ArrayList<Integer> max = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                ano = cursor.getString(cursor.getColumnIndex("ano"));
                mes = cursor.getString(cursor.getColumnIndex("mes"));
                ocorrencias = cursor.getInt(cursor.getColumnIndex("soma"));
                try {
                    date = df.parse(cursor.getString(cursor.getColumnIndex(ImageDAO.DATA_IN)));
                } catch (ParseException e) {
                    Log.d("graphteste",e.getMessage());
                }

                series.appendData(new DataPoint(date,ocorrencias),true,cursor.getColumnCount());

                //series.appendData(new DataPoint(Double.parseDouble(mes),ocorrencias),true,cursor.getColumnCount());  // testar esse metodo ainda!
                max.add(new Integer(ocorrencias));



            }while(cursor.moveToNext());

            viewport = graphView.getViewport();
            viewport.setYAxisBoundsManual(true);
            viewport.setMinX(0);
            viewport.setMaxY(Collections.max(max)+(Collections.max(max)/2));
            viewport.setMinY(0);
        }
        cursor.close();
        db.close();

    }



}
*/
