package StatesMap;

import processing.core.PVector;
import application.DBUtil;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.NotificationCenter;
import com.anotherbrick.inthewall.StateInfo;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizButton;
import com.anotherbrick.inthewall.VizPanel;

class State extends VizPanel implements TouchEnabled {
  int id;
  String name;
  String acName;

  PVector cen;
  float len;
  float scale;
  VizButton v;

  State(String _id, String _name, float xc, float yc, float len, String _acName, float scale,
      VizPanel parent) {
    super(scale * (xc - len / 4) + 1, scale * (yc - len / 4) + 1, scale * len / 2, scale * len / 2,
        parent);

    id = Integer.parseInt(_id);
    name = _name;
    acName = _acName;
    cen = new PVector();
    cen.x = scale * len / 2;
    cen.y = scale * len / 2;
    this.scale = scale;
  }

  @Override
  public void setup() {
    v = new VizButton(0, 0, getWidth(), getHeight(), this);
    v.setStyle(MyColorEnum.BLACK, MyColorEnum.DARK_BLUE, MyColorEnum.DARK_WHITE, 255, 255, 6);
    v.setStyleSelected(MyColorEnum.BLACK, MyColorEnum.LIGHT_BLUE, MyColorEnum.WHITE, 255, 255, 6);
    v.setText(acName);

  }

  @Override
  public boolean draw() {
    v.draw();
    v.drawTextCentered();
    return false;
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    if (down && v.containsPoint(x, y)) {
      System.out.println("touched " + name);
      StateInfo state = DBUtil.getInstance().getStateCenter(name);

      NotificationCenter.getInstance().notifyEvent("state-changed", state);
    }
    return false;
  }

  public void setSelected(boolean selected) {
    v.setSelected(selected);

  }
}
