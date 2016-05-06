package com.example.felipe.aedesmap.POJO;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Felipe on 03/05/2016.
 */
public class Position implements Serializable {

    private long id;
    private float longitude;
    private float latitude;
    private Date dataInserida;
    private static final long serialVersionUID = 1L;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public Date getDataInserida() {
        return dataInserida;
    }

    public void setDataInserida(Date dataInserida) {
        this.dataInserida = dataInserida;
    }


}
