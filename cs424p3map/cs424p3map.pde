import processing.net.*;

///////////////////////////////////
//
//
//                  SETUP
//
//
///////////////////////////////////

StatesMap map;

int isDrag = -1;
float diffX;
float diffY;
float startX;
float startY;

void setup()
{
  size(1200, 720);
  
  PFont plotFont;
  // setup Font
  //if (scale == 1) {
    plotFont = createFont("Helvetica-Bold", 12);
  //}
  //else if (scale == 5) {
    //plotFont = loadFont("Helvetica-Bold-70.vlw");
 // }
  textFont(plotFont);

  map = new StatesMap(0,0,1,"states.txt");
  smooth();
} // end of setup

///////////////////////////////////
//
//
//                  DRAW
//
//
///////////////////////////////////
void draw() {

  background(0);
  
  map.render(0,0,width,height);
}

// mouse
void mousePressed() {
  for (int i=0;i<map.getNum();i++) {
    if (map.states[i].checkIn(mouseX, mouseY, map.scale) == true) {
      isDrag = i;
      startX = mouseX;
      startY = mouseY;
      diffX = startX - map.states[i].cen.x;
      diffY = startY - map.states[i].cen.y;
      //println("mouse at: ("+startX+", "+startY+")");
      //println("center at:("+map.states[i].cen.x+", "+map.states[i].cen.y+")");
      //println(map.states[i].len*0.5);
      break;
    }
  }
}

void mouseDragged() {
  if (isDrag > -1) {
    map.states[isDrag].cen.x = mouseX - diffX;
    map.states[isDrag].cen.y = mouseY - diffY;
  }
}

void mouseReleased() {
  isDrag = -1;
}

// keyboard
void keyPressed() {
  if (key == 'a') {
    for (int i=0;i<map.getNum();i++) {
      map.states[i].cen.x -= 1 / map.scale;
    }
  }
  if (key == 'A') {
    for (int i=0;i<map.getNum();i++) {
      map.states[i].cen.x -= 10 / map.scale;
    }
  }
  if (key == 's') {
    for (int i=0;i<map.getNum();i++) {
      map.states[i].cen.y += 1 / map.scale;
    }
  }
  if (key == 'S') {
    for (int i=0;i<map.getNum();i++) {
      map.states[i].cen.y += 10 / map.scale;
    }
  }if (key == 'd') {
    for (int i=0;i<map.getNum();i++) {
      map.states[i].cen.x += 1 / map.scale;
    }
  }
  if (key == 'D') {
    for (int i=0;i<map.getNum();i++) {
      map.states[i].cen.x += 10 / map.scale;
    }
  }if (key == 'w') {
    for (int i=0;i<map.getNum();i++) {
      map.states[i].cen.y -= 1 / map.scale;
    }
  }
  if (key == 'W') {
    for (int i=0;i<map.getNum();i++) {
      map.states[i].cen.y -= 10 / map.scale;
    }
  }

  if (key == 'z' || key == 'Z') {
    map.scale -= 0.1;
  }

  if (key == 'x' || key == 'X') {
    map.scale += 0.1;
  }

  if (key == 'p' || key == 'P') {
    map.update();
    map.writeToFile();
    text("created\n",600,15);
  }
}
