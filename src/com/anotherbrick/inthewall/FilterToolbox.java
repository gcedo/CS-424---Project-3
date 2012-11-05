package com.anotherbrick.inthewall;

import static com.anotherbrick.inthewall.Config.MyColorEnum.*;
import static com.anotherbrick.inthewall.ListsFiller.*;

import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;

import application.DBUtil;
import application.FilterWrapper;

import com.anotherbrick.inthewall.VizList.SelectionMode;
import StatesMap.StatesMap;

public class FilterToolbox extends VizPanel implements TouchEnabled, EventSubscriber {
  public FilterToolbox(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
  }

  public static float LIST_W = 110;
  public static float LIST_H = 150;
  public static float BUTTON_H = 22;
  public static int N_ROWS = 7;
  public static float FIRST_COL_X0 = 30;
  public static float SECOND_COL_X0 = 155;

  public static final float FIRST_ROW_Y0 = 80;
  public static final float SECOND_ROW_Y0 = 110;
  public static final float THIRD_ROW_Y0 = 140;
  public static final float FOURTH_ROW_Y0 = 170;
  private static final float FIFTH_ROW_Y0 = 200;
  private static final float APPLY_X0 = 233;
  private static final float APPLY_Y0 = 268;
  private static final float APPLY_W = 41;
  private static final float APPLY_H = 31;
  private static final float REMOVE_X0 = 240;
  private static final float REMOVE_Y0 = 306;
  private static final float REMOVE_W = 30;
  private static final float REMOVE_Y = 30;
  private static final float TABBUTTON_W = 20;
  private static final float TABBUTTON_H = 36;

  private static final float STATES_MAP_X0 = 20;
  private static final float STATES_MAP_Y0 = 225;
  private static final float STATES_MAP_WIDTH = 230;
  private static final float STATES_MAP_HEIGHT = 200;

  private VizList weatherList, alcoholList, speedList, vehicleList, ageList, dayList, monthList,
      hourList, sexList, lcList;
  private VizButton weatherButton, alcoholButton, speedButton, vehicleButton, ageButton, dayButton,
      monthButton, hourButton, sexButton, lcButton, applyButton, removeButton;

  private VizButton tab0Button, tab1Button;

  private StatesMap statesMap;

  private Integer currentYear = 2001;
  private StateInfo currentState;

  private HashMap<VizButton, VizList> listsAndButtons;
  private FilterWrapper fw;
  private int selector;

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    if (down) {

      if (tab0Button.containsPoint(x, y)) {
        selector = 0;
        NotificationCenter.getInstance().notifyEvent("update-selector", 0);
        tab0Button.setSelected(true);
        tab1Button.setSelected(false);
      }
      if (tab1Button.containsPoint(x, y)) {
        selector = 1;
        NotificationCenter.getInstance().notifyEvent("update-selector", 1);
        tab0Button.setSelected(false);
        tab1Button.setSelected(true);
      }

      if (removeButton.containsPoint(x, y)) {
        NotificationCenter.getInstance().notifyEvent("remove-graph", selector);
      }

      if (applyButton.containsPoint(x, y)) {
        fw.clearFilters(CRASH_ID);
        for (VizList list : listsAndButtons.values()) {
          for (Object filter : list.getSelected()) {
            fw.addFilter(list.getListName(), (String) filter);
          }
        }
        NotificationCenter.getInstance().notifyEvent("update-graph",
            DBUtil.getInstance().getCounts(fw));
      }

      for (Map.Entry<VizButton, VizList> entry : listsAndButtons.entrySet()) {
        VizButton button = entry.getKey();
        VizList list = entry.getValue();
        if (button.containsPoint(x, y) && !button.isDisabled()) {
          list.toggleVisible();
          if (list.isVisible()) {
            disableOtherButtons(button, true);
          } else {
            disableOtherButtons(button, false);
          }
        }
      }
    }

