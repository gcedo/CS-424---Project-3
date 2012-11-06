package com.anotherbrick.inthewall;

import processing.core.PApplet;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import static com.anotherbrick.inthewall.Helper.*;

public class Legend extends VizPanel implements EventSubscriber {

  private String filter1 = "sdfghjklwertyuikolsxdcfvgbhnjmksxdcfvgbhnmsdxcfvgbhndcfvgbh";
  private String filter2 = "Graph 2";

  public Legend(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
    NotificationCenter.getInstance().registerToEvent("filter0-update", this);
    NotificationCenter.getInstance().registerToEvent("filter1-update", this);
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

  @Override
  public void eventReceived(String eventName, Object data) {
    if (eventName.equals("filter0-update")) {
      filter1 = new String(limitStringLength(data.toString(), 60, true));
      log("String = " + data.toString());
    } else if (eventName.equals("filter1-update")) {
      filter2 = new String(limitStringLength(data.toString(), 60, true));
    }

  }

}
