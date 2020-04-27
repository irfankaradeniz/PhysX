package sample.physx;

public class Vector2d {

    private double x;
    private double y;

    //constructor method
    public Vector2d(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double get_x(){
        return x;
    }

    public double get_y(){
        return y;
    }

    public void set_x(double x){
        this.x = x;
    }

    public void set_y(double y){
        this.y = y;
    }

    //method to return scalar value of a vector
    public double get_scalar(){
        return Math.sqrt((x*x) + (y*y));
    }

    public String toString() {
        return "X: "+x+"\t Y: "+y;
    }

}
