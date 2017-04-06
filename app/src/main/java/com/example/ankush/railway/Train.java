package com.example.ankush.railway;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ankushbabbar on 16-Jan-17.
 */

public class Train implements Serializable{
    public static final int TRAIN_SEAT_TYPE_AC = 1;
    public static final int TRAIN_SEAT_TYPE_GEN = 2;

    public String name;
    public int number;
    public String src;
    public String dest;
    public int acCost;
    public int genCost;
    public String travel_time;
    public String departure_time;
    public String arrival_time;
    public String date;
}
