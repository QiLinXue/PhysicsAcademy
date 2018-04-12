PFont questionfont;
String[][] questionData = problem1();
Boolean quizModeInAnswerBox = false;
String quizModeInputtedAnswer = "";
Boolean failed = false;
//TODO: make this infinite by loading and uploading a data file
int[] pastCorrectAnswers = {0,0,0,0,0,0,0,0};

void learnMode() {

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
void quizModeCorrect(){
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
    hintNum = -1;
}

//TODO Weed out accidental mistakes
//TODO implement machine learning alg that learns common mistakes
void quizModeIncorrect(){
    if(!failed) {
        for(int i=0;i<7;i++){
            pastCorrectAnswers[i] = pastCorrectAnswers[i+1];
        }
        pastCorrectAnswers[7] = -1;
        failed = true;
    }
    println(questionData[0][2]);

}


void quizModeKeyPressed(){

    //TODO make inputted text more efficient
    if(quizModeInAnswerBox && keyCode != BACKSPACE && keyCode != CONTROL && keyCode != SHIFT && keyCode != ENTER){
        quizModeInputtedAnswer += key;
    } else if(quizModeInAnswerBox && keyCode == BACKSPACE && quizModeInputtedAnswer.length() > 0){
        quizModeInputtedAnswer = quizModeInputtedAnswer.substring(0, quizModeInputtedAnswer.length() - 1);
    }

    //Check answer

    if(quizModeInAnswerBox && keyCode == ENTER){
        //NOTE can't compare strings. Dunno why
        if(abs(float(quizModeInputtedAnswer)-float(questionData[0][2]))<0.01) quizModeCorrect();
        else quizModeIncorrect();
    }

    //CHEATING
    if(key == ' ') println(questionData[0][2]);
}

int hintNum=-1;
int viewHintBeforeTrying=1; // 0=yes, 1=no, 2=cancel


void quizModeMousePressed(){
    //Answer Box
    quizModeInAnswerBox = (mouseX<700 && mouseX>300 && mouseY>550 && mouseY<630) ? true : false;

    //Sandbox
    if(mouseX>25 && mouseX<100 && mouseY>550 && mouseY<630){
        initializeProblem1(float(questionData[4][0]),float(questionData[4][1]),float(questionData[4][2]));
        screenMode = 1;
        previousScreenMode = 3;
    }

    //Hints
    for(int i=0;i<4;i++){
        if(mouseX<250*i+225 && mouseX>250*i+25 && mouseY>650 && mouseY<700){

            if(!failed){
                viewHintBeforeTrying = 1;
                //JOptionPane.showConfirmDialog(null,"Are you sure you want to view a hint? If you do, this will automatically be marked as incorrect.");
                if(viewHintBeforeTrying == 1) quizModeIncorrect();
            }
            hintNum=i;
            break;
        }
    }


}

void quizModeMouseReleased(){}
