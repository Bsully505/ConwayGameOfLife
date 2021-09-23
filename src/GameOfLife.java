import javax.swing.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

/**
 * Author Bryan Sullivan
 * This is a gui version of conways game
 *
 *
 * HOW TO RUN
 * you have option
 * 1) have the board randomly select spots to concider alive plots
 *      -Make sure that the StartRandom global variable is turned on
 * 2) have the board start with all living plots
 *      -turn off StartRandom but make sure that startAlive is true. If you want to change any of the plots to turn them on or off
 *      the only thing you have to do is click on the specific panel
 *
 * Ideas i want to include
 * - make the alive color be a range of colors to make this a little more colorful
 *
 * Ideas that I have learned or had to reteach myself
 * - mouse events
 * - GUI
 * - Panels
 * -
 *
 * Enchantments
 *  -creating a randomly generated field
 *
 * some errors that I am working on
 *  - when the game is running,as well when not running and is not at a width and height of 10 the click to toggle life is idnex out of bounds
 * errors that i fixed
 *
 * -cannot start the conways game and have it continue going while also updating the gui
 *      -fixed this by using a thread and having it run when the menu item:Start is equal to End becuase when you start it
 *        it changes the text to end and vice versa so when it has the start text it should not be running
 *
 *
 * - Logic is not correct it might be using the new copy version
 *  - the error happened when using the clone function when trying to create the new matrix whenever the cloned matrix
 *      was changed the original matrix changed as well
 */

public class GameOfLife implements Runnable {
    JFrame frame;
    JPanel panel;
    JMenuBar Bar;
    JMenu Options;
    JMenuItem start;
    boolean[][] GOL;
    JPanel[][] mat;
    Color Alive = Color.BLACK;
    Color Dead = Color.WHITE;
    int Width = 100;
    int Height = 100;
    boolean startAlive= false;
    boolean StartRandom= true;

    public GameOfLife(){
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
                if(StartRandom){
                    if(Math.random()<0.35){
                        mat[i][j].setBackground(Alive);
                    }
                    else{
                        mat[i][j].setBackground(Dead);
                    }
                }
                else {
                    //this is if you want to start either all dead or all alive
                     if(startAlive) {
                     mat[i][j].setBackground(Alive);
                     }
                     else{
                     mat[i][j].setBackground(Dead);
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
                            mat[yVal][xVal].setBackground(Alive);
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
                java.lang.Thread.sleep(1000);
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
            if(mat[p.x-1][p.y-1].getBackground()==Alive){
                result++;
            }
        }catch(Exception e){}
        try{//top
            if(mat[p.x][p.y-1].getBackground()==Alive){
                result++;
            }
        }catch(Exception e){}
        try{//top right
            if(mat[p.x+1][p.y-1].getBackground()==Alive){
                result++;
            }
        }catch(Exception e){}
        try{//left
            if(mat[p.x-1][p.y].getBackground()==Alive){
                result++;
            }
        }catch(Exception e){}
        try{//right
            if(mat[p.x+1][p.y].getBackground()==Alive){
                result++;
            }
        }catch(Exception e){}
        try{//bottom left
            if(mat[p.x-1][p.y+1].getBackground()==Alive){
                result++;
            }
        }catch(Exception e){}

        try{//bottom
            if(mat[p.x][p.y+1].getBackground()==Alive){
                result++;
            }
        }catch(Exception e){}
        try{//bottom right
            if(mat[p.x+1][p.y+1].getBackground()==Alive){
                result++;
            }
        }catch(Exception e){}
        return result;
    }

    public static void main(String[] args){
        GameOfLife run = new GameOfLife();

    }





    @Override
    public void run() {
        JPanel[][] copy = mat.clone();
        for(int i =0;i< mat.length;i++){
            for(int j=0;j<mat[i].length;j++){
                System.out.print(AliveNeighbors(mat,new Point(i,j)));
                /**
                 * the three rules
                 * if a live cell has two or three neighbors
                 */
                if(mat[i][j].getBackground()==Alive){
                    int neighbors= AliveNeighbors(mat,new Point(i,j));
                    if(neighbors !=2&&neighbors!=3){
                        copy[i][j].setBackground(Dead);
                    }
                }
                else{
                    int neighbors= AliveNeighbors(mat,new Point(i,j));
                    if(neighbors==3){
                        copy[i][j].setBackground(Alive);
                    }
                }
            }
            System.out.println();
        }
        for(int i =0;i< copy.length; i++){
            for(int j=0;j<copy[i].length;j++){
                mat[i][j].setBackground(copy[i][j].getBackground());
            }
        }
        mat = copy.clone();
    }
    public boolean isAlive(JPanel cur){
        return (cur.getBackground()==Alive);
    }


    public void run2(){
        Stack<Point> alterPoints = new Stack<Point>();
        JPanel[][] copy = mat.clone();
        for(int i =0;i< mat.length;i++){
            for(int j=0;j<mat[i].length;j++){
                //System.out.print(AliveNeighbors(mat,new Point(i,j)));
                /**
                 * the three rules
                 * if a live cell has two or three neighbors
                 */
                if(mat[i][j].getBackground()==Alive){
                    int neighbors= AliveNeighbors(mat,new Point(i,j));
                    if(neighbors !=2&&neighbors!=3){
                        //copy[j][i].setBackground(Dead);
                        alterPoints.push(new Point(i,j));
                    }
                }
                else{
                    int neighbors= AliveNeighbors(mat,new Point(i,j));
                    if(neighbors==3){
                        //copy[i][j].setBackground(Alive);
                        alterPoints.push(new Point(i,j));
                    }
                }
            }
            //System.out.println();
        }
//        for(int i =0;i< copy.length; i++){
//            for(int j=0;j<copy[i].length;j++){
//                mat[i][j].setBackground(copy[i][j].getBackground());
//            }
//        }
        while(!alterPoints.isEmpty()){
            Point p = alterPoints.pop();
            if(isAlive(mat[p.x][p.y])){
                mat[p.x][p.y].setBackground(Dead);
            }
            else{ mat[p.x][p.y].setBackground(Alive);}
        }


    }
}
