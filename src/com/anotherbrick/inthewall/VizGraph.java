package com.anotherbrick.inthewall;

import static com.anotherbrick.inthewall.Helper.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

import com.anotherbrick.inthewall.Config.MyColorEnum;

public class VizGraph extends VizPanel implements TouchEnabled, EventSubscriber {

  public int TICK_COUNT = 10;
  public int CLUSTER_SIZE = 10;
  public float PLOT_PADDING_LEFT = 40;
  public float PLOT_PADDING_BOTTOM = 40;
  public float SLIDER_HEIGHT;
  public float SLIDER_WIDTH = 22;
  public float HALF_SLIDER = 11;
  private float xStart = 0, xStop = 0;
  private ArrayList<PlotData> plots;
  private ArrayList<PlotData> clusteredPlots;
  private YearSlider yearSlider;
  private boolean clustered;
  private int selector;

  public ArrayList<PlotData> getClusteredPlots() {
    return clusteredPlots;
  }

  public boolean isClustered() {
    return clustered;
  }

  public void setClustered(boolean clustered) {
    this.clustered = clustered;
    updateBounds();
  }

  public MyColorEnum[] palette = { MyColorEnum.GRAPH_COLOR_1, MyColorEnum.GRAPH_COLOR_2,
      MyColorEnum.GRAPH_COLOR_3, MyColorEnum.GRAPH_COLOR_4 };

  public VizGraph(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
    SLIDER_HEIGHT = height - PLOT_PADDING_BOTTOM;
    clustered = false;
    setPlots(new ArrayList<PlotData>());
    yearSlider = new YearSlider(PLOT_PADDING_LEFT - HALF_SLIDER, 0, SLIDER_HEIGHT, this);
    this.clusteredPlots = new ArrayList<PlotData>();
    if (plots.isEmpty()) {
      plots.ensureCapacity(4);
      while (plots.size() < 4) {
        plots.add(null);
      }
    }
    if (clusteredPlots.isEmpty()) {
      clusteredPlots.ensureCapacity(4);
      while (clusteredPlots.size() < 4) {
        clusteredPlots.add(null);
      }
    }
  }

  public void setup() {
    NotificationCenter.getInstance().registerToEvent("update-graph", this);
    NotificationCenter.getInstance().registerToEvent("update-selector", this);
    NotificationCenter.getInstance().registerToEvent("remove-graph", this);
    updateBounds();
  }

  private void addClusteredPlot(PlotData plot, int index) {

    PlotData clusteredPlot = calculateClusteredPlot(plot, CLUSTER_SIZE);
    clusteredPlot.setColor(palette[index]);
    clusteredPlot.setFilled(true);

    try {
      clusteredPlots.set(index, clusteredPlot);
    } catch (IndexOutOfBoundsException e) {
      clusteredPlots.ensureCapacity(index + 1);
      while (clusteredPlots.size() < index + 1) {
        clusteredPlots.add(null);
      }
      clusteredPlots.set(index, clusteredPlot);
    }

  }

  private void updateBounds() {
    if (clustered) {
      xStop = getOverallXMax(clusteredPlots);
      xStart = getOverallXMin(clusteredPlots);
    } else {
      xStart = getOverallXMin(getPlots());
      xStop = getOverallXMax(getPlots());
    }
  }

  private void sortPlots() {
    ArrayList<PVector> plots = new ArrayList<PVector>();
    for (PlotData p : clusteredPlots) {
      if (p != null) {
        plots.add(new PVector(clusteredPlots.indexOf(p), p.getYMax()));
      }
    }

    Collections.sort(plots, new Comparator<PVector>() {

      @Override
      public int compare(PVector p1, PVector p2) {
        return (int) (p1.y - p2.y);
      }
    });

  }

  public void addPlot(PlotData plot, int index) {
    plots.set(index, plot);
    addClusteredPlot(plot, index);
    updateBounds();
    sortPlots();
    setToRedraw();
  }

  private static PlotData calculateClusteredPlot(PlotData plot, int step) {
    float sum = 0;
    ArrayList<PVector> points = new ArrayList<PVector>();
    for (int i = 0; i < plot.getPoints().size(); i += step) {
      sum = 0;
      for (int j = i; j < step + i && j < plot.getPoints().size(); j++) {
        sum += plot.getPoints().get(j).y;
      }
      points.add(new PVector(plot.getPoints().get(i).x, sum));
    }

    PlotData clusteredPlot = new PlotData(points, plot.getColor());
    return clusteredPlot;
  }

