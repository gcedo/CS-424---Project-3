package com.anotherbrick.inthewall;

import java.util.ArrayList;

import com.anotherbrick.inthewall.Config.MyColorEnum;

import processing.core.PApplet;
import processing.core.PVector;

public class VizScatterPlot extends VizPanel {

    private static final MyColorEnum AXIS_COLOR_SCATTER = null;
    private static final float AXIS_WEIGHT_SCATTER = 0;
    private static final MyColorEnum DOT_COLOR_SCATTER = null;
    private static final float DOT_SIZE_SCATTER = 0;
    private static final float AXIS_LABEL_SIZE_SCATTER = 0;
    private static final MyColorEnum AXIS_LABEL_COLOR_SCATTER = null;
    private static final float VOL_LABEL_SIZE_SCATTER = 0;
    private static final MyColorEnum VOL_TICK_COLOR_SCATTER = null;
    private static final float VOL_TICK_WEIGHT_SCATTER = 0;
    ArrayList<PVector> dots;
    int num;
    float maxX; // max value of X
    float maxY; // max value of Y
    private String xLabel = "";
    private String yLabel = "";

    private MyColorEnum VOL_LABEL_COLOR_SCATTER;

    // OPTIONAL
    // float increase; // for volume labels

    public VizScatterPlot(float x0, float y0, float width, float height,
	    VizPanel parent) {
	super(x0, y0, width, height, parent);

	num = 0;

	maxX = -Float.MAX_VALUE;
	maxY = -Float.MAX_VALUE;
    }

    public void setDots(ArrayList<PVector> dots) {
	this.dots = dots;
	num = dots.size();

	for (PVector p : dots) {
	    if (p.x > maxX) {
		maxX = p.x;
	    }
	    if (p.y > maxY) {
		maxY = p.y;
	    }
	}

	// leave some blank
	maxX = (float) (maxX * 1.05);
	maxY = (float) (maxY * 1.05);
	num = dots.size();
	this.dots = dots;
    }

    @Override
    public boolean draw() {

	this.renderAxisLabels();

	// this.renderVolumeLabels();

	// TODO integrate Processing functions
	pushStyle();

	stroke(AXIS_COLOR_SCATTER);
	strokeWeight(AXIS_WEIGHT_SCATTER);
	line(getX0(), getY0() + getHeight(), getY0() + getWidth(), getY0()
		+ getHeight());
	line(getX0(), getY0(), getX0(), getY0() + getHeight());

	noStroke();
	fill(DOT_COLOR_SCATTER);

	for (PVector p : dots) {
	    float x = PApplet.map(p.x, 0, maxX, getX0(), getX0() + getWidth());
	    float y = PApplet.map(p.y, 0, maxY, getY0() + getHeight(), getY0());
	    ellipse(x, y, DOT_SIZE_SCATTER, DOT_SIZE_SCATTER); // assume all
							       // dots are in
							       // same size
	}

	popStyle();
	return false;
    }

    // Axis Labels
    private void renderAxisLabels() {

	pushStyle();
	textSize(AXIS_LABEL_SIZE_SCATTER);
	fill(AXIS_LABEL_COLOR_SCATTER);
	textAlign(PApplet.LEFT, PApplet.CENTER);
	text(xLabel, getX0() + getWidth() + 3, getY0() + getHeight());
	textAlign(PApplet.CENTER, PApplet.BOTTOM);
	text(yLabel, getX0(), (float) (getY0() + getHeight() * 0.5));
	popStyle();
    }

    // Volume Labels
    private void drawVolumeLabels(int increase, int whichAxis) {

	pushStyle();
	fill(VOL_LABEL_COLOR_SCATTER);
	textSize(VOL_LABEL_SIZE_SCATTER);
	stroke(VOL_TICK_COLOR_SCATTER);
	strokeWeight(VOL_TICK_WEIGHT_SCATTER);

	// x axis
	if (whichAxis == 0) {
	    textAlign(PApplet.CENTER, PApplet.TOP);

	    // TODO change '3' to correct number
	    for (float v = 0; v < maxX; v += increase) {
		float x = PApplet
			.map(v, 0, maxX, getX0(), getX0() + getWidth());
		if (v == 0) {
		} else {
		    text(PApplet.round(v) + "", x, getY0() + getHeight() + 3);
		}
		line(x, getY0() + getHeight(), x, getY0());
	    }
	}
	// y axis
	else if (whichAxis == 1) {
	    textAlign(PApplet.RIGHT, PApplet.CENTER);

	    // TODO change '5' to correct number
	    for (float v = 0; v < maxY; v += increase) {
		float y = PApplet.map(v, 0, maxY, getY0() + getHeight(),
			getY0());
		// float textOffset = parent.textAscent() * 0.5;
		if (v == 0) {
		} else if (v == maxY) {
		    text(PApplet.round(v) + "", getX0() - 5, y /* + textAscent */);
		} else {
		    text(PApplet.round(v) + "", getX0() - 5, y /* + textOffset */);
		}
		line(getX0(), y, getX0() + getWidth(), y);
	    }
	}
	popStyle();
    }

    @Override
    public void setup() {
	// TODO Auto-generated method stub

    }
}