    propagateTouch(x, y, down, touchType);
    return false;
  }

  @Override
  public void setup() {
    NotificationCenter.getInstance().registerToEvent("state-changed", this);
    NotificationCenter.getInstance().registerToEvent("year-changed", this);

    tab0Button = new VizButton(0, 62, TABBUTTON_W, TABBUTTON_H, this);
    tab0Button.setStyle(GRAPH_COLOR_1, WHITE, GRAPH_COLOR_1, 255, 255, 12);
    tab0Button.setStyleSelected(GRAPH_COLOR_1, WHITE, WHITE, 255, 255, 12);
    tab0Button.setRoundedCornerd(3, 3, 3, 3);
    tab0Button.setSelected(true);

    tab1Button = new VizButton(0, 104, TABBUTTON_W, TABBUTTON_H, this);
    tab1Button.setStyle(GRAPH_COLOR_2, WHITE, GRAPH_COLOR_2, 255, 255, 12);
    tab1Button.setStyleSelected(GRAPH_COLOR_2, WHITE, WHITE, 255, 255, 12);
    tab1Button.setRoundedCornerd(3, 3, 3, 3);

    // Filter Wrapper
    fw = new FilterWrapper(CRASH_ID);

    // Apply Button
    applyButton = new VizButton(APPLY_X0, APPLY_Y0, APPLY_W, APPLY_H, this);
    applyButton.setShape("tick.svg");

    // Remove Button
    removeButton = new VizButton(REMOVE_X0, REMOVE_Y0, REMOVE_W, REMOVE_Y, this);
    removeButton.setShape("cross.svg");

    listsAndButtons = new HashMap<VizButton, VizList>();

    // Days of the week
    dayList = new VizList(SECOND_COL_X0, THIRD_ROW_Y0 + BUTTON_H, LIST_W, LIST_H, this);
    dayList.setup(LIGHT_GRAY, DARK_GRAY, N_ROWS, getDaysOfWeek(), false, SelectionMode.MULTIPLE);
    dayList.setListName(DAY_OF_WEEK);

    dayButton = new VizButton(SECOND_COL_X0, THIRD_ROW_Y0, LIST_W, BUTTON_H, this);
    dayButton.setText("Day of Week");
    listsAndButtons.put(dayButton, dayList);

    // Weather Condition
    weatherList = new VizList(FIRST_COL_X0, FIRST_ROW_Y0 + BUTTON_H, 2 * LIST_W + 25, LIST_H, this);
    weatherList.setup(LIGHT_GRAY, DARK_GRAY, N_ROWS, getWeatherConditions(), false,
        SelectionMode.MULTIPLE);
    weatherList.setListName(WEATHER_CONDITION);

    weatherButton = new VizButton(FIRST_COL_X0, FIRST_ROW_Y0, LIST_W, BUTTON_H, this);
    weatherButton.setText("Weather");
    listsAndButtons.put(weatherButton, weatherList);

    // Months
    monthList = new VizList(FIRST_COL_X0, FOURTH_ROW_Y0 + BUTTON_H, LIST_W, LIST_H, this);
    monthList.setup(LIGHT_GRAY, DARK_GRAY, N_ROWS, getMonths(), false, SelectionMode.MULTIPLE);
    monthList.setListName(MONTHS);

    monthButton = new VizButton(FIRST_COL_X0, FOURTH_ROW_Y0, LIST_W, BUTTON_H, this);
    monthButton.setText("Months");
    listsAndButtons.put(monthButton, monthList);

    // Alcohol
    alcoholList = new VizList(SECOND_COL_X0, FIRST_ROW_Y0 + BUTTON_H, LIST_W, LIST_H, this);
    alcoholList.setup(LIGHT_GRAY, DARK_GRAY, N_ROWS, getMonths(), false, SelectionMode.MULTIPLE);
    alcoholList.setListName(ALCOHOL);

    alcoholButton = new VizButton(SECOND_COL_X0, FIRST_ROW_Y0, LIST_W, BUTTON_H, this);
    alcoholButton.setText("Alcohol");
    listsAndButtons.put(alcoholButton, alcoholList);

    // Speed
    speedList = new VizList(FIRST_COL_X0, SECOND_ROW_Y0 + BUTTON_H, LIST_W, LIST_H, this);
    speedList.setup(LIGHT_GRAY, DARK_GRAY, N_ROWS, getSpeeds(), false, SelectionMode.MULTIPLE);
    speedList.setListName(SPEED);

    speedButton = new VizButton(FIRST_COL_X0, SECOND_ROW_Y0, LIST_W, BUTTON_H, this);
    speedButton.setText("Speed");
    listsAndButtons.put(speedButton, speedList);

    // Type of Vehicle
    vehicleList = new VizList(SECOND_COL_X0, SECOND_ROW_Y0 + BUTTON_H, LIST_W + 25, 90, this);
    vehicleList.setup(LIGHT_GRAY, DARK_GRAY, 4, getVehicles(), false, SelectionMode.MULTIPLE);
    vehicleList.setListName(VEHICLE);

    vehicleButton = new VizButton(SECOND_COL_X0, SECOND_ROW_Y0, LIST_W, BUTTON_H, this);
    vehicleButton.setText("Vehicle");
    listsAndButtons.put(vehicleButton, vehicleList);

    // Age
    ageList = new VizList(FIRST_COL_X0, THIRD_ROW_Y0 + BUTTON_H, LIST_W, LIST_H, this);
    ageList.setup(LIGHT_GRAY, DARK_GRAY, N_ROWS, getAge(), false, SelectionMode.MULTIPLE);
    ageList.setListName(AGE);

    ageButton = new VizButton(FIRST_COL_X0, THIRD_ROW_Y0, LIST_W, BUTTON_H, this);
    ageButton.setText("Age");
    listsAndButtons.put(ageButton, ageList);

    // Hour
    hourList = new VizList(SECOND_COL_X0, FOURTH_ROW_Y0 + BUTTON_H, 70, LIST_H, this);
    hourList.setup(LIGHT_GRAY, DARK_GRAY, N_ROWS, getHour(), false, SelectionMode.MULTIPLE);
    hourList.setListName(HOUR);

    hourButton = new VizButton(SECOND_COL_X0, FOURTH_ROW_Y0, LIST_W, BUTTON_H, this);
    hourButton.setText("Hour");
    listsAndButtons.put(hourButton, hourList);

    // Sex
    sexList = new VizList(FIRST_COL_X0, FIFTH_ROW_Y0 + BUTTON_H, LIST_W, 40, this);
    sexList.setup(LIGHT_GRAY, DARK_GRAY, 2, getSex(), false, SelectionMode.MULTIPLE);
    sexList.setListName(SEX);

    sexButton = new VizButton(FIRST_COL_X0, FIFTH_ROW_Y0, LIST_W, BUTTON_H, this);
    sexButton.setText("Sex");
    listsAndButtons.put(sexButton, sexList);

    // Light Condition
    lcList = new VizList(SECOND_COL_X0, FIFTH_ROW_Y0 + BUTTON_H, LIST_W, LIST_H, this);
    lcList
        .setup(LIGHT_GRAY, DARK_GRAY, N_ROWS, getLightConditions(), false, SelectionMode.MULTIPLE);
    lcList.setListName(LIGHT_CONDITION);

    lcButton = new VizButton(SECOND_COL_X0, FIFTH_ROW_Y0, LIST_W, BUTTON_H, this);
    lcButton.setText("Light Cond.");
    listsAndButtons.put(lcButton, lcList);

    for (Map.Entry<VizButton, VizList> entry : listsAndButtons.entrySet()) {
      entry.getKey().setStyle(MEDIUM_GRAY, WHITE, DARK_WHITE, 255, 255, 14);
      entry.getKey().setStyleDisabled(MEDIUM_GRAY, WHITE, DARK_WHITE, 255, 255, 14);
      entry.getKey().setRoundedCornerd(5, 5, 5, 5);
      entry.getValue().setVisible(false);
      addTouchSubscriber(entry.getValue());
    }

    statesMap = new StatesMap(STATES_MAP_X0, STATES_MAP_Y0, STATES_MAP_WIDTH, STATES_MAP_HEIGHT,
        this);
    addTouchSubscriber(statesMap);
  }

  private void disableOtherButtons(VizButton button, boolean disable) {
    for (VizButton b : listsAndButtons.keySet()) {
      if (!b.getText().equals(button.getText())) {
        b.setDisabled(disable);
      }
    }
  }

  private void drawCurrentStatus() {
    pushStyle();
    fill(WHITE);
    textSize(18);
    textAlign(PApplet.LEFT, PApplet.TOP);
    text("Current Year: " + currentYear, 30, 10);
    text("Current State: " + currentState.getName(), 30, 35);
    popStyle();
  }

  @Override
  public boolean draw() {
    pushStyle();
    noStroke();
    background(DARK_GRAY);

    strokeWeight(1);
    stroke(DARK_WHITE);

    fill(LIGHT_GRAY);
    rect(TABBUTTON_W / 2, 0, getWidth() - TABBUTTON_W / 2, getHeight(), 5, 5, 5, 5);

    drawCurrentStatus();

    tab0Button.draw();
    tab1Button.draw();
    statesMap.draw();
    applyButton.draw();
    removeButton.draw();

    for (VizButton button : listsAndButtons.keySet()) {
      button.draw();
      button.drawTextCentered();
    }

    for (VizList list : listsAndButtons.values()) {
      list.draw();
      list.setToRedraw();
    }

    popStyle();
    return true;
  }

  @Override
  public void eventReceived(String eventName, Object data) {
    if (eventName.equals("state-changed")) {
      currentState = (StateInfo) data;
      log("Event received, current state: " + currentState.getName());
    } else if (eventName.equals("year-changed")) {
      currentYear = (Integer) data;
    }

  }

}