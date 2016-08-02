package com.example.felipe.aedesmap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.felipe.aedesmap.DAO.ImageDAO;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Felipe on 02/08/2016.
 */
public class NewGraphView extends AppCompatActivity {

    private LineChart lineChart;
    private ProgressBar resultBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_acitivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lineChart = (LineChart) findViewById(R.id.chart);
        new getDataForGraph().execute("");
    }

    private void setGraph(LineData data, LineDataSet dataSet, Integer max){

        dataSet.setDrawCubic(true);
        dataSet.setColor(ContextCompat.getColor(this, R.color.colorCustomActionBar));
        dataSet.setDrawCircles(false);
        YAxis leftAxix = lineChart.getAxisLeft();
        leftAxix.setAxisMaxValue((float)max+10);

        lineChart.setData(data);
        lineChart.animateY(3000);
        lineChart.invalidate();
    }

    class getDataForGraph extends AsyncTask<String, Void, ArrayList<Object>> {

        @Override
        protected void onPreExecute() {
            resultBar = (ProgressBar) findViewById(R.id.pbGraph);
            resultBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected ArrayList<Object> doInBackground(String... params) {
            return getFromDB(NewGraphView.this);
        }


        @Override
        protected void onPostExecute(ArrayList<Object> objects) {

            setGraph((LineData) objects.get(0),(LineDataSet) objects.get(1),(Integer) objects.get(2));
            resultBar.setVisibility(View.INVISIBLE);
        }

    }

    public ArrayList<Object> getFromServer(){

        return null;
    }

    public ArrayList<Object> getFromDB(Context context){

        ArrayList<Object> objects = new ArrayList<>();
        ImageDAO imageDAO = new ImageDAO(context);
        SQLiteDatabase db = imageDAO.getWritableDatabase();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df2 = new SimpleDateFormat("MMM/yy");
        Cursor cursor = imageDAO.getOccurrenceByMonth();

        ArrayList<Integer> max = new ArrayList<>();
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        Date date = null;

        if(cursor.moveToFirst()){
            int i = 0;
            do{

                int ocorrencias = 0;
                ocorrencias = cursor.getInt(cursor.getColumnIndex("soma"));
                try {
                     date = df.parse(cursor.getString(cursor.getColumnIndex(ImageDAO.DATA_IN)));
                } catch (ParseException e) {
                    Log.d("graphteste",e.getMessage());
                }
                entries.add(new Entry(ocorrencias,i));
                labels.add(df2.format(date));
                max.add(new Integer(ocorrencias));
                i++;
            }while(cursor.moveToNext());
        }
        LineDataSet dataset = new LineDataSet(entries, "Ocorrências por mês");
        LineData data = new LineData(labels, dataset);
        objects.add(data);
        objects.add(dataset);
        if(!max.isEmpty()){objects.add(Collections.max(max));}else objects.add(new Integer(0));
        cursor.close();
        db.close();
        return objects;
    }


}
