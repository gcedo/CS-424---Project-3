package markers;

import com.anotherbrick.inthewall.VizPanel;

public class FemaleMarker extends AbstractMarker {

    public FemaleMarker(float x0, float y0, float width, float height,
	    VizPanel parent, Integer id) {
	super(x0, y0, width, height, parent, id);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void setup() {
	shape = c.getShape("female.svg");
    }

}
