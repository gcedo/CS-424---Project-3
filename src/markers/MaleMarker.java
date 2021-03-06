package markers;

import com.anotherbrick.inthewall.VizPanel;

public class MaleMarker extends AbstractMarker {

    public MaleMarker(float x0, float y0, float width, float height,
	    VizPanel parent, Integer id) {
	super(x0, y0, width, height, parent, id);
    }

    @Override
    public void setup() {
	shape = c.getShape("male.svg");
    }

}
