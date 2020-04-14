package physics_engine;

import PhysiX.CourseDesigner;
import PhysiX.Vector2D;

public class RungeKutta {
    CourseDesigner courseDesigner;

    public RungeKutta(CourseDesigner courseDesigner) {
        this.courseDesigner = courseDesigner;
    }

    private final double DELTA_X = (double) (Math.pow(10, -3));
    private final double DELTA_Y = (double) (Math.pow(10, -3));
    private final double EULER_STEP = 0.01;
    private static final double G = 9.8;
    private static final double MASS = 0.043;
    private static final double FRICTION_COEFFICIENT = 0.06;

    public Vector2D calculateHxHy(double x, double y) {
        double currentX = courseDesigner.getZ(x, y);
        double nextX = courseDesigner.getZ(x + DELTA_X, y);
        double hx_xy = (currentX - nextX) / DELTA_X;
        System.out.println("hx: " + hx_xy);
        double currentY = courseDesigner.getZ(x, y);
        double nextY = courseDesigner.getZ(x, y + DELTA_Y);
        double hy_xy = (currentY - nextY) / DELTA_Y;
        System.out.println("hy: " + hy_xy);
        return new Vector2D(hx_xy, hy_xy);

    }

    public Vector2D calculateGForce(Vector2D vector2D) {
        double hx = vector2D.getX();
        double hy = vector2D.getY();
        double gx = -G * MASS * hx;
        double gy = -G * MASS * hy;
        System.out.println("gx: " + gx + " gy: " + gy);
        return new Vector2D(gx, gy);
    }

    public Vector2D calculateFrictionForce(double vx, double vy) {
        double moduleV = (double) Math.sqrt(vx * vx + vy * vy);
        double frX = -FRICTION_COEFFICIENT * MASS * G * vx * moduleV;
        double frY = -FRICTION_COEFFICIENT * MASS * G * vy * moduleV;

        return new Vector2D(frX, frY);
    }

    public Vector2D calculateAcceleration(Vector2D gForce, Vector2D fForce) {
        return new Vector2D(gForce.getX() + fForce.getX(), gForce.getY() + fForce.getY());
    }

    public Vector2D puttingTogether(Vector2D initialVelocity, Vector2D initialPosition) {
        double px = initialPosition.getX();
        double py = initialPosition.getY();
        System.out.println("Initial pos: " + px + " y: " + py);
        double vx = initialVelocity.getX();
        double vy = initialVelocity.getY();
        System.out.println("Inivital v: " + vx + " " + vy);
        double t0 = 0;
        double t = 2;
        while (t0 < t) {
            Vector2D k1p = calculateHxHy(px, py);
            Vector2D k2p = calculateK2Position(k1p, px, py);
            Vector2D k3p = calculateK3Position(k2p, px, py);
            Vector2D k4p = calculateK4Position(k3p, px, py);
            px = px + EULER_STEP * (k1p.getX() + 2 * k2p.getX() + 2 * k3p.getX() + k4p.getX()) / 6;
            py = py + EULER_STEP * (k1p.getY() + 2 * k2p.getY() + 2 * k3p.getY() + k4p.getY()) / 6;
            Vector2D hXhY = calculateHxHy(px, py); // dao ham cua p
            Vector2D gForce = calculateGForce(hXhY); // tinh g force theo dao ham cua p
            Vector2D frForce = calculateFrictionForce(vx, vy);// tinh fforce
            double ax = gForce.getX() + frForce.getX();
            double ay = gForce.getY() + frForce.getY();
            Vector2D k1 = new Vector2D(ax, ay);
            Vector2D k2 = calculateK2Velocity(k1, gForce, vx, vy);
            Vector2D k3 = calculateK3Velocity(k2, gForce, vx, vy);
            Vector2D k4 = calculateK4Velocity(k3, gForce, vx, vy);
            vx = vx + (k1.getX() + 2 * k2.getX() + 2 * k3.getX() + k4.getX()) / 6 * EULER_STEP;
            vy = vy + (k1.getY() + 2 * k2.getY() + 2 * k3.getY() + k4.getY()) / 6 * EULER_STEP;
            t0 += EULER_STEP;
        }
        return new Vector2D(px, py);
    }

    private Vector2D calculateK2Velocity(Vector2D k1, Vector2D gForce, double vx, double vy) {
        double x = k1.getX();
        double x2 = vx + x * EULER_STEP / 2;
        double y = k1.getY();
        double y2 = vy + y * EULER_STEP / 2;
        Vector2D fForce = calculateFrictionForce(x2, y2);
        return calculateAcceleration(gForce, fForce);
    }

    private Vector2D calculateK3Velocity(Vector2D k2, Vector2D gForce, double vx, double vy) {
        double x = k2.getX();
        double x2 = vx + x * EULER_STEP / 2;
        double y = k2.getY();
        double y2 = vy + y * EULER_STEP / 2;
        Vector2D fForce = calculateFrictionForce(x2, y2);
        return calculateAcceleration(gForce, fForce);
    }

    private Vector2D calculateK4Velocity(Vector2D k3, Vector2D gForce, double vx, double vy) {
        double x = k3.getX();
        double x2 = vx + EULER_STEP * x;
        double y = k3.getY();
        double y2 = vy + EULER_STEP * y;
        Vector2D fForce = calculateFrictionForce(x2, y2);
        return calculateAcceleration(fForce, gForce);
    }


    private Vector2D calculateK4Position(Vector2D k3, double px, double py) {
        double x = k3.getX();
        double x2 = px + EULER_STEP * x;
        double y = k3.getY();
        double y2 = py + EULER_STEP * y;
        return calculateHxHy(x2, y2);
    }

    private Vector2D calculateK3Position(Vector2D k2, double px, double py) {
        double x = k2.getX();
        double x2 = px + EULER_STEP * x / 2;
        double y = k2.getY();
        double y2 = py + EULER_STEP * y / 2;
        return calculateHxHy(x2, y2);
    }

    private Vector2D calculateK2Position(Vector2D k1, double px, double py) {
        double x = k1.getX();
        double x2 = px + x * EULER_STEP / 2;
        double y = k1.getY();
        double y2 = py + y * EULER_STEP / 2;
        return calculateHxHy(x2, y2);
    }

}
