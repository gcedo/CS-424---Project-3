package com.anotherbrick.inthewall;

import java.util.ArrayList;

public class ListsFiller {

  public static ArrayList<String> getDaysOfWeek() {
    ArrayList<String> days = new ArrayList<String>();
    days.add("Monday");
    days.add("Tuesday");
    days.add("Wednesday");
    days.add("Thursday");
    days.add("Friday");
    days.add("Saturday");
    days.add("Sunday");

    return days;
  }

  public static ArrayList<String> getWeatherConditions() {
    ArrayList<String> wc = new ArrayList<String>();
    wc.add("Blowing Sand, Soil, Dirt");
    wc.add("Blowing Snow");
    wc.add("Clear");
    wc.add("Clear/Cloudy (No Adverse Conditions)");
    wc.add("Cloudy");
    wc.add("Fog");
    wc.add("Fog, Smog, Smoke");
    wc.add("Rain");
    wc.add("Rain and Fog");
    wc.add("Severe Crosswinds");
    wc.add("Sleet (Hail)");
    wc.add("Sleet and Fog");

    return wc;
  }

}
