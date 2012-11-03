package com.anotherbrick.inthewall;

import markers.AbstractMarker;
import markers.DawnMarker;
import markers.FemaleMarker;
import markers.FoggyMarker;
import markers.MaleMarker;
import markers.NightMarker;
import markers.QuestionMarker;
import markers.SnowyMarker;
import markers.SunnyMarker;
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

    AbstractMarker getCorrespondingMarker(float x, float y, float width,
	    float height, VizPanel parent, Integer id) {

	switch (markerType) {
	case DEFAULT_MARKER:
	    return new DefaultMarker(x, y, width, height, parent, id);
	case GENDER:
	    // SHITTY JAVA 1.6 DOESN'T ALLOW SWITCHES OVER STRINGS
	    if (getGender().indexOf("Female") != -1)
		return new FemaleMarker(x, y, width, height, parent, id);
	    if (getGender().indexOf("Male") != -1)
		return new MaleMarker(x, y, width, height, parent, id);
	    return new QuestionMarker(x, y, width, height, parent, id);
	case LIGHT:
	    // SHITTY JAVA 1.6 DOESN'T ALLOW SWITCHES OVER STRINGS
	    if (getLight().indexOf("Dawn") != -1)
		return new DawnMarker(x, y, width, height, parent, id);
	    if (getLight().indexOf("Dark") != -1)
		return new NightMarker(x, y, width, height, parent, id);
	    if (getLight().indexOf("Daylight") != -1)
		return new SunnyMarker(x, y, width, height, parent, id);
	    return new QuestionMarker(x, y, width, height, parent, id);
	case WEATHER:
	    // SHITTY JAVA 1.6 DOESN'T ALLOW SWITCHES OVER STRINGS
	    if (getWeather().indexOf("Snow") != -1)
		return new SnowyMarker(x, y, width, height, parent, id);
	    if (getWeather().indexOf("Rain") != -1
		    || getLight().indexOf("Hail") != -1)
		return new QuestionMarker(x, y, width, height, parent, id);
	    if (getWeather().indexOf("No adverse") != -1)
		return new SunnyMarker(x, y, width, height, parent, id);
	    if (getWeather().indexOf("Fog") != -1
		    || getLight().indexOf("Smog") != -1)
		return new FoggyMarker(x, y, width, height, parent, id);
	    return new QuestionMarker(x, y, width, height, parent, id);
	default:
	    return null;
	}
    }
}
