package com.anotherbrick.inthewall;

import com.anotherbrick.inthewall.Config.MyColorEnum;

import processing.core.PApplet;
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

    textSize(14);
    textAlign(PApplet.LEFT, PApplet.CENTER);
    fill(MyColorEnum.DARK_GRAY);
    text(Integer.toString(wrapper.getId()), 15, 15);

    popStyle();
    return false;
  }
}
