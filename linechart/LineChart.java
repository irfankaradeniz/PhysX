package sample.physx.linechart;
import javax.swing.*;
import java.awt.*;
import org.jfree.chart.*;
import org.jfree.data.xy.*;
import org.jfree.chart.plot.*;
import sample.physx.*;


public class LineChart extends JFrame {

    public LineChart() {


        JPanel chartPanel = createChartPanel();
        add(chartPanel, BorderLayout.CENTER);

        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private JPanel createChartPanel() {
        String chartTitle = "Objects Movement Chart";
        String xAxisLabel = "X";
        String yAxisLabel = "Y";

        XYDataset dataset = createDataset();

        boolean showLegend = false;
        boolean createURL = false;
        boolean createTooltip = false;

        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,
                xAxisLabel, yAxisLabel, dataset,
                PlotOrientation.VERTICAL, showLegend, createTooltip, createURL);

        return new ChartPanel(chart);
    }

    private XYDataset createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        PuttingCourse course = new PuttingCourse();
        course.readFile("C:\\Users\\IRFAN\\IdeaProjects\\GolfPhase1\\src\\sample\\physx\\test.txt");
        PuttingSimulator simulator = new PuttingSimulator(course,new EulerSolver());
        simulator.take_shot(new Vector2d(4,4));
        dataset.addSeries(simulator.eulerSeries);

        VerletSolver verlet = new VerletSolver(new EulerSolver(),course);
        verlet.take_shot_verlet(new Vector2d(3,3));
        dataset.addSeries(verlet.verletSeries);

//        RungeKutta rungeKutta = new RungeKutta(course);
//        rungeKutta.puttingTogether(new Vector2d(3,3),new Vector2d(0,0));
//        dataset.addSeries(rungeKutta.rungeKuttaSeries);
        return dataset;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LineChart().setVisible(true);
            }
        });
    }
}