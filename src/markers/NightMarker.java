package markers;

import com.anotherbrick.inthewall.VizPanel;

public class NightMarker extends AbstractMarker {

    public NightMarker(float x0, float y0, float width, float height,
	    VizPanel parent, Integer id) {
	super(x0, y0, width, height, parent, id);
    }

    @Override
    public void setup() {
	shape = c.getShape("moon.svg");
    }

}
