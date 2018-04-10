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
  solarSystemInitialize();


}

int screenMode = 4;
public void draw() {
  switch(screenMode) {

  case 1: simulationMode(); break;
  case 2: drawMode(); break;   //TODO make drawMode
  case 3: learnMode(); break;
  case 4: solarSystemMode(); break;

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
      case 4: solarSystemKeyPressed(); break;

  }
}

public void mousePressed() {
  switch(screenMode){
      case 3: quizModeMousePressed(); break;
  }
}

public void mouseReleased() {
  switch(screenMode){
      case 3: quizModeMouseReleased(); break;
      case 4: solarSystemMouseReleased(); break;
 }
}

public void mouseWheel(MouseEvent event) {
    switch(screenMode){
        case 4:
            if(spaceMode==1){
              solarSystemIsScroll = event.getCount();
              solarSystemZoom = solarSystemZoom + (solarSystemIsScroll/20);
              if(solarSystemZoom>0){
                solarSystemZoom = solarSystemZoom - (solarSystemIsScroll/20);
              }
              //println(solarSystemZoom);
            }
            break;
   }
}
class Moon{
  float objectRadius; //km
  float mass; //kg
  float periapsis; //m
  float apoapsis; //m
  float periapsisLongitude; //degrees
  float inclination; //degrees
  float planetMass; // kg
  float planetPE; // m
  float planetAP; // m
  float planetPeriapsisLongitude; //m
  float planetInclination; //m
  float starMass; // kg

  Moon(float tempobjectRadius, float tempMass, float tempPeriapsis, float tempApoapsis, float tempPeriapsisLongitude, float tempInclination,
       float tempPlanetMass, float tempPlanetPE, float tempPlanetAP, float tempPlanetPeriapsisLongitude, float tempPlanetInclination, float tempStarMass){
          objectRadius = tempobjectRadius;
          mass = tempMass;
          periapsis = tempPeriapsis;
          apoapsis = tempApoapsis;
          periapsisLongitude = tempPeriapsisLongitude;
          inclination = tempInclination;
          planetMass = tempPlanetMass;
          planetPE = tempPlanetPE;
          planetAP = tempPlanetAP;
          planetPeriapsisLongitude = tempPlanetPeriapsisLongitude;
          planetInclination = tempPlanetInclination;
          starMass = tempStarMass;


  }

  //Vital Lunar Facts
  public float moonEccentricity(){
    return ((apoapsis-periapsis)/2)/(apoapsis+periapsis);
  }

  public float moonSemiMajor(){
    return (apoapsis+periapsis)/2;
  }

  public float moonSemiMinor(){
    return (moonSemiMajor()*sqrt(1-pow(moonEccentricity(),2)));
  }

  public float moonPeriod(){
    return

      //Kepler's Third Law
      (2*PI*
        sqrt(
              (pow(moonSemiMajor(),3) //Gives the semi-major axis
              /
              (6.674e-11f*planetMass)) //Gives the Standard Gravitational Parameter of the System
            )
      );
  }

  //Vital Planetary Facts
  public float planetEccentricity(){
    return ((planetAP-planetPE)/2)/(planetAP+planetPE);
  }

  public float planetSemiMajor(){
    return (planetAP+planetPE)/2;
  }

  public float planetSemiMinor(){
    return (planetSemiMajor()*sqrt(1-pow(planetEccentricity(),2)));
  }

  public float planetPeriod(){
    return

      //Kepler's Third Law
      (2*PI*
        sqrt(
              (pow(planetSemiMajor(),3) //Gives the semi-major axis
              /
              (6.674e-11f*starMass)) //Gives the Standard Gravitational Parameter of the System
            )
      );
  }

