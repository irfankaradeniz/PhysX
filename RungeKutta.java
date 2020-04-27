package sample.physx;


import org.jfree.data.xy.XYSeries;

public class RungeKutta {


    private final double DELTA_X = (double) (Math.pow(10, -3));
    private final double DELTA_Y = (double) (Math.pow(10, -3));
    private final double EULER_STEP = 0.01;
    private static final double G = 9.8;
    private static final double MASS = 0.043;
    private static final double FRICTION_COEFFICIENT = 0.06;
    private PuttingCourse puttingCourse;
    public XYSeries rungeKuttaSeries;

    public RungeKutta(PuttingCourse puttingCourse) {
        this.puttingCourse = puttingCourse;
    }

    public Vector2d calculateHxHy(double x, double y) {
        return puttingCourse.get_height().gradient(new Vector2d(x,y));

    }

    public Vector2d calculateGForce(Vector2d vector2D) {
        double hx = vector2D.get_x();
        double hy = vector2D.get_y();
        double gx = -G * MASS * hx;
        double gy = -G * MASS * hy;
//        System.out.println("gx: " + gx + " gy: " + gy);
        return new Vector2d(gx, gy);
    }

    public Vector2d calculateFrictionForce(double vx, double vy) {
        double moduleV = (double) Math.sqrt(vx * vx + vy * vy);
        double frX = -FRICTION_COEFFICIENT * MASS * G * vx * moduleV;
        double frY = -FRICTION_COEFFICIENT * MASS * G * vy * moduleV;

        return new Vector2d(frX, frY);
    }

    public Vector2d calculateAcceleration(Vector2d gForce, Vector2d fForce) {
        return new Vector2d(gForce.get_x() + fForce.get_x(), gForce.get_x() + fForce.get_x());
    }
    public Vector2d puttingTogether(Vector2d initialVelocity, Vector2d initialPosition) {
        this.rungeKuttaSeries = new XYSeries("RungeKutta Series");

        double px = initialPosition.get_x();
        double py = initialPosition.get_y();
        System.out.println("Initial pos: " + px + " y: " + py);
        double vx = initialVelocity.get_x();
        double vy = initialVelocity.get_y();
        System.out.println("Inivital v: " + vx + " " + vy);

        boolean cont = true;
        while (cont) {
            Vector2d k1p = calculateHxHy(px, py);
            Vector2d k2p = calculateK2Position(k1p, px, py);
            Vector2d k3p = calculateK3Position(k2p, px, py);
            Vector2d k4p = calculateK4Position(k3p, px, py);
            px = px + EULER_STEP * (k1p.get_x() + 2 * k2p.get_x() + 2 * k3p.get_x() + k4p.get_x()) / 6;
            py = py + EULER_STEP * (k1p.get_y() + 2 * k2p.get_y() + 2 * k3p.get_y() + k4p.get_y()) / 6;
            rungeKuttaSeries.add(px,py);
            System.out.println(px + " " + py);
            Vector2d hXhY = calculateHxHy(px, py);
            Vector2d gForce = calculateGForce(hXhY);
            Vector2d frForce = calculateFrictionForce(vx, vy);
//            double ax = gForce.get_x() + frForce.get_x();
//            double ay = gForce.get_y() + frForce.get_y();
//            Vector2d k1 = new Vector2d(ax, ay);
//            Vector2d k2 = calculateK2Velocity(k1, gForce, vx, vy);
//            Vector2d k3 = calculateK3Velocity(k2, gForce, vx, vy);
//            Vector2d k4 = calculateK4Velocity(k3, gForce, vx, vy);
            Vector2d a = calculateAcceleration(gForce,frForce);
            vx = vx + EULER_STEP*a.get_x();
            vy = vy + EULER_STEP*a.get_y();
            if(vx <= 0.003 || vy <= 0.003){
                cont = false;
            }
        }
        return new Vector2d(px, py);
    }

    private Vector2d calculateK2Velocity(Vector2d k1, Vector2d gForce, double vx, double vy) {
        double x = k1.get_x();
        double x2 = vx + x * EULER_STEP / 2;
        double y = k1.get_y();
        double y2 = vy + y * EULER_STEP / 2;
        Vector2d fForce = calculateFrictionForce(x2, y2);
        return calculateAcceleration(gForce, fForce);
    }

    private Vector2d calculateK3Velocity(Vector2d k2, Vector2d gForce, double vx, double vy) {
        double x = k2.get_x();
        double x2 = vx + x * EULER_STEP / 2;
        double y = k2.get_y();
        double y2 = vy + y * EULER_STEP / 2;
        Vector2d fForce = calculateFrictionForce(x2, y2);
        return calculateAcceleration(gForce, fForce);
    }

    private Vector2d calculateK4Velocity(Vector2d k3, Vector2d gForce, double vx, double vy) {
        double x = k3.get_x();
        double x2 = vx + EULER_STEP * x;
        double y = k3.get_y();
        double y2 = vy + EULER_STEP * y;
        Vector2d fForce = calculateFrictionForce(x2, y2);
        return calculateAcceleration(fForce, gForce);
    }


    private Vector2d calculateK4Position(Vector2d k3, double px, double py) {
        double x = k3.get_x();
        double x2 = px + EULER_STEP * x;
        double y = k3.get_y();
        double y2 = py + EULER_STEP * y;
        return calculateHxHy(x2, y2);
    }

    private Vector2d calculateK3Position(Vector2d k2, double px, double py) {
        double x = k2.get_x();
        double x2 = px + EULER_STEP * x / 2;
        double y = k2.get_y();
        double y2 = py + EULER_STEP * y / 2;
        return calculateHxHy(x2, y2);
    }

    private Vector2d calculateK2Position(Vector2d k1, double px, double py) {
        double x = k1.get_x();
        double x2 = px + x * EULER_STEP / 2;
        double y = k1.get_y();
        double y2 = py + y * EULER_STEP / 2;
        return calculateHxHy(x2, y2);
    }

}
