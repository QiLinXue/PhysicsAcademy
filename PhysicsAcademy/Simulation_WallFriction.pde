float boxMass, staticFriction, kineticFriction, force, originalStaticFriction, originalKineticFriction;
float a, b, c, d;
int forceScaler = 100;

void initializeWallSimulation(float mass, float sFriction, float kFriction) {
  println(sFriction);
  boxMass = mass;
  staticFriction = sFriction;
  kineticFriction = kFriction;
  force = staticFriction*boxMass*10+10;

  originalStaticFriction = staticFriction;
  originalKineticFriction = kineticFriction;

  a = 390;
  b = 390;
  c = (staticFriction*380+200);
  d = (kineticFriction*380+200);

  Fisica.init(this);

  world = new FWorld();
  world.setEdges();
  world.setEdgesRestitution(0);
  world.setGravity(0, 0);

  box = new FBox(0.6*200, 0.6*200);
  box.setPosition(0.6*106, height/6);
  box.setDensity(1);
  box.setNoStroke();
  box.setFill(119, 125, 167);
  box.setGrabbable(false);
  box.setRotatable(false);
  box.setRestitution(0);
  box.setVelocity(0, 0);
  world.add(box);
  //startSimul = true;
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
      if ((box.getMass()*forceScaler*10)+(-force*staticFriction*forceScaler) > 0) { //Checks if object moves down
        box.addForce(0, box.getMass()*forceScaler*10); //force of gravity

        if (box.getVelocityY() == 0) box.addForce(0, -force*staticFriction*forceScaler); //force of static friction
        else box.addForce(0, -force*kineticFriction*forceScaler); //force of kinetic friction
      }
    }

    //Reset Location
    if (key == 'r'){
        box.setPosition(0.6*106, height/6);
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
  textSize(0.6*30);
  textAlign(LEFT,CENTER);
  text("Static Friction: "+staticFriction, 0.6*350, 0.6*100, 0.6*600, 0.6*50);
  text("Kinetic Friction: "+kineticFriction, 0.6*350, 0.6*150, 0.6*600, 0.6*50);
  text("Mass: "+box.getMass()+" kg", 0.6*350, 0.6*200, 0.6*600, 0.6*50);
  text("Velocity: "+box.getVelocityY()+" m/s", 0.6*350, 0.6*250, 0.6*600, 0.6*50);
  text("Applied Force: "+force+" N", 0.6*350, 0.6*300, 0.6*600, 0.6*50);

  textAlign(CENTER,CENTER);
  text("Press r to reset",0.6*350,0.6*770,0.6*600,0.6*50);
  text("Press SPACE to activate forces",0.6*360,0.6*820,0.6*600,0.6*50);
  text("Press BACKSPACE to return",0.6*350,0.6*870,0.6*600,0.6*50);



  fill(255);
}

void wallForces(){
    //Applied Force
    rect(0.6*350, 0.6*400, 0.6*600, 0.6*20);
    if (mouseX<0.6*950 && mouseX>0.6*350 && mouseY>0.6*390 && mouseY<0.6*430 && mousePressed) {
      a=mouseX;
    }
    rect((a-0.6*20), 0.6*390, 0.6*40, 0.6*40);
    force = (((boxMass*10)/originalStaticFriction+0.6*50)+(a-390)*1.05);
    //println(boxMass,originalStaticFriction);
    //Mass
    rect(0.6*350, 0.6*500, 0.6*600, 0.6*20);
    if (mouseX<0.6*950 && mouseX>0.6*350 && mouseY>0.6*490 && mouseY<0.6*530 && mousePressed) {
      b=mouseX;
    }
    rect((b-0.6*20), 0.6*490, 0.6*40, 0.6*40);
    box.setDensity((boxMass/(100*0.6*0.6)+(b-390)*0.0005));

    //staticFriction
    rect(0.6*350, 0.6*600, 0.6*600, 0.6*20);
    if (mouseX<0.6*950 && mouseX>0.6*350 && mouseY>0.6*590 && mouseY<0.6*630 && mousePressed) {
      c=mouseX;
    }
    rect(c-0.6*20, 0.6*590, 0.6*40, 0.6*40);
    staticFriction = (c-200)/380;

    //KineticFriction
    rect(0.6*350, 0.6*700, 0.6*600, 0.6*20);
    if (mouseX<0.6*950 && mouseX>0.6*350 && mouseY>0.6*690 && mouseY<0.6*730 && mousePressed) {
      d=mouseX;
    }
    rect(d-0.6*20, 0.6*690, 0.6*40, 0.6*40);
    kineticFriction = (d-200)/380;
}
