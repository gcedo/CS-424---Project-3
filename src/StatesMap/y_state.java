package StatesMap;

import com.anotherbrick.inthewall.Config.MyColorEnum;
import com.anotherbrick.inthewall.VizPanel;

import processing.core.PApplet;
import processing.core.PVector;

class y_state extends VizPanel {
    int id;
    String name;
    String acName;

    PVector cen;
    float len;
    float scale;

    y_state(String _id, String _name, float xc, float yc, float len,
	    String _acName, float scale, VizPanel parent) {
	super(scale * (xc - len / 2), scale * (yc - len / 2), scale * len,
		scale * len, parent);

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
	// TODO Auto-generated method stub

    }

    @Override
    public boolean draw() {
	pushStyle();
	fill(MyColorEnum.DARK_WHITE);
	noStroke();
	rectMode(PApplet.CENTER);
	rect(cen.x, cen.y, (float) (getWidth() * 0.5),
		(float) (getHeight() * 0.5));
	fill(MyColorEnum.DARK_ORANGE);
	textAlign(PApplet.CENTER, PApplet.CENTER);
	textSize(6);
	text(acName, cen.x, cen.y);
	popStyle();
	return false;
    }
}
