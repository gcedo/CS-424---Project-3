package com.anotherbrick.inthewall;

import com.modestmaps.geo.Location;

public class StateInfo {
    private Location loc;
    private Integer id;
    private String name;
    private Integer zoomLevel;

    public StateInfo(Integer id, String name, Location loc, Integer zoomLevel) {
	super();
	this.id = id;
	this.name = name;
	this.loc = loc;
	this.zoomLevel = zoomLevel;
    }

    public Location getLoc() {
	return loc;
    }

    public void setLoc(Location loc) {
	this.loc = loc;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Integer getZoomLevel() {
	return zoomLevel;
    }
}
