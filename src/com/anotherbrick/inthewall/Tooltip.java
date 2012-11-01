package com.anotherbrick.inthewall;

import processing.core.PShape;

public class Tooltip extends VizPanel {

  private PShape background;
  private Integer id;
  private LocationWrapper wrapper;

  public Tooltip(float x0, float y0, float width, float height, VizPanel parent, Integer id) {
    super(x0, y0, width, height, parent);
    this.id = id;
  }

  @Override
  public void setup() {
    background = c.getShape("tooltip.svg");
  }

  public void setup(LocationWrapper wrapper) {
    setup();
    this.wrapper = wrapper;
  }

  @Override
  public boolean draw() {
    pushStyle();
    noStroke();
    shape(background, 0, 0);
    popStyle();
    return false;
  }
}
