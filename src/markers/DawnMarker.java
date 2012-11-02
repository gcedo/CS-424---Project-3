package markers;

import com.anotherbrick.inthewall.VizPanel;

public class DawnMarker extends AbstractMarker {

  public DawnMarker(float x0, float y0, float width, float height, VizPanel parent, Integer id) {
    super(x0, y0, width, height, parent, id);
  }

  @Override
  public boolean draw() {
    pushStyle();
    shape(shape, 0, 0);
    popStyle();
    return false;
  }

  @Override
  public void setup() {
    shape = c.getShape("dawn.svg");

  }

}
