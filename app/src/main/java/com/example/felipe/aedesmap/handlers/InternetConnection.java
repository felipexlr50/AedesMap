package com.example.felipe.aedesmap.handlers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.List;

public class InternetConnection {

    public static String getRequest(String url, Context context) {

        if(isNetworkAvailable(context)) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse responseGet = client.execute(get);
                StatusLine statusLine = responseGet.getStatusLine();
                if(statusLine.getStatusCode()==200) {
                    HttpEntity resEntityGet = responseGet.getEntity();
                    if (resEntityGet != null) {
                        String aux = EntityUtils.toString(resEntityGet);
                        Log.d("httpCode",statusLine.getStatusCode()+"");
                        Log.d("httpResponse",aux);
                        return aux;
                    }
                }else if(statusLine.getStatusCode()==404){
                    return "Não foi possível conectar com o servidor!";
                }
                else {
                    return "Erro de rede "+statusLine.getStatusCode();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static  String postRequest(String url, List<NameValuePair>params,Context context){
        if(isNetworkAvailable(context)) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            try {
                post.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse responsePost = client.execute(post);
                StatusLine statusLine = responsePost.getStatusLine();
                HttpEntity resEntityGet = responsePost.getEntity();
                if (resEntityGet != null) {
                    String aux = EntityUtils.toString(resEntityGet);
                    Log.d("httpCode",statusLine.getStatusCode()+"");
                    Log.d("httpResponse",aux);
                    return aux;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    private static boolean isNetworkAvailable(final Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()&& cm.getActiveNetworkInfo().isAvailable()&& cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}