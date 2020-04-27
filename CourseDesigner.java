package sample.physx;

public class CourseDesigner  {
    //z = a*sin(b(x-c))*sin(d(y-e)) + f
    private float z;
    private int b, a, c, d ,e,f;

    public CourseDesigner(int a, int b, int c, int d, int e, int f) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
    }
    public double getZ(double x, double y){
        return (double) ((a*Math.sin(b*(x-c)))*Math.sin(d*(y-e)) + f);
    }
}
