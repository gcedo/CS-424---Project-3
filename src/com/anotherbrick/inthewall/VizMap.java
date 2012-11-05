package com.anotherbrick.inthewall;

import static com.anotherbrick.inthewall.Config.MyColorEnum.*;

import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import markers.AbstractMarker;

import processing.core.PVector;
import application.DBUtil;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.VizList.SelectionMode;
import com.modestmaps.InteractiveMap;
import com.modestmaps.core.Point2f;
import com.modestmaps.geo.Location;
import com.modestmaps.providers.Microsoft;

public class VizMap extends VizPanel implements TouchEnabled, EventSubscriber {

    private static final float CHANGE_MODE_X0 = 213;
    private static final int CHANGE_MODE_ROWS = 3;

    private static final float CHANGE_MODE_TOGGLE_X0 = 213;
    private static final float CHANGE_MODE_TOGGLE_H = 10;
    private static final float CHANGE_MODE_TOGGLE_Y0 = 370;

    private static final float CHANGE_MODE_W = 130;
    private static final float CHANGE_MODE_H = 80;
    private static final float TTIP_H = 200;
    private static final float TTIP_W = 150;

    private static final float CHANGE_MODE_Y0 = CHANGE_MODE_TOGGLE_Y0
	    - CHANGE_MODE_H;

    private MarkerType markersType = MarkerType.GENDER;

    private InteractiveMap map;
    private PVector mapOffset;
    private PVector mapSize;
    private PVector lastTouchPos, lastTouchPos2;
    private PVector initTouchPos, initTouchPos2;
    private int touchID1, touchID2;
    private Hashtable<Integer, VizTouch> touchList;
    private HashMap<Integer, LocationWrapper> locationsList;
    private ArrayList<AbstractMarker> markers;
    private static int id;
    private boolean mapTouched;
    private float touchWidth = 5;
    public float MARKER_WIDTH = 10;
    public float MARKER_HEIGHT = 10;
    private float SC_MIN = 12;
    private float SC_MAX = 2041;
    private float CLUSTER_SC_MAX = 400;
    private float TX_MIN = (float) -80.1;
    private float TX_MAX = (float) -8.5;
    private float TY_MIN = (float) -114;
    private float TY_MAX = (float) -54.4;
    public StateInfo currentState = new StateInfo(17, "Illinois", new Location(
	    40.633125f, -89.398528f));
    public Integer currentYear = 2001;

    private VizList changeMapMode;
    private VizButton changeModeToggle;
    private Tooltip tooltip;

    public VizMap(float x0, float y0, float width, float height, VizPanel parent) {
	super(x0, y0, width, height, parent);
	this.parent = parent;
	changeMapMode = new VizList(CHANGE_MODE_X0, CHANGE_MODE_Y0,
		CHANGE_MODE_W, CHANGE_MODE_H, this);
	ArrayList<MapStyles> mapModes = new ArrayList<MapStyles>();
	mapModes.add(MapStyles.MICROSOFT_AERIAL);
	mapModes.add(MapStyles.MICROSOFT_HYBRID);
	mapModes.add(MapStyles.MICROSOFT_ROAD);
	changeMapMode.setup(LIGHT_GRAY, DARK_GRAY, CHANGE_MODE_ROWS, mapModes,
		false, SelectionMode.SINGLE);
	changeMapMode.setVisible(false);
	addTouchSubscriber(changeMapMode);
	changeModeToggle = new VizButton(CHANGE_MODE_TOGGLE_X0,
		CHANGE_MODE_TOGGLE_Y0, CHANGE_MODE_W, CHANGE_MODE_TOGGLE_H,
		this);
	changeModeToggle.setText("Map Mode");
	changeModeToggle.setStyle(MEDIUM_GRAY, WHITE, LIGHT_GRAY, 255, 255, 14);
	changeModeToggle.setStyleDisabled(MEDIUM_GRAY, WHITE, LIGHT_GRAY, 255,
		255, 14);
	changeModeToggle.setRoundedCornerd(5, 5, 5, 5);

	NotificationCenter.getInstance().registerToEvent("year-changed", this);
	NotificationCenter.getInstance().registerToEvent("state-changed", this);

    }