  public float planetAngle(){
    return
            ((timewarp * //Speeds everything up
               (timeticker/planetPeriod()) // percentage of the period completed so it can be transformed into an angle
               - differenceAngle)
               % 360
            ); //Transforms a percentage into an angle
  }

float moonOrbitRadiusScaler = 600000000;
  public void plotOrbit(){
    stroke(255); //color of the orbit line
    noFill(); //ensures the ellipse is transparent
    strokeWeight(100*exp((3*solarSystemZoom)-1.5f)+0.25f); //calculated with a table of values at www.desmos.com/calculator/fz1zzieuwa

    pushMatrix(); //Begin transformation

    rotateZ(radians(planetPeriapsisLongitude)); //Matches its longitude of periapsis
    rotateX(radians(planetInclination)); //Matches its orbital inclination
    //periapsisLine();


    //The actual orbit
    translate(
               cos(radians(planetAngle())-radians(planetPeriapsisLongitude))* //x-angle of planet with respect to center body
               ((planetSemiMinor())/orbitRadiusScaler) //length magnitude of sateliite with respect to center body
               + (cos(radians(planetPeriapsisLongitude))*((planetAP-planetPE)/2))/orbitRadiusScaler //Adjustment from foci
               ,
               sin(radians(planetAngle())-radians(planetPeriapsisLongitude))* //y-angle of planet with respect to center body
               ((planetSemiMajor())/orbitRadiusScaler) //Length magnitude of planet with respect to center body
               + (sin(radians(planetPeriapsisLongitude))*((planetAP-planetPE)/2))/orbitRadiusScaler //Adjustment from foci
             );

    rotateZ(radians(periapsisLongitude)); //Matches its longitude of periapsis
    rotateX(radians(inclination)); //Matches its orbital inclination
    translate(    (cos(radians(periapsisLongitude))*((apoapsis-periapsis)/2))/orbitRadiusScaler,
                  (sin(radians(periapsisLongitude))*((apoapsis-periapsis)/2))/orbitRadiusScaler);

    if(key == 'a'){
      moonOrbitRadiusScaler = 30000000;
    } else{
      moonOrbitRadiusScaler = 600000000;
    }
    ellipse(
              0,0,
              (moonSemiMinor()*2)/moonOrbitRadiusScaler,
              (moonSemiMajor()*2)/moonOrbitRadiusScaler
           );

    popMatrix(); //End transformation
    strokeWeight(1);
  }

  //Time Warp
  float differenceAngle = 0;
  float solarSystemSavedAngle;
  float timewarp = 100000;

  public void changeTimeWarp(){
    //println("works");
    solarSystemSavedAngle = planetAngle();

    if(key == '.'){
      timewarp = timewarp * 1.2f;
    }
    if(key == ','){
      timewarp = timewarp / 1.2f;
    }

    differenceAngle = differenceAngle + planetAngle() - solarSystemSavedAngle;


  }


}
class Planet{
  float objectRadius; //km
  float mass; //kg
  float periapsis; //m
  float apoapsis; //m
  float periapsisLongitude; //degrees
  float inclination; //degrees
  float centerMass; // kg

  PVector orbit; // (angle, magnitude of length from star)


  Planet(float tempobjectRadius, float tempMass, float tempPeriapsis, float tempApoapsis, float tempPeriapsisLongitude, float tempInclination, float tempCenterMass){
    objectRadius = tempobjectRadius;
    mass = tempMass;
    periapsis = tempPeriapsis;
    apoapsis = tempApoapsis;
    periapsisLongitude = tempPeriapsisLongitude;
    inclination = tempInclination;
    centerMass = tempCenterMass;
  }

  //Vital Planetary Facts
  public float eccentricity(){
    return ((apoapsis-periapsis)/2)/(apoapsis+periapsis);
  }

  public float semiMajor(){
    return (apoapsis+periapsis)/2;
  }

  public float semiMinor(){
    return (semiMajor()*sqrt(1-pow(eccentricity(),2)));
  }

  public float period(){
    return

      //Kepler's Third Law
      (2*PI*
        sqrt(
              (pow(semiMajor(),3) //Gives the semi-major axis
              /
              (6.674e-11f*centerMass)) //Gives the Standard Gravitational Parameter of the System
            )
      );
  }

  public float angle(){
    return
            ((timewarp * //Speeds everything up
               (timeticker/period()) // percentage of the period completed so it can be transformed into an angle
               - differenceAngle)
               % 360
            ); //Transforms a percentage into an angle
  }

