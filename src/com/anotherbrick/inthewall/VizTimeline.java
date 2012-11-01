package com.anotherbrick.inthewall;

import com.anotherbrick.inthewall.Config.MyColorEnum;

public class VizTimeline extends VizPanel implements TouchEnabled {

    private static final float GRAPH_HEIGHT = 240;
    private static final float GRAPH_WIDTH = 440;
    private static final float GRAPH_Y = 0;
    private static final float GRAPH_X = 0;
    private static final float SLIDER_HEIGHT = 25;
    private static final float SLIDER_WIDTH = 440;
    private static final float SLIDER_Y = 260;
    private static final float SLIDER_X = 0;
    private static final float TIMELINE_WIDTH = 440;
    private VizGraph graph;
    private VizTimeSlider timeSlider;

    public enum Modes {
	GRAPH, TABLE
    };

    public Modes selection = Modes.GRAPH;

    public VizTimeline(float x0, float y0, float width, float height,
	    VizPanel parent) {
	super(x0, y0, width, height, parent);

    }

    public void setup() {
	graph = new VizGraph(GRAPH_X, GRAPH_Y, GRAPH_WIDTH, GRAPH_HEIGHT, this);
	graph.setup();
	addTouchSubscriber(graph);

	timeSlider = new VizTimeSlider(SLIDER_X, SLIDER_Y, SLIDER_WIDTH,
		SLIDER_HEIGHT, this, graph);
	timeSlider.setup();
	addTouchSubscriber(timeSlider);

    }

    public VizGraph getGraph() {
	return graph;
    }

    public void addPlot(PlotData plot, int index) {
	graph.addPlot(plot, index);
	timeSlider.addPlot(plot, index);
	setToRedraw();
    }

    public void removePlotAtIndex(int index) {
	graph.removePlotAtIndex(index);
	timeSlider.removePlotAtIndex(index);
    }

    @Override
    public boolean draw() {
	// if (!startDraw())
	// return false;
	boolean willNeedToBeRedrawn = false;

	graph.setToRedraw();
	timeSlider.setToRedraw();
	pushStyle();
	noStroke();
	background(MyColorEnum.DARK_GRAY);
	willNeedToBeRedrawn = timeSlider.draw();
	drawTitle();

	switch (selection) {
	case GRAPH:
	    willNeedToBeRedrawn = graph.draw() || willNeedToBeRedrawn;
	    break;
	case TABLE:
	    break;
	default:
	    break;
	}

	popStyle();
	return false;
    }

    private void drawTitle() {
	pushStyle();
	fill(MyColorEnum.DARK_GRAY);
	noStroke();
	rect(0, -100, TIMELINE_WIDTH, 100);
	fill(MyColorEnum.WHITE);
	textSize(24);

	String titleString = null;
	switch (m.currentDataDisplayed) {
	case AVERAGE_BUDGET:
	    titleString = c.translate("Average Budget USD");
	    break;
	case AVERAGE_RATING:
	    titleString = c.translate("Average Rating");
	    break;
	case AVERAGE_VOTES:
	    titleString = c.translate("Average Number of Votes");
	    break;
	case NUMBER_OF_MOVIES:
	    titleString = c.translate("Number of Movies");
	    break;
	default:
	    break;
	}
	text(titleString, 0, -10);

	popStyle();

    }

    @Override
    public boolean touch(float x, float y, boolean down, TouchTypeEnum touchType) {
	if (propagateTouch(x, y, down, touchType)) {
	    setToRedraw();
	    return true;
	}
	return false;
    }

}
