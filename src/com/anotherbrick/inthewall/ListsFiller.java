package com.anotherbrick.inthewall;

import java.util.ArrayList;

public class ListsFiller {
    public static final String DAY_OF_WEEK = "dayofweek";
    public static final String WEATHER_CONDITION = "weather";
    public static final String MONTHS = "month";
    public static final String ALCOHOL = "alcohol";
    public static final String SPEED = "avgspeed";
    public static final String VEHICLE = "vehicletype";
    public static final String AGE = "age";
    public static final String HOUR = "hour";
    public static final String SEX = "sex";
    public static final String LIGHT_CONDITION = "lightcondition";
    public static final String CRASH_ID = "crashid";

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

    public static ArrayList<String> getMonths() {
	ArrayList<String> months = new ArrayList<String>();
	months.add("January");
	months.add("February");
	months.add("March");
	months.add("April");
	months.add("May");
	months.add("June");
	months.add("July");
	months.add("August");
	months.add("September");
	months.add("October");
	months.add("November");
	months.add("December");

	return months;
    }

    public static ArrayList<String> getLightConditions() {
	ArrayList<String> lc = new ArrayList<String>();
	lc.add("Daylight");
	lc.add("Dark - Not Lighted");
	lc.add("Dark - Lighted");
	lc.add("Dawn");
	lc.add("Dusk");
	lc.add("Dark - Unknown Lighting");

	return lc;
    }

    public static ArrayList<String> getSex() {
	ArrayList<String> sex = new ArrayList<String>();
	sex.add("Male");
	sex.add("Female");

	return sex;

    }

    public static ArrayList<String> getAge() {
	ArrayList<String> age = new ArrayList<String>();
	for (int i = 1; i <= 110; i += 10) {
	    age.add(Integer.toString(i));
	}
	return age;
    }

    public static ArrayList<String> getHour() {
	ArrayList<String> hour = new ArrayList<String>();
	for (int i = 1; i <= 24; i++) {
	    hour.add(Integer.toString(i));
	}
	return hour;
    }

    public static ArrayList<String> getVehicles() {
	ArrayList<String> vehicles = new ArrayList<String>();
	vehicles.add("Automobile");
	vehicles.add("Working Vehicle");
	vehicles.add("Motorcycle");
	vehicles.add("Other");

	return vehicles;
    }

    public static ArrayList<String> getSpeeds() {
	ArrayList<String> speeds = new ArrayList<String>();
	for (int i = 0; i <= 150; i += 10) {
	    speeds.add(Integer.toString(i) + " MPH");
	}

	return speeds;
    }

}
