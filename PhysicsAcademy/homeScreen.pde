//import java.lang.*;
String homeScreenInputText = "";
void homeScreenMode(){
    background(50);

    //Standard Buttons
    fill(255);
    rect(50,500,900,80,200);
    rect(50,610,900,80,200);
    rect(50,720,900,80,200);
    rect(50,830,900,80,200);

    //Text
    textAlign(CENTER,CENTER);
    fill(0);
    textSize(40);
    text("User Account",50,500,900,80);
    text("Quiz",50,610,900,80);
    text("Solar System",50,720,900,80);
    text("HELP",50,830,900,80);

    //Login Button
    fill(200,250,200);
    rect(290,50,200,60,50);
    rect(510,50,200,60,50);

    fill(0);
    text("Login",290,50,200,60);
    text("Register",510,50,200,60);

    displayBox(50,130,900,100,homeScreenInputText,30);
}

void homeScreenModeMousePressed(){
    //Makes sure following programs can go back
    previousScreenMode = 1;

    //If an action interrupts the log in / register process
    if(mouseY > 500 && mouseY < 800 && homeScreenTypeMode != 0){
        println("login/register failed. Please try again");
        homeScreenInputText = ("login/register failed. Please try again");
        //Reset User Initalizers
        inputtedUser = "";
        inputtedPassword = "";
        newUser = "";
        newPassword = "";

        homeScreenTypeMode = 0; //A cheaty way of resetting the login and register button
    }
    if(mouseX>50 && mouseX < 950 && mouseY > 500 && mouseY < 580) userAccountIntialize(); //screenMode = 5;
    if(mouseX>50 && mouseX < 950 && mouseY > 610 && mouseY < 690) screenMode = 3;
    if(mouseX>50 && mouseX < 950 && mouseY > 720 && mouseY < 800) screenMode = 4;
    if(mouseX>50 && mouseX < 950 && mouseY > 830 && mouseY < 910) screenMode = 6;
    if(mouseX>290 && mouseX < 490 && mouseY > 50 && mouseY < 110) login();
    if(mouseX>510 && mouseX < 710 && mouseY > 50 && mouseY < 110) register();

}

String activeUser = "EXPERIMENTAL"; //This is the default
String inputtedUser = ""; //This is the user inputted user.
String newUser = "";
String inputtedPassword = ""; //This is the user inputted password.
String newPassword = "";

int homeScreenTypeMode = 0; //1=Login 2=Register 0=null 3=LoginSend 4=RegisterSend 5=LoginPassword 6=RegisterPassword

