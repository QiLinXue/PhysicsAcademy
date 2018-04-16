//Initalize
Planet AnyPlanet;
Star star;

void solarSystemInitialize(float radius, float mass, float perigee, float apogee){
     star = new Star(695700,1.989E30,213,182,10);
     AnyPlanet = new Planet(radius,mass,perigee,apogee,0,0,star.mass);

     //These variables are all declared and shared with the complete sandbox version
     timeticker = 0;
     orbitRadiusScaler = 600000000;
     planetSizeScaler = 600;
     solarSystemZoom = -1;
}

void solarSystemSimulation(){
  //Background Setup
  background(30);
  timeticker++; //global increment that increases each frame

  //Complexity of Planet and Sun
  sphereDetail(10);
  //pointLight(213,182,10, width/2, height/2, 0);

  pushMatrix(); //Begin Transformation
  translate(width/2,height/2); //Sets the center to the middle of the screen
  scale(solarSystemZoom); //Scales it up or down

  //Planet Orbit Line
  AnyPlanet.plotOrbit();

  //Star
  star.plotBody();

  //Planet
  AnyPlanet.plotPlanet();

  popMatrix(); //End Transformation. Anything put beyond this line will not be affected by rotation

  //Warps Time
  if(keyPressed) AnyPlanet.changeTimeWarp();
  textSize(40);
  fill(255);

  //Information
  text("Altitude: "+(AnyPlanet.altitude(AnyPlanet.angle()*2)) +" metres",50,50,900,200);

  if(keyPressed && keyCode == BACKSPACE) screenMode = previousScreenMode;
}
