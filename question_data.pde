String commonMistakeMessage(String errorCode){
    String message = "MARGIN TOO SMALL";
    switch(errorCode){
        case "FRICTIONTYPE":
            message = "Make sure to review the different friction types";
            break;
        case "NORMALFORCE":
            message = "Make sure to review the normal force";
            break;
    }
    return message;
}

//TODO use same int-string conversion functions
String[][] problem1(){
    //TODO make bonus sections for common mistakes
    String[][] problem =
    {
        {"question","question type","answer"}, //Basic Data
        {"hint1","hint2","hint3","hint4"}, //Hints
        {"badAns1","badAns2","badAns3","badAns4"}, //Bad Answers
        {"typeBadAns1","typeBadAns2","typeBadAns3","typeBadAns4"} //Types of bad answer
    };

    //Question Type
    problem[0][1] = "FREE";

    //Stats
    //TODO: make this more efficient (find easier bug fix)
    String mass = Integer.toString(floor(random(1,50)));
    String force = Integer.toString(floor(random(1,50))+Integer.parseInt(mass));
    String kineticFriction = Float.toString(int(random(1,9)));
    String staticFriction = Float.toString(int(random(int(kineticFriction)+1,9)));

    //Question
    problem[0][0] = "A book of mass "+mass+"kg is held to a vertical wall by a person's hand applying a "+force+"N force directly toward the wall. The wall has a static friction coefficient of 0."+staticFriction.charAt(0)+" and a kinetic friction coefficient of 0."+kineticFriction.charAt(0)+". With the book held at rest, what is the frictional force keeping the book from sliding down the wall?";

    //Answer
    problem[0][2] = Float.toString(int(force)*float(staticFriction)*0.1);

    //Hints
    //TODO: Transfer this to data file
    problem[1][0] = "In this problem, the phrase 'what is the frictional force keeping the book from sliding down the wall' suggests that the box is stationary. Therefore, static friction exists but there is no kinetic friction";
    problem[1][1] = "Since the wall is perfectly vertical, there will be no normal force that is generated from the weight of the book. Therefore, the mass of the book is irrelevant.";
    problem[1][2] = "The normal force generated from applying a perpendicular force to the wall is just the force applied. Therefore, the normal force from the force applied is "+force+" Newtons.";
    problem[1][3] = "The frictional force is equal to product of the coefficient of friction (static/kinetic) and the normal force";

    //Bad Answer
    problem[2][0] = Float.toString(int(force)*float(kineticFriction)*0.1);
    problem[3][0] = "FRICTIONTYPE";

    problem[2][1] = Float.toString(int(mass)*float(staticFriction)*0.1);
    problem[3][1] = "NORMALFORCE";

    problem[2][2] = "99999";
    problem[3][2] = "NULL";

    problem[2][3] = "99999";
    problem[3][3] = "NULL";
    return problem;
}
