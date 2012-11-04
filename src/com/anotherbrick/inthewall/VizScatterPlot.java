package com.anotherbrick.inthewall;

import static com.anotherbrick.inthewall.Config.MyColorEnum.*;
import static com.anotherbrick.inthewall.Helper.*;

import java.util.ArrayList;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.VizList.SelectionMode;

import processing.core.PApplet;
import processing.core.PVector;

public class VizScatterPlot extends VizPanel implements TouchEnabled {

  private static final float BUTTON_W = 40;
  private static final float BUTTON_H = 20;
  private static final float BUTTON_X_Y0 = 140;
  private static final float BUTTON_Y_Y0 = 165;

  private static final MyColorEnum AXIS_COLOR_SCATTER = MyColorEnum.RED;
  private static final float AXIS_WEIGHT_SCATTER = 4;
  private static final MyColorEnum DOT_COLOR_SCATTER = MyColorEnum.LIGHT_BLUE;
  private static final float DOT_SIZE_SCATTER = 10;
  private static final float AXIS_LABEL_SIZE_SCATTER = 10;
  private static final MyColorEnum AXIS_LABEL_COLOR_SCATTER = MyColorEnum.BLACK;
  private static final float VOL_LABEL_SIZE_SCATTER = 10;
  private static final MyColorEnum VOL_TICK_COLOR_SCATTER = MyColorEnum.LIGHT_GREEN;
  private static final float VOL_TICK_WEIGHT_SCATTER = 1;

  private static final float PADDING_RIGHT = 100;
  private static final int N_ROWS = 2;
  private static final float LIST_W = 100;
  private static int NOFTICKS = 10;

  ArrayList<PVector> dots;
  int num;
  float maxX;
  float maxY;
  private String xLabel = "x label";
  private String yLabel = "y label";

  private VizList xAxisVar, yAxisVar;
  private VizButton xAxisButton, yAxisButton;

  private MyColorEnum VOL_LABEL_COLOR_SCATTER = MyColorEnum.LIGHT_BLUE;

  public VizScatterPlot(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);

    num = 0;

    maxX = -Float.MAX_VALUE;
    maxY = -Float.MAX_VALUE;
  }

  @Override
  public void setup() {
    xAxisButton = new VizButton(0, BUTTON_X_Y0, BUTTON_W, BUTTON_H, this);
    xAxisButton.setStyle(MEDIUM_GRAY, WHITE, LIGHT_GRAY, 255, 255, 14);
    xAxisButton.setText("X");

    yAxisButton = new VizButton(0, BUTTON_Y_Y0, BUTTON_W, BUTTON_H, this);
    yAxisButton.setStyle(MEDIUM_GRAY, WHITE, LIGHT_GRAY, 255, 255, 14);
    yAxisButton.setText("Y");

    xAxisVar = new VizList(0, BUTTON_X_Y0 - 60, LIST_W, 60, this);
    xAxisVar.setup(LIGHT_GRAY, DARK_GRAY, N_ROWS, getVariablesNames(), false, SelectionMode.SINGLE);
    xAxisVar.setVisible(false);
    addTouchSubscriber(xAxisVar);

    yAxisVar = new VizList(0, BUTTON_Y_Y0 + BUTTON_H, LIST_W, 60, this);
    yAxisVar.setup(LIGHT_GRAY, DARK_GRAY, N_ROWS, getVariablesNames(), false, SelectionMode.SINGLE);
    yAxisVar.setVisible(false);
    addTouchSubscriber(yAxisVar);

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

    maxX = (float) (maxX * 1.05);
    maxY = (float) (maxY * 1.05);
    num = dots.size();
    this.dots = dots;
  }

  private ArrayList<String> getVariablesNames() {
    ArrayList<String> names = new ArrayList<String>();
    names.add("Alcohol");
    names.add("Speed");
    names.add("Age");
    names.add("Hour");
    return names;
  }

  @Override
  public boolean draw() {

    pushStyle();

    background(MyColorEnum.LIGHT_ORANGE);

    xAxisButton.draw();
    xAxisButton.drawTextCentered();
    yAxisButton.draw();
    yAxisButton.drawTextCentered();

    xAxisVar.draw();
    xAxisVar.setToRedraw();
    yAxisVar.draw();
    yAxisVar.setToRedraw();

    renderAxisLabels();
    drawVolumeLabels();

    stroke(AXIS_COLOR_SCATTER);
    strokeWeight(AXIS_WEIGHT_SCATTER);

    line(PADDING_RIGHT, getHeight(), getWidth(), getHeight());
    line(PADDING_RIGHT, 0, PADDING_RIGHT, getHeight());

    noStroke();
    fill(DOT_COLOR_SCATTER);

    if (dots != null) {
      for (PVector p : dots) {
        float x = PApplet.map(p.x, PADDING_RIGHT, maxX, 0, getWidth());
        float y = PApplet.map(p.y, 0, maxY, getHeight(), 0);
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
    text(xLabel, getWidth(), getHeight());
    textAlign(PApplet.CENTER, PApplet.BOTTOM);
    text(yLabel, PADDING_RIGHT, 0);

    popStyle();
  }

  // Volume Labels
  private void drawVolumeLabels() {

    pushStyle();
    fill(VOL_LABEL_COLOR_SCATTER);
    textSize(VOL_LABEL_SIZE_SCATTER);
    stroke(VOL_TICK_COLOR_SCATTER);
    strokeWeight(VOL_TICK_WEIGHT_SCATTER);

    textAlign(PApplet.CENTER, PApplet.TOP);

    int increase = (int) getXTicksRangeForSinglePlot(NOFTICKS, dots);

    for (float v = 0; v < maxX; v += increase) {
      float x = PApplet.map(v, 0, maxX, PADDING_RIGHT, getWidth());
      text(PApplet.round(v) + "", x, getHeight() + 3);
    }

    // y axis

    textAlign(PApplet.RIGHT, PApplet.CENTER);

    increase = (int) getYTicksRangeForSinglePlot(NOFTICKS, dots);
    for (float v = 0; v < maxY; v += increase) {
      float y = PApplet.map(v, 0, maxY, getHeight(), 0);
      text(PApplet.round(v) + "", PADDING_RIGHT - 5, y);
    }

    popStyle();
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    if (down) {

      log("touchdown");

      if (xAxisButton.containsPoint(x, y)) {
        xAxisVar.toggleVisible();
      }

      if (yAxisButton.containsPoint(x, y)) {
        yAxisVar.toggleVisible();
      }
    }
    propagateTouch(x, y, down, touchType);
    return false;
  }

}