void homeScreenModeKeyPressed(){

    //Login
    if(homeScreenTypeMode == 1){
        if(keyCode != SHIFT && keyCode != ENTER && keyCode != BACKSPACE && key != ' '){
            inputtedUser+=key;
            homeScreenInputText = inputtedUser;
        } else if(keyCode == BACKSPACE && inputtedUser.length() > 0){
            inputtedUser = inputtedUser.substring(0, inputtedUser.length() - 1);
            homeScreenInputText = inputtedUser;
        }
        if(keyCode == ENTER){
             homeScreenTypeMode = 5;
             println("Hello "+ inputtedUser+ "! Please enter your password");
             homeScreenInputText = ("Hello "+ inputtedUser+ "! Please enter your password");
             //login();
        }
    }

    //Login Password
    if(homeScreenTypeMode == 5){
        if(keyCode != SHIFT && keyCode != ENTER && keyCode != BACKSPACE && key != ' '){
            inputtedPassword+=key;
            homeScreenInputText = inputtedPassword;
        } else if(keyCode == BACKSPACE && inputtedPassword.length() > 0){
            inputtedPassword = inputtedPassword.substring(0, inputtedPassword.length() - 1);
            homeScreenInputText = inputtedPassword;
        }
        if(keyCode == ENTER && inputtedPassword != ""){
            try{
                if(inputtedPassword.contains(nameDataTable.getString(0,inputtedUser)) && inputtedPassword.length() == nameDataTable.getString(0,inputtedUser).length()){
                    homeScreenTypeMode = 3;
                    login();
                } else{
                    println("Password is wrong. Click log in to try again");
                    homeScreenInputText = ("Password is wrong. Click log in to try again");
                    homeScreenTypeMode = 0;
                    inputtedUser = "";
                    inputtedPassword = "";
                }
            }
            catch(Exception e){
                println("Username is wrong. Click log in to try again");
                homeScreenInputText = ("Username is wrong. Click log in to try again");
                homeScreenTypeMode = 0;
                inputtedUser = "";
                inputtedPassword = "";
            }
        }
    }

    //Register
    else if(homeScreenTypeMode == 2){
        if(keyCode != SHIFT && keyCode != ENTER && key != BACKSPACE && key != ' '){
            newUser+=key;
            homeScreenInputText = newUser;
        } else if(keyCode == BACKSPACE && newUser.length() > 0){
            newUser = newUser.substring(0, newUser.length() - 1);
            homeScreenInputText = newUser;
        }
        if(keyCode == ENTER){
            if(newUser.contains("empty") || newUser.contains("userData")){
                println("Nice try bud. This trick won't work on me");
                homeScreenInputText = ("Nice try bud. This trick won't work on me");
                homeScreenTypeMode = 0;
                newUser = "";
            }
            else{
                println("Welcome to the club " + newUser + "!");
                println("Please set your password. Because QiLin's a shitty programmer, you aren't able to change this yet.");
                homeScreenInputText = ("Welcome to the club " + newUser + "! Please set your password.");
                homeScreenTypeMode = 6;
            }
        }
    }

    //Register Password
    if(homeScreenTypeMode == 6){
        if(keyCode != SHIFT && keyCode != ENTER && keyCode != BACKSPACE && key != ' '){
            newPassword+=key;
            homeScreenInputText = newPassword;
        } else if(keyCode == BACKSPACE && newPassword.length() > 0){
            newUser = newPassword.substring(0, newPassword.length() - 1);
            homeScreenInputText = newPassword;
        }
        if(keyCode == ENTER && newPassword != ""){
            try{
                nameDataTable.getString(0,newUser); //Checks for errors
                println("User is already registered. Please either log in or register");
                homeScreenInputText = ("User is already registered. Please either log in or register");
                homeScreenTypeMode = 0;
                newUser = "";
                newPassword = "";
            }
            catch(Exception e){
                println("Your password is " + newPassword);
                homeScreenInputText = ("Welcome " + newUser);
                inputtedPassword = newPassword;
                homeScreenTypeMode = 4;
                register();
            }
        }
    }

}

void login(){
    if(homeScreenTypeMode == 0 || homeScreenTypeMode == 2 || homeScreenTypeMode == 6){
        newUser = "";
        newPassword = "";

        inputtedUser = "";
        println("input your username");
        homeScreenInputText = ("input your username");
        homeScreenTypeMode = 1;
    }
    if(homeScreenTypeMode == 3){
        homeScreenTypeMode = 0;
        for(int i=0;i<nameDataTable.getColumnCount();i++){
           if(nameDataTable.getColumnTitle(i).contains(inputtedUser)){
               activeUser = inputtedUser;
               learnModeInitialize();
               println("Welcome back " + activeUser + "! It's nice to see you again.");
               homeScreenInputText = ("Welcome back " + activeUser + "! It's nice to see you again.");
               break;
           }
           if(i==nameDataTable.getColumnCount()-1) println("no such user exists");
        }
    }
}

void register(){
    if(homeScreenTypeMode == 0 || homeScreenTypeMode == 1 || homeScreenTypeMode == 5){
        inputtedUser = "";
        inputtedPassword = "";

        newUser = "";
        newPassword = "";
        println("create your username");
        homeScreenInputText = ("create your username");
        homeScreenTypeMode = 2;
    }

    if(homeScreenTypeMode == 4){
        homeScreenTypeMode = 0;
        activeUser = newUser;
        saveTable(empty,"data/"+newUser+".csv");
        nameDataTable.addColumn(newUser);
        nameDataTable.setString(0,nameDataTable.getColumnCount()-1,newPassword);
        saveTable(nameDataTable, "data/userData.csv");
        learnModeInitialize();
    }
}

void displayBox(int x1, int y1, int w, int l, String text, int textSize) {
  fill(0);
  rect(x1, y1, w, l, 100);

  fill(255);
  textSize(textSize);
  textAlign(CENTER, CENTER);
  text(text, x1, y1, w, l);
}
