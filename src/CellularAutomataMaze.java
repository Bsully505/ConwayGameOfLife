import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

public class CellularAutomataMaze {
    JFrame frame;
    JPanel panel;
    JMenuBar Bar;
    JMenu Options;
    JMenuItem start;
    boolean[][] GOL;
    JPanel[][] mat;
    //Color[] AliveColors = {Color.BLACK,Color.PINK,Color.BLUE,Color.RED,Color.cyan};
    Color[] AliveColors = {Color.BLACK};
    Color Alive = Color.BLACK;
    Color Dead = Color.WHITE;
    int Width = 150;
    int Height = 150;
    int centerRange = 5;
    boolean startAlive= false;
    boolean StartRandom= false;
    boolean MiddleCenter = true;
    boolean reboot = false;//restarts the maze when amount of changes is passes a point
    double waitTime = .25;
    double ChanceOfAliveOnStart= 0.35;


    public CellularAutomataMaze(){
        GOL = new boolean[Width][Height];
        //set up the frame
        frame = new JFrame();
        frame.setDefaultCloseOperation(3);
        //set up the panel inside the frame
        panel = new JPanel();
        mat = new JPanel[GOL.length][GOL[0].length];
        panel.setLayout(new GridLayout(mat.length,mat[0].length,1,1));

        for(int i =0;i< mat.length;i++){
            for(int j =0;j< mat[i].length;j++){
                mat[i][j] = new JPanel();
                mat[i][j].setOpaque(true);

                //this is if you want to start with a random amount of alive
                if(MiddleCenter){
                    if((i>((Height/2)-centerRange))&&(i<((Height/2)+centerRange)) && (j>((Width/2)-centerRange)) &&(j<((Width/2)+centerRange))){
                        mat[i][j].setBackground(AliveColors[(int) (Math.random() * (AliveColors.length))]);
                    }
                    else{
                        mat[i][j].setBackground(Dead);
                    }
                }
                else {
                    if (StartRandom) {
                        if (Math.random() < ChanceOfAliveOnStart) {
                            mat[i][j].setBackground(AliveColors[(int) (Math.random() * (AliveColors.length))]);
                        } else {
                            mat[i][j].setBackground(Dead);
                        }
                    } else {
                        //this is if you want to start either all dead or all alive
                        if (startAlive) {
                            mat[i][j].setBackground(AliveColors[(int) (Math.random() * (AliveColors.length))]);
                        } else {
                            mat[i][j].setBackground(Dead);
                        }


                    }
                }


                mat[i][j].addMouseListener(new MouseAdapter(){
                    public void mouseClicked(MouseEvent e)
                    {
                        //get the x, y of the screen and divide it by the (unit x and unit y)

                        int x = e.getXOnScreen();
                        int y = e.getYOnScreen();
                        int unitX = panel.getWidth()/mat.length;
                        int unitY=  (panel.getHeight())/mat[0].length;

                        System.out.println("X: "+x+"  Y: "+y);
                        System.out.println("UnitX: "+ unitX+"  UnitY: "+unitY);
                        System.out.println(x/unitX+" "+(y-79)/(unitY));

                        int xVal = x/unitX;
                        int yVal = (y-79)/unitY;

                        if(mat[yVal][xVal].getBackground()==Alive){
                            mat[yVal][xVal].setBackground(Dead);
                        }
                        else{
                            mat[yVal][xVal].setBackground(AliveColors[(int)(Math.random()*(AliveColors.length))]);
                        }

                    }
                });
                panel.add(mat[i][j]);
            }

        }
        //paint2();
        frame.add(panel);
        //set up the start button
        Bar = new JMenuBar();
        Options = new JMenu("Options");
        start = new JMenuItem("Start");

        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean begin = false;


                if(start.getText().toString().equals("Start")){

                    start.setText("END");


                    new Thread(new Runnable() {
                        public void run() {
                            while(start.getText().equals("END")) {

                                ConwayGame();



                            }
                        }
                    }).start();
                }
                else{
                    start.setText("Start");


                }
                // Runs outside of the Swing UI thread

            }
        });

        Options.add(start);
        Bar.add(Options);
        frame.setJMenuBar(Bar);

        frame.setSize(500,500);
        frame.setVisible(true);

        //set up the board for conways game




    }

    /**
     * tries calling the run funciton that implements the rules of conways game and alters the panels colors
     * based on the living or dead idea
     */
    public void ConwayGame() {
        int i = 0;
        while (i++ < 1) {
            run2();
            try {
                java.lang.Thread.sleep((long)(waitTime*1000));
            } catch (InterruptedException e) {
                System.err.println("sleep for conways game");
            }
        }

    }

    /**
     *
     * @param mat is the matrix that holds all of the panels which are acting as the objects for the game of Life
     * @param p the point that is getting its neighbors checked
     * @return all of the neighbors that are alive of point p
     *
     * neighbors are adjacent panels including diagonals
     */
    public int AliveNeighbors(JPanel[][] mat, Point p){
        int result= 0;


        try{//check the top left
            if(isAlive(mat[p.x-1][p.y-1])){
                result++;
            }
        }catch(Exception e){}
        try{//top
            if(isAlive(mat[p.x][p.y-1])){
                result++;
            }
        }catch(Exception e){}
        try{//top right
            if(isAlive(mat[p.x+1][p.y-1])){
                result++;
            }
        }catch(Exception e){}
        try{//left
            if(isAlive(mat[p.x-1][p.y])){
                result++;
            }
        }catch(Exception e){}
        try{//right
            if(isAlive(mat[p.x+1][p.y])){
                result++;
            }
        }catch(Exception e){}
        try{//bottom left
            if(isAlive(mat[p.x-1][p.y+1])){
                result++;
            }
        }catch(Exception e){}

        try{//bottom
            if(isAlive(mat[p.x][p.y+1])){
                result++;
            }
        }catch(Exception e){}
        try{//bottom right
            if(isAlive(mat[p.x+1][p.y+1])){
                result++;
            }
        }catch(Exception e){}
        return result;
    }

    public static void main(String[] args) {
        CellularAutomataMaze run = new CellularAutomataMaze();

    }
    public boolean isAlive(JPanel cur){
        for(Color v : AliveColors){
            if(v.equals(cur.getBackground())){
                return true;
            }

        }
        return false;
    }


    public void run2() {
        Stack<Point> alterPoints = new Stack<Point>();
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                /**
                 * the three rules
                 * if a live cell has two or three neighbors
                 */
                if (isAlive(mat[i][j])) {
                    int neighbors = AliveNeighbors(mat, new Point(i, j));
                    if (neighbors != 1 && neighbors != 2 && neighbors != 3 && neighbors != 4) {
                        alterPoints.push(new Point(i, j));
                    }
                } else {
                    int neighbors = AliveNeighbors(mat, new Point(i, j));
                    if (neighbors == 3) {
                        alterPoints.push(new Point(i, j));
                    }
                }
            }
        }
        int count = 0;
        while (!alterPoints.isEmpty()) {
            count++;
            Point p = alterPoints.pop();
            if (isAlive(mat[p.x][p.y])) {
                mat[p.x][p.y].setBackground(Dead);
            } else {
                mat[p.x][p.y].setBackground(AliveColors[(int) (Math.random() * (AliveColors.length))]);
            }
        }
        if (reboot) {
            if (count > 200) {
                for (int i = 0; i < mat.length; i++) {
                    for (int j = 0; j < mat[i].length; j++) {
                        if (StartRandom) {
                            if (Math.random() < ChanceOfAliveOnStart) {
                                mat[i][j].setBackground(AliveColors[(int) (Math.random() * (AliveColors.length))]);
                            } else {
                                mat[i][j].setBackground(Dead);
                            }
                        } else {
                            //this is if you want to start either all dead or all alive
                            if (startAlive) {
                                mat[i][j].setBackground(AliveColors[(int) (Math.random() * (AliveColors.length))]);
                            } else {
                                mat[i][j].setBackground(Dead);
                            }


                        }
                    }
                }
            }


        }
    }
}
