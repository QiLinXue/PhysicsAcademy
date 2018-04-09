import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import fisica.*; 
import javax.swing.JOptionPane; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class PhysicsAcademy extends PApplet {




//Sandbox Objects
FWorld world;
FPoly inclinedPlane;
FCircle b;
FBox box;

public void setup() {
  

  simulationScreenInitialize();

}

int screenMode = 3;
public void draw() {
  switch(screenMode) {

  case 1:
    simulationMode();
    break;

  case 2:

    drawMode();
    break;

  case 3:
    learnMode();
    break;

  }

}

public void drawMode() {
  background(255);
}

public void keyPressed() {
  if (screenMode == 1) {
    simulationModeKeyActions();
  }
}

public void mousePressed() {
  println(b.getX());
}
public float angle_triangle(float x1, float y1, float x2, float y2, float x3, float y3) { //Top Left, Bottom Left, Bottom Right
  return atan((y2-y1)/(x3-x2));
}

public float angle_IP(){
  float rise = (inclinedPlaneCoordinates[1][1]-inclinedPlaneCoordinates[0][1]);
  float run = (inclinedPlaneCoordinates[2][0]-inclinedPlaneCoordinates[1][0]);
  return atan(rise/run);
}

public float minCoFriction_IP(float radians){
  return tan(radians);
}


String mass = Integer.toString(floor(random(1,50)));
String force = Integer.toString(floor(random(1,50))+Integer.parseInt(mass));
String kineticFriction = Float.toString(PApplet.parseInt(random(1,9)));
String staticFriction = Float.toString(PApplet.parseInt(random(PApplet.parseInt(kineticFriction)+1,9)));


String problem1 = "A book of mass "+mass+"kg is held to a vertical wall by a person's hand applying a "+force+"N force directly toward the wall. The wall has a static friction coefficient of 0."+staticFriction.charAt(0)+" and a kinetic friction coefficient of 0."+kineticFriction.charAt(0)+". With the book held at rest, what the is frictional force keeping the book from sliding down the wall?";
PFont questionfont;

public void learnMode() {
  background(33, 26, 29);

  textAlign(CENTER);
  fill(255);
  textSize(33);
  questionfont = createFont("Montserrat-Regular.ttf", 30);
  textFont(questionfont);

  text(problem1, 0, 210, width, height);

  fill(255, 255, 0);
  noStroke();
  rect(0, 500, width, 10);
  rect(0, 150, width, 10);

  fill(0, 200, 200);
  for (int i=0; i<8; i++) {
    rect(i*120+30, 20, 100, 100);
  }
}
int[][] inclinedPlaneCoordinates = {{150,100},{150,994},{500,994}};

public void simulationScreenInitialize() {
  Fisica.init(this);

  world = new FWorld();
  world.setEdges();
  world.setEdgesRestitution(0);
  world.setGravity(0, 980);
  //world.setGrabbable(false);

  inclinedPlane = new FPoly();
    
  inclinedPlane.vertex(inclinedPlaneCoordinates[0][0], inclinedPlaneCoordinates[0][1]);
  inclinedPlane.vertex(inclinedPlaneCoordinates[1][0], inclinedPlaneCoordinates[1][1]);
  inclinedPlane.vertex(inclinedPlaneCoordinates[2][0], inclinedPlaneCoordinates[2][1]);
  inclinedPlane.setDensity(10000);
  inclinedPlane.setFill(207, 92, 54);
  inclinedPlane.setFriction(5);
  inclinedPlane.setNoStroke();
  inclinedPlane.setRestitution(0);

  b = new FCircle(100);
  b.setPosition(width/4, height/6);
  b.setRestitution(0);
  b.setNoStroke();
  b.setFill(119, 125, 167);
  b.setGrabbable(true);
  b.setRotatable(true);
  b.setFriction(0.5f);


  box = new FBox(100, 100);
  box.setPosition(width/4, height/6);
  box.setRestitution(0);
  box.setNoStroke();
  box.setFill(119, 125, 167);
  box.setGrabbable(true);
  box.setDensity(0.1f);
  box.setRotation(angle_IP());
  box.setFriction(1);
  box.setRotatable(false);


  world.add(box);
  world.add(inclinedPlane);
}

public void simulationMode() {
  background(255);
  world.step();
  world.draw();
}

int x1, y1, x2, y2, x3, y3;
String[] defaultTriangle = {"100", "994", "700", "994", "100", "600"};

public void simulationModeKeyActions() {
  if (keyCode == TAB) 

  {

    String[] newObjectSettings = {"vertices"};
    newObjectSettings[0] = JOptionPane.showInputDialog("How many vertices?", "3");
    int[] newObjectLocation = new int[Integer.parseInt(newObjectSettings[0])*2];
    for (int i = 0; i < newObjectLocation.length; i+=2) {

      if (newObjectLocation.length == 6) //If object is a triangle (this is for testing)
      {
        String tempCoordinate = JOptionPane.showInputDialog("Please enter x" + Integer.toString(1+i/2) + ", y" + Integer.toString(1+i/2) + " (seperated by a space)", defaultTriangle[i] + " " + defaultTriangle[i+1]);
        newObjectLocation[i] = Integer.parseInt(tempCoordinate.split(" ")[0]);
        newObjectLocation[i+1] = Integer.parseInt(tempCoordinate.split(" ")[1]);
      } else //If object is not a triangle
      {
        String tempCoordinate = JOptionPane.showInputDialog("Please enter x" + Integer.toString(1+i+1) + ", y" + Integer.toString(1+i+1) + " (seperated by a space)", Integer.toString(floor(random(0, 1000))) + " " + Integer.toString(floor(random(0, 1000))));
        newObjectLocation[i] = Integer.parseInt(tempCoordinate.split(" ")[0]);
        newObjectLocation[i+1] = Integer.parseInt(tempCoordinate.split(" ")[1]);
      }
    }

    FPoly myPoly = new FPoly();
    for (int i=0; i<newObjectLocation.length; i+=2) {
      myPoly.vertex(newObjectLocation[i], newObjectLocation[i+1]);
    }
    myPoly.setDensity(10000);
    myPoly.setFill(100);
    myPoly.setNoStroke();
    world.add(myPoly);
  }
}
  public void settings() {  size(1000, 1000); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "PhysicsAcademy" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
