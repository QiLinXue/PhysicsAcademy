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
    if(mouseY > 500 && mouseY < 800 && homeScreenTypeMode != 0){
        println("login/register failed. Please try again");

        //Reset User Initalizers
        inputtedUser = "";
        inputtedPassword = "";
        newUser = "";
        newPassword = "";

        homeScreenTypeMode = 0; //A cheaty way of resetting the login and register button
    }
    if(mouseX>50 && mouseX < 950 && mouseY > 500 && mouseY < 580) println("doesn't work");
    if(mouseX>50 && mouseX < 950 && mouseY > 610 && mouseY < 690) screenMode = 3;
    if(mouseX>50 && mouseX < 950 && mouseY > 720 && mouseY < 800) screenMode = 4;
    if(mouseX>290 && mouseX < 490 && mouseY > 50 && mouseY < 110) login();
    if(mouseX>510 && mouseX < 710 && mouseY > 50 && mouseY < 110) register();
}

String activeUser = "EXPERIMENTAL"; //This is the default
String inputtedUser = ""; //This is the user inputted user.
String newUser = "";
String inputtedPassword = ""; //This is the user inputted password.
String newPassword = "PLACEHOLDER";

//import javax.swing.JOptionPane;

int homeScreenTypeMode = 0; //1=Login 2=Register 0=null 3=LoginSend 4=RegisterSend 5=LoginPassword 6=RegisterPassword

void homeScreenKeyPressed(){

    //Login
    if(homeScreenTypeMode == 1){
        if(keyCode != SHIFT && keyCode != ENTER){
            inputtedUser+=key;
            println(inputtedUser);
        }
        if(keyCode == ENTER){
             homeScreenTypeMode = 5;
             println("Please enter your password");
             //login();
        }
    }

    //Login Password
    if(homeScreenTypeMode == 5){
        if(keyCode != SHIFT && keyCode != ENTER){
            inputtedPassword+=key;
            println(inputtedPassword);
        }
        if(keyCode == ENTER && inputtedPassword != ""){
            try{
                if(inputtedPassword.contains(nameDataTable.getString(0,inputtedUser))){
                    homeScreenTypeMode = 3;
                    login();
                } else{
                    println("Password is wrong. Click log in to try again");
                    homeScreenTypeMode = 0;
                    inputtedUser = "";
                    inputtedPassword = "";
                }
            }
            catch(Exception e){
                println("Username is wrong. Click log in to try again");
                homeScreenTypeMode = 0;
                inputtedUser = "";
                inputtedPassword = "";
            }
        }
    }

    //Register
    else if(homeScreenTypeMode == 2){
        if(keyCode != SHIFT && keyCode != ENTER){
            newUser+=key;
        }
        if(keyCode == ENTER){
             homeScreenTypeMode = 4;
             register();
        }
    }

}

void login(){
    if(homeScreenTypeMode == 0){
        inputtedUser = "";
        println("input your username");
        homeScreenTypeMode = 1;
    }
    if(homeScreenTypeMode == 3){
        homeScreenTypeMode = 0;
        for(int i=0;i<nameDataTable.getColumnCount();i++){
           if(nameDataTable.getColumnTitle(i).contains(inputtedUser)){
               activeUser = inputtedUser;
               learnModeInitialize();
               println("Welcome back " + activeUser + "! It's nice to see you again.");
               break;
           }
           if(i==nameDataTable.getColumnCount()-1) println("no such user exists");
        }
    }
}

void register(){
    if(homeScreenTypeMode == 0){
        newUser = "";
        println("create your username");
        homeScreenTypeMode = 2;
    }

    if(homeScreenTypeMode == 4){
        homeScreenTypeMode = 0;
        for(int i=0;i<nameDataTable.getColumnCount();i++){
           if(nameDataTable.getColumnTitle(i).contains(newUser)){
               println("user already in database");
               break;
           }

           //The below will activate if there are no matches
           if(i == nameDataTable.getColumnCount()-1){
               activeUser = newUser;
               saveTable(empty,"data/"+newUser+".csv");
               nameDataTable.addColumn(newUser);
               nameDataTable.setString(0,i+1,newPassword);
               saveTable(nameDataTable, "data/userData.csv" );
               learnModeInitialize();
               break;
           }
       }
   }
}
