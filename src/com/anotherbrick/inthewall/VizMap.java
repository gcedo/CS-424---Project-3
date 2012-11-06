package com.anotherbrick.inthewall;

import static com.anotherbrick.inthewall.Config.MyColorEnum.*;

import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import markers.AbstractMarker;

import processing.core.PVector;
import application.DBUtil;
import application.FilterWrapper;

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

    private static final float CHANGE_MODE_W = 100;
    private static final float CHANGE_MODE_H = 80;
    private static final float TTIP_H = 200;
    private static final float TTIP_W = 150;

    private static final float CHANGE_MODE_Y0 = CHANGE_MODE_TOGGLE_Y0
	    - CHANGE_MODE_H;
    private static final float CHANGE_MARKERS_X0 = 340;
    private static final float CHANGE_MARKERS_Y0 = CHANGE_MODE_TOGGLE_Y0
	    - CHANGE_MODE_H;

    private InteractiveMap map;
    private PVector mapOffset;
    private PVector mapSize;
    private PVector lastTouchPos;
    private HashMap<Integer, LocationWrapper> locationsList;
    private ArrayList<AbstractMarker> markers;
    private ArrayList<Cluster> clusters;
    private boolean mapTouched;
    public float MARKER_WIDTH = 10;
    public float MARKER_HEIGHT = 10;
    private float SC_MIN = 12;
    private float SC_MAX = 2041;
    private float CLUSTER_SC_MAX = s(1400);
    private float TX_MIN = (float) -80.1;
    private float TX_MAX = (float) -8.5;
    private float TY_MIN = (float) -114;
    private float TY_MAX = (float) -54.4;
    public StateInfo currentState;
    public Integer currentYear = 2001;

    private VizList changeMapMode, changeMarkers;
    private VizButton changeModeToggle;
    private Tooltip tooltip;
    private VizButton zoomIn, zoomOut, changeMarkersButton;

    private FilterWrapper fw = new FilterWrapper("crashid");

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
	changeModeToggle.setStyle(MEDIUM_GRAY, WHITE, LIGHT_GRAY, 255, 255, 8);
	changeModeToggle.setStyleDisabled(MEDIUM_GRAY, WHITE, LIGHT_GRAY, 255,
		255, 8);
	changeModeToggle.setRoundedCornerd(5, 5, 5, 5);
	changeModeToggle.setText("Change map mode");

	changeMarkers = new VizList(CHANGE_MARKERS_X0, CHANGE_MARKERS_Y0,
		CHANGE_MODE_W, CHANGE_MODE_H, this);
	changeMarkers.setup(LIGHT_GRAY, DARK_GRAY, CHANGE_MODE_ROWS,
		markersTypes(), false, SelectionMode.SINGLE);
	changeMarkers.setVisible(false);
	changeMarkersButton = new VizButton(CHANGE_MODE_TOGGLE_X0
		+ CHANGE_MODE_W + 10, CHANGE_MODE_TOGGLE_Y0, CHANGE_MODE_W,
		CHANGE_MODE_TOGGLE_H, this);
	changeMarkersButton.setStyle(MEDIUM_GRAY, WHITE, LIGHT_GRAY, 255, 255,
		8);
	changeMarkersButton.setRoundedCornerd(5, 5, 5, 5);
	changeMarkersButton.setText("Change Markers");
	addTouchSubscriber(changeMarkers);

	NotificationCenter.getInstance().registerToEvent("state-changed", this);

	NotificationCenter.getInstance().registerToEvent("year-changed", this);
	NotificationCenter.getInstance()
		.registerToEvent("filter0-update", this);
	NotificationCenter.getInstance()
		.registerToEvent("filter1-update", this);
    }

    @Override
    public void setup() {
	setModal(false);
	lastTouchPos = new PVector();

	locationsList = new HashMap<Integer, LocationWrapper>();
	markers = new ArrayList<AbstractMarker>();
	clusters = new ArrayList<Cluster>();

	mapOffset = new PVector(0, 0);
	mapSize = new PVector(getWidthZoom(), getHeightZoom());

	map = new InteractiveMap(m.p, new Microsoft.RoadProvider(),
		mapOffset.x, mapOffset.y, mapSize.x, mapSize.y);

	zoomIn = new VizButton(getWidth() - 22, 150, 20, 20, this);
	zoomIn.setStyle(MEDIUM_GRAY, WHITE, WHITE, 255, 255, 14);
	zoomIn.setText("+");
	zoomIn.setRoundedCornerd(5, 5, 5, 5);
	zoomOut = new VizButton(getWidth() - 22, 175, 20, 20, this);
	zoomOut.setStyle(MEDIUM_GRAY, WHITE, WHITE, 255, 255, 14);
	zoomOut.setText("-");
	zoomOut.setRoundedCornerd(5, 5, 5, 5);

	m.p.addMouseWheelListener(new MouseWheelListener() {
	    @Override
	    public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
		mouseWheel(evt.getWheelRotation());
	    }
	});

    }

    private ArrayList<String> markersTypes() {
	ArrayList<String> markers = new ArrayList<String>();
	markers.add("Weather");
	markers.add("Sex");
	markers.add("Light Condition");

	return markers;
    }

    @Override
    public boolean draw() {

	updateMapZoomAndPosition();
	pushStyle();

	background(MyColorEnum.BLACK);
	map.draw();
	drawLocationMarkers();

	zoomIn.draw();
	zoomIn.drawTextCentered();
	zoomOut.draw();
	zoomOut.drawTextCentered();

	noFill();
	stroke(MyColorEnum.RED);

	changeMarkers.draw();
	changeMarkers.setToRedraw();

	changeMarkersButton.draw();
	changeMarkersButton.drawTextCentered();

	changeMapMode.setToRedraw();
	changeMapMode.draw();
	changeModeToggle.setToRedraw();
	changeModeToggle.draw();
	changeModeToggle.drawTextCentered();

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
	    // ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	    clusters.clear();
	    for (AbstractMarker a : markers) {
		if (a.getX0() < this.getX0()
			|| a.getX0() > this.getX0() + getWidth()
			|| a.getY0() < this.getY0()
			|| a.getY0() > this.getY0() + getHeight()) {
		    continue;
		}

		if (clusters.size() == 0) {
		    Cluster cl = new Cluster(a, this, map.sc);
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
			}
		    }
		    if (inCluster == -1) {
			Cluster b = new Cluster(a, this, map.sc);

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
	    AbstractMarker marker = wrapper.getCorrespondingMarker(point.x
		    / s(1), point.y / s(1), MARKER_WIDTH, MARKER_HEIGHT, this,
		    id);
	    marker.setup();
	    markers.add(marker);
	}
    }

    private void updateLocationMarkersPosition() {
	for (AbstractMarker a : markers) {
	    LocationWrapper lw = locationsList.get(a.getId());
	    Point2f point = map.locationPoint(lw.getLocation());
	    a.modifyPosition(point.x / s(1), point.y / s(1));
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

	if (mapTouched && m.touchX != lastTouchPos.x
		&& m.touchY != lastTouchPos.y && m.touchX != 0 && m.touchY != 0) {

	    map.tx += (m.touchX - lastTouchPos.x) * c.multiply / map.sc;
	    map.ty += (m.touchY - lastTouchPos.y) * c.multiply / map.sc;
	    map.tx = costrain((float) map.tx, TX_MAX, TX_MIN);
	    map.ty = costrain((float) map.ty, TY_MAX, TY_MIN);

	    lastTouchPos.x = m.touchX;
	    lastTouchPos.y = m.touchY;

	    System.out.println("Update: LastPosition.x = " + lastTouchPos.x);
	    System.out.println("Update: LastPosition.y = " + lastTouchPos.y);

	    System.out.println("Update: touchx = " + m.touchX);
	    System.out.println("Update: touchy = " + m.touchY);
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
		if (!changeMapMode.getSelected().isEmpty()) {
		    changeMapStyle((MapStyles) changeMapMode.getSelected().get(
			    0));
		}
	    }
	    changeMapMode.toggleVisible();
	    return false;
	}

	if (down && changeMarkersButton.containsPoint(x, y)) {
	    if (changeMarkers.isVisible()) {
		if (!changeMarkers.getSelected().isEmpty()) {
		    changeMarkers((String) changeMarkers.getSelected().get(0));
		}
	    }
	    changeMarkers.toggleVisible();
	    return false;
	}

	if (down && zoomIn.containsPoint(x, y)) {
	    map.setZoom(map.getZoom() + 1);
	    updateLocationMarkersPosition();
	}

	if (down && zoomOut.containsPoint(x, y)) {
	    map.setZoom(map.getZoom() - 1);
	    updateLocationMarkersPosition();
	}

	if (down) {

	    lastTouchPos.x = m.touchX;
	    lastTouchPos.y = m.touchY;

	    mapTouched = true;

	    if (tooltip != null && tooltip.isVisible()) {
		tooltip.setVisible(false);
	    } else {
		if (map.sc > CLUSTER_SC_MAX) {
		    for (AbstractMarker marker : markers) {
			if (marker.containsPoint(x, y)) {
			    LocationWrapper wrapper = locationsList.get(marker
				    .getId());
			    Location location = wrapper.getLocation();
			    log("You have touched location located at lat: "
				    + location.lat + " and long: "
				    + location.lon);
			    tooltip = new Tooltip(marker.getX0() - TTIP_W / 2
				    + MARKER_WIDTH / 2,
				    marker.getY0() - TTIP_H, TTIP_W, TTIP_H,
				    this, marker.getId());
			    tooltip.setup(wrapper);
			}
		    }
		} else {
		    for (Cluster cl : clusters) {
			if (cl.isDrawingMarkers() == true) {
			    for (AbstractMarker marker : cl.getMarkers()) {
				if (marker.containsPoint(x, y)) {
				    LocationWrapper wrapper = locationsList
					    .get(marker.getId());
				    Location location = wrapper.getLocation();
				    log("You have touched location located at lat: "
					    + location.lat
					    + " and long: "
					    + location.lon);
				    tooltip = new Tooltip(marker.getX0()
					    - TTIP_W / 2 + MARKER_WIDTH / 2,
					    marker.getY0() - TTIP_H, TTIP_W,
					    TTIP_H, this, marker.getId());
				    tooltip.setup(wrapper);
				}
			    }
			}

		    }
		}
	    }

	} else if (!down) {
	    mapTouched = false;
	}

	propagateTouch(x, y, down, touchType);
	return false;
    }

    private void changeMarkers(String markerName) {

	MarkerType markerType = MarkerType.DEFAULT_MARKER;

	if (markerName.equals("Weather")) {
	    markerType = MarkerType.WEATHER;
	} else if (markerName.equals("Sex")) {
	    markerType = MarkerType.GENDER;
	} else if (markerName.equals("Light Condition")) {
	    markerType = MarkerType.LIGHT;
	}
	for (LocationWrapper location : locationsList.values()) {
	    location.setMarkerType(markerType);
	}
	updateLocationMarkers();

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
		currentState.getId(), currentYear, fw));
    }

    @Override
    public void eventReceived(String eventName, Object data) {
	log(eventName);
	if (eventName.equals("year-changed"))
	    setYear((Integer) data);
	if (eventName.equals("state-changed"))
	    setState((StateInfo) data);
	if (eventName.equals("filter1-update")
		|| eventName.equals("filter0-update")) {
	    setFilterWrapper((FilterWrapper) data);
	}

    }

    private void setFilterWrapper(FilterWrapper fw) {
	this.fw = fw;
	fetchPoints();
    }

    private void setState(StateInfo data) {
	currentState = data;
	map.setCenterZoom(currentState.getLoc(), data.getZoomLevel()
		+ (c.onWall ? 2 : 0));
	fetchPoints();

    }
}
