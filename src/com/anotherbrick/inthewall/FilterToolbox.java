package com.anotherbrick.inthewall;

import static com.anotherbrick.inthewall.Config.MyColorEnum.*;
import static com.anotherbrick.inthewall.ListsFiller.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.anotherbrick.inthewall.VizList.SelectionMode;

public class FilterToolbox extends VizPanel implements TouchEnabled {

  public FilterToolbox(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
    // TODO Auto-generated constructor stub
  }

  public static float LIST_W = 110;
  public static float LIST_H = 150;
  public static float BUTTON_H = 22;
  public static int N_ROWS = 7;
  public static float FIRST_COL_X0 = 30;
  public static float SECOND_COL_X0 = 155;

  public static float DAYS_Y0 = 162;
  public static float WEATHER_Y0 = 80;

  private VizList weatherList, alcoholList, speedList, vehicleList, ageList, dayList, monthList,
      hourList;

  private VizButton weatherButton, alcoholButton, sppedButton, vehicleButton, ageButton, dayButton,
      monthButton, hourButton;

  private ArrayList<VizList> lists;
  private ArrayList<VizButton> buttons;
  private HashMap<VizButton, VizList> listsAndButtons;

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    if (down) {
      for (Map.Entry<VizButton, VizList> entry : listsAndButtons.entrySet()) {
        if (entry.getKey().containsPoint(x, y)) {
          entry.getValue().toggleVisible();
        }
      }
    }

    propagateTouch(x, y, down, touchType);
    return false;
  }

  @Override
  public void setup() {
    lists = new ArrayList<VizList>();
    buttons = new ArrayList<VizButton>();
    listsAndButtons = new HashMap<VizButton, VizList>();

    // Days of the week
    dayList = new VizList(SECOND_COL_X0, DAYS_Y0 + BUTTON_H, LIST_W, LIST_H, this);
    dayList.setup(LIGHT_GRAY, DARK_GRAY, N_ROWS, getDaysOfWeek(), false, SelectionMode.MULTIPLE);

    dayButton = new VizButton(SECOND_COL_X0, DAYS_Y0, LIST_W, BUTTON_H, this);
    dayButton.setName("Day of Week");
    listsAndButtons.put(dayButton, dayList);

    // Weather Condition
    weatherList = new VizList(FIRST_COL_X0, WEATHER_Y0 + BUTTON_H, LIST_W, LIST_H, this);
    weatherList.setup(LIGHT_GRAY, DARK_GRAY, N_ROWS, getWeatherConditions(), false,
        SelectionMode.MULTIPLE);

    weatherButton = new VizButton(FIRST_COL_X0, WEATHER_Y0, LIST_W, BUTTON_H, this);
    weatherButton.setName("Weather");
    listsAndButtons.put(weatherButton, weatherList);

    for (Map.Entry<VizButton, VizList> entry : listsAndButtons.entrySet()) {
      entry.getKey().setStyle(MEDIUM_GRAY, WHITE, LIGHT_GRAY, 255, 255, 14);
      entry.getKey().setRoundedCornerd(5, 5, 5, 5);
      entry.getValue().setVisible(false);
      addTouchSubscriber(entry.getValue());
    }
  }

  @Override
  public boolean draw() {
    pushStyle();
    background(LIGHT_BLUE);

    for (Map.Entry<VizButton, VizList> entry : listsAndButtons.entrySet()) {
      entry.getKey().draw();
      entry.getKey().drawTextCentered();
      entry.getValue().draw();
      entry.getValue().setToRedraw();
    }

    popStyle();
    return true;
  }

}
/*
 * weather, alcohol, speed, type of vehicle, age of driver, day of week, month
 * of year, hour of day,
 */