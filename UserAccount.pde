//Below list transfers the csv data to a simple array, to save time during for loops
int[] currentUserAnswerScore, currentUserAnswerProblem;
int numberOfUserResponses;

//Below list will contain data about number of correct answers, number of incorrect answers, and the questions failed, seperated into the different quesiton categories (friction,kinematics,space), then the last part will be seperated into the different question categories, but listed in total
float[] currentUserStats;

float[] failPercentages; //A more detailed version which uses currentUserStats to generate actual statistics

void userAccountIntialize(){

    //Stat Lists Declared
    currentUserStats = new float[2+2*3];
    failPercentages = new float[3];

    //Transfers raw data onto lists
    currentUserAnswerScore = scoreSheetTable.getIntColumn("Score");
    currentUserAnswerProblem = scoreSheetTable.getIntColumn("Problem");

    //Helps reduce for loop time
    numberOfUserResponses = scoreSheetTable.getRowCount();

    //Updates Statistics
    for(int i=8;i<numberOfUserResponses;i++){

        if(currentUserAnswerScore[i] == 1) currentUserStats[0]++;
        else{
            currentUserStats[1]++;
            switch(currentUserAnswerProblem[i]){
                case 1: currentUserStats[2]++; break;
                case 2: currentUserStats[2]++; break;
                case 3: currentUserStats[3]++; break;
                case 4: currentUserStats[4]++; break;
            }
        }

        switch(currentUserAnswerProblem[i]){
            case 1: currentUserStats[5]++; break;
            case 2: currentUserStats[5]++; break;
            case 3: currentUserStats[6]++; break;
            case 4: currentUserStats[7]++; break;
        }
    }

    //More detailed statistics about percentage of failed questions by type
    for(int i=0;i<failPercentages.length;i++){
        failPercentages[i] = (currentUserStats[2+i]/currentUserStats[2+i+3]);
        if(currentUserStats[2+i] == 0 && currentUserStats[2+i+3] == 0) failPercentages[i] = 0;
  }
    screenMode = 5;
}

void userAccountMode(){
    background(33, 26, 29);

    //Upper Title
    noStroke();
    fill(150,0,255); //TODO: Better color
    rect(25,25,950,100,100);

    //Title Text
    fill(0);
    textAlign(CENTER,CENTER);
    textSize(50);
    text("User Account for " + activeUser,25,25,950,100);

    //Questions wrong by type graph
    pieChart(200,300,300,failPercentages);

    //Questions wrong by type - legend Box
    fill(255);
    rect(400,150,50,50,100);

    fill(255-50);
    rect(400,210,50,50,100);

    fill(255-100);
    rect(400,270,50,50,100);

    //Questions wrong by type - legend Text
    fill(255);
    textSize(25);
    textAlign(LEFT,CENTER);

    text("Friction Problems - "  + failPercentages[0]*100 + "% Wrong",460,125,500,100);
    text("Kinematic Problems - " + failPercentages[1]*100 + "% Wrong",460,185,500,100);
    text("Space Problems - "     + failPercentages[2]*100 + "% Wrong",460,245,500,100);

    //Overall Statistics
    fill(255);
    rect(50,500,400,70,100); //Lifetime Questions
    rect(550,500,400,70,100); //Total Correct Answers

    fill(0);
    textAlign(CENTER,CENTER);
    textSize(25);
    text("Lifetime Questions: " + (numberOfUserResponses-8),50,500,400,70);
    text(
        "Total Correct Answers: " +
            (numberOfUserResponses-8-
             currentUserStats[2]-
             currentUserStats[3]-
             currentUserStats[4]
            )
        ,550,500,400,70
        );

}

void userAccountKeyPressed(){
    if(keyCode == BACKSPACE) screenMode = 0;
}

void userAccountMousePressed(){

}

//Taken off Processing Website. I'm pretty sure I can write a similar code
void pieChart(int x, int y, float diameter, float[] data) {

  //Local Variables
  float sum = 0;
  float[] angles = new float[data.length];

  for(int i=0;i<data.length;i++) sum+=data[i]; //Gets the sum
  for(int i=0;i<angles.length;i++) angles[i] = (data[i]/sum)*360; //Converts data into percentages

  float lastAngle = 0;

  //Draws the arc
  for (int i = 0; i < angles.length; i++) {
    float graphColor = 255-i*50; //TODO create better colors
    fill(graphColor);
    arc(x, y, diameter, diameter, lastAngle, lastAngle+radians(angles[i]));
    lastAngle += radians(angles[i]);
  }

}