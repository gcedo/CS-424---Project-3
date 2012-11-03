package markers;

import com.anotherbrick.inthewall.VizPanel;

import processing.core.PShape;

public abstract class AbstractMarker extends VizPanel {

    private Integer id;
    protected PShape shape;

    public AbstractMarker(float x0, float y0, float width, float height,
	    VizPanel parent, Integer id) {
	super(x0, y0, width, height, parent);
	this.setId(id);
    }

    public boolean draw() {
	pushStyle();
	shape(shape, 0, 0, getWidth(), getHeight());
	popStyle();
	return false;
    };

    public Integer getId() {
	return id;
    }

    private void setId(Integer id) {
	this.id = id;
    }

}