  public void removePlot(PlotData plot) {
    if (getPlots().contains(plot)) {
      getPlots().remove(plot);
    }
  }

  public void removePlotAtIndex(int index) {
    getPlots().set(index, null);
  }

  public int getNoOfActivePlots() {
    int sum = 0;
    for (PlotData p : plots) {
      if (p != null) {
        sum++;
      }
    }
    return sum;
  }

  @Override
  public float getX0() {
    return xStart;
  }

  public void setX0(float x0) {
    this.xStart = x0;
  }

  public float getXn() {
    return xStop;
  }

  public void setXn(float xn) {
    this.xStop = xn;
  }

  @Override
  public boolean draw() {
    // if (!startDraw())
    // return false;
    pushStyle();
    fill(MyColorEnum.DARK_GRAY);
    rect(getX0(), getY0(), getWidth(), getHeight());
    textSize(20);
    drawBackground();

    ArrayList<PlotData> drawPlots = new ArrayList<PlotData>();
    for (PlotData p : getPlots()) {
      if (p != null) {
        drawPlots.add(p);
      }
    }

    Collections.sort(drawPlots, new Comparator<PlotData>() {
      @Override
      public int compare(PlotData p1, PlotData p2) {
        return (int) (p2.getYPointsSum() - p1.getYPointsSum());
      }
    });

    for (PlotData cluster : drawPlots) {
      drawPlot(cluster, drawPlots);
    }

    forceYearSliderUpdate();
    yearSlider.draw();
    popStyle();

    drawAxisLabels();
    return false;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void eventReceived(String eventName, Object data) {
    if (eventName.equals("update-graph")) {
      PlotData pdata = new PlotData((ArrayList<PVector>) data, palette[selector]);
      addPlot(pdata, selector);
    } else if (eventName.equals("update-selector")) {
      selector = (Integer) data;
      log("Selector = " + selector);
    } else if (eventName.equals("remove-graph")) {
      removePlotAtIndex(selector);
    }

  }

  private void drawPlot(PlotData plot, ArrayList<PlotData> plots) {
    if (plot != null) {

      ArrayList<PVector> points = plot.getPoints();
      pushStyle();
      stroke(plot.getColor());
      strokeWeight(plot.getWeight());
      fill(plot.getColor(), plot.getAlpha());

      float histogramOffset = ((getWidth() - PLOT_PADDING_LEFT) / points.size())
          / (plots.indexOf(plot) * (float) 0.25 + 1);

      Object[] p = points.toArray();

      beginShape();
      for (int i = (int) xStart, j = 0; i <= xStop && j < points.size(); i++, j++) {
        System.out.println(i + ": y = " + ((PVector) p[i - (int) plot.getXMin()]).y);
        float x = PApplet.map(((PVector) p[i - (int) plot.getXMin()]).x, xStart, xStop,
            PLOT_PADDING_LEFT, getWidth());
        float y = PApplet.map(((PVector) p[i - (int) plot.getXMin()]).y, 0, getOverallYMax(plots),
            getHeight() - PLOT_PADDING_BOTTOM, 0);
        if (!clustered) {
          vertex(x, y);
        } else if (j != points.size() - 1) {
          Float year = ((PVector) p[i - (int) plot.getXMin()]).x;
          pushStyle();
          fill(MyColorEnum.WHITE);
          textAlign(PApplet.LEFT, PApplet.TOP);
          text(Integer.toString(year.intValue()), x, getHeight() - PLOT_PADDING_BOTTOM);
          popStyle();
          vertex(x, getHeight() - PLOT_PADDING_BOTTOM);
          vertex(x, y - PLOT_PADDING_BOTTOM);
          vertex(x + histogramOffset, y - PLOT_PADDING_BOTTOM);
          vertex(x + histogramOffset, getHeight() - PLOT_PADDING_BOTTOM);

        }
      }

      if (plot.isFilled()) {
        vertex(getWidth(), getHeight() - PLOT_PADDING_BOTTOM);
        vertex(PLOT_PADDING_LEFT, getHeight() - PLOT_PADDING_BOTTOM);
        endShape(PApplet.CLOSE);
      } else {
        endShape();
      }
      popStyle();
    }
  }

  private void drawBackground() {
    pushStyle();
    noStroke();
    background(MyColorEnum.DARK_GRAY);
    fill(MyColorEnum.MEDIUM_GRAY);
    rect(PLOT_PADDING_LEFT, 0, getWidth() - PLOT_PADDING_LEFT, getHeight() - PLOT_PADDING_BOTTOM);
    popStyle();
  }

  private void drawAxisLabels() {
    drawXAxisLabels();
    drawYAxisLabels();
  }

  private void drawXAxisLabels() {
    pushStyle();
    fill(MyColorEnum.WHITE);
    stroke(MyColorEnum.WHITE);
    strokeWeight((float) 1);
    textAlign(PApplet.CENTER, PApplet.TOP);
    textSize(12);
    for (int i = (int) xStart; i <= xStop; i++) {
      int x = (int) PApplet.map(i, xStart, xStop, PLOT_PADDING_LEFT, getWidth());
      text(Integer.toString(i), x, getHeight() - PLOT_PADDING_BOTTOM);
    }
    popStyle();

  }

  private void drawYAxisLabels() {
    int range;
    range = (int) getYTicksRange(TICK_COUNT, plots);

    range = range == 0 ? 1 : range;

    pushStyle();
    stroke(MyColorEnum.WHITE);
    strokeWeight((float) 0.5);
    fill(MyColorEnum.WHITE);
    textAlign(PApplet.RIGHT, PApplet.CENTER);
    for (int i = 0; i < getOverallYMax(plots); i += range) {
      int y = (int) PApplet.map(i, 0, getOverallYMax(plots), getHeight() - PLOT_PADDING_BOTTOM, 0);

      text(Integer.toString(i), PLOT_PADDING_LEFT, y);

      line(PLOT_PADDING_LEFT, y, getWidth(), y);
    }
    popStyle();
  }

  public void setYearSliderMoving(boolean moving) {
    yearSlider.moving = moving;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    if (isVisible()) {
      if (down) {
        if (overYearSlider(x, y)) {
          yearSlider.moving = true;
        }
        setModal(true);
        return true;
      } else if (!down) {
        yearSlider.moving = false;
        setModal(false);
      }
    }
    return false;

  }

  private boolean wasMoving = false;

  public void forceYearSliderUpdate() {
    if (yearSlider.isMoving())
      yearSlider.modifyPositionWithAbsoluteValue(
          costrain(m.touchX, getX0Absolute() + getWidth() - HALF_SLIDER, PLOT_PADDING_LEFT
              - HALF_SLIDER + getX0Absolute()), getY0Absolute());
    if (wasMoving && !yearSlider.moving)
      setYear(yearSlider.getX0());
    wasMoving = yearSlider.moving;
  }

  private void setYear(float position) {
    float year = PApplet.map(position + HALF_SLIDER, PLOT_PADDING_LEFT, getWidth(), xStart, xStop);
    NotificationCenter.getInstance().notifyEvent("year-changed", new Integer((int) year));
    log("Notifying set year");
  }

  private float costrain(float value, float maxValue, float minValue) {
    return Math.min(Math.max(value, minValue), maxValue);
  }

  public boolean overYearSlider(float x, float y) {
    return yearSlider.containsPoint(x, y);
  }

  private class YearSlider extends VizPanel {

    PShape s;
    private boolean moving = false;

    public YearSlider(float x0, float y0, float height, VizPanel parent) {
      super(x0, y0, SLIDER_WIDTH, height, parent);
      s = c.getShape("yearSlider2.svg");
    }

    @Override
    public boolean draw() {
      pushStyle();
      shape(s, 0, 0, getWidth(), getHeight());
      popStyle();
      return false;
    }

    public boolean isMoving() {
      return moving;
    }

    public void setMoving(boolean moving) {
      this.moving = moving;
    }

    @Override
    public void setup() {

    }

  }

  public void toggleClustered() {
    setClustered(!clustered);
    updateBounds();
  }

  public ArrayList<PlotData> getPlots() {
    return plots;
  }

  public void setPlots(ArrayList<PlotData> plots) {
    this.plots = plots;
  }

}
