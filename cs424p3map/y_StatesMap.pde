class StatesMap {
	int num;
	y_state[] states;

	float scale;
	

	PVector offset;

	String[] lines;
	String[][] pieces;

	StatesMap(float _x, float _y, float _scale, String filename) {
		String[] rows = loadStrings(filename);

		num = rows.length;

		states = new y_state[num];

		offset = new PVector();
		offset.x = _x;
		offset.y = _y;

		scale = _scale;

		for (int i=0;i<num;i++) {
			String[] pieces = split(rows[i], TAB);
			float lon = Float.parseFloat(pieces[2]);
			float lat = Float.parseFloat(pieces[3]);
			states[i] = new y_state(pieces[0], pieces[1], lon, lat, pieces[4], pieces[5]);
			states[i].len = sqrt(states[i].pop);
		}

		lines = new String[num];
		pieces = new String[num][6];
	}

	void render(float x1, float y1, float x2, float y2) {
		for (int i=0;i<num;i++) {
			// fake render
			states[i].render(this.scale);
			// real render
			// add

			pushStyle();
			fill(255);
			textAlign(LEFT, CENTER);
			text(states[i].name+": ("+states[i].cen.x+", "+states[i].cen.y+")", 10, 8+i*14);
			popStyle();
		}
	}

	int getNum() {
		return num;
	}

	void update() {
		for (int i=0;i<num;i++) {
			states[i].cen.x *= this.scale;
			states[i].cen.y *= this.scale;
			states[i].len *= this.scale;
		}
		this.scale = 1;
	}
	void writeToFile() {
		for (int i=0;i<num;i++) {
			String[] rows = loadStrings("states.txt");

			pieces[i] = split(rows[i], TAB);
			//scrubQuotes(pieces);
			pieces[i][2] = String.valueOf(states[i].cen.x);
			pieces[i][3] = String.valueOf(states[i].cen.y);
			pieces[i][4] = String.valueOf(states[i].len*states[i].len);

			lines[i] = pieces[i][0]+"\t"+pieces[i][1]+"\t"+pieces[i][2]+"\t"+pieces[i][3]+"\t"+pieces[i][4]+"\t"+pieces[i][5];
		}
		saveStrings(dataPath("states.txt"), lines);
	}
}