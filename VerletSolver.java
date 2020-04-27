package sample.physx;

import org.jfree.data.xy.XYSeries;

public class VerletSolver {
    private Vector2d velocity;
    private Vector2d acceleration;
    private Vector2d position;
    private Vector2d prevAcceleration;
    private PuttingCourse course;
    private EulerSolver engine;
    public XYSeries verletSeries;

    public VerletSolver(EulerSolver engine, PuttingCourse course){
        this.engine = engine;
        this.course = course;
        this.position = course.get_start_position();
    }

    public Vector2d calculate_velocity(){
        double h = engine.get_step_size();
        double vX = velocity.get_x() + (prevAcceleration.get_x() + acceleration.get_x())*(h*0.5);
        double vY = velocity.get_y() + (prevAcceleration.get_y() + acceleration.get_y())*(h*0.5);
        return new Vector2d(vX,vY);
    }
    public Vector2d calculate_displacement_verlet(){
        double h = engine.get_step_size();
        double posX = (position.get_x() + velocity.get_x()*h + 0.5*acceleration.get_x()*h*h);
        double posY = (position.get_y() + velocity.get_y()*h + 0.5*acceleration.get_y()*h*h);
        return new Vector2d(posX,posY);
    }

    public Vector2d calculate_acceleration(Vector2d velocity){
        double aX ,aY;
        double mu = course.get_friction_coefficient();
        double g = course.get_gravity();
        Vector2d gradient = course.get_height().gradient(position);
        aX = (-g*(gradient.get_x())) - (mu*g*velocity.get_x()/velocity.get_scalar());
        aY = (-g*(gradient.get_y())) - (mu*g*velocity.get_y()/velocity.get_scalar());
        return new Vector2d(aX,aY);

    }

    public void take_shot_verlet(Vector2d initial_ball_velocity){
        this.verletSeries = new XYSeries("Verlet Series");
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
            this.verletSeries.add(position.get_x(),position.get_y());
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

        VerletSolver verlet = new VerletSolver( new EulerSolver(),course1);
        verlet.take_shot_verlet(new Vector2d(3,3));
    }
}

