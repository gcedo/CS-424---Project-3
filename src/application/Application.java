package application;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.FilterToolbox;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizGraph;
import com.anotherbrick.inthewall.VizMap;
import com.anotherbrick.inthewall.VizPanel;
import com.anotherbrick.inthewall.VizTimeline;

public class Application extends VizPanel implements TouchEnabled {

  private VizMap map;
  private final float MAP_WIDTH = 537;
  private final float MAP_HEIGHT = 384;
  private final float MAP_X0 = 0;
  private final float MAP_Y0 = 0;

  private VizGraph graph;
  private final float GRAPH_WIDTH = 476;
  private final float GRAPH_HEIGHT = 270;
  private final float GRAPH_X0 = 861;
  private final float GRAPH_Y0 = 21;

  private FilterToolbox ft;
  private final float FT_WIDTH = 281;
  private final float FT_HEIGHT = 340;
  private final float FT_X0 = 560;
  private final float FT_Y0 = 21;

  public Application(float x0, float y0, float width, float height) {
    super(x0, y0, width, height);
    // TODO Auto-generated constructor stub
  }

  @Override
  public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
    return propagateTouch(x, y, down, touchType);
  }

  @Override
  public void setup() {
    map = new VizMap(MAP_X0, MAP_Y0, MAP_WIDTH, MAP_HEIGHT, this);
    map.setup();
    // map.addLocations(DBUtil.getInstance().getPoints(map.ILLINOIS));
    addTouchSubscriber(map);

    ft = new FilterToolbox(FT_X0, FT_Y0, FT_WIDTH, FT_HEIGHT, this);
    ft.setup();
    addTouchSubscriber(ft);

    graph = new VizGraph(GRAPH_X0, GRAPH_Y0, GRAPH_WIDTH, GRAPH_HEIGHT, this);
    graph.setup();
  }

  @Override
  public boolean draw() {
    pushStyle();
    background(MyColorEnum.DARK_GRAY);

    map.draw();
    coverExceedingTiles();

    ft.draw();
    graph.draw();

    popStyle();
    return false;
  }

  private void coverExceedingTiles() {
    pushStyle();
    noStroke();
    fill(MyColorEnum.DARK_GRAY);
    rect(MAP_X0 + MAP_WIDTH, 0, getWidth() - MAP_WIDTH, getHeight());
    popStyle();
  }
}
