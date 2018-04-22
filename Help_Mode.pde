void helpMode(){
    //background
    background(33, 26, 29);

    fill(255);
    textSize(35);
    textAlign(CENTER,TOP);
    text("To read documentation (flowchart included) in a more accessible manner, go to the readme markdown files in the github folder.",0,50,width,500);


    if(keyPressed && keyCode == BACKSPACE) screenMode = 0; //Go back
}
