String simulationType = "NULL";

void initializeSimulation(){
    if(simulationType == "WALL_FRICTION")
        initializeWallSimulation(
            float(questionData[4][0]),
            float(questionData[4][1]),
            float(questionData[4][2])
        );
        
    if(simulationType == "KINEMATIC_GRAPH")
        initializeKinematicGraphSimulation(
            float(questionData[4][0]),
            float(questionData[4][1]),
            float(questionData[4][2]),
            float(questionData[4][3])
        );
}

void simulationMode() {
  if(simulationType == "WALL_FRICTION") wallSimulation();
  if(simulationType == "KINEMATIC_GRAPH") kinematicGraphSimulation();
}

void simulationMousePressed(){
    if(simulationType == "KINEMATIC_GRAPH") kinematicMousePressed();
}
