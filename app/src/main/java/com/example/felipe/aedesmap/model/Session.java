package com.example.felipe.aedesmap.model;


public  class Session {

    private static double latNow;
    private static double lngNow;
    private static final String APIURL  = "http://aedesmap.16mb.com/";
    private static final String API_PEGAR_PONTOS = "pegarPontos.php";
    private static final String API_SALVAR_PONTOS = "salvarImagem.php";
    private static String imageBase64;


    public static String getApiSalvarPontos() {
        return API_SALVAR_PONTOS;
    }

    public static String getImageBase64() {
        return imageBase64;
    }

    public static void setImageBase64(String imageBase64) {
        Session.imageBase64 = imageBase64;
    }

    public static double getLatNow() {
        return latNow;
    }

    public static void setLatNow(double latNow) {
        Session.latNow = latNow;
    }

    public static double getLngNow() {
        return lngNow;
    }

    public static void setLngNow(double lngNow) {
        Session.lngNow = lngNow;
    }

    public static String getAPIURL() {
        return APIURL;
    }

    public static String getApiPegarPontos() {
        return API_PEGAR_PONTOS;
    }
}
