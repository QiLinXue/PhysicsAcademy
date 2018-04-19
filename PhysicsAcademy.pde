import fisica.*;

//Sandbox Objects
FWorld world;
FPoly inclinedPlane;
FBox box;

void setup() {
  questionfont = createFont("Montserrat-Regular.ttf", 30);

  size(1000, 1000, P3D);
  println("Welcome back " + activeUser + "! It's nice to see you again.");
  homeScreenInputText = ("Welcome back " + activeUser + "! It's nice to see you again.");
  
  solarSystemInitialize();
  learnModeInitialize();

  //Temp
  userAccountIntialize();
  screenMode = 0;
}

int screenMode = 0;
int previousScreenMode;

void draw() {
  switch(screenMode) {

  case 0: homeScreen(); break;
  case 1: simulationMode(); break;
  case 2: drawMode(); break;   //TODO make drawMode
  case 3: learnMode(); break;
  case 4: solarSystemMode(); break;
  case 5: userAccountMode(); break;

  }

}

void drawMode() {
  background(255);
}

void keyPressed() {
  switch(screenMode){
      case 0: homeScreenKeyPressed(); break;
      case 3: quizModeKeyPressed(); break;
      case 4: solarSystemKeyPressed(); break;
      case 5: userAccountKeyPressed(); break;

  }
}

void mousePressed() {
  switch(screenMode){
      case 0: homeScreenMousePressed(); break;
      case 1: simulationMousePressed(); break;
      case 3: quizModeMousePressed(); break;
      case 5: userAccountMousePressed(); break;

  }
}

void mouseReleased() {
  switch(screenMode){
      case 3: quizModeMouseReleased(); break;
      case 4: solarSystemMouseReleased(); break;
 }
}

void mouseWheel(MouseEvent event) {
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
        case 1:
            if(simulationType == "SPACE_SIMULATION"){
                solarSystemIsScroll = event.getCount();
                solarSystemZoom = solarSystemZoom + (solarSystemIsScroll/20);
                if(solarSystemZoom>0){
                  solarSystemZoom = solarSystemZoom - (solarSystemIsScroll/20);
                }
            }

   }
}