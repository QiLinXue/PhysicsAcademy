import fisica.*;
import javax.swing.JOptionPane;

//Sandbox Objects
FWorld world;
FPoly inclinedPlane;
FBox box;

void setup() {
  size(1000, 1000, P3D);
  //simulationScreenInitialize();
  solarSystemInitialize();
  learnModeInitialize();

}

int screenMode = 3;
int previousScreenMode;
void draw() {
  switch(screenMode) {

  case 1: simulationMode(); break;
  case 2: drawMode(); break;   //TODO make drawMode
  case 3: learnMode(); break;
  case 4: solarSystemMode(); break;

  }

}

void drawMode() {
  background(255);
}

void keyPressed() {
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

void mousePressed() {
  switch(screenMode){
      case 3: quizModeMousePressed(); break;
  }
}

void mouseReleased() {
  switch(screenMode){
      case 3: quizModeMouseReleased(); break;
      // case 4: solarSystemMouseReleased(); break;
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
   }
}
