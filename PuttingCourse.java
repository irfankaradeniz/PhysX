package sample.physx;

import java.io.*;
import java.util.Scanner;
import org.mariuszgromada.math.mxparser.*;
import org.mariuszgromada.math.mxparser.Function;
import sample.physx.Function2d;
import sample.physx.Vector2d;

public class PuttingCourse {
    private Function2d height;
    private Vector2d flag;
    private Vector2d start;
    private double friction;
    private double maxVelocity;
    private double holeT;
    private double gravity;
    private double mass;
    private double startX;
    private double startY;
    private double goalX;
    private double goalY;
    private double xCo;
    private double x2Co;
    private double yCo;
    private String heightLiteral;
    private Function2 function;
    private Parser parser;
    private double degrees;
    private double radians;


    public PuttingCourse(){}

    public String getHeightLiteral() {
        return heightLiteral;
    }

    //constructor method for PuttingCourse class
    public PuttingCourse(Function2d height, Vector2d flag, Vector2d start){
        this.height=height;
        this.flag=flag;
        this.start=start;
    }
    //constructor method
    public PuttingCourse(Function2d height, Vector2d flag, Vector2d start,double friction,double maxVelocity,double holeT, double gravity){
        this.height=height;
        this.flag=flag;
        this.start=start;
        this.friction=friction;
        this.maxVelocity=maxVelocity;
        this.holeT=holeT;
        this.gravity=gravity;
    }

    //getter and setter methods
    public double get_mass(){return mass;}
    public Function2 get_height(){
        Parser parser = new Parser(heightLiteral);
        Function2 function = new Function2(parser);
        return function;

    }
    public Vector2d get_flag_position(){
        return flag;
    }
    public Vector2d get_start_position(){
        return start;
    }
    public double get_friction_coefficient(){
        return friction;
    }
    public double get_maximum_velocity(){
        return maxVelocity;
    }
    public double get_hole_tolerance(){
        return holeT;
    }
    public double get_gravity(){
        return gravity;
    }

    public Vector2d calculate_velocity_hit(double speed, double degrees){
        radians = (degrees/180)* Math.PI;
        double y = speed * Math.sin(radians);
        double x = speed * Math.cos(radians);
        return new Vector2d(x,y);
    }

    //method to read course data from a file
    public void readFile(String path){
        File file = new File(path);
        Scanner in = new Scanner(System.in);
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            String[] word;
            int i = 0;
            while((st = br.readLine()) != null){
                ++i;
                st = st.replaceAll("\\s+","");
                word = st.split("=");
                switch(word[0]){
                    case "g":
                        gravity = Double.parseDouble(word[1]);
                        break;
                    case "m":
                        mass = Double.parseDouble(word[1]);
                        break;
                    case "mu":
                        friction = Double.parseDouble(word[1]);
                        break;
                    case "vmax":
                        maxVelocity = Double.parseDouble(word[1]);
                        break;
                    case "tol":
                        holeT = Double.parseDouble(word[1]);
                        break;
                    case "start":
                        String s = word[1];
                        s = s.replaceAll("[()]", "");
                        String[] coords = s.split(",");
                        start = new Vector2d(Double.parseDouble(coords[0]),Double.parseDouble(coords[1]));
                        break;
                    case "goal":
                        String s1 = word[1];
                        s1= s1.replaceAll("[()]", "");
                        String[] coords1 = s1.split(",");
                        flag = new Vector2d(Double.parseDouble(coords1[0]),Double.parseDouble(coords1[1]));
                        break;
                    case "height":
                        heightLiteral = word[1];
                        break;
                    default:
                        System.out.println("Parsing error at line"+i);
                        break;
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public double calculate_height(Vector2d p){
        Parser parser = new Parser(this.getHeightLiteral());
        Function2 function = new Function2(parser);
        return function.evaluate(p);
    }




}
