//import java.lang.*;

void homeScreen(){
    background(50);

    //Standard Buttons
    fill(255);
    rect(50,500,900,80);
    rect(50,610,900,80);
    rect(50,720,900,80);
    rect(50,830,900,80);

    textAlign(CENTER,CENTER);
    fill(0);
    textSize(40);
    text("Sandbox (not working)",50,500,900,80);
    text("Quiz",50,610,900,80);
    text("Solar System",50,720,900,80);

    //Login Button
    fill(200,250,200);
    rect(290,50,200,60);
    rect(510,50,200,60);

    fill(0);
    text("Login",290,50,200,60);
    text("Register",510,50,200,60);

}

void homeScreenMousePressed(){
    if(mouseX>50 && mouseX < 950 && mouseY > 500 && mouseY < 680) println("doesn't work");
    if(mouseX>50 && mouseX < 950 && mouseY > 610 && mouseY < 690) screenMode = 3;
    if(mouseX>50 && mouseX < 950 && mouseY > 720 && mouseY < 800) screenMode = 4;
    if(mouseX>290 && mouseX < 490 && mouseY > 50 && mouseY < 110) login();
    if(mouseX>510 && mouseX < 710 && mouseY > 50 && mouseY < 110) register();
}
String activeUser = "";
String inputtedUser = "User 2";
void login(){
    //scoreSheetTable = loadTable("score.csv", "header");
    //println(scoreSheetTable.getColumnCount());



     for(int i=0;i<scoreSheetTable.getColumnCount();i+=3){
        if(scoreSheetTable.getColumnTitle(i).contains(inputtedUser)){
            activeUser = inputtedUser;
            println(activeUser);
            break;
        }
    }
}


void register(){}
