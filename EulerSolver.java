package sample.physx;

import java.io.*;
//TODO setup set_step_size
public class EulerSolver implements PhysicsEngine {
    private double step_size;

    public EulerSolver(){
        this.step_size = 0.01;
    }

    void set_step_size(double h){
        step_size =h;
    }

    public double get_step_size(){
        return step_size;
    }

}
