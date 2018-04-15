float graphScaler = 30;
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
}

void kinematicGraphSimulation(){
    background(200);

    //Y and X Axis
    stroke(0);
    strokeWeight(1);
     line(-1000+xShift,height/2+yShift,width+1000+xShift,height/2+yShift);
    line(width/2+xShift,1000+yShift,width/2+xShift,-1000+height+yShift);


    //Start Graphing
    pushMatrix();
    translate(width/2+xShift,height/2+yShift);
    scale(graphScaler);
    strokeWeight(10/graphScaler);

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

    rect(50, 50,50,50); rect(110, 50,50,50); rect(170, 50,50,50);
    rect(50,110,50,50); rect(110,110,50,50); rect(170,110,50,50);
                        rect(110,170,50,50);

    fill(255,0,0);
    rect(50,230,170,50);

    fill(0,255,0);
    rect(50,290,170,50);

    fill(0,0,255);
    rect(50,350,170,50);

    fill(0);
    textSize(25);
    text((mouseX-width/2-xShift)/graphScaler + ", " + (-1*(mouseY-height/2-yShift))/graphScaler,50,410,300,300);

    if(mousePressed){

        //Zoom Out or In
        if(mouseX>50 && mouseX<100 && mouseY>50 && mouseY<100 && graphScaler > 1) graphScaler--;
        if(mouseX>170 && mouseX<220 && mouseY>50 && mouseY<100) graphScaler++;

        //Scale Up or Down
        if(mouseX>110 && mouseX<160 && mouseY>50 && mouseY<100) yShift+=10;
        if(mouseX>110 && mouseX<160 && mouseY>170 && mouseY<220) yShift-=10;

        //Scale Left or Right
        if(mouseX>50 && mouseX<100 && mouseY>110 && mouseY<160) xShift+=10;
        if(mouseX>170 && mouseX<220 && mouseY>110 && mouseY<160) xShift-=10;

        //Reset
        if(mouseX>110 && mouseX<160 && mouseY>110 && mouseY<160){
            xShift = 0;
            yShift = 0;
            graphScaler = 30;
        }

        if(mouseButton == RIGHT){
            stroke(0);
            strokeWeight(5);
            line(mouseX,0,mouseX,height);
            line(0,mouseY,width,mouseY);
        }
    }
}

void kinematicMousePressed(){
    //Toggle Plotted Lines
    if(mouseX>50 && mouseX<220 && mouseY>230 && mouseY < 280) pShow = !pShow;
    if(mouseX>50 && mouseX<220 && mouseY>290 && mouseY < 340) vShow = !vShow;
    if(mouseX>50 && mouseX<220 && mouseY>350 && mouseY < 400) aShow = !aShow;

}

float poly(float i,float a, float b, float c, float d){
    float value = a*pow(i,3)+b*pow(i,2)+c*i+d;
    return -value;
}
