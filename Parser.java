package sample.physx;

import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;

public class Parser {
    private Function function;
    public Parser(String heightLiteral){
        String prefix = "At(x,y) = ";
        function = new Function(prefix+heightLiteral);
    }

    public double getHeight(Vector2d coords){
        double x = coords.get_x();
        double y = coords.get_y();
        String s = "At(" + x +","+ y + ")";
        Expression e1 = new Expression(s,function);
        return e1.calculate();


    }

}
