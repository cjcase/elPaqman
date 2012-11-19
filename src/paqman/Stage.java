/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paqman;

/**
 *
 * @author itzcoatl90
 */

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Stage extends JPanel implements ActionListener{
    private int matrix[][];
    private int matrix_width;
    private int matrix_height;
    private Vector<Character> ghosts;
    private Pacman pacman;
    private Timer timer;
    private Vector<LineTrace> tracert;
    private int ballsToEat;
    
    
    Stage(int width, int height){
        matrix = new int[height][width];
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                matrix[i][j] = i;
            }
        }
    }
    
    Stage(String filepath){
        read_config(filepath);
        ballsToEat=0;
        this.setBackground(Color.black);
        this.setSize(matrix_height * RunGame.TILE_LEN, matrix_width * RunGame.TILE_LEN);
        ghosts = new <Character>Vector();
        tracert = new <LineTrace>Vector();
        calculeMaze();
        setDoubleBuffered(true);
        addKeyListener(new TAdapter());
        setFocusable(true);
        timer = new Timer(30, this);
        timer.start();
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int getMatrix_width() {
        return matrix_width;
    }

    public void setMatrix_width(int matrix_width) {
        this.matrix_width = matrix_width;
    }

    public int getMatrix_height() {
        return matrix_height;
    }

    public void setMatrix_height(int matrix_height) {
        this.matrix_height = matrix_height;
    }
    
    public void add_ghost(String config){
        Character ghost = new Character(config, this);
        ghosts.add(ghost);
    }
    
    public void add_pacman(Pacman new_pacman){
        this.pacman = new_pacman;
    }
    
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawMazeTracer(g);
        drawDinamics(g);
        try {
            calculeCollition();
        } catch (IOException ex) {
            Logger.getLogger(Stage.class.getName()).log(Level.SEVERE, null, ex);
        }
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
    
    private void drawDinamics(Graphics g){
        drawPoints(g);
        drawGhosts(g);
        drawPacman(g);
    }
    
    private void drawPoints(Graphics g){
        g.setColor( Color.yellow );
        for(int row = 0; row < matrix_height; row++){
            for(int col = 0; col < matrix_width; col++){
                if(matrix[row][col]==2){
                    pointAt(g,col*RunGame.TILE_LEN,row*RunGame.TILE_LEN,RunGame.TILE_LEN);
                }
            }
        }
    }
    
    private void pointAt(Graphics g,int x,int y,int size){
        g.fillOval(x+(size/3), y+(size/3), size/3, size/3);
    }
    
    private void drawGhosts(Graphics g){
        Iterator <Character>itr = ghosts.iterator();
        Character ghost;
        
        while(itr.hasNext()){
            ghost = itr.next();
            ghost.draw((Graphics2D) g);   
        }
    }
    
    private void drawPacman(Graphics g){
        if(pacman != null){
            pacman.draw((Graphics2D) g);
        }
    }
    
    private void drawMazeTracer(Graphics g){
        g.setColor( Color.blue );
        Iterator <LineTrace>itr = tracert.iterator();
        LineTrace trace;
        while(itr.hasNext()){
            trace = itr.next();
            trace.execute(g);
        }
        g.setColor(Color.RED);
        g.drawRect(0,0,this.getMatrix_width()*RunGame.TILE_LEN,this.getMatrix_height() * RunGame.TILE_LEN);
    }
    
    private void calculeMaze(){
        calculeRoof();
        calculeFloor();
        calculeLeftSide();
        calculeRightSide();
    }
    
    private void calculeRoof(){
        boolean matrixAuxiliar[][] = new boolean[matrix_height][matrix_width];
        int auxLen;
        for(int row = 0; row < matrix_height; row++){
            for(int col = 0; col < matrix_width; col++){
                if(!matrixAuxiliar[row][col] && matrix[row][col]==1){
                    auxLen = chaseRoof(row, col);
                    for(int i=0;i<auxLen;i++){
                        matrixAuxiliar[row][col+i]=true;
                    }
                    if(auxLen>0){
                        tracert.add(new LineTrace(col,row,col+auxLen,row, RunGame.TILE_LEN));
         //tracert.add(new LineTrace(this,"drawHorizontalLine",new Object[] {(Object) g,(Object)row,(Object) col,(Object) auxLen}));
                    }
                }else if(matrix[row][col]==2){
                    ballsToEat++;
                }
            }
        }
    }
    
    private int chaseRoof(int row,int col){
        int len=0;
        if(!(row > 0 && matrix[row-1][col]==1)){
            len++;
            if(col < matrix_width-1 && matrix[row][col+1] == 1){
                len+=chaseRoof(row, col+1);
            }
        }
        return len;
    }
    
    private void calculeFloor(){
        boolean matrixAuxiliar[][] = new boolean[matrix_height][matrix_width];
        int auxLen;
        for(int row = 0; row < matrix_height; row++){
            for(int col = 0; col < matrix_width; col++){
                if(!matrixAuxiliar[row][col] && matrix[row][col]==1){
                    auxLen = chaseFloor(row, col);
                    for(int i=0;i<auxLen;i++){
                        matrixAuxiliar[row][col+i]=true;
                    }
                    if(auxLen>0){
                        tracert.add(new LineTrace(col,row+1,col+auxLen,row+1, RunGame.TILE_LEN));
                    }
                }
            }
        }
    }
    
    private int chaseFloor(int row,int col){
        int len=0;
        if(row < matrix_height-1 && matrix[row+1][col]!=1){
            len++;
            if(col < matrix_width-1 && matrix[row][col+1] == 1){
                len+=chaseFloor(row, col+1);
            }
        }
        return len;
    }
    
    private void calculeLeftSide(){
        boolean matrixAuxiliar[][] = new boolean[matrix_height][matrix_width];
        int auxWit;
        for(int row = 0; row < matrix_height; row++){
            for(int col = 0; col < matrix_width; col++){
                if(!matrixAuxiliar[row][col] && matrix[row][col]==1){
                    auxWit = chaseLefts(row, col);
                    for(int i=0;i<auxWit;i++){
                        matrixAuxiliar[row+i][col]=true;
                    }
                    if(auxWit>0){
                        tracert.add(new LineTrace(col,row,col,row+auxWit, RunGame.TILE_LEN));
                    }
                }
            }
        }
    }
    
    private int chaseLefts(int row,int col){
        int wit=0;
        if(!(col > 0 && matrix[row][col-1]==1)){
            wit++;
            if(row < matrix_height-1 && matrix[row+1][col] == 1){
                wit+=chaseLefts(row+1, col);
            }
        }
        return wit;
    }
    
    private void calculeRightSide(){
        boolean matrixAuxiliar[][] = new boolean[matrix_height][matrix_width];
        int auxWit;
        for(int row = 0; row < matrix_height; row++){
            for(int col = 0; col < matrix_width; col++){
                if(!matrixAuxiliar[row][col] && matrix[row][col]==1){
                    auxWit = chaseRights(row, col);
                    for(int i=0;i<auxWit;i++){
                        matrixAuxiliar[row+i][col]=true;
                    }
                    if(auxWit>0){
                        tracert.add(new LineTrace(col+1,row,col+1,row+auxWit, RunGame.TILE_LEN));
                    }
                }
            }
        }
    }
    
    private int chaseRights(int row,int col){
        int wit=0;
        if(!(col < matrix_width -1 && matrix[row][col+1]==1)){
            wit++;
            if(row < matrix_height-1 && matrix[row+1][col] == 1){
                wit+=chaseRights(row+1, col);
            }
        }
        return wit;
    }
    
    void read_config(String filepath){
        try{
            // Open the file that is the first 
            // command line parameter
            FileInputStream fstream = new FileInputStream(filepath);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            //Read File Line By Line
            matrix_height =  Integer.parseInt(br.readLine());
            matrix_width =  Integer.parseInt(br.readLine());
            matrix = new int[matrix_height][matrix_width];
            
            int lines_counter =  matrix_height;
            int index = 0;
            while(lines_counter > 0){
                strLine = br.readLine();
                StringTokenizer tokens = new StringTokenizer(strLine, ",");
                index = 0;
                while(tokens.hasMoreTokens()){
                    matrix[matrix_height - lines_counter][index++] = Integer.parseInt(tokens.nextToken());
                }
                lines_counter--;
            }
            in.close();
            
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    void print_matrix(){
        for(int i = 0; i < matrix_height; i++){
            for(int j = 0; j < matrix_width; j++){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    
    int[][] get_matrix(){
        return matrix;
    }
    
    private void calculeCollition() throws IOException{
        calculePointCol();
        calculeGhostCol();
    }
    
    private void calculePointCol(){
        if(matrix[(int)pacman.getLocation().getY()][(int)pacman.getLocation().getX()]==2){
            matrix[(int)pacman.getLocation().getY()][(int)pacman.getLocation().getX()]=0;
            Game.pointToScore();
        }
    }

    private void calculeGhostCol() throws IOException{
        Iterator <Character>itr = ghosts.iterator();
        Character ghost;
        while(itr.hasNext()){
            ghost = itr.next();
            //System.out.println("GHOST:"+ghost.locationToString());
            //System.out.println("PACMAN:"+pacman.locationToString());
            if((int)ghost.getLocation().getY()==(int)pacman.getLocation().getY() &&
                (int)ghost.getLocation().getX()==(int)pacman.getLocation().getX()){
                Game.ghostToScore();
                die();
            }
        }
    }
    
    private void die() throws IOException{
        pacman.lifes--;
        if(pacman.lifes>=0){
            pacman.resetLocation();
        } else {
            RunGame.endGame();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        //throw new UnsupportedOperationException("Not supported yet.");
        repaint();
        //System.out.println("action performed");
    }

   
    class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

          int key = e.getKeyCode();
          switch(key){
              case KeyEvent.VK_UP:
                  pacman.set_direction(0);
                  break;
              case KeyEvent.VK_DOWN:
                  pacman.set_direction(1);
                  break;
              case KeyEvent.VK_LEFT:
                  pacman.set_direction(2);
                  break;
              case KeyEvent.VK_RIGHT:
                  pacman.set_direction(3);
                  break;
          }

        }
          //System.out.println("keylistener");
      }

    /*static public class LineTrace {
        private Object maze;
        private Method draw;
        private Object[] parameters;
        
        public LineTrace( Object pmaze, String lineToDraw, Object[] arguments ) {
            maze = pmaze;
            parameters = arguments;
            Class cls = pmaze.getClass();
            Class[] argTypes = new Class[parameters.length];
            for (int i=0; i < parameters.length; i++){
                argTypes[i] = parameters[i].getClass();
            }
            try {
                draw = cls.getMethod( lineToDraw, argTypes );
            } catch( NoSuchMethodException e ) {
                System.out.println( e );
            }
        }
        
        public Object execute() {
            try {
                return draw.invoke( maze, parameters );
            } catch(IllegalAccessException e) {
                System.out.println( e );
            } catch(InvocationTargetException e) {
                System.out.println( e );
            }
        return null;
        }
        
    }*/
}


/*
 
 public static void main( String[] args ) {
      LineTrace[] objs = { new LineTrace(1), new LineTrace(2) };
      System.out.print( "Normal call results: " );
      System.out.print( objs[0].addOne( new Integer(3) ) + " " );
      System.out.print( objs[1].addTwo( new Integer(4),
                                        new Integer(5) ) + " " );
      Command[] cmds = {
         new Command( objs[0], "addOne", new Integer[] { new Integer(3) } ),
         new Command( objs[1], "addTwo", new Integer[] { new Integer(4),
                                                         new Integer(5) } ) };
      System.out.print( "\nReflection results:  " );
      for (int i=0; i < cmds.length; i++)
          System.out.print( cmds[i].execute() + " " );
      System.out.println();
}
 
 
 private int state;
   public LineTrace( int in ) {
      state = in;
   }
   public int addOne( Integer one ) {
      return state + one.intValue();
   }
   public int addTwo( Integer one, Integer two ) {
      return state + one.intValue() + two.intValue();
   }
 
 * 
 * /*
    public void drawHorizontalLine(Object g, Object x1, Object y1, Object x2){
        //Graphics2D g, Integer x1, Integer y1, Integer x2){
        ((Graphics) g).drawLine((Integer) x1, (Integer) y1, (Integer) x2, (Integer) y1);
    }
    
    private void drawVerticalLine(Graphics g){
    
    }
    
 * 
 * /*private void drawMaze(Graphics g){
        g.setColor( Color.blue );
        for(int row = 0; row < matrix_height; row++){
            for(int col = 0; col < matrix_width; col++){
                drawTile(g, matrix[row][col], col, row);
            }
        }
        g.setColor(Color.RED);
        g.drawRect(0,0,this.getMatrix_width()*RunGame.TILE_LEN,this.getMatrix_height() * RunGame.TILE_LEN);
    }
    
    private void drawTile(Graphics g, int tile, int col, int row){
        switch (tile){
            case 0:
            break;
            case 1:
                g.drawRect( col * RunGame.TILE_LEN, row * RunGame.TILE_LEN, RunGame.TILE_LEN-5, RunGame.TILE_LEN-5 );
            break;
            case 2:
            break;
            default:
            break;
        }
    }
 
 COMENTARIOS, para sacar todo lo inesesario de allá
 * 
 * Imports:
 * import java.awt.Dimension;
import java.awt.Image; 
 import java.lang.reflect.*;
 
 * 
 * en el constructor
 * 
 *         //Dimension d = new Dimension(matrix_height * 20, matrix_width * 20);
        //this.setPreferredSize(d);
 * 
 * 
 * en el add ghosts
 * 
 * //ghost.run();      
        
 * 
 * en el paint
 * 
 * //Graphics2D panel = (Graphics2D) g;
        //drawMaze(g);
 *         //Image ii = new Image();
        //g.drawImage(ii, 5, 5, this);
 * 
 * en los tracerlines
 * cuando el semi-command era command
 *         //tracert.add(new LineTrace(this,"drawHorizontalLine",new Object[] {(Object) g,(Object)row,(Object) col,(Object) auxLen}));
 * 
 * 
 */