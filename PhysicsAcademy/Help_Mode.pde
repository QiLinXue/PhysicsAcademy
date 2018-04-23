void helpMode(){
    //background
    background(33, 26, 29);

    fill(255);
    textSize(0.6*35);
    textAlign(CENTER,TOP);
    text("To read documentation (flowchart included) in a more accessible manner, go to the readme markdown files in the github folder.",0,0.6*50,width,0.6*500);

    textSize(0.6*22);
    textAlign(LEFT,CENTER);
    text("Press BACKSPACE to go to the previous page in any screen.",0,0.6*300,width,0.6*300);
    if(keyPressed && keyCode == BACKSPACE) screenMode = 0; //Go back
    text("Scroll and drag the mouse around in space simulations.",0,0.6*400,width,0.6*300);
    if(keyPressed && keyCode == BACKSPACE) screenMode = 0; //Go back
    text("Clicking 'hints' or going to the sandbox mode before answering a question during the quiz will lead to an automatic failure. You can explore them afterwards.",0,0.6*500,width,0.6*300);
    if(keyPressed && keyCode == BACKSPACE) screenMode = 0; //Go back
    text("Pressing space during the quiz mode will print the correct answer to the console. This is to be only used for comp sci teachers to navigate easily without doing the physics",0,0.6*600,width,0.6*300);
    if(keyPressed && keyCode == BACKSPACE) screenMode = 0; //Go back
    text("Read each question carefully. You can always round to more decimal places, but not less",0,0.6*700,width,0.6*300);
    if(keyPressed && keyCode == BACKSPACE) screenMode = 0; //Go back
    text("You need to log in or register in order to save your customized work. Otherwise, you will be using the public account named EXPERIMENTAL",0,0.6*800,width,0.6*300);
    if(keyPressed && keyCode == BACKSPACE) screenMode = 0; //Go back
}
