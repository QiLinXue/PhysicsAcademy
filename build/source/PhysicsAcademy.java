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
  switch(screenMode){
      case 1:
        sandboxModeKeyActions();
        break;
      case 3:
        quizModeKeyPressed();
        break;
  }
}

public void mousePressed() {
  switch(screenMode){
      case 1:
        //sandboxModeMousePressed();
        break;
      case 3:
        quizModeMousePressed();
        break;
  }
}

public void mouseReleased() {
  switch(screenMode){
      case 1:
        //sandboxModeMouseReleased();
        break;
      case 3:
        quizModeMouseReleased();
        break;
 }
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

public String[] problem1(){
    //TODO make bonus sections for common mistakes
    String[] problem = {"question","question type","answer"};

    //Question Type
    problem[1] = "FREE";

    //Stats
    //TODO: make this more efficient (find easier bug fix)
    String mass = Integer.toString(floor(random(1,50)));
    //String mass = tempMass;

    String force = Integer.toString(floor(random(1,50))+Integer.parseInt(mass));
    //String force = tempForce;

    String kineticFriction = Float.toString(PApplet.parseInt(random(1,9)));
    //String kineticFriction = tempKineticFriction;

    String staticFriction = Float.toString(PApplet.parseInt(random(PApplet.parseInt(kineticFriction)+1,9)));
    //String staticFriction = tempStaticFriction;

    //Question
    problem[0] = "A book of mass "+mass+"kg is held to a vertical wall by a person's hand applying a "+force+"N force directly toward the wall. The wall has a static friction coefficient of 0."+staticFriction.charAt(0)+" and a kinetic friction coefficient of 0."+kineticFriction.charAt(0)+". With the book held at rest, what is the frictional force keeping the book from sliding down the wall?";

    //TODO make answer real (really simple for now)
    problem[2] = mass;

    return problem;
}
PFont questionfont;
String[] questionData = problem1();
Boolean quizModeInAnswerBox = false;
String quizModeInputtedAnswer = "";
Boolean failed = false;
//TODO: make this infinite by loading and uploading a data file
int[] pastCorrectAnswers = {0,0,0,0,0,0,0,0};

public void learnMode() {

  //background
  background(33, 26, 29);

  //History (Past Answers Correct/Incorrect)
  //TODO: enable this feature

  //fill(0, 100, 100); //Cyan
  for (int i=0; i<8; i++) {
    switch(pastCorrectAnswers[i]){
        case 0: fill(100); break;
        case 1: fill(0,255,100); break;
        case -1: fill(200,0,0); break;
    }
    rect(i*120+30, 20, 100, 100);
  }

  //Decor
  // fill(255, 255, 0); //Yellow
  fill(0, 100, 100); //Cyan

  noStroke();
  rect(0, 500, width, 10);
  rect(0, 150, width, 10);

  // Answer Box
  fill(150);
  if(quizModeInAnswerBox) fill(255);
  rect(300,550,400,80);
  fill(0);

  textAlign(LEFT,(CENTER));
  text(quizModeInputtedAnswer,300,560,400,60);

  // Question
  questionfont = createFont("Montserrat-Regular.ttf", 30);
  textAlign(CENTER);
  fill(220);
  textSize(33);
  textFont(questionfont);
  text(questionData[0], 0, 210, width, height);

}

//NOTE quizModeCorrect and quizMode Incorrect could be integrated below depending on how long the code is
public void quizModeCorrect(){
    questionData = problem1();
    quizModeInputtedAnswer = "";

    //Shift history colors down
    if(!failed){
        for(int i=0;i<7;i++){
            pastCorrectAnswers[i] = pastCorrectAnswers[i+1];
        }
        pastCorrectAnswers[7] = 1;
    }
    else{
        failed = false;
    }
}

//TODO Weed out accidental mistakes
//TODO implement machine learning alg that learns common mistakes
public void quizModeIncorrect(){
    if(!failed) {
        for(int i=0;i<7;i++){
            pastCorrectAnswers[i] = pastCorrectAnswers[i+1];
        }
        pastCorrectAnswers[7] = -1;
        failed = true;
    }

}


public void quizModeKeyPressed(){

    //TODO make inputted text more efficient
    if(quizModeInAnswerBox && keyCode != BACKSPACE && keyCode != CONTROL && keyCode != SHIFT && keyCode != ENTER){
        quizModeInputtedAnswer += key;
    } else if(quizModeInAnswerBox && keyCode == BACKSPACE && quizModeInputtedAnswer.length() > 0){
        quizModeInputtedAnswer = quizModeInputtedAnswer.substring(0, quizModeInputtedAnswer.length() - 1);
    }

    //Check answer

    if(quizModeInAnswerBox && keyCode == ENTER){
        //NOTE can't compare strings. Dunno why
        if(Integer.parseInt(quizModeInputtedAnswer) == Integer.parseInt(questionData[2])) quizModeCorrect();
        else quizModeIncorrect();
    }
}

public void quizModeMousePressed(){
    quizModeInAnswerBox = (mouseX<700 && mouseX>300 && mouseY>550 && mouseY<630) ? true : false;
}

public void quizModeMouseReleased(){

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

public void sandboxModeKeyActions() {
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
