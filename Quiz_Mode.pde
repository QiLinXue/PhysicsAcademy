Table scoreSheetTable;
Table nameDataTable;
Table empty;

PFont questionfont;

String[][] questionData; //Gathers the data for the current question
Boolean quizModeInAnswerBox = false; //Determines if user mouse has clicked the input box
String quizModeInputtedAnswer = ""; //Variable for the input user gives
Boolean quizModeAlreadyFailed = false; //Determines if the user has already failed that question
int[] pastAnswerValidity ={}; //An infinite list from activeUser which displays the validity of past answers
String[] pastProblems={}; //An infinite list from activeUser which displays the problem number for past problems
String[] pastAnswers={}; //An infinite list from activeUser which displays the user submitted answer

String currentUserPassword;
int currentUserLength;

void learnModeInitialize(){
    empty = loadTable("empty.csv","header");

    nameDataTable = loadTable("userData.csv", "header");
    currentUserPassword = nameDataTable.getString(0,activeUser); //Not Needed for now

    generateNewProblem(); //Generate New Problem
    scoreSheetTable = loadTable(activeUser+".csv", "header");
    pastAnswerValidity = scoreSheetTable.getIntColumn("Score");
    pastAnswers = scoreSheetTable.getStringColumn("Problem");
    pastProblems = scoreSheetTable.getStringColumn("Answer");
}

void learnMode() {

  //background
  background(33, 26, 29);

  //History (Past Answers Correct/Incorrect)
  //TODO: enable this feature

  fill(100);
  for (int i=pastAnswerValidity.length-1; i>pastAnswerValidity.length-9; i--) {
    switch(pastAnswerValidity[i]){
        case 0: fill(100); break;
        case 1: fill(0,255,100); break;
        case -1: fill(200,0,0); break;
    }
    rect((8-pastAnswerValidity.length+i)*120+30, 20, 100, 100);
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

  //Back Button
  rect(25,550,85,80);

  //Sandbox
  rect(140,550,85,80);

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
          text(questionData[1][hintNum],25,725,950,250);
      }
}

void quizModeCorrect(){
    generateNewProblem();

    //Update colors if there were no failed attempts
    if(!quizModeAlreadyFailed){
        pastAnswerValidity = (int[])append(pastAnswerValidity,1);
        pastProblems = (String[])append(pastProblems,questionData[0][3]);
        pastAnswers = (String[])append(pastAnswers,quizModeInputtedAnswer);
    }

    //Changes the question if there has been at least 1 failed attempt
    else{
        quizModeAlreadyFailed = false;
    }

    hintNum = -1; //Resets the hint
    quizModeInputtedAnswer = ""; //Resets the answer box

    //Flash Green
    fill(0,50,0);
    rect(0,0,width,height);

    //Update CSV
    saveQuizDataToCSV();
}

//TODO implement machine learning alg that learns common mistakes
void quizModeIncorrect(){

    if(!quizModeAlreadyFailed) {
        pastAnswerValidity = (int[])append(pastAnswerValidity,-1);
        pastProblems = (String[])append(pastProblems,questionData[0][3]);
        pastAnswers = (String[])append(pastAnswers,quizModeInputtedAnswer);
        quizModeAlreadyFailed = true;

        //Update CSV
        saveQuizDataToCSV(); //However, this loses progress if user exits the screen or closes program and when the user logs back in, it'll be a different problem.
    }

    //Flash Red
    fill(50,0,0);
    rect(0,0,width,height);
}

void quizModeKeyPressed(){

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
        if(abs(float(quizModeInputtedAnswer)-float(questionData[0][2]))<0.01) quizModeCorrect();
        else quizModeIncorrect();
    }

    //Saving to CSV
    if(keyCode == TAB){
        saveQuizDataToCSV();
    }

    //CHEATING
    if(key == ' ') println(questionData[0][2]);
}

int hintNum=-1;
int viewHintBeforeTrying=1; // 0=yes, 1=no, 2=cancel

void quizModeMousePressed(){
    //Answer Box
    quizModeInAnswerBox = (mouseX<700 && mouseX>300 && mouseY>550 && mouseY<630) ? true : false;

    //Go back
    if(mouseX>25 && mouseX<110 && mouseY>550 && mouseY<630){
        saveQuizDataToCSV();
        screenMode = 0;
    }
    //Sandbox
    if(mouseX>140 && mouseX<225 && mouseY>550 && mouseY<630){
        simulationType = questionData[5][0];
        initializeSimulation();
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

void quizModeMouseReleased(){}

void generateNewProblem(){
    int random = int(random(0,1000));
    if(random>750) questionData = problem1();
    else if(random>500) questionData = problem2();
    else if(random>255) questionData = problem3();
    else if(random>0) questionData = problem4();
}

void saveQuizDataToCSV(){
    currentUserLength = pastAnswerValidity.length;
    for(int i=0;i<currentUserLength;i++){
        scoreSheetTable.setInt(i, "Score", pastAnswerValidity[i]);
        scoreSheetTable.setString(i, "Problem", pastProblems[i]);
        scoreSheetTable.setString(i, "Answer", pastAnswers[i]);
    }

    saveTable(scoreSheetTable, "data/"+activeUser+".csv" );

}
