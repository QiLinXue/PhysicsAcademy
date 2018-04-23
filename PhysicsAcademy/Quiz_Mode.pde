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

String currentUserPassword; //Gets the current password. Currently not needed
int currentUserLength; //Gets the current user length - used for faster iterations

int[] submitColor = {220,220,220}; //The color of the submit button

int overrideHints = 1; //Used to create a safety layer if user accidentally hits sandbox or hint

void learnModeInitialize(){
    empty = loadTable("empty.csv","header");

    nameDataTable = loadTable("userData.csv", "header"); //Initialize name table
    currentUserPassword = nameDataTable.getString(0,activeUser); //Not Needed for now

    generateNewProblem(); //Generate New Problem

    //Reinitialize arrays
    scoreSheetTable = loadTable(activeUser+".csv", "header");
    pastAnswerValidity = scoreSheetTable.getIntColumn("Score");
    pastAnswers = scoreSheetTable.getStringColumn("Answer");
    pastProblems = scoreSheetTable.getStringColumn("Problem");

    //Reset overiding hints
    overrideHints = 1;
}

void quizMode() {

  //background
  background(33, 26, 29);

  fill(100);
  for (int i=pastAnswerValidity.length-1; i>pastAnswerValidity.length-9; i--) {
    switch(pastAnswerValidity[i]){
        case 0: fill(100); break;
        case 1: fill(0,255,100); break;
        case -1: fill(200,0,0); break;
    }
    rect(0.6*((8-pastAnswerValidity.length+i)*120+30), 0.6*20, 0.6*100, 0.6*100);
  }

  //Decor
  noStroke();
  fill(0, 100, 100); //Cyan
  rect(0, 0.6*500, width, 0.6*10);
  rect(0, 0.6*150, width, 0.6*10);

  // Answer Box
  fill(150);
  if(quizModeInAnswerBox) fill(255);
  rect(0.6*300,0.6*550,0.6*400,0.6*80);
  fill(0);

  textAlign(LEFT,(CENTER));
  text(quizModeInputtedAnswer,0.6*300,0.6*560,0.6*400,0.6*60);
  if(quizModeInputtedAnswer.length() < 1 && !quizModeInAnswerBox){
      textAlign(CENTER,CENTER);
      text("CLICK ME TO ANSWER",0.6*300,0.6*560,0.6*400,0.6*60);
  }

  // Question
  textAlign(CENTER, TOP);
  fill(220);
  textFont(questionfont);
  textSize(0.6*30);
  text(questionData[0][0], 0, 0.6*210, width, height);

  //Back Button
  rect(0.6*25,0.6*550,0.6*85,0.6*80);

  //Sandbox
  rect(0.6*140,0.6*550,0.6*85,0.6*80);

  //Submit button
  fill(submitColor[0],submitColor[1],submitColor[2]);
  rect(0.6*775,0.6*550,0.6*85,0.6*80);

  //Button Text
  textAlign(CENTER, CENTER);
  textSize(0.6*18);
  fill(0);
  text("Back",0.6*25,0.6*550,0.6*85,0.6*80);
  text("Sandbox",0.6*140,0.6*550,0.6*85,0.6*80);
  text("Submit",0.6*775,0.6*550,0.6*85,0.6*80);
  textSize(0.6*33); //Reset text size

  //Hints
  //Style
      textAlign(CENTER, CENTER);

      //Hint Button
      for(int i=0;i<4;i++){
          fill(50,100,175);
          rect(0.6*(250*i+25),0.6*650,0.6*200,0.6*50);
          fill(255);
          textAlign(CENTER,CENTER);
          text("Hint "+(i+1),0.6*(250*i+25),0.6*650,0.6*200,0.6*50);
      }

      //Actual Hints
      textSize(0.6*22);
      textAlign(LEFT, TOP);
      if(hintNum>-1 && (overrideHints > 2 || submitColor[2] == 100)) {
          text(questionData[1][hintNum],0.6*25,0.6*725,0.6*950,0.6*250);
      } else if(overrideHints == 2){
          textAlign(CENTER, CENTER);
          text("ARE YOU SURE YOU WANT A HINT OR ENTER SANDBOX MODE. IF YOU CONTINUE BY CLICKING EITHER A HINT OR THE SANDBOX MODE, IT WILL AUTOMATICALLY BE COUNTED AS INCORRECT. YOUR CHOICE",0.6*25,0.6*725,0.6*950,0.6*250);
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
    overrideHints = 1; //Resets overriding hint variable

    //Flash Green
    flashGreen();

    //Reset submit button color
    submitColor[0]=220;
    submitColor[1]=220;
    submitColor[2]=220;

    //Update CSV
    saveQuizDataToCSV();
}

void flashGreen(){
    //Flash Green
    fill(0,50,0);
    rect(0,0,width,height);

    //Update Submit Button color
    submitColor[0]=0;
    submitColor[1]=255;
    submitColor[2]=100;
}

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

    //Update Submit Button Color
    submitColor[0]=200;
    submitColor[1]=0;
    submitColor[2]=0;
}

