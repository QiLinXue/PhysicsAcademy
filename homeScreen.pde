void homeScreen(){
    background(50);

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
}

void homeScreenMousePressed(){
    if(mouseX>50 && mouseX < 950 && mouseY > 500 && mouseY < 680) println("doesn't work");
    if(mouseX>50 && mouseX < 950 && mouseY > 610 && mouseY < 690) screenMode = 3;
    if(mouseX>50 && mouseX < 950 && mouseY > 720 && mouseY < 800) screenMode = 4;

}