  public float altitude(float angle){
    //Using an adaptation of Kepler's Equation
    //r^3=(GM*T^2)/4pi^2
    //The bottom only works for circular orbits
    /*
    return (
      pow(
        (((6.674e-11*centerMass)  //Standard Gravitational Parameter
          *
          pow(period(),2)) //Period Squared
        /
        (4*pow(PI,2))) //4pi^2
        ,
       0.3333) //Cube Rooted
    );*/

    //Using trigonometry with polar coordinates
    // To prove this, first start with the cartesian equation of ellipse
    // x^2/a^2 + y^2/b^2 = 1
    // x^2 * b^2 + y^2 * a^2 = a^2 & b^2
    // Replace x = r cos(θ) and y = r sin(θ) to move it into the polar coordinates
    // r = ab/sqrt(a^2 * sin^2(θ) + b^2 * cos^2(θ))
    return(
        (periapsis*apoapsis)
        /
        sqrt(
          pow(apoapsis,2)*pow(cos(radians(angle)),2)+pow(periapsis,2)*pow(sin(radians(angle)),2)
        )
      );
  }

  public float angularvelocity(){
    //Vis-Viva Equation
    //sqrt(μ*[(2/r)-(1/a)])
    return (
      sqrt(
        (6.674e-11f*centerMass) // standard gravitational Parameter
        *
        (
          (2/altitude(angle())) // 2/r
          -
          (1/(semiMajor())) // 1/a
        )
      )
      );
  }

  public void periapsisLine(){
    pushMatrix();
    translate(              (cos(radians(periapsisLongitude))*(apoapsis-periapsis)/2)/orbitRadiusScaler,
              (sin(radians(periapsisLongitude))*(apoapsis-periapsis)/2)/orbitRadiusScaler);
    //fill(0);
    //ellipse(0,0,100,100);
    //noFill();
    line(0,0,(cos(radians(periapsisLongitude-90))*semiMinor())/orbitRadiusScaler,(sin(radians(periapsisLongitude-90))*semiMajor())/orbitRadiusScaler);
    popMatrix();
  }


  //Plotting Things Out
  public void plotPlanet(){

    pushMatrix(); //Begin transformation
    rotateZ(radians(periapsisLongitude)); //Matches its longitude of periapsis
    rotateX(radians(inclination)); //Matches its orbital inclination

    //Transforms the planet according to the current angle and its orbital properties
    translate(
               cos(radians(angle())-radians(periapsisLongitude))* //x-angle of planet with respect to center body
               ((semiMinor())/orbitRadiusScaler) //length magnitude of sateliite with respect to center body
               + (cos(radians(periapsisLongitude))*((apoapsis-periapsis)/2))/orbitRadiusScaler //Adjustment from foci
               ,
               sin(radians(angle())-radians(periapsisLongitude))* //y-angle of planet with respect to center body
               ((semiMajor())/orbitRadiusScaler) //Length magnitude of planet with respect to center body
               + (sin(radians(periapsisLongitude))*((apoapsis-periapsis)/2))/orbitRadiusScaler //Adjustment from foci
             );

    //stroke(0); //Gives the stroke color of the sphere details
    noStroke();
    sphereDetail(20);
    fill(153,50,204); //pinkish color
    if(key=='s'){
      sphere(objectRadius/orbitRadiusScaler);
    } else{
      sphere(objectRadius/planetSizeScaler); //Actual planet itself
    }
    stroke(1);
    popMatrix(); //Ends transformation
  }

  public void plotOrbit(){
    stroke(255); //color of the orbit line
    noFill(); //ensures the ellipse is transparent
    strokeWeight(100*exp((3*solarSystemZoom)-1.5f)+0.25f); //calculated with a table of values at www.desmos.com/calculator/fz1zzieuwa

    pushMatrix(); //Begin transformation

    rotateZ(radians(periapsisLongitude)); //Matches its longitude of periapsis
    rotateX(radians(inclination)); //Matches its orbital inclination
    //periapsisLine();


    //The actual orbit
    translate(    (cos(radians(periapsisLongitude))*((apoapsis-periapsis)/2))/orbitRadiusScaler,
                  (sin(radians(periapsisLongitude))*((apoapsis-periapsis)/2))/orbitRadiusScaler);

    ellipse(
              0,0,
              (semiMinor()*2)/orbitRadiusScaler,
              (semiMajor()*2)/orbitRadiusScaler
           );

    popMatrix(); //End transformation
    strokeWeight(1);
  }

  //Debugging
  public void displayFacts(){
    println(angle(),altitude(angle()));
    //line(0,0,(cos(radians(180+periapsisLongitude))*periapsis)/orbitRadiusScaler,(sin(radians(180+periapsisLongitude))*periapsis)/orbitRadiusScaler);
  }


