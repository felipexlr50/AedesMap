package com.example.felipe.aedesmap.handlers;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


public class APIKeyGen {

    public static String getAPIKEY(){
        Date date = new Date();
        String password = "batata";
        long timestamp = date.getTime()/1000;
        //String tokenAux = getMD5(password+timestamp);
        String tokenAux = getSHA256(password+timestamp);
        Log.d("apikey",tokenAux+"-"+timestamp);
        return tokenAux+"-"+timestamp;
    }

    public static String getMD5(String senha)  {

        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(senha.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder MD5Hash = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                MD5Hash.append(h);
            }

            return MD5Hash.toString();

        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static String getSHA256(String text){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(text.getBytes("UTF-8"));
            byte[] digest = md.digest();

            return String.format("%064x", new java.math.BigInteger(1, digest));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return "";
    }
}
