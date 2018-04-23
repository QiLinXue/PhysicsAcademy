float graphScaler = 18;
float xShift = 0;
float yShift = 0;

float[] position = new float[4];
float[] velocity = new float[4];
float[] acceleration = new float[4];
boolean pShow = true;
boolean vShow = true;
boolean aShow = true;

void initializeKinematicGraphSimulation(float co1, float co2, float co3, float co4){
    position[0] = co1;
    position[1] = co2;
    position[2] = co3;
    position[3] = co4;

    velocity[0] = 0;
    velocity[1] = 3*position[0];
    velocity[2] = 2*position[1];
    velocity[3] = position[2];

    acceleration[0] = 0;
    acceleration[1] = 0;
    acceleration[2] = 6*position[0];
    acceleration[3] = 2*position[1];

    graphScaler = 30;
    xShift = 0;
    yShift = 0;
}

void kinematicGraphSimulation(){
    background(200);

    //Y and X Axis
    stroke(0);
    strokeWeight(0.6*1);
    line(-1000+xShift,height/2+yShift,width+1000+xShift,height/2+yShift);
    line(width/2+xShift,1000+yShift,width/2+xShift,-1000+height+yShift);


    //Start Graphing
    pushMatrix();
    translate(width/2+xShift,height/2+yShift);
    scale(graphScaler);
    strokeWeight(0.6*7/graphScaler);

    //Although below code runs fast, it creates overlapping lines which is bad
    /*
    for(float i=-width/2;i<width/2;i++){
        //Position
        stroke(255,0,0);
        if(pShow)
            line(

                i/graphScaler,
                poly(i/graphScaler,position[0],position[1],position[2],position[3]),

                (i+1)/graphScaler,
                poly((i+1)/graphScaler,position[0],position[1],position[2],position[3])

            );

        //Velocity
        stroke(0,255,0);
        if(vShow)
            line(

                i/graphScaler,
                poly(i/graphScaler,velocity[0],velocity[1],velocity[2],velocity[3]),

                (i+1)/graphScaler,
                poly((i+1)/graphScaler,velocity[0],velocity[1],velocity[2],velocity[3])

            );

        //Acceleration
        stroke(0,0,255);
        if(aShow)
            line(

                i/graphScaler,
                poly(i/graphScaler,acceleration[0],acceleration[1],acceleration[2],acceleration[3]),

                (i+1)/graphScaler,
                poly((i+1)/graphScaler,acceleration[0],acceleration[1],acceleration[2],acceleration[3])

            );
    }
    */

    //Position
    stroke(255,0,0);
    if(pShow)
        for(float i=-width/2;i<width/2;i++){
                line(

                    i/graphScaler,
                    poly(i/graphScaler,position[0],position[1],position[2],position[3]),

                    (i+1)/graphScaler,
                    poly((i+1)/graphScaler,position[0],position[1],position[2],position[3])

                );
        }

    //Velocity
    stroke(0,255,0);
    if(vShow)
        for(float i=-width/2;i<width/2;i++){
                line(

                    i/graphScaler,
                    poly(i/graphScaler,velocity[0],velocity[1],velocity[2],velocity[3]),

                    (i+1)/graphScaler,
                    poly((i+1)/graphScaler,velocity[0],velocity[1],velocity[2],velocity[3])

                );
        }

    //Acceleration
    stroke(0,0,255);
    if(aShow)
        for(float i=-width/2;i<width/2;i++){
                line(

                    i/graphScaler,
                    poly(i/graphScaler,acceleration[0],acceleration[1],acceleration[2],acceleration[3]),

                    (i+1)/graphScaler,
                    poly((i+1)/graphScaler,acceleration[0],acceleration[1],acceleration[2],acceleration[3])

                );
        }

    popMatrix(); //End Graph

    //Custom Buttons!
    graphButtons();

    //Go Back
    if (keyPressed && keyCode == BACKSPACE) screenMode = previousScreenMode;

}

void graphButtons(){
    fill(0);
    noStroke();

    //Upper Right Matrix Buttons
    rect(30, 30,30,30);
    rect(66, 30,30,30);
    rect(0.6*170, 30,30,30);
    rect(30,66,30,30);
    rect(66,66,30,30);
    rect(0.6*170,66,30,30);
    rect(30,0.6*170,30,30);
    rect(66,0.6*170,30,30);

    //Matrix Button Text
    fill(255);
    textSize(0.6*15);
    textAlign(CENTER,CENTER);
    text("Out",30,30,30,30);
    text("Up",66,30,30,30);
    text("In",0.6*170,30,30,30);
    text("Left",30,66,30,30);
    text("Home",66,66,30,30);
    text("Right",0.6*170,66,30,30);
    text("Back",30,0.6*170,30,30);
    text("Down",66,0.6*170,30,30);

    //Toggle X/V/T Button and Text
    fill(255,0,0); rect(30,0.6*230,0.6*170,30); //Position Button
    fill(0,255,0); rect(30,174,0.6*170,30); //Velocity Button
    fill(0,0,255); rect(30,210,0.6*170,30); //Acceleration Button
    fill(255); //Prepares for the text
    textSize(0.6*18); //Makes it slightly bigger
    text("Position - Click me",30,0.6*230,0.6*170,30);
    text("Velocity - Click me",30,174,0.6*170,30);
    text("Acceleration - Click me",30,210,0.6*170,30);


    fill(0);
    textSize(0.6*25);
    text("Mouse Location: "+(mouseX-width/2-xShift)/graphScaler + ", " + (-1*(mouseY-height/2-yShift))/graphScaler,30,0.6*410,180,180);

    if(mousePressed){

        //Zoom Out or In
        if(mouseX>30 && mouseX<60 && mouseY>30 && mouseY<60 && graphScaler > 1) graphScaler--;
        if(mouseX>0.6*170 && mouseX<0.6*220 && mouseY>30 && mouseY<60) graphScaler++;

        //Scale Up or Down
        if(mouseX>66 && mouseX<0.6*160 && mouseY>30 && mouseY<60) yShift+=10;
        if(mouseX>66 && mouseX<0.6*160 && mouseY>0.6*170 && mouseY<0.6*220) yShift-=10;

        //Scale Left or Right
        if(mouseX>30 && mouseX<60 && mouseY>66 && mouseY<0.6*160) xShift+=10;
        if(mouseX>0.6*170 && mouseX<0.6*220 && mouseY>66 && mouseY<0.6*160) xShift-=10;

        //Reset
        if(mouseX>66 && mouseX<0.6*160 && mouseY>66 && mouseY<0.6*160){
            xShift = 0;
            yShift = 0;
            graphScaler = 18;
        }

        //Go back
        if(mouseX>30 && mouseX < 60 && mouseY>0.6*170 && mouseY<0.6*220){
            screenMode = 3;
        }

        if(mouseButton == RIGHT){
            stroke(0);
            strokeWeight(0.6*5);
            line(mouseX,0,mouseX,height);
            line(0,mouseY,width,mouseY);
        }
    }
}

void kinematicMousePressed(){
    //Toggle Plotted Lines
    if(mouseX>30 && mouseX<0.6*220 && mouseY>0.6*230 && mouseY < 0.6*280) pShow = !pShow;
    if(mouseX>30 && mouseX<0.6*220 && mouseY>174 && mouseY < 0.6*340) vShow = !vShow;
    if(mouseX>30 && mouseX<0.6*220 && mouseY>210 && mouseY < 240) aShow = !aShow;

}

float poly(float i,float a, float b, float c, float d){
    float value = a*pow(i,3)+b*pow(i,2)+c*i+d;
    return -value;
}