  //Time Warp
  float differenceAngle = 0;
  float solarSystemSavedAngle;
  float timewarp = 100000;

  public void changeTimeWarp(){
    //println("works");
    solarSystemSavedAngle = angle();

    if(key == '.'){
      timewarp = timewarp * 1.2f;
    }
    if(key == ','){
      timewarp = timewarp / 1.2f;
    }

    differenceAngle = differenceAngle + angle() - solarSystemSavedAngle;


  }

}
//TODO replace variable names with more specific ones

Planet Mercury;
Planet Venus;
Planet Earth;
Planet Mars;
Planet Jupiter;
Planet Saturn;
Planet Uranus;
Planet Neptune;
Planet Halley;
Moon Moon;

Star sun;

public void solarSystemInitialize(){
    sun = new Star(695700,1.989e30f,213,182,10);

      //plamet radius (km), mass (kg),periapsis (m), apoapsis (m), longitude of peripasis,orbital inclination
      Mercury = new Planet(2440,0.33011e24f,4.6e10f,6.982e10f,77.46f,7,sun.mass);
      Venus = new Planet(6052,4.8675e24f,1.0748e11f,1.0894e11f,131.533f,3.3947f,sun.mass);
      Earth = new Planet(6357,5.972e24f,1.471e11f,1.521e11f,102.9f,0,sun.mass);
      Mars = new Planet(3390,6.39e23f,2.066e11f,2.492e11f,336,1.85f,sun.mass);
      Jupiter = new Planet(69911,1.898e27f,7.4052e11f,8.1662e11f,14.75f,1.304f,sun.mass);
      Saturn = new Planet(60268,5.6834e26f,1.35255e12f,1.5145e12f,92.43194f,2.48446f,sun.mass);
      Uranus = new Planet(25559,8.6813e25f,2.7413e12f,3.00362e12f,170.96f,0.76986f,sun.mass);
      Neptune = new Planet(24764,1.02413e26f,4.44445e12f,4.54567e12f,44.9713f,1.76917f,sun.mass);
      Halley = new Planet(6,2.2e14f,8.766e10f,5.2481925e12f,111.33f,1,sun.mass);
      Moon = new Moon(1737,7.348e22f,3.633e8f,4.055e8f,318.15f,5.1f,Earth.mass,Earth.periapsis,Earth.apoapsis,Earth.periapsisLongitude,Earth.inclination,sun.mass);
}

//Settings to adjust the dimension of the planets
int timeticker = 0;
int orbitRadiusScaler = 600000000;
int planetSizeScaler = 600;


//0=homescreen, 1=solarSystem, 2=planetSystem
int spaceMode = 1;

public void solarSystemMode(){

  //time ticker (100 fps)
  timeticker++; //global increment that increases each frame
  background(30);

  switch(spaceMode){
    //case 0: homescreen(); break;
    case 1: solarSystem(); break;
    //case 2: planetsystem(); break;
  }
  fill(255);
}

public void solarSystem(){
  sphereDetail(10);
  //pointLight(213,182,10, width/2, height/2, 0);

  pushMatrix(); //Begin Transformation
  translate(width/2,height/2); //Sets the center to the middle of the screen
  scale(solarSystemZoom);

  enableRotation();

  //Planet Orbit Lines
  Mercury.plotOrbit();
  Venus.plotOrbit();
  Earth.plotOrbit();
  Mars.plotOrbit();
  Jupiter.plotOrbit();
  Saturn.plotOrbit();
  Uranus.plotOrbit();
  Neptune.plotOrbit();
  Halley.plotOrbit();
  Moon.plotOrbit();

  //Star
  sun.plotBody();

  //Plane of Reference
  //noStroke();
  //ellipse(0,0,147,147);
  //stroke(200);
  //strokeWeight(1);

  //Planets
  Mercury.plotPlanet();
  Venus.plotPlanet();
  Earth.plotPlanet();
  Mars.plotPlanet();
  Jupiter.plotPlanet();
  Saturn.plotPlanet();
  Uranus.plotPlanet();
  Neptune.plotPlanet();
  Halley.plotPlanet();

  popMatrix(); //End Transformation. Anything put beyond this line will not be affected by rotation
}


