package com.anotherbrick.inthewall;

import processing.core.PApplet;

import com.anotherbrick.inthewall.Config.MyColorEnum;

public class HistoricalEvent extends VizPanel {

  private Integer stateId;

  public HistoricalEvent(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  @Override
  public void setup() {
  }

  @Override
  public boolean draw() {
    pushStyle();

    fill(MyColorEnum.LIGHT_GRAY);
    stroke(MyColorEnum.DARK_WHITE);
    rect(0, 0, getWidth(), getHeight(), 5, 5, 5, 5);

    textAlign(PApplet.LEFT, PApplet.TOP);
    textSize(8);
    fill(MyColorEnum.WHITE);

    switch (stateId) {
    case 17:
      text("In 2003 Illinois introduced new laws about\n"
          + "seatbelts. This successfully led to \n" + "a dicrease in the number of crashes.\n"
          + "When the laws are perceived as 'on \n" + " the driver side', trying to enchance \n"
          + "its safety, good results are soon to come.", 5, 5);
      break;
    case 26: // Missisipi
      text("In 2006 the state of Michigan revised its speed\n"
          + "limit formulas. The result was quite good, \n" + "the number of crashes decreased.\n"
          + "If you set speed limits using a good criterion,\n"
          + "rather than just lowering them, you can obtain \n" + "better results", 5, 5);
      break;
    case 28: // Mississipi
      text("April 2004 - December 2008 Gas Price Report\n"
          + "'Higher gas price reduces alcohol-related \n"
          + "crashes, reduces younger better than older,\n"
          + "reduces overall frequency and rate of crashes'\n"
          + "Using the interface we saw how the car accidents\n"
          + "statistics were strongly related to the gas price.", 5, 5);
      break;
    case 45:
      text("In 2009 South Carolina introduces new DUI laws,\n"
          + "more strict about the alcohol limits. Using the\n"
          + "interface we saw how alcohol is strictly related\n"
          + "to the number of car accidents.", 5, 5);
      break;
    case 51:
      text("In 2004 Virginia gets more severe about speed \n"
          + "limits. But as you can see from the graph,\n"
          + "the number of car accidents has not decreased,\n"
          + "actually it has increased. Using the interface \n"
          + "we saw that being more severe about speed limits\n"
          + "tends to worsen the statistics.", 5, 5);
      break;
    default:
      text("No historical event available", 5, 5);
      break;
    }

    popStyle();
    return false;
  }

  public void setStateId(Integer id) {
    this.stateId = id;
  }

}
