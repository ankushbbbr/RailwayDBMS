package com.example.ankush.railway;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ankushbabbar on 16-Jan-17.
 */

public class Train implements Serializable{
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
