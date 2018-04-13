float boxMass, staticFriction, kineticFriction, force, originalStaticFriction, originalKineticFriction;
int scaler = 100;
float a, b, c, d;
boolean startSimul = false;

void simulationMode() {
  if(startSimul) wallSimulation();
}

void initializeWallSimulation(float mass, float sFriction, float kFriction) {

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
  box.setGrabbable(false);
  box.setRotatable(false);
  box.setRestitution(0);
  box.setVelocity(0, 0);
  world.add(box);
  startSimul = true;
}

void wallSimulation() {

  //Setup
  background(255);
  world.step();
  world.draw();

  //Enables and resets Forces
  if (keyPressed) {

    //Enables All Forces
    if (key == ' ') {
      if ((box.getMass()*scaler*10)+(-force*staticFriction*scaler) > 0) { //Checks if object moves down
        box.addForce(0, box.getMass()*scaler*10); //force of gravity
        if (box.getVelocityY() == 0) box.addForce(0, -force*staticFriction*scaler); //force of static friction
        else box.addForce(0, -force*kineticFriction*scaler); //force of kinetic friction
      }
    }

    //Reset Location
    if (key == 'r'){
        box.setPosition(106, height/6);
        box.setVelocity(0,0);
    }

    //Move Back
    if (keyCode == BACKSPACE) screenMode = previousScreenMode;

  }

  stroke(0);

  //Force Calculation Draggers
  wallForces();

  //Text
  fill(0);
  textSize(30);
  text("Static Friction: "+staticFriction, 500, 100, 900, 50);
  text("Kinetic Friction: "+kineticFriction, 500, 150, 900, 50);
  text("Mass: "+box.getMass()+" kg", 500, 200, 900, 50);
  text("Velocity: "+box.getVelocityY()+" m/s", 500, 250, 900, 50);
  text("Applied Force: "+force+" N", 500, 300, 900, 50);



  fill(255);
}

void wallForces(){
    //Applied Force
    rect(350, 500, 600, 20);
    if (mouseX<950 && mouseX>350 && mouseY>500-10 && mouseY<500+30 && mousePressed) {
      a=mouseX;
    }
    rect(a-20, 500-10, 40, 40);
    force = ((boxMass*10)/originalStaticFriction+50)+(a-650)*1.05;

    //Mass
    rect(350, 600, 600, 20);
    if (mouseX<950 && mouseX>350 && mouseY>600-10 && mouseY<600+30 && mousePressed) {
      b=mouseX;
    }
    rect(b-20, 600-10, 40, 40);
    box.setDensity((boxMass/100+(b-650)*0.0005));

    //staticFriction
    rect(350, 700, 600, 20);
    if (mouseX<950 && mouseX>350 && mouseY>700-10 && mouseY<700+30 && mousePressed) {
      c=mouseX;
    }
    rect(c-20, 700-10, 40, 40);
    staticFriction = (c-200)*0.0011111111111;

    //KineticFriction
    rect(350, 800, 600, 20);
    if (mouseX<950 && mouseX>350 && mouseY>800-10 && mouseY<800+30 && mousePressed) {
      d=mouseX;
    }
    rect(d-20, 800-10, 40, 40);
    kineticFriction = (d-200)*0.0011111111111;
}