void quizModeKeyPressed(){

    //Inputting Text
    if(quizModeInAnswerBox && keyCode != BACKSPACE && keyCode != CONTROL && keyCode != SHIFT && keyCode != ENTER){
        quizModeInputtedAnswer += key;
    } else if(quizModeInAnswerBox && keyCode == BACKSPACE && quizModeInputtedAnswer.length() > 0){
        quizModeInputtedAnswer = quizModeInputtedAnswer.substring(0, quizModeInputtedAnswer.length() - 1);
    }

    //Checking answer
    if(quizModeInAnswerBox && keyCode == ENTER){
        if(abs(float(quizModeInputtedAnswer)-float(questionData[0][2]))<0.01){
            if(submitColor[2] == 100) quizModeCorrect();
            else flashGreen();
        }
        else if(submitColor[2] != 100) quizModeIncorrect();
    }

    //Go back if mouse is not in input box
    if(!quizModeInAnswerBox && keyCode == BACKSPACE){
        overrideHints = 1;
        screenMode = 0;
    }

    //CHEATING
    if(key == ' ') println(questionData[0][2]);
}

int hintNum=-1;
int viewHintBeforeTrying=1; // 0=yes, 1=no, 2=cancel

void quizModeMousePressed(){
    //Answer Box
    quizModeInAnswerBox = (mouseX<0.6*700 && mouseX>0.6*300 && mouseY>0.6*550 && mouseY<0.6*630) ? true : false;

    //Go back
    if(mouseX>0.6*25 && mouseX<0.6*110 && mouseY>0.6*550 && mouseY<0.6*630){
        saveQuizDataToCSV();
        overrideHints = 1;
        screenMode = 0;
    }
    //Sandbox
    if(mouseX>0.6*140 && mouseX<0.6*225 && mouseY>0.6*550 && mouseY<0.6*630){
        if(submitColor[2] != 100) overrideHints++;

        //Ensures it has to be pressed two times
        if(overrideHints > 2 || submitColor[2] == 100){
            simulationType = questionData[5][0];
            initializeSimulation();
            screenMode = 1;
            previousScreenMode = 3;
            if(overrideHints > 2) quizModeIncorrect();
        }
    }
    //Submit
    if(mouseX>0.6*775 && mouseX<0.6*860 && mouseY>0.6*550 && mouseY<0.6*630){
      if(abs(float(quizModeInputtedAnswer)-float(questionData[0][2]))<0.01) quizModeCorrect();
      else quizModeIncorrect();
    }

    //Hints
    for(int i=0;i<4;i++){
        if(mouseX<0.6*(250*i+225) && mouseX>0.6*(250*i+25) && mouseY>0.6*650 && mouseY<0.6*700){

            if(submitColor[2] != 100){
                overrideHints++;
            }
            if(overrideHints > 2) quizModeIncorrect();

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
    else if(random>250) questionData = problem3();
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
