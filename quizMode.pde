PFont questionfont;
String[] questionData = problem1();
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
  textAlign(CENTER);
  fill(220);
  textSize(33);
  textFont(questionfont);
  text(questionData[0], 0, 210, width, height);

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
        if(float(quizModeInputtedAnswer) == float(questionData[2])) quizModeCorrect();
        else quizModeIncorrect();
    }
}

void quizModeMousePressed(){
    quizModeInAnswerBox = (mouseX<700 && mouseX>300 && mouseY>550 && mouseY<630) ? true : false;
}

void quizModeMouseReleased(){

}
