/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paqman;

// Code by cjcase

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Stage extends JPanel implements ActionListener{
    private int matrix[][];
    private int matrix_width;
    private int matrix_height;
    private Vector<Character> ghosts;
    private Character pacman;
    private Timer timer;
    
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
        this.setBackground(Color.black);
        //Dimension d = new Dimension(matrix_height * 20, matrix_width * 20);
        //this.setPreferredSize(d);
        this.setSize(matrix_height * 40, matrix_width * 40);
        ghosts = new <Character>Vector();
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
        //ghost.run();      
        
    }
    
    public void add_pacman(Character new_pacman){
        this.pacman = new_pacman;
    }
    
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D panel = (Graphics2D) g;
        
        drawMaze(g);
        
        Iterator <Character>itr = ghosts.iterator();
        Character ghost;
        
        while(itr.hasNext()){
            ghost = itr.next();
            ghost.draw(panel);   
        }
        
        if(pacman != null){
            pacman.draw(panel);
        }
        
        
        //Image ii = new Image();
        //g.drawImage(ii, 5, 5, this);
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
    
    private void drawMaze(Graphics g){
        g.setColor( Color.blue );
        for(int row = 0; row < matrix_height; row++){
            for(int col = 0; col < matrix_width; col++){
                if(matrix[row][col] != 0)
                g.drawRect( col * 40, row * 40, 35, 35 );
            }
        }
        g.setColor(Color.RED);
        g.drawRect(0,0,this.getMatrix_width()*40,this.getMatrix_height() * 40);
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
}
