public class MazeSolver {

    public static void main(String[] args){
        CellularAutomataMaze maze = new CellularAutomataMaze();
        for(int i =0;i<500;i++){
            maze.run2();
        }
        maze.getStart();
        maze.getEnd();
    }
}
