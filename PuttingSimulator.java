package sample.physx;

import java.util.ArrayList;

public class PuttingSimulator {
    private Vector2d velocity;
    private Vector2d acceleration;
    private Vector2d position;
    private Vector2d prevAcceleration;
    private PuttingCourse course;
    private EulerSolver engine;
    private double goalX;
    private double goalY;
//    static ArrayList<Double> coordinatesX = new ArrayList<Double>();
//    static ArrayList<Double> coordinatesY = new ArrayList<Double>();

    // constructor for PuttingSimulator class
    public PuttingSimulator(PuttingCourse course , EulerSolver engine){
        this.course = course;
        this.engine = engine;
        this.position = course.get_start_position();
    }
    //set method to set ball position
    public void set_ball_position(Vector2d pos){
        position= pos;
    }
    // get method to get ball position
    public Vector2d get_ball_position(){
        return position;
    }

    //method to take shot and call on physics equations to determine when and where the ball stops and where it's current pos is
    public void take_shot(Vector2d initial_ball_velocity){
        this.velocity = initial_ball_velocity;
        Vector2d temporary = position;
        Vector2d stopV = new Vector2d(0.01,0.01);
        boolean cont = true;
        while(cont){

            acceleration = calculate_acceleration(velocity);
            position = calculate_displacement();
            System.out.println(position.toString());
            if(in_water(position)){
                position = temporary;
                velocity = new Vector2d(0,0);
                cont = false;
                break;
            }
            velocity = calculate_velocity();
            if(velocity.get_scalar()<stopV.get_scalar() && acceleration.get_scalar()< calculate_acceleration(stopV).get_scalar()){
                cont = false;
            }
        }
        //System.out.println(position.toString());
    }
    public boolean in_water(Vector2d pos){
        boolean water = false;
        double height = course.calculate_height(pos);
        if(height < 0){
            water = true;
        }
        return water;
    }
    //method to determine if the ball at rest is in goal area
    public boolean in_goal(Vector2d p, double diff){

        Vector2d flag = course.get_flag_position();
        boolean in = false;
        double x = p.get_x()-flag.get_x();
        double y = p.get_y()-flag.get_y();
        double tol = course.get_hole_tolerance()*diff;
        if((x*x+y*y)<= (tol*tol)){
            in = true;
        }
        System.out.println("In flag boolean: " + in + " position of ball - X: " + x +" Y: " + y);
        return in;
    }

    //method to calculate friction force acting on ball taking friction into consideration and returns vector
    public Vector2d calculate_acceleration(Vector2d velocity){
        double aX ,aY;
        double mu = course.get_friction_coefficient();
        double g = course.get_gravity();
        Vector2d gradient = course.get_height().gradient(position);
        aX = (-g*(gradient.get_x())) - (mu*g*velocity.get_x()/velocity.get_scalar());
        aY = (-g*(gradient.get_y())) - (mu*g*velocity.get_y()/velocity.get_scalar());
        return new Vector2d(aX,aY);

    }

    //method to calculate the position of the ball
    public Vector2d calculate_displacement(){
        double h = engine.get_step_size();
        double sX = position.get_x()+h*velocity.get_x();
        double sY = position.get_y()+h*velocity.get_y();
        return new Vector2d(sX,sY);
    }

    //method to calculate velocity of ball
    public Vector2d calculate_velocity(){
            double h = engine.get_step_size();
            double vX = velocity.get_x() + (prevAcceleration.get_x() + acceleration.get_x())*(h*0.5);
            double vY = velocity.get_y() + (prevAcceleration.get_y() + acceleration.get_y())*(h*0.5);
            return new Vector2d(vX,vY);
    }

    public Vector2d calculate_final_pos(double speed, double direction){
        Vector2d velocity = course.calculate_velocity_hit(speed,direction);
        take_shot(velocity);
        return position;
    }

    public Vector2d calculate_displacement_verlet(){
        double h = engine.get_step_size();
        double posX = (position.get_x() + velocity.get_x()*h + 0.5*acceleration.get_x()*h*h);
        double posY = (position.get_y() + velocity.get_y()*h + 0.5*acceleration.get_y()*h*h);
        return new Vector2d(posX,posY);
    }



    public void take_shot_verlet(Vector2d initial_ball_velocity){
        this.velocity = initial_ball_velocity;
        prevAcceleration = new Vector2d(0,0);
        acceleration = new Vector2d(0,0);
        Vector2d stopV = new Vector2d(0.01,0.01);
        boolean cont = true;
        while(cont){
            Vector2d tempAcc = acceleration;
            acceleration = calculate_acceleration(velocity);
            prevAcceleration = tempAcc;
            position = calculate_displacement_verlet();
            System.out.println(position.toString());
            velocity =  calculate_velocity();
            if(velocity.get_scalar()<stopV.get_scalar() && acceleration.get_scalar()< calculate_acceleration(stopV).get_scalar()){
                cont = false;
            }
        }
    }

    public static void main(String[] args) {
        PuttingCourse course1 = new PuttingCourse();
        course1.readFile("C:\\Users\\IRFAN\\IdeaProjects\\GolfPhase1\\src\\sample\\physx\\test.txt");

        PuttingSimulator simulator1 = new PuttingSimulator(course1, new EulerSolver());
        simulator1.take_shot_verlet(new Vector2d(3,3));
//        simulator1.take_shot(new Vector2d(3,3));

        }

}