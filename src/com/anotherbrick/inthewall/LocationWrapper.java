package com.anotherbrick.inthewall;

import application.DBUtil;

import com.modestmaps.geo.Location;

public class LocationWrapper {

    private Location location;
    private MarkerType markerType;
    private Integer id;
    private boolean isMarker = true;
    private String weather = "";

    private String light = "";
    private String gender = "";

    // My constructors are fuzzier than your constructors... :)

    public LocationWrapper(Integer id, Location location,
	    MarkerType markerType, String weather, String light, String gender) {
	this.id = id;
	this.setLocation(location);
	this.setMarkerType(markerType);
	this.weather = weather;
	this.light = light;
	this.gender = gender;
    }

    public LocationWrapper(Integer id, float lat, float lon, String weather,
	    String light, String gender) {
	this(id, new Location(lat, lon), MarkerType.DEFAULT_MARKER, weather,
		light, gender);
    }

    public LocationWrapper(Integer id, Location location, MarkerType markerType) {
	this(id, location, markerType, "", "", "");
    }

    public LocationWrapper(Integer id, Location location) {
	this(id, location, MarkerType.DEFAULT_MARKER);

    }

    public LocationWrapper(Integer id, float lat, float lon) {
	this(id, new Location(lat, lon), MarkerType.DEFAULT_MARKER);
    }

    public LocationWrapper(Integer id, float lat, float lon,
	    MarkerType markerType) {
	this.setLocation(new Location(lat, lon));
	this.setMarkerType(markerType);
    }

    public LocationWrapper(Location location, MarkerType markerType) {
	this.setLocation(location);
	this.setMarkerType(markerType);
	isMarker = false;
    }

    public String getWeather() {
	return weather;
    }

    public String getLight() {
	return light;
    }

    public String getGender() {
	return gender;
    }

    public boolean isMarker() {
	return isMarker;
    }

    public Location getLocation() {
	return location;
    }

    public void setLocation(Location location) {
	this.location = location;
    }

    public MarkerType getMarkerType() {
	return markerType;
    }

    public void setMarkerType(MarkerType markerType) {
	this.markerType = markerType;
    }

    public int getId() {
	return id;
    }

    public CrashDetails explain() {
	return DBUtil.getInstance().getCrashDetailsById(id);
    }

}