    @Override
    public void setup() {

	lastTouchPos = new PVector();
	lastTouchPos2 = new PVector();
	initTouchPos = new PVector();
	initTouchPos2 = new PVector();

	touchList = new Hashtable<Integer, VizTouch>();
	locationsList = new HashMap<Integer, LocationWrapper>();
	markers = new ArrayList<AbstractMarker>();

	mapOffset = new PVector(0, 0);
	mapSize = new PVector(getWidth(), getHeight());

	map = new InteractiveMap(m.p, new Microsoft.RoadProvider(),
		mapOffset.x, mapOffset.y, mapSize.x, mapSize.y);

	map.setCenterZoom(currentState.getLoc(), 6 + (c.onWall ? 0 : 3));

	m.p.addMouseWheelListener(new MouseWheelListener() {
	    @Override
	    public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
		mouseWheel(evt.getWheelRotation());
	    }
	});

    }

    @Override
    public boolean draw() {
	pushStyle();

	background(MyColorEnum.BLACK);
	updateMapZoomAndPosition();
	map.draw();
	drawLocationMarkers();

	noFill();
	stroke(MyColorEnum.RED);
	strokeWeight(10);
	rect(mapOffset.x, mapOffset.y, mapSize.x, mapSize.y);

	changeMapMode.setToRedraw();
	changeMapMode.draw();
	changeModeToggle.setToRedraw();
	changeModeToggle.draw();

	if (tooltip != null && tooltip.isVisible()) {
	    tooltip.draw();
	}

	popStyle();
	return false;
    }

    private void addLocation(LocationWrapper wrapper) {
	locationsList.put(wrapper.getId(), wrapper);
    }

    public void removeLocation(Integer id) {
	if (locationsList.containsKey(id)) {
	    locationsList.remove(id);
	}
    }

    private void drawLocationMarkers() {
	if (map.sc > CLUSTER_SC_MAX) {
	    for (AbstractMarker a : markers) {
		if (a.getX0() < this.getX0()
			|| a.getX0() > this.getX0() + getWidth()
			|| a.getY0() < this.getY0()
			|| a.getY0() > this.getY0() + getHeight()) {
		    continue;
		}
		a.draw();
	    }
	} else {
	    ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	    // clusters.clear();
	    for (AbstractMarker a : markers) {
		if (a.getX0() < this.getX0()
			|| a.getX0() > this.getX0() + getWidth()
			|| a.getY0() < this.getY0()
			|| a.getY0() > this.getY0() + getHeight()) {
		    continue;
		}
		// if it is in the viewable area of the map, we try to cluster
		// it

		if (clusters.size() == 0) {
		    Cluster cl = new Cluster(a, this, map.sc);
		    // Cluster cl = new Cluster(a.getX0(),
		    // a.getY0(),a.getWidth(),a.getHeight(), this, a.getId());
		    // TODO in the Constructor I need to put a into it, set the
		    // number of it as 1 and set the position of it as the
		    // position
		    // of a
		    clusters.add(cl);
		} else {
		    float minDis = Float.MAX_VALUE;
		    int inCluster = -1;
		    for (Cluster cl : clusters) {
			float dis = dist(a, cl);
			if (dis < 10000) {
			    if (dis < minDis) {
				minDis = dis;
				inCluster = cl.getId();
			    }
			    // TODO if the position of this cluster changes,
			    // that'll
			    // be better but that is too much work
			}
		    }
		    // not in any cluster
		    if (inCluster == -1) {
			Cluster b = new Cluster(a, this, map.sc);
			// Cluster b = new Cluster(a.getX0(),
			// a.getY0(),a.getWidth(),a.getHeight(), this,
			// a.getId());
			clusters.add(b);
		    }
		    // in some cluster
		    else {
			for (Cluster cl : clusters) {
			    if (cl.getId() != inCluster)
				continue;
			    cl.add(a);
			    break;
			}
		    }
		}
	    }
	    for (Cluster cl : clusters) {
		cl.draw();
	    }
	}
	// if not too many markers, draw them
	/*
	 * if (count < 50) { for (AbstractMarker a : markers) { if (a.getX0() <
	 * this.getX0() || a.getX0() > this.getX0() + getWidth() || a.getY0() <
	 * this.getY0() || a.getY0() > this.getY0() + getHeight()) { continue; }
	 * a.draw(); } }
	 */
	// System.out.println(count+" markers drawn.");
    }

    private float dist(AbstractMarker a, Cluster cl) {
	return ((a.getX0() - cl.getPos().x) * (a.getX0() - cl.getPos().x) + (a
		.getY0() - cl.getPos().y) * (a.getY0() - cl.getPos().y));
    }

    private void updateLocationMarkers() {
	markers.clear();
	for (Map.Entry<Integer, LocationWrapper> pair : locationsList
		.entrySet()) {
	    LocationWrapper wrapper = pair.getValue();
	    Point2f point = map.locationPoint(wrapper.getLocation());
	    Integer id = pair.getKey();
	    wrapper.setMarkerType(markersType);
	    AbstractMarker marker = wrapper.getCorrespondingMarker(point.x,
		    point.y, MARKER_WIDTH, MARKER_HEIGHT, this, id);
	    marker.setup();
	    markers.add(marker);
	}
    }

    private void updateLocationMarkersPosition() {
	for (AbstractMarker a : markers) {
	    LocationWrapper lw = locationsList.get(a.getId());
	    Point2f point = map.locationPoint(lw.getLocation());
	    a.modifyPosition(point.x, point.y);
	}
    }

    public void changeMapStyle(MapStyles style) {
	switch (style) {
	case MICROSOFT_ROAD:
	    map.setMapProvider(new Microsoft.RoadProvider());
	    break;
	case MICROSOFT_AERIAL:
	    map.setMapProvider(new Microsoft.AerialProvider());
	    break;
	case MICROSOFT_HYBRID:
	    map.setMapProvider(new Microsoft.HybridProvider());
	    break;
	default:
	    map.setMapProvider(new Microsoft.RoadProvider());
	    break;
	}
    }

    private void updateMapZoomAndPosition() {
	if (mapTouched) {

	    float xPos = m.touchX;
	    float yPos = m.touchY;

	    if (touchList.size() < 2) {
		map.tx += (xPos - lastTouchPos.x) / map.sc;
		map.ty += (yPos - lastTouchPos.y) / map.sc;
		map.tx = costrain((float) map.tx, TX_MAX, TX_MIN);
		map.ty = costrain((float) map.ty, TY_MAX, TY_MIN);
	    } else if (touchList.size() == 2) {
		float sc = dist(lastTouchPos.x, lastTouchPos.y,
			lastTouchPos2.x, lastTouchPos2.y);
		float initPos = dist(initTouchPos.x, initTouchPos.y,
			initTouchPos2.x, initTouchPos2.y);

		PVector midpoint = new PVector(
			(lastTouchPos.x + lastTouchPos2.x) / 2,
			(lastTouchPos.y + lastTouchPos2.y) / 2);
		sc -= initPos;
		sc /= 5000;
		sc += 1;
		float mx = (midpoint.x - mapOffset.x) - mapSize.x / 2;
		float my = (midpoint.y - mapOffset.y) - mapSize.y / 2;
		map.tx -= mx / map.sc;
		map.ty -= my / map.sc;
		map.sc *= sc;
		map.sc = costrain((float) map.sc, SC_MAX, SC_MIN);
		map.tx += mx / map.sc;
		map.ty += my / map.sc;
	    }

	    if (id == touchID1) {
		lastTouchPos.x = xPos;
		lastTouchPos.y = yPos;
	    } else if (id == touchID2) {
		lastTouchPos2.x = xPos;
		lastTouchPos2.y = yPos;
	    }

	    // Update touch list
	    VizTouch t = new VizTouch(id, xPos, yPos, touchWidth, touchWidth);
	    touchList.put(id, t);
	    updateLocationMarkersPosition();
	}
    }

    public void addLocations(ArrayList<LocationWrapper> locations) {
	for (LocationWrapper l : locations)
	    addLocation(l);
	updateLocationMarkers();
    }

    @Override
    public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {

	if (down && changeModeToggle.containsPoint(x, y)) {
	    if (changeMapMode.isVisible()) {
		changeMapStyle((MapStyles) changeMapMode.getSelected().get(0));
	    }
	    changeMapMode.toggleVisible();
	    return false;
	}
	if (down) {
	    if (tooltip != null && tooltip.isVisible()) {
		tooltip.setVisible(false);
	    }

	    for (AbstractMarker marker : markers) {
		if (marker.containsPoint(x, y)) {
		    LocationWrapper wrapper = locationsList.get(marker.getId());
		    Location location = wrapper.getLocation();
		    log("You have touched location located at lat: "
			    + location.lat + " and long: " + location.lon);
		    tooltip = new Tooltip(marker.getX0() - TTIP_W / 2
			    + MARKER_WIDTH / 2, marker.getY0() - TTIP_H,
			    TTIP_W, TTIP_H, this, marker.getId());
		    tooltip.setup(wrapper);
		}
	    }

	    lastTouchPos.x = x;
	    lastTouchPos.y = y;

	    VizTouch t = new VizTouch(id, x, y, touchWidth, touchWidth);
	    touchList.put(id, t);

	    if (touchList.size() == 1) {
		touchID1 = id;
		initTouchPos.x = x;
		initTouchPos.y = y;
	    } else if (touchList.size() == 2) {
		touchID2 = id;
		initTouchPos2.x = x;
		initTouchPos2.y = y;
	    }
	    mapTouched = true;
	    log("Map touched");
	} else if (!down) {
	    touchList.remove(id);
	    mapTouched = false;
	}
	updateMapZoomAndPosition();
	propagateTouch(x, y, down, touchType);
	return false;
    }

    protected void mouseWheel(int wheelRotation) {
	float sc = 1.0f;
	if (wheelRotation < 0) {
	    sc = 1.05f;
	} else if (wheelRotation > 0) {
	    sc = 1.0f / 1.05f;
	}
	float mx = m.touchX - getWidth() / 2;
	float my = m.touchY - getHeight() / 2;
	map.tx -= mx / map.sc;
	map.ty -= my / map.sc;
	map.sc *= sc;
	map.sc = costrain((float) map.sc, SC_MAX, SC_MIN);
	map.tx += mx / map.sc;
	map.ty += my / map.sc;
	updateLocationMarkersPosition();
    }

    private float costrain(float value, float maxValue, float minValue) {
	return Math.min(Math.max(value, minValue), maxValue);
    }

    private void setYear(Integer year) {
	currentYear = year;
	fetchPoints();
    }

    private void fetchPoints() {
	locationsList.clear();
	addLocations(DBUtil.getInstance().getPointsByState(
		currentState.getId(), currentYear));
    }

    @Override
    public void eventReceived(String eventName, Object data) {
	if (eventName.equals("year-changed"))
	    setYear((Integer) data);
	if (eventName.equals("state-changed"))
	    setState((StateInfo) data);

    }

    private void setState(StateInfo data) {
	currentState = data;
	map.setCenterZoom(currentState.getLoc(), 6 + (c.onWall ? 0 : 3));
	fetchPoints();

    }
}
