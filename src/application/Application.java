package application;
import com.anotherbrick.inthewall.TouchEnabled;
import com.anotherbrick.inthewall.VizPanel;


public class Application extends VizPanel implements TouchEnabled {

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
	// TODO Auto-generated method stub
	
    }

    @Override
    public boolean draw() {
	// TODO Auto-generated method stub
	return false;
    }

}
