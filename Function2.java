package sample.physx;

public class Function2 implements Function2d {
    private Parser parser;

    public Function2(Parser parser) {

        this.parser = parser;
    }

    @Override
    public double evaluate(Vector2d p) {

        return parser.getHeight(p);
    }

    @Override
    public Vector2d gradient(Vector2d p) {
        double h = 0.0001;
        double z = evaluate(p);
        double x = (evaluate((new Vector2d(p.get_x()+h,p.get_y() )))-z)/h;
        double y = (evaluate((new Vector2d(p.get_x(),p.get_y()+h)))-z)/h;
        return new Vector2d(x,y);
    }
}
