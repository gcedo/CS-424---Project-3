package StatesMap;

import processing.core.PApplet;
import processing.core.PVector;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizPanel;

public class StatesMap extends VizPanel implements TouchEnabled {
  int num;
  State[] states;

  float scale;

  PVector offset;

  String[] lines;
  String[][] pieces;

  public StatesMap(float x0, float y0, float width, float height, VizPanel parent) {
    super(x0, y0, width, height, parent);
    String[] rows = m.p.loadStrings("data/states.txt");

    num = rows.length;

    states = new State[num];

    for (int i = 0; i < num; i++) {
      String[] pieces = PApplet.split(rows[i], PApplet.TAB);
      float lon = Float.parseFloat(pieces[2]);
      float lat = Float.parseFloat(pieces[3]);
      float len = PApplet.sqrt(Float.parseFloat(pieces[4]));
      states[i] = new State(pieces[0], pieces[1], lon, lat, len, pieces[5], (float) 0.39, this);
      states[i].setup();
      addTouchSubscriber(states[i]);
    }

    lines = new String[num];
    pieces = new String[num][6];
  }

  int getNum() {
    return num;
  }

  @Override
  public void setup() {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean draw() {
    pushStyle();

    for (int i = 0; i < num; i++) {
      states[i].draw();
    }
    popStyle();
    return false;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    if (down) {
      for (int i = 0; i < num; i++) {
        states[i].setSelected(false);
        if (states[i].containsPoint(x, y)) {
          states[i].setSelected(true);
        }
      }
    }
    propagateTouch(x, y, down, touchType);
    return false;
  }
}