boolean solarSystemNoClick = true; //The mouse is not being clicked down
float[] solarSystemSavedPositions = {0,0,0,0}; //{x-position,y-position,x-angle,y-angle}

//Function that allows everything to be rotated
public void enableRotation(){
  //Conditions:
    //A translate function setting the origin to 500,500
    //Must be wrapped in pushMatrix() and popMatrix()

  if(mousePressed){

    //Since there are no booleans that differentiates between mouseClicked and mousePressed, here is a workaround with the global variable solarSystemNoClick
    if(solarSystemNoClick){ //Checks if the mouse is clicking (and not being held down)
      solarSystemNoClick = false; //the mouse is no longer clicking, it's being held down

      //Gives reference point to original position of mouse
      solarSystemSavedPositions[0] = mouseX;
      solarSystemSavedPositions[1] = -mouseY;
    }

    //Rotates EVERYTHING around the x and y axis by the current x and y position of the mouse
    rotateY(solarSystemSavedPositions[2]+radians((mouseX-solarSystemSavedPositions[0])/10.8f)); //the 10.8 ensures it takes longer to rotate along y-axis
    rotateX(solarSystemSavedPositions[3]+radians((-mouseY-solarSystemSavedPositions[1])/5.4f)); //the 5.4 ensures it's easier to rotate along x-axis to see orbital inclination

  } else{

    //Rotates EVERYTHING according to the last saved angle
    rotateY(solarSystemSavedPositions[2]);
    rotateX(solarSystemSavedPositions[3]);
  }
}

public void solarSystemMouseReleased(){
  //Saves positions when rotation is enabled
  if(solarSystemNoClick == false){ //Ensures this will only be called after rotation
    solarSystemSavedPositions[2] = solarSystemSavedPositions[2]+radians((mouseX-solarSystemSavedPositions[0])/10.8f);
    solarSystemSavedPositions[3] = solarSystemSavedPositions[3]+radians((-mouseY-solarSystemSavedPositions[1])/5.4f);

    solarSystemNoClick = true;
  }
}

float solarSystemZoom = -1;
float solarSystemIsScroll;

//BUG solarSystemZoom code has to be directly put under PhysicsAcademy.pde
/*
void solarSystemMouseWheel() {
  if(spaceMode==1){
    solarSystemIsScroll = event.getCount();
    solarSystemZoom = solarSystemZoom + (solarSystemIsScroll/20);
    if(solarSystemZoom>0){
      solarSystemZoom = solarSystemZoom - (solarSystemIsScroll/20);
    }
    //println(solarSystemZoom);
  }
}
*/

public void solarSystemKeyPressed(){
  Mercury.changeTimeWarp();
  Venus.changeTimeWarp();
  Earth.changeTimeWarp();
  Mars.changeTimeWarp();
  Jupiter.changeTimeWarp();
  Saturn.changeTimeWarp();
  Uranus.changeTimeWarp();
  Neptune.changeTimeWarp();
  Halley.changeTimeWarp();
  Moon.changeTimeWarp();
}
class Star{
  float objectRadius; //km
  float mass; //kg
  float objectColorR;
  float objectColorG;
  float objectColorB;

  Star(int tempObjectRadius, float tempMass, int tempObjectColorR, int tempObjectColorG, int tempObjectColorB){
    objectRadius = tempObjectRadius;
    mass = tempMass;
    objectColorR = tempObjectColorR;
    objectColorG = tempObjectColorG;
    objectColorB = tempObjectColorB;
  }

  public void plotBody(){
    fill(objectColorR,objectColorG,objectColorB);
    if(key=='s'){
      sphere(objectRadius/orbitRadiusScaler);
    } else{
      sphere(objectRadius/15000); //Actual planet itself
    }
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

//TODO use same int-string conversion functions
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

    problem[2] = Float.toString(PApplet.parseInt(force)*PApplet.parseFloat(staticFriction)*0.1f);

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

//IDEA make input box flash after each correct/incorrect (maybe add sound library for sound effects)
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
        if(PApplet.parseFloat(quizModeInputtedAnswer) == PApplet.parseFloat(questionData[2])) quizModeCorrect();
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
  public void settings() {  size(1000, 1000, P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "PhysicsAcademy" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
