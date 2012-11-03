//// There is something
//// need
//// TODO
//// 1: assign values to constants(colors, weights, size, etc)
//// 2: cannot use Processing functions directly, so need to add Main or PApplet or so
//// 3: decide weather keep optional functions (volumes, axis, labels)
////
//// NOTE:
//// 1: curretly dots are in the same size
//// 2:
//
//
//package com.anotherbrick.inthewall;
//
//import java.util.ArrayList;
//
//import processing.core.PVector;
//
//public class VizScatterPlot extends VizPanel {
//
//	ArrayList<PVector> dots;
//	int num;
//	float maxX; // max value of X
//	float maxY; // max value of Y
//
//	//OPTIONAL
//	//float increase; // for volume labels
//
//	public VizScatterPlot(float x0, float y0, float width, float height, VizPanel parent) {
//		super(x0,y0,width,height,parent);
//
//		num = 0;
//
//		maxX = -Float.MAX_VALUE;
//		maxY = -Float.MAX_VALUE;
//	}
//
//	public void setDots(ArrayList<PVector> dots) {
//		this.dots = dots;
//		num = dots.size();
//
//		for (int i=0;i<num;i++) {
//			if (dots[i].x > maxX) {
//				maxX = dots[i].x;
//			}
//			if (dots[i].y > maxY) {
//				maxY = dots[i].y;
//			}
//		}
//
//		// leave some blank
//		maxX = maxX * 1.05; 
//		maxY = maxY * 1.05;
//	}
//
//	@Override
//	public boolean draw() {
//
//		//OPTIONAL
//		//this.renderAxisLabels();
//
//		//OPTIONAL
//		//this.renderVolumeLabels();
//
//		//TODO integrate Processing functions
//		pushStyle();
//
//		stroke(AXIS_COLOR_SCATTER);
//		strokeWeight(AXIS_WEIGHT_SCATTER);
//		line(x0,y0+height,x0+width,y0+height);
//		line(x0,y0,x0,y0+weight);
//
//		noStroke();
//		fill(DOT_COLOR_SCATTER);
//
//		for (int i=0;i<num;i++) {
//			float x = (this.dots[i].x, 0, maxX, x0, x0+width);
//			float y = (this.dots[i].y, 0, maxY, y0+height, y0);
//			ellipse(x,y,DOT_SIZE_SCATTER,DOT_SIZE_SCATTER); // assume all dots are in same size
//		}
//
//		popStyle();
//		return false;
//	}
//
//////
////
////
//// OPTIONAL
////
////
////
////
//////
//
//	// Axis Labels
//	private void renderAxisLabels(filter1, filter2) {
//
//		pushStyle();
//		textSize(AXIS_LABEL_SIZE_SCATTER);
//		fill(AXIS_LABEL_COLOR_SCATTER);
//		textAlign(LEFT,CENTER);
//		text(filter1.name, x0+width+3, y0+height);
//		textAlign(CENTER,BOTTOM);
//		text(filter2.name, x0, y0+height*0.5);
//		popStyle();
//	}
//
//	// Volume Labels
//	private void drawVolumeLabels(int increase, int whichAxis) {
//
//		pushStyle();
//		fill(VOL_LABEL_COLOR_SCATTER);
//		textSize(VOL_LABEL_SIZE_SCATTER);
//		stroke(VOL_TICK_COLOR_SCATTER);
//		strokeWeight(VOL_TICK_WEIGHT_SCATTER);
//
//		// x axis
//		if (whichAxis == 0) {
//			textAlign(CENTER,TOP);
//
//			//TODO change '3' to correct number
//			for (float v = 0; v < maxX;v+=increase) {
//				float x = map(v,0,maxX,x0,x0+width);
//				if (v == 0) {
//				}
//				else {
//					text(round(v), x, y0+height+3);
//				}
//				line(x, y0+height, x, y0);
//			}
//		}
//		// y axis
//		else if (whichAxis == 1) {
//			textAlign(RIGHT);
//
//			//TODO change '5' to correct number
//			for (float v = 0; v < maxY; v += increase) {
//				float y = map(v, 0, maxY, y0+height, y0);
//				float textOffset = textAscent()*0.5;
//				if (v == 0) {
//				}
//				else if (v == maxY) {
//					text(round(v), x0 - 5, y+textAscent);
//				}
//				else {
//					text(round(v), x0 - 5, y+textOffset);
//				}
//				line(x0, y, x0+width, y);
//			}
//		}
//		popStyle();
//	}
//
//	@Override
//	public void setup() {
//	    // TODO Auto-generated method stub
//	    
//	}
// }