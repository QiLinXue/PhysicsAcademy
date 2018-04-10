import fisica.*;
import javax.swing.JOptionPane;

//Sandbox Objects
FWorld world;
FPoly inclinedPlane;
FCircle b;
FBox box;

void setup() {
  size(1000, 1000);

  simulationScreenInitialize();

}

int screenMode = 3;
void draw() {
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

void drawMode() {
  background(255);
}

void keyPressed() {
  switch(screenMode){
      case 1:
        sandboxModeKeyActions();
        break;
      case 3:
        quizModeKeyPressed();
        break;
  }
}

void mousePressed() {
  switch(screenMode){
      case 1:
        //sandboxModeMousePressed();
        break;
      case 3:
        quizModeMousePressed();
        break;
  }
}

void mouseReleased() {
  switch(screenMode){
      case 1:
        //sandboxModeMouseReleased();
        break;
      case 3:
        quizModeMouseReleased();
        break;
 }
}
