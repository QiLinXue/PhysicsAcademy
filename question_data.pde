//TODO use same int-string conversion functions
String[] problem1(){
    //TODO make bonus sections for common mistakes
    String[] problem = {"question","question type","answer"};

    //Question Type
    problem[1] = "FREE";

    //Stats
    //TODO: make this more efficient (find easier bug fix)
    String mass = Integer.toString(floor(random(1,50)));
    //String mass = tempMass;

    String force = Integer.toString(floor(random(1,50))+Integer.parseInt(mass));
    //String force = tempForce;

    String kineticFriction = Float.toString(int(random(1,9)));
    //String kineticFriction = tempKineticFriction;

    String staticFriction = Float.toString(int(random(int(kineticFriction)+1,9)));
    //String staticFriction = tempStaticFriction;

    //Question
    problem[0] = "A book of mass "+mass+"kg is held to a vertical wall by a person's hand applying a "+force+"N force directly toward the wall. The wall has a static friction coefficient of 0."+staticFriction.charAt(0)+" and a kinetic friction coefficient of 0."+kineticFriction.charAt(0)+". With the book held at rest, what is the frictional force keeping the book from sliding down the wall?";

    //TODO make answer real (really simple for now)

    problem[2] = Float.toString(int(force)*float(staticFriction)*0.1);

    return problem;
}
