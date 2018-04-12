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
FBox box;

public void setup() {
  
  //simulationScreenInitialize();
  solarSystemInitialize();
  learnModeInitialize();

}

int screenMode = 3;
int previousScreenMode;
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
      //sandboxModeKeyActions();
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
      // case 4: solarSystemMouseReleased(); break;
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
/*
float angle_triangle(float x1, float y1, float x2, float y2, float x3, float y3) { //Top Left, Bottom Left, Bottom Right
  return atan((y2-y1)/(x3-x2));
}

float angle_IP(){
  float rise = (inclinedPlaneCoordinates[1][1]-inclinedPlaneCoordinates[0][1]);
  float run = (inclinedPlaneCoordinates[2][0]-inclinedPlaneCoordinates[1][0]);
  return atan(rise/run);
}

float minCoFriction_IP(float radians){
  return tan(radians);
}
*/
public String commonMistakeMessage(String errorCode){
    String message = "MARGIN TOO SMALL";
    switch(errorCode){
        case "FRICTION":
            message = "Make sure to review the different friction types";
            break;
        case "NORMALFORCE":
            message = "Make sure to review the normal force";
            break;
    }
    return message;
}

//TODO use same int-string conversion functions
public String[][] problem1(){
    //TODO make bonus sections for common mistakes
    String[][] problem =
    {
        {"question","question type","answer","question number"}, //Basic Data
        {"hint1","hint2","hint3","hint4"}, //Hints
        {"badAns1","badAns2","badAns3","badAns4"}, //Bad Answers
        {"typeBadAns1","typeBadAns2","typeBadAns3","typeBadAns4"}, //Types of bad answer
        {"data1","data2","data3"} //Important data in order to simulate
    };
    //Question Number
    problem[0][3] = "1";

    //Question Type
    problem[0][1] = "FREE";

    //Stats
    //TODO: make this more efficient (find easier bug fix)
    String mass = Integer.toString(floor(random(1,50)));
    String kineticFriction = Float.toString(PApplet.parseInt(random(2,7)));
    String staticFriction = Float.toString(PApplet.parseInt(random(PApplet.parseInt(kineticFriction)+1,8)));
    String force = Integer.toString(floor(random(1,50))+Integer.parseInt(mass)*100/PApplet.parseInt(staticFriction));

    problem[4][0] = mass;
    problem[4][1] = Float.toString(PApplet.parseFloat(staticFriction)/10);
    problem[4][2] = Float.toString(PApplet.parseFloat(kineticFriction)/10);


    //Question
    problem[0][0] = "A book of mass "+mass+"kg is held to a vertical wall by a person's hand applying a "+force+"N force directly toward the wall. The wall has a static friction coefficient of 0."+staticFriction.charAt(0)+" and a kinetic friction coefficient of 0."+kineticFriction.charAt(0)+". With the book held at rest, what is the frictional force keeping the book from sliding down the wall? Round to the nearest unit";

    //Answer
    problem[0][2] = Float.toString(round(PApplet.parseInt(force)*PApplet.parseFloat(staticFriction)*0.1f));

    //Hints
    //TODO: Transfer this to data file
    problem[1][0] = "In this problem, the phrase 'what is the frictional force keeping the book from sliding down the wall' suggests that the box is stationary. Therefore, static friction exists but there is no kinetic friction";
    problem[1][1] = "Since the wall is perfectly vertical, there will be no normal force that is generated from the weight of the book. Therefore, the mass of the book is irrelevant.";
    problem[1][2] = "The normal force generated from applying a perpendicular force to the wall is just the force applied. Therefore, the normal force from the force applied is "+force+" Newtons.";
    problem[1][3] = "The frictional force is equal to product of the coefficient of friction (static/kinetic) and the normal force";

    //Bad Answer
    problem[2][0] = Float.toString(round(PApplet.parseInt(force)*PApplet.parseFloat(kineticFriction)*0.1f));
    problem[3][0] = "FRICTION";

    problem[2][1] = Float.toString(round(PApplet.parseInt(mass)*PApplet.parseFloat(staticFriction)*0.1f));
    problem[3][1] = "NORMALFORCE";

    problem[2][2] = "99999";
    problem[3][2] = "NULL";

    problem[2][3] = "99999";
    problem[3][3] = "NULL";
    return problem;
}

