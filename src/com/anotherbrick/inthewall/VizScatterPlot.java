package com.anotherbrick.inthewall;

import static com.anotherbrick.inthewall.Config.MyColorEnum.*;
import static com.anotherbrick.inthewall.Helper.*;

import java.util.ArrayList;

import application.DBUtil;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.VizList.SelectionMode;

import processing.core.PApplet;
import processing.core.PVector;

public class VizScatterPlot extends VizPanel implements TouchEnabled,
	EventSubscriber {

    private static final float BUTTON_W = 40;
    private static final float BUTTON_H = 20;
    private static final float BUTTON_X_Y0 = 140;
    private static final float BUTTON_Y_Y0 = 165;
    private static final float YEAR_BUTTON_Y0 = 190;
    private static final float PLOT_BUTTON_Y0 = 215;

    private static final MyColorEnum DOT_COLOR_SCATTER = MyColorEnum.LIGHT_BLUE;
    private static final float DOT_SIZE_SCATTER = 3;
    private static final float AXIS_LABEL_SIZE_SCATTER = 10;
    private static final float VOL_LABEL_SIZE_SCATTER = 10;
    private static final MyColorEnum VOL_TICK_COLOR_SCATTER = MyColorEnum.LIGHT_GREEN;
    private static final float VOL_TICK_WEIGHT_SCATTER = 1;

    private static final float PADDING_RIGHT = 100;
    private static final float PADDING_LEFT = 40;
    private static final float PADDING_TOP = 20;
    private static final float PADDING_BOTTOM = 20;
    private static final int N_ROWS = 3;
    private static final float LIST_W = 100;
    private static int NOFTICKS = 10;

    ArrayList<PVector> dots;
    float maxX;
    float maxY;
    private String xLabel = "x label";
    private String yLabel = "y label";
    private int currentYear = 2001;
    private StateInfo currentState;

    private VizList xAxisVar, yAxisVar, yearList;
    private VizButton xAxisButton, yAxisButton, yearButton, plotButton;
    private ArrayList<VizButton> buttons;

    private MyColorEnum VOL_LABEL_COLOR_SCATTER = MyColorEnum.LIGHT_BLUE;

    public VizScatterPlot(float x0, float y0, float width, float height,
	    VizPanel parent) {
	super(x0, y0, width, height, parent);

	maxX = -Float.MAX_VALUE;
	maxY = -Float.MAX_VALUE;
    }

    @Override
    public void setup() {
	NotificationCenter.getInstance().registerToEvent(
		"year-changed-timeslider", this);
	NotificationCenter.getInstance().registerToEvent("state-changed", this);

	buttons = new ArrayList<VizButton>();

	yearButton = new VizButton(0, YEAR_BUTTON_Y0, BUTTON_W, BUTTON_H, this);
	yearButton.setText("Year");
	buttons.add(yearButton);

	xAxisButton = new VizButton(0, BUTTON_X_Y0, BUTTON_W, BUTTON_H, this);
	xAxisButton.setText("X");
	buttons.add(xAxisButton);

	yAxisButton = new VizButton(0, BUTTON_Y_Y0, BUTTON_W, BUTTON_H, this);
	yAxisButton.setText("Y");
	buttons.add(yAxisButton);

	plotButton = new VizButton(0, PLOT_BUTTON_Y0, BUTTON_W, BUTTON_H, this);
	plotButton.setText("Plot");
	buttons.add(plotButton);

	for (VizButton b : buttons) {
	    b.setStyle(MEDIUM_GRAY, WHITE, DARK_WHITE, 255, 255, 14);
	    b.setStyleDisabled(MEDIUM_GRAY, WHITE, DARK_WHITE, 255, 255, 14);
	    b.setRoundedCornerd(5, 5, 5, 5);
	}

	xAxisVar = new VizList(0, BUTTON_X_Y0 - 60, LIST_W, 60, this);
	xAxisVar.setup(LIGHT_GRAY, DARK_GRAY, N_ROWS, getVariablesNames(),
		false, SelectionMode.SINGLE);
	xAxisVar.setVisible(false);
	xAxisVar.setListName("x");
	addTouchSubscriber(xAxisVar);

	yAxisVar = new VizList(0, BUTTON_Y_Y0 + BUTTON_H, LIST_W, 60, this);
	yAxisVar.setup(LIGHT_GRAY, DARK_GRAY, N_ROWS, getVariablesNames(),
		false, SelectionMode.SINGLE);
	yAxisVar.setVisible(false);
	yAxisVar.setListName("y");
	addTouchSubscriber(yAxisVar);

	yearList = new VizList(0, YEAR_BUTTON_Y0 - 100, LIST_W, 100, this);
	yearList.setup(LIGHT_GRAY, DARK_GRAY, 6, getYears(), false,
		SelectionMode.SINGLE);
	yearList.setVisible(false);
	yearList.setListName("year");
	addTouchSubscriber(yearList);

    }

    private ArrayList<Integer> getYears() {
	ArrayList<Integer> years = new ArrayList<Integer>();
	for (int i = 2001; i <= 2010; i++) {
	    years.add(i);
	}

	return years;
    }

    public void setDots(ArrayList<PVector> dots) {
	this.dots = dots;

	maxX = Float.MIN_VALUE;
	maxY = Float.MIN_VALUE;

	for (PVector p : dots) {
	    if (p.x > maxX) {
		maxX = p.x;
	    }
	    if (p.y > maxY) {
		maxY = p.y;
	    }
	}

	maxX = (float) (maxX * 1.05);
	maxY = (float) (maxY * 1.05);
    }

    private ArrayList<String> getVariablesNames() {
	ArrayList<String> names = new ArrayList<String>();
	names.add("Alcohol");
	names.add("MaxSpeed");
	names.add("Age");
	names.add("Hour");
	return names;
    }

    @Override
    public boolean draw() {

	pushStyle();

	fill(LIGHT_GRAY);
	stroke(DARK_WHITE);
	strokeWeight(1);
	rect(BUTTON_W / 2, 0, getWidth() - BUTTON_W / 2, getHeight(), 5, 5, 5,
		5);

	if (dots != null) {
	    renderAxisLabels();
	    drawVolumeLabels();
	}

	for (VizButton b : buttons) {
	    b.draw();
	    b.drawTextCentered();
	}

	xAxisVar.draw();
	xAxisVar.setToRedraw();
	yAxisVar.draw();
	yAxisVar.setToRedraw();
	yearList.draw();
	yearList.setToRedraw();

	stroke(DARK_WHITE);
	strokeWeight(0.5f);

	line(PADDING_RIGHT, getHeight() - PADDING_BOTTOM, getWidth()
		- PADDING_LEFT, getHeight() - PADDING_BOTTOM);
	line(PADDING_RIGHT, 0 + PADDING_TOP, PADDING_RIGHT, getHeight()
		- PADDING_BOTTOM);

	noStroke();
	fill(DOT_COLOR_SCATTER, 120);

	if (dots != null) {
	    for (PVector p : dots) {
		float x = PApplet.map(p.x, 0, maxX, PADDING_RIGHT, getWidth()
			- PADDING_LEFT);
		float y = PApplet.map(p.y, 0, maxY, getHeight()
			- PADDING_BOTTOM, PADDING_TOP);
		ellipse(x, y, DOT_SIZE_SCATTER, DOT_SIZE_SCATTER);
	    }
	}

	popStyle();
	return false;
    }

    // Axis Labels
    private void renderAxisLabels() {

	pushStyle();

	textSize(AXIS_LABEL_SIZE_SCATTER);
	fill(MyColorEnum.WHITE);
	textAlign(PApplet.LEFT, PApplet.CENTER);
	text(xLabel, getWidth() - PADDING_LEFT, getHeight() - PADDING_BOTTOM);
	textAlign(PApplet.CENTER, PApplet.BOTTOM);
	text(yLabel, PADDING_RIGHT, PADDING_TOP);

	popStyle();
    }

    private void drawVolumeLabels() {

	pushStyle();
	fill(VOL_LABEL_COLOR_SCATTER);
	textSize(VOL_LABEL_SIZE_SCATTER);
	stroke(VOL_TICK_COLOR_SCATTER);
	strokeWeight(VOL_TICK_WEIGHT_SCATTER);

	// X axis
	textAlign(PApplet.CENTER, PApplet.TOP);

	int increase = (int) getXTicksRangeForSinglePlot(NOFTICKS, dots);
	for (float v = 0; v < maxX; v += increase) {
	    float x = PApplet.map(v, 0, maxX, PADDING_RIGHT, getWidth()
		    - PADDING_LEFT);
	    text(PApplet.round(v) + "", x, getHeight() + 3 - PADDING_BOTTOM);
	}

	// Y axis
	textAlign(PApplet.RIGHT, PApplet.CENTER);

	increase = (int) getYTicksRangeForSinglePlot(NOFTICKS, dots);

	for (float v = 0; v < maxY; v += increase) {
	    float y = PApplet.map(v, 0, maxY, getHeight() - PADDING_BOTTOM,
		    PADDING_TOP);
	    text(PApplet.round(v) + "", PADDING_RIGHT - 5, y);
	}

	popStyle();
    }

    private void disableOtherButtons(VizButton button, boolean disable) {
	for (VizButton b : buttons) {
	    if (!b.getText().equals(button.getText())) {
		b.setDisabled(disable);
	    }
	}
    }

    @Override
    public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
	if (isVisible()) {
	    if (down) {

		if (xAxisButton.containsPoint(x, y)
			&& !xAxisButton.isDisabled()) {
		    xAxisVar.toggleVisible();
		    if (!xAxisVar.isVisible()) {
			xLabel = !xAxisVar.getSelected().isEmpty() ? (String) xAxisVar
				.getSelected().get(0) : "x Axis";
			disableOtherButtons(xAxisButton, false);
		    } else {
			disableOtherButtons(xAxisButton, true);
		    }
		}

		if (yAxisButton.containsPoint(x, y)
			&& !yAxisButton.isDisabled()) {
		    yAxisVar.toggleVisible();
		    if (!yAxisVar.isVisible()) {
			yLabel = !yAxisVar.getSelected().isEmpty() ? (String) yAxisVar
				.getSelected().get(0) : "y Axis";
			disableOtherButtons(yAxisButton, false);
		    } else {
			disableOtherButtons(yAxisButton, true);
		    }
		}

		if (yearButton.containsPoint(x, y) && !yearButton.isDisabled()) {
		    yearList.toggleVisible();
		    if (!yearList.isVisible()) {
			currentYear = (Integer) (!yearList.getSelected()
				.isEmpty() ? yearList.getSelected().get(0)
				: currentYear);
			disableOtherButtons(yearButton, false);
		    } else {
			disableOtherButtons(yearButton, true);
		    }

		}

		if (plotButton.containsPoint(x, y) && !plotButton.isDisabled()) {
		    String xAxis = (String) xAxisVar.getSelected().get(0);
		    String yAxis = (String) yAxisVar.getSelected().get(0);
		    setDots(DBUtil.getInstance().getScatterData(xAxis, yAxis,
			    currentYear, currentState.getId()));
		}
	    }
	    propagateTouch(x, y, down, touchType);
	}
	return false;
    }

    @Override
    public void eventReceived(String eventName, Object data) {
	if (eventName.equals("year-changed-timeslider")) {
	    currentYear = (Integer) data;
	    log("Year changed received, current year = " + currentYear);
	} else if (eventName.equals("state-changed")) {
	    currentState = (StateInfo) data;
	    log("State changed received, current state = "
		    + currentState.getName());
	}
    }
}