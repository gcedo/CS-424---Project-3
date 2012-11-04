package com.anotherbrick.inthewall;

import markers.AbstractMarker;
import java.util.ArrayList;
import processing.core.PVector;
import java.lang.Math;
import processing.core.*;

public class Cluster extends AbstractMarker {
	
	private int num; // num of markers
	private ArrayList<AbstractMarker> markerList;
	private PVector pos;
	
	private int sCale_Num;
	
	public Cluster(AbstractMarker a, VizPanel parent, double scale) {
		super(a.getX0(), a.getY0(),a.getWidth(),a.getHeight(),parent,a.getId());
		
		this.num = 1;
		
		markerList = new ArrayList<AbstractMarker>();
		
		pos = new PVector(a.getX0(),a.getY0());
		
		if (scale>300) {
			sCale_Num = 3;
		}
		if (scale>200) {
			sCale_Num = 6;
		}
		else if (scale>100) {
			sCale_Num = 10;
		}
		else if (scale>50) {
			sCale_Num = 20;
		}
		else {
			sCale_Num = 30;
		}
	}
	
	public Cluster(float x0, float y0, float width, float height, VizPanel parent, Integer id) {
		super(x0, y0, width, height, parent, id);
		
		this.num = 0;
		
		pos = new PVector(-100,-100);
	}
	
	public void add(AbstractMarker a) {
		markerList.add(a);
		if (pos.x == -100) {
			pos.x = a.getX0();
			pos.y = a.getY0();
			// TODO getX0 and getY0 may not be correct
		}
		// get the avg position in this cluster
		else {
			float avgX = 0;
			float avgY = 0;
			for (AbstractMarker m : markerList) {
				avgX += m.getX0();
				avgY += m.getY0();
			}
			avgX /= num;
			avgY /= num;
			pos.x = avgX;
			pos.y = avgY;
		}
		this.num++;
	}
	
	public PVector getPos() {
		return pos;
	}
	
	public int getNum() {
		return this.num;
	}
	
	@Override
	public boolean draw() {
	if (num<sCale_Num) {
		for (AbstractMarker mm : markerList) {
			mm.draw();
		}
		return false;
	}
	else {
		pushStyle();
		fill(getP().color(255,(float)(255-num/500.0*255),(float)(255-num/500.0*255),200));
		ellipse(getWidth()/2,getHeight()/2,getWidth()*5,getWidth()*5);
		//ellipse(getWidth() / 2, getHeight() / 2, (float)(getWidth()*Math.sqrt(num)), (float)(getHeight()*Math.sqrt(num)));
		fill(0);
		textAlign(PApplet.CENTER, PApplet.CENTER);
		text(Integer.toString(num), getWidth() / 2, getHeight() / 2);
		popStyle();
		return false;
	}
	}
	    
	@Override
	public void setup() {

	}
	    
}