public String[][] problem2(){
    //TODO make bonus sections for common mistakes
    String[][] problem =
    {
        {"question","question type","answer","question number"}, //Basic Data
        {"hint1","hint2","hint3","hint4"}, //Hints
        {"badAns1","badAns2","badAns3","badAns4"}, //Bad Answers
        {"typeBadAns1","typeBadAns2","typeBadAns3","typeBadAns4"}, //Types of bad answer
        {"data1","data2","data3"} //Important data in order to simulate
    };
    //Question Number
    problem[0][3] = "2";

    //Question Type
    problem[0][1] = "FREE";

    //Stats
    String mass = Integer.toString(floor(random(1,50)));
    String kineticFriction = Float.toString(PApplet.parseInt(random(2,7)));
    String staticFriction = Float.toString(PApplet.parseInt(random(PApplet.parseInt(kineticFriction)+1,8)));

    problem[4][0] = mass;
    problem[4][1] = Float.toString(PApplet.parseFloat(staticFriction)/10);
    problem[4][2] = Float.toString(PApplet.parseFloat(kineticFriction)/10);

    //Question
    problem[0][0] = "A box of mass "+mass+"kg is held to a vertical wall by a person's hand applying a force 'F' directly toward the wall. The wall has a static friction coefficient of 0."+staticFriction.charAt(0)+" and a kinetic friction coefficient of 0."+kineticFriction.charAt(0)+". With the book held at rest, what is the minimum force needed to ensure the box is at rest? Assume g = -10m/s^2 and round to the nearest unit.";

    //Answer
    problem[0][2] = Float.toString(round(PApplet.parseInt(mass)*100/PApplet.parseInt(staticFriction)));

    //Hints
    //TODO: Transfer this to data file
    problem[1][0] = "In this problem, the phrase 'With the book held at rest' suggests that the box is stationary. Therefore, static friction exists but there is no kinetic friction";
    problem[1][1] = "Since the wall is perfectly vertical, there will be no normal force that is generated from the weight of the book. The only normal force generated is from the force applied, which you are trying to find";
    problem[1][2] = "Since the box isn't moving, the frictional force cancels out the weight of the box. The weight of the box is mg = " + Integer.toString((PApplet.parseInt(mass)*10));
    problem[1][3] = "The frictional force is equal to product of the coefficient of friction (static/kinetic) and the normal force.";

    //Bad Answer
    problem[2][0] = Float.toString(PApplet.parseInt(mass)*100);
    problem[3][0] = "FRICTION";

    problem[2][1] = Float.toString(PApplet.parseInt(mass)*10);
    problem[3][1] = "FRICTION";

    problem[2][2] = Float.toString(round(PApplet.parseInt(mass)*100/PApplet.parseInt(kineticFriction)));
    problem[3][2] = "FRICTION";

    problem[2][3] = Float.toString(round(PApplet.parseInt(mass)*10/PApplet.parseInt(kineticFriction)));
    problem[3][3] = "WEIGHT";
    return problem;
}
Table scoreSheetTable;
PFont questionfont;


String[][] questionData; //Gathers the data for the current question
Boolean quizModeInAnswerBox = false; //Determines if user mouse has clicked the input box
String quizModeInputtedAnswer = ""; //Variable for the input user gives
Boolean quizModeAlreadyFailed = false; //Determines if the user has already failed that question
int[] pastAnswers; //An infinite list from "score.csv" which displays the validity of past answers
String[] pastProblems; //An infinite list from "score.csv" which displays the problem number for past problems

public void learnModeInitialize(){
    generateNewProblem();
    scoreSheetTable = loadTable("score.csv", "header");
    pastAnswers = scoreSheetTable.getIntColumn("User 1 Score");
    pastProblems = scoreSheetTable.getStringColumn("User 1 Problem");
}

public void learnMode() {

  //background
  background(33, 26, 29);

  //History (Past Answers Correct/Incorrect)
  //TODO: enable this feature

  fill(100);
  for (int i=pastAnswers.length-1; i>pastAnswers.length-9; i--) {
    switch(pastAnswers[i]){
        case 0: fill(100); break;
        case 1: fill(0,255,100); break;
        case -1: fill(200,0,0); break;
    }
    rect((8-pastAnswers.length+i)*120+30, 20, 100, 100);
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
  textAlign(CENTER, TOP);
  fill(220);
  textSize(33);
  textFont(questionfont);
  text(questionData[0][0], 0, 210, width, height);

  //Sandbox
  rect(25,550,85,80);

  //Hints
  //Style
      textAlign(CENTER, CENTER);

      //Hint Button
      for(int i=0;i<4;i++){
          fill(50,100,175);
          rect(250*i+25,650,200,50);
          fill(255);
          textAlign(CENTER,CENTER);
          text("Hint "+(i+1),250*i+25,650,200,50);
      }

      //Actual Hints
      textSize(22);
      textAlign(LEFT, TOP);
      if(hintNum>-1) {
          //println("hello");
          text(questionData[1][hintNum],25,725,950,250);
      }
}

//IDEA make input box flash after each correct/incorrect (maybe add sound library for sound effects)
//NOTE quizModeCorrect and quizMode Incorrect could be integrated below depending on how long the code is
public void quizModeCorrect(){
    generateNewProblem();
    quizModeInputtedAnswer = "";

    //Shift history colors down
    if(!quizModeAlreadyFailed){
        pastAnswers = (int[])append(pastAnswers,1);
        pastProblems = (String[])append(pastProblems,questionData[0][3]);
    }
    else{
        quizModeAlreadyFailed = false;
    }
    hintNum = -1;
}

//TODO implement machine learning alg that learns common mistakes
public void quizModeIncorrect(){
    if(!quizModeAlreadyFailed) {
        pastAnswers = (int[])append(pastAnswers,-1);
        pastProblems = (String[])append(pastProblems,questionData[0][3]);
        quizModeAlreadyFailed = true;
    }
    println(questionData[0][2]);
}

public void quizModeKeyPressed(){

    //Inputting Text
    //TODO make inputted text more efficient
    if(quizModeInAnswerBox && keyCode != BACKSPACE && keyCode != CONTROL && keyCode != SHIFT && keyCode != ENTER){
        quizModeInputtedAnswer += key;
    } else if(quizModeInAnswerBox && keyCode == BACKSPACE && quizModeInputtedAnswer.length() > 0){
        quizModeInputtedAnswer = quizModeInputtedAnswer.substring(0, quizModeInputtedAnswer.length() - 1);
    }

    //Checking answer
    if(quizModeInAnswerBox && keyCode == ENTER){
        //NOTE can't compare strings. Dunno why
        if(abs(PApplet.parseFloat(quizModeInputtedAnswer)-PApplet.parseFloat(questionData[0][2]))<0.01f) quizModeCorrect();
        else quizModeIncorrect();
    }

    //Saving to CSV
    if(keyCode == TAB){
        for(int i=0;i<pastAnswers.length;i++){
            scoreSheetTable.setInt(i, "User 1 Score", pastAnswers[i]);
            scoreSheetTable.setString(i, "User 1 Problem", pastProblems[i]);
        }

        saveTable(scoreSheetTable, "data/score.csv" );

    }


    //CHEATING
    if(key == ' ') println(questionData[0][2]);
}

int hintNum=-1;
int viewHintBeforeTrying=1; // 0=yes, 1=no, 2=cancel

public void quizModeMousePressed(){
    //Answer Box
    quizModeInAnswerBox = (mouseX<700 && mouseX>300 && mouseY>550 && mouseY<630) ? true : false;

    //Sandbox
    if(mouseX>25 && mouseX<100 && mouseY>550 && mouseY<630){
        initializeProblem1(PApplet.parseFloat(questionData[4][0]),PApplet.parseFloat(questionData[4][1]),PApplet.parseFloat(questionData[4][2]));
        screenMode = 1;
        previousScreenMode = 3;
    }

    //Hints
    for(int i=0;i<4;i++){
        if(mouseX<250*i+225 && mouseX>250*i+25 && mouseY>650 && mouseY<700){

            if(!quizModeAlreadyFailed){
                viewHintBeforeTrying = 1;
                //JOptionPane.showConfirmDialog(null,"Are you sure you want to view a hint? If you do, this will automatically be marked as incorrect.");
                if(viewHintBeforeTrying == 1) quizModeIncorrect();
            }
            hintNum=i;
            break;
        }
    }
}

public void quizModeMouseReleased(){}

public void generateNewProblem(){
    if(random(0,100)>50){
        questionData = problem1();
    } else{
        questionData = problem2();
    }
}
float boxMass, staticFriction, kineticFriction, force, originalStaticFriction, originalKineticFriction;
int scaler = 100;
float a, b, c, d;
boolean startSimul = false;

public void simulationMode() {
  if(startSimul) problem1Simulation();
}

public void initializeProblem1(float mass, float sFriction, float kFriction) {

  boxMass = mass;
  staticFriction = sFriction;
  kineticFriction = kFriction;
  force = staticFriction*boxMass*10+10;

  originalStaticFriction = staticFriction;
  originalKineticFriction = kineticFriction;

  a = 650;
  b = 650;
  c = staticFriction*900+200;
  d = kineticFriction*900+200;
  Fisica.init(this);

  world = new FWorld();
  world.setEdges();
  world.setEdgesRestitution(0);
  world.setGravity(0, 0);

  box = new FBox(200, 200);
  box.setPosition(106, height/6);
  box.setDensity(1);
  box.setNoStroke();
  box.setFill(119, 125, 167);
  box.setGrabbable(true);
  box.setRotatable(false);
  box.setRestitution(0);
  box.setVelocity(0, 0);
  world.add(box);
  startSimul = true;
}

public void problem1Simulation() {
  background(255);
  world.step();
  world.draw();

  if (keyPressed) {
    if (key == ' ') {
      if ((box.getMass()*scaler*10)+(-force*staticFriction*scaler) > 0) {
        box.addForce(0, box.getMass()*scaler*10);
        if (box.getVelocityY() == 0) box.addForce(0, -force*staticFriction*scaler);
        else box.addForce(0, -force*kineticFriction*scaler);
      } else if (box.getVelocityY()<0) {
        box.addForce(0, -force*kineticFriction*scaler);
      }
    }
    if (keyCode == BACKSPACE) screenMode = previousScreenMode;
  }
  stroke(0);

  //Applied Force
  rect(350, 500, 600, 20);
  if (mouseX<950 && mouseX>350 && mouseY>500-10 && mouseY<500+30 && mousePressed) {
    a=mouseX;
  }
  rect(a-20, 500-10, 40, 40);
  force = ((boxMass*10)/originalStaticFriction+50)+(a-650)*1.05f;

  //Mass
  rect(350, 600, 600, 20);
  if (mouseX<950 && mouseX>350 && mouseY>600-10 && mouseY<600+30 && mousePressed) {
    b=mouseX;
  }
  rect(b-20, 600-10, 40, 40);
  box.setDensity((boxMass/100+(b-650)*0.0005f));

  //staticFriction
  rect(350, 700, 600, 20);
  if (mouseX<950 && mouseX>350 && mouseY>700-10 && mouseY<700+30 && mousePressed) {
    c=mouseX;
  }
  rect(c-20, 700-10, 40, 40);
  staticFriction = (c-200)*0.0011111111111f;

  //KineticFriction
  rect(350, 800, 600, 20);
  if (mouseX<950 && mouseX>350 && mouseY>800-10 && mouseY<800+30 && mousePressed) {
    d=mouseX;
  }
  rect(d-20, 800-10, 40, 40);
  kineticFriction = (d-200)*0.0011111111111f;

  fill(0);
  textSize(30);
  text("Static Friction: "+staticFriction, 500, 100, 900, 50);
  text("Kinetic Friction: "+kineticFriction, 500, 150, 900, 50);
  text("Mass: "+box.getMass()+" kg", 500, 200, 900, 50);
  text("Velocity: "+box.getVelocityY()+" m/s", 500, 250, 900, 50);
  text("Applied Force: "+force+" N", 500, 300, 900, 50);


  fill(255);
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
