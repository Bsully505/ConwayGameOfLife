import javax.swing.*;
import java.awt.*;
import java.util.Stack;

/**
 * The problem is that the history of the walls is not preserved aka once you start knocking down walls it starts the maze over
 */

public class CellularAutomataSolver {
    JPanel[][] mat;
    CellularAutomataMaze maze;
    Stack<Point> walls = new Stack<Point>();
    boolean solved = false;

    public CellularAutomataSolver() {
        maze = new CellularAutomataMaze();

        maze.runWithRules(new int[] {2,3,4,5},new int[] {2,3},700);
        Point Start = getStart(maze.mat);
        maze.repaintSingle(Start.x,Start.y,Color.pink);

        Point End = getEnd(maze.mat);
        maze.repaintSingle(End.x,End.y,Color.GREEN);
        mat = maze.mat;
        try{
            Thread.sleep(2000);
        }catch(InterruptedException e){}
        int count = 0;
        int fullCount=0;


        boolean flag = false;

        while(!flag){
            walls.clear();
            solved = false;
            for(Point v: getWalls(Start)){
                walls.add(v);
            }
            int res = venture(Start,false,Color.BLACK);
            if(res==1){
                flag = true;
            }

            while (!solved) {
                flag=false;
                fullCount++;

                if (res == 0) {
                    if (!walls.isEmpty()) {

                        Point val = walls.pop();
                        maze.repaintSingle(val.x, val.y, Color.RED);
                        res = venture(val, true, Color.BLACK);

                        if (res == 0) {
                            count++;
                            flag = true;
                            maze.repaintSingle(val.x, val.y, Color.GRAY);
                        }

                    }

                }


            }


            ResetBesidesRED();
        }
        ResetBesidesRED();

        maze.repaintSingle(Start.x,Start.y,Color.pink);
        maze.repaintSingle(End.x,End.y,Color.cyan);
    }
    public static void main(String[] args){
        new CellularAutomataSolver();


    }

    public Point getStart(JPanel[][] mat){
        boolean flag = true;
        while(flag){
            int x = (int)(Math.random()*mat.length);
            int y = (int)(Math.random()*mat[0].length);
            if(mat[x][y].getBackground()==Color.WHITE){
                return new Point(x,y);
            }
        }
        return new Point(0,0);
    }

    public Point getEnd(JPanel[][] mat){
        boolean flag = true;
        while(flag){
            int x = (int)(Math.random()*mat.length);
            int y = (int)(Math.random()*mat[0].length);
            if(mat[x][y].getBackground()==Color.WHITE){
                return new Point(x,y);
            }
        }
        return new Point(0,0);
    }

    public Point[] getAdjacent(Point p) {
        Stack<Point> pos = new Stack<>();
        try {
            if (mat[p.x - 1][p.y].getBackground() == Color.white||mat[p.x - 1][p.y].getBackground() == Color.GREEN) {
                pos.push(new Point(p.x - 1, p.y));
            }
        } catch (Exception e) {
        }
        try {
            if (mat[p.x][p.y + 1].getBackground() == Color.white||mat[p.x][p.y+1].getBackground() == Color.GREEN) {
                pos.push(new Point(p.x, p.y + 1));
            }
        } catch (Exception e) {
        }
        try {
            if (mat[p.x + 1][p.y].getBackground() == Color.white||mat[p.x +1][p.y].getBackground() == Color.GREEN) {
                pos.push(new Point(p.x + 1, p.y));
            }
        } catch (Exception e) {
        }
        try {
            if (mat[p.x][p.y - 1].getBackground() == Color.white||mat[p.x][p.y-1].getBackground() == Color.GREEN) {
                pos.push(new Point(p.x, p.y - 1));
            }
        } catch (Exception e) {
        }
        Point[] result = new Point[pos.size()];
        int v = 0;
        while (!pos.isEmpty()) {
            result[v++] = pos.pop();
        }
        return result;
    }
    public boolean NotBoundry(Point p){
       return( p.x>1&&p.x<mat.length-1&&p.y>1&&p.y<mat[0].length-1);
    }
        public Point[] getWalls(Point p){
            Stack<Point> pos = new Stack<>();
            try {
                if (mat[p.x - 1][p.y].getBackground()==Color.BLACK&&NotBoundry(new Point(p.x-1,p.y))){
                    pos.push(new Point(p.x-1,p.y));
                }
            }catch(Exception e){}
            try {
                if (mat[p.x][p.y+1].getBackground()==Color.BLACK&&NotBoundry(new Point(p.x,p.y+1))){
                    pos.push(new Point(p.x,p.y+1));
                }
            }catch(Exception e){}
            try {
                if (mat[p.x + 1][p.y].getBackground()==Color.BLACK&&NotBoundry(new Point(p.x+1,p.y))){
                    pos.push(new Point(p.x+1,p.y));
                }
            }catch(Exception e){}
            try {
                if (mat[p.x][p.y-1].getBackground()==Color.BLACK&&NotBoundry(new Point(p.x,p.y-1))){
                    pos.push(new Point(p.x,p.y-1));
                }
            }catch(Exception e){}
            Point[] result = new Point[pos.size()];
            int v =0;
            while(!pos.isEmpty()){
                result[v++]= pos.pop();
            }return result;


    }
    public int venture(Point p,boolean wall,Color curCol) {
            if(solved)return 0;
            if(curCol== Color.BLACK){
                curCol = new Color(
                        (float)(Math.random()),
                        (float)(Math.random()),
                        (float)(Math.random())

                );
            }
            if(mat[p.x][p.y].getBackground()==Color.GREEN){
                return 1;
            }
            else {
                if(!wall) {
                    maze.repaintSingle(p.x, p.y, curCol);
                }else maze.repaintSingle(p.x,p.y,Color.RED);
                Point[] nextvals = getAdjacent(p);
                for (Point z : getWalls(p)) {
                    if (!walls.contains(z)) {
                        walls.push(z);
                    }

                }

                int temp =0;
                for (Point next : nextvals) {

                    if(mat[next.x][next.y].getBackground()==Color.GREEN){
                        solved = true;
                        return 1;
                    }
                    else {
                        int tempvar = venture(next,false,curCol);
                        if (tempvar ==1){//means that it is apart of the solvable path
                            maze.repaintSingle(next.x,next.y,Color.GREEN);

                        }
                        temp+= tempvar;
                    }

                }
                if(temp!=0) {//need to fix this
                    maze.repaintSingle(p.x,p.y,Color.GREEN);
                    return 1;
                }

            }
        return 0;

    }
    public void ResetBesidesRED(){
        for(int i =0;i<mat.length;i++){
            for(int j = 0;j< mat[i].length;j++){
                if(mat[i][j].getBackground()==Color.GRAY){
                    maze.repaintSingle(i,j,Color.BLACK);
                }
                if(mat[i][j].getBackground()!=Color.BLACK&&mat[i][j].getBackground()!=Color.GREEN){
                    maze.repaintSingle(i,j,Color.white);
                }

            }
        }
    }




}
