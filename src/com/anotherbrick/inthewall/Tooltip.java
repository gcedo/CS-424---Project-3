package com.anotherbrick.inthewall;

import application.DBUtil;

import com.anotherbrick.inthewall.Config.MyColorEnum;

import processing.core.PApplet;
import processing.core.PShape;

public class Tooltip extends VizPanel {

  private PShape background;
  private Integer id;
  private LocationWrapper wrapper;
  private CrashDetails cd;

  public Tooltip(float x0, float y0, float width, float height, VizPanel parent, Integer id) {
    super(x0, y0, width, height, parent);
    this.id = id;
    cd = DBUtil.getInstance().getCrashDetailsById(id);
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

    textSize(8);
    textAlign(PApplet.LEFT, PApplet.TOP);
    fill(MyColorEnum.DARK_GRAY);
    text(cd.toString(), 15, 5);

    popStyle();
    return false;
  }
}
