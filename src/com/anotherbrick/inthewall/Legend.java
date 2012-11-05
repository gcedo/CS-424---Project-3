package com.anotherbrick.inthewall;

import processing.core.PApplet;

import com.anotherbrick.inthewall.Config.MyColorEnum;

public class Legend extends VizPanel {

  private String filter1 = "Graph 1";
  private String filter2 = "Graph 2";

  public Legend(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void setup() {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean draw() {
    pushStyle();

    stroke(MyColorEnum.DARK_WHITE);
    strokeWeight(1);
    fill(MyColorEnum.GRAPH_COLOR_1);
    rect(0, 0, 20, 20, 5, 5, 5, 5);

    fill(MyColorEnum.GRAPH_COLOR_2);
    rect(0, 24, 20, 20, 5, 5, 5, 5);

    drawFiltersText();

    popStyle();
    return false;
  }

  private void drawFiltersText() {
    pushStyle();
    textSize(10);
    fill(MyColorEnum.DARK_WHITE);

    textAlign(PApplet.LEFT, PApplet.CENTER);

    text(filter1, 22, 10);
    text(filter2, 22, 34);
    popStyle();
  }

}
