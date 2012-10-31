class y_state {
	int id;
	String name;
	float pop;
	String acName;

	PVector cen;
	float len;

	y_state(String _id, String _name, float _lon, float _lat, String _pop, String _acName) {
		id = Integer.parseInt(_id);
		name = _name;
		pop = Float.parseFloat(_pop);
		acName = _acName;
		cen = new PVector();
		cen.x = _lon;
		cen.y = _lat;
		len = 0;
	}

/*/ real function
	void render(color _color, float _scale) {
		pushStyle();
		fill(_color);
		noStroke();
		rectMode(CENTER);
		rect(lat, lon, sqrt(pop)*_scale, sqrt(pop)*_scale);
		popStyle();
	}
/*/

//  fake function
 	void render(float scale) {
		pushStyle();
		fill(200);
		noStroke();
		rectMode(CENTER);
		rect(cen.x * scale, cen.y * scale, len * scale * 0.5, len * scale * 0.5);
		fill(#ffff00);
		textAlign(CENTER,CENTER);
		textSize(12);
		text(acName,cen.x * scale,cen.y * scale);
		popStyle();
	}

	boolean checkIn(float _x, float _y, float scale) {
		if (_x > cen.x*scale - len*scale*0.25 && _x < cen.x*scale + len*scale*0.25 && _y > cen.y*scale - len*scale*0.25 && _y < cen.y*scale + len*scale*0.25) {
			return true;
		}
		return false;
	}
}