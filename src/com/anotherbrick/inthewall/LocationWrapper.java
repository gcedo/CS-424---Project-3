package com.anotherbrick.inthewall;

import com.modestmaps.geo.Location;

public class LocationWrapper {

    private Location location;
    private MarkerType markerType;
    private Integer id;
    private boolean isMarker = true;

    public LocationWrapper(Integer id, Location location, MarkerType markerType) {
	this.id = id;
	this.setLocation(location);
	this.setMarkerType(markerType);
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

    public LocationWrapper(float lat, float lon, MarkerType markerType) {
	this.setLocation(new Location(lat, lon));
	this.setMarkerType(markerType);
	isMarker = false;
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

}
