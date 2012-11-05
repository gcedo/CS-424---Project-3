package application;

import static com.anotherbrick.inthewall.Config.MyColorEnum.GRAPH_COLOR_1;
import static com.anotherbrick.inthewall.Config.MyColorEnum.WHITE;

import java.util.ArrayList;
import java.util.Random;

import processing.core.PVector;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.FilterToolbox;
import com.anotherbrick.inthewall.NotificationCenter;
import com.anotherbrick.inthewall.PlotData;
import com.anotherbrick.inthewall.StateInfo;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizButton;
import com.anotherbrick.inthewall.VizGraph;
import com.anotherbrick.inthewall.VizMap;
import com.anotherbrick.inthewall.VizPanel;
import com.anotherbrick.inthewall.VizScatterPlot;
import com.anotherbrick.inthewall.VizTimeSlider;
import com.modestmaps.geo.Location;

public class Application extends VizPanel implements TouchEnabled {

    private VizMap map;
    private final float MAP_WIDTH = 537;
    private final float MAP_HEIGHT = 384;
    private final float MAP_X0 = 0;
    private final float MAP_Y0 = 0;

    private VizGraph graph;
    private final float GRAPH_WIDTH = 436;
    private final float GRAPH_HEIGHT = 270;
    private final float GRAPH_X0 = 861;
    private final float GRAPH_Y0 = 21;

    private VizScatterPlot sp;
    private final float SP_WIDTH = GRAPH_WIDTH;
    private final float SP_HEIGHT = 250;
    private final float SP_X0 = GRAPH_X0;
    private final float SP_Y0 = GRAPH_Y0;

    private VizTimeSlider timeslider;
    private final float SLIDER_WIDTH = 436;
    private final float SLIDER_HEIGHT = 25;
    private final float SLIDER_X0 = 861;
    private final float SLIDER_Y0 = 290;

    private FilterToolbox ft;
    private final float FT_WIDTH = 281;
    private final float FT_HEIGHT = 363;
    private final float FT_X0 = 560;
    private final float FT_Y0 = 18;

    private VizButton tabButton;
    private final float BUTTON_HEIGHT = 40;
    private final float BUTTON_WIDTH = 60;
    private final float BUTTON_X0 = getWidth() - BUTTON_WIDTH;
    private final float BUTTON_Y0 = getHeight() / 2;

    private enum Mode {
	GRAPH, SCATTER
    }

    private Mode currentMode = Mode.SCATTER;

    public Application(float x0, float y0, float width, float height) {
	super(x0, y0, width, height);
    }

    @Override
    public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
	if (down && tabButton.containsPoint(x, y)) {
	    currentMode = currentMode == Mode.GRAPH ? Mode.SCATTER : Mode.GRAPH;
	}
	propagateTouch(x, y, down, touchType);
	return false;
    }

    @Override
    public void setup() {
	map = new VizMap(MAP_X0, MAP_Y0, MAP_WIDTH, MAP_HEIGHT, this);
	map.setup();
	addTouchSubscriber(map);
	NotificationCenter.getInstance().notifyEvent(
		"state-changed",
		new StateInfo(17, "Illinois", new Location(40.633125f,
			-89.398528f), 6));
	NotificationCenter.getInstance().notifyEvent("year-changed", 2001);

	ft = new FilterToolbox(FT_X0, FT_Y0, FT_WIDTH, FT_HEIGHT, this);
	ft.setup();
	addTouchSubscriber(ft);

	graph = new VizGraph(GRAPH_X0, GRAPH_Y0, GRAPH_WIDTH, GRAPH_HEIGHT,
		this);
	graph.setup();
	addTouchSubscriber(graph);

	sp = new VizScatterPlot(SP_X0, SP_Y0, SP_WIDTH, SP_HEIGHT, this);
	sp.setup();
	addTouchSubscriber(sp);

	timeslider = new VizTimeSlider(SLIDER_X0, SLIDER_Y0, SLIDER_WIDTH,
		SLIDER_HEIGHT, this, graph);
	timeslider.setup();
	addTouchSubscriber(timeslider);

	tabButton = new VizButton(BUTTON_X0, BUTTON_Y0, BUTTON_WIDTH,
		BUTTON_HEIGHT, this);
	tabButton.setStyle(MyColorEnum.MEDIUM_GRAY, WHITE,
		MyColorEnum.MEDIUM_GRAY, 255, 255, 12);
	tabButton.setText("Graph /\n Scatter");
    }

    @Override
    public boolean draw() {
	pushStyle();
	background(MyColorEnum.DARK_GRAY);

	map.draw();
	coverExceedingTiles();
	switch (currentMode) {
	case GRAPH:
	    graph.setVisible(true);
	    sp.setVisible(false);
	    graph.draw();
	    break;
	case SCATTER:
	    sp.setVisible(true);
	    graph.setVisible(false);
	    sp.draw();
	    break;
	default:
	    break;
	}

	ft.draw();
	timeslider.draw();
	tabButton.draw();
	tabButton.drawTextCentered();

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

    private void addDummyPlots() {
	Random generator = new Random();
	ArrayList<PVector> points = new ArrayList<PVector>();

	for (int i = 2001; i < 2011; i++) {
	    points.add(new PVector(i, 10 * generator.nextFloat()));
	}

	PlotData plot = new PlotData(points, MyColorEnum.RED);
	plot.setFilled(true);
	graph.addPlot(plot, 0);
	timeslider.addPlot(plot, 0);

	sp.setDots(points);
    }
}
