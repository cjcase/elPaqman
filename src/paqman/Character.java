/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paqman;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

// Code by cjcase
public class Character implements Runnable{

    private Point2D location;
    private Point2D map_location_px;
    enum Direction{UP, DOWN, RIGHT, LEFT, STATIC};
    
    private Image[] up_animation;
    private Image[] down_animation;
    private Image[] left_animation;
    private Image[] right_animation;
    private int up_animation_length;
    private int down_animation_length;
    private int left_animation_length;
    private int right_animation_length;
    private Stage stage;
    private float delta;
    private float acumulated_delta;
    private int current_animation_index;
    private int tmp_animation_index;
    //boundary;
    private String config_file;
    private Direction pacman_direction;
    Thread runner;
    private Point2D upper_left_boundary;
    private Point2D lower_right_boundary;
    //stage references
    
    
    public Character(String path){
        config_file = path;
        read_config(path);
        pacman_direction =  Direction.UP;
        map_location_px = new Point2D.Double(0,0);
    }
    
    public Character(String path, Stage new_stage){
        config_file = path;
        this.stage = new_stage;
        pacman_direction =  Direction.UP;
        read_config(path);
        map_location_px = new Point2D.Double(0,0);
        upper_left_boundary = new Point2D.Double(0,0);
        lower_right_boundary = new Point2D.Double(0,0);
        current_animation_index = 0;
        runner = new Thread(this, "character");
        runner.start();
    }

    public void move(){
        //set_direction(direction);
        //System.out.println("is_relative_pos_updated: " + is_relative_pos_updated());
        
        //System.out.println("location.getX() " + location.getX() + " * " + 40 + " map_location_px.getX "+ map_location_px.getX());
        
        //System.out.println("location.getY() " + location.getY() + " * " + 40 + " map_location_px.getX "+ map_location_px.getY());
        //if(canMove() && is_relative_pos_updated()){
         update_location_in_matrix();
        //}
        //if(!is_relative_pos_updated()){
         update_location_in_pixels();
        //}
         update_boundaries();
        
        
    }
    
    public void set_direction(int direction){
        switch(direction){
            case 0:
                pacman_direction = Direction.UP;
                break;
            case 1:
                pacman_direction = Direction.DOWN;
                break;
            case 2:
                pacman_direction = Direction.LEFT;
                break;
            case 3:
                pacman_direction = Direction.RIGHT;
                break;
        }
    }
    
    
    private boolean canMove(){
        int matrix[][] =  stage.getMatrix();
        switch(pacman_direction){
            case UP:
                if((int)location.getY() >= 0 && map_location_px.getY() > (location.getY() * 40)){
                    return true;
                }
                if((map_location_px.getY() - (location.getY() * 40) < 2) && matrix[(int)location.getY()-1][(int)location.getX()] == 0 ){
                    return true;
                }
                return false;
                
            case DOWN:
                if((int)location.getY() < stage.getMatrix_height()-1 && matrix[(int)location.getY()+1][(int)location.getX()] == 0){
                    return true;
                }else{
                    return false;
                }
                //
            case LEFT:
                System.out.println((upper_left_boundary.getY() - location.getY()*40) < 2);
                System.out.println((lower_right_boundary.getY() <= ((location.getY()+1) * 40)));
                System.out.println(lower_right_boundary.getY());
                System.out.println((((location.getY()+1) * 40)));
                if((int)location.getX() >= 0 && map_location_px.getX() > (location.getX() * 40) && 
                        ((upper_left_boundary.getY() - location.getY()*40) < 2) && lower_right_boundary.getY() <= ((location.getY()+1) * 40)){
                    return true;
                }
                System.out.println((upper_left_boundary.getY() - location.getY()*40) < 2);
                System.out.println((lower_right_boundary.getY() <= ((location.getY()+1) * 40)));
                System.out.println(lower_right_boundary.getY());
                System.out.println((((location.getY()+1) * 40)));
                if((((upper_left_boundary.getY() - location.getY()*40) < 2) && lower_right_boundary.getY() <= ((location.getY()+1) * 40) &&
                        map_location_px.getX() - (location.getX() * 40) < 2) && matrix[(int)location.getY()][(int)location.getX()-1] == 0){
                    return true;
                }
                return false;
                
                //break;
            case RIGHT:
                if(location.getX() < stage.getMatrix_width()-1 &&matrix[(int)location.getY()][(int)location.getX()+1] == 0 && (map_location_px.getY()-location.getY()*40) < 2){
                    return true;
                }
                return false;
            case STATIC:
                return false;
            
        }
        return true;
    }
    
    private void update_boundaries(){
        upper_left_boundary.setLocation(map_location_px.getX(), map_location_px.getY());
        lower_right_boundary.setLocation(map_location_px.getX() + 40, map_location_px.getY() + 40);
    }
    
    private void update_location_in_matrix(){
        //int matrix[][] =  stage.getMatrix();
        /*switch(pacman_direction){
            case UP:
                location.setLocation(location.getX(),location.getY()-1);
                break;
            case DOWN:
                location.setLocation(location.getX(),location.getY()+1);
                break;
            case LEFT:
                location.setLocation(location.getX()-1,location.getY());
                break;
            case RIGHT:
                location.setLocation(location.getX()+1,location.getY());
                break;
            case STATIC:
                break;
        }*/
        location.setLocation((int)(map_location_px.getX()) / 40 , (int)(map_location_px.getY()) / 40 );
    }
    
    private boolean is_relative_pos_updated(){
        int width = stage.getWidth();
        int height =  stage.getHeight();
        int squares_size_px = 40;//height / stage.getMatrix_height();
 
        if( (Math.abs(((location.getX() * squares_size_px)) - map_location_px.getX()) < 2)  &&
            (Math.abs(((location.getY() * squares_size_px)) - map_location_px.getY()) < 2)){
            return true;
        }else{
            return false;
        }    
    }
    
    private void update_location_in_pixels(){
        int width = stage.getWidth();
        int height =  stage.getHeight();
        int squares_size_px = 40;
        /*
        System.out.println("location.getX() " + location.getX() + " * " + squares_size_px + " map_location_px.getX "+ map_location_px.getX());
        
        System.out.println("location.getY() " + location.getY() + " * " + squares_size_px + " map_location_px.getX "+ map_location_px.getY());
        */
        /*
        if(location.getX() * squares_size_px != map_location_px.getX() && ((location.getX() * squares_size_px)- map_location_px.getX() > 1 )){
            acumulate_delta();
            if(location.getX() * squares_size_px > map_location_px.getX()){
                map_location_px.setLocation(map_location_px.getX() + delta, map_location_px.getY());
            }else{
                map_location_px.setLocation(map_location_px.getX() - delta, map_location_px.getY());
            }
        }
        
        if(location.getY() * squares_size_px != map_location_px.getY() && ((location.getY() * squares_size_px)- map_location_px.getY() > 1 )){
            acumulate_delta();
            if(location.getY() * squares_size_px > map_location_px.getY()){
                map_location_px.setLocation(map_location_px.getX(), map_location_px.getY() + delta);
            }else{
                map_location_px.setLocation(map_location_px.getX(), map_location_px.getY() - delta);
            }
        }
        */
        switch(pacman_direction){
            case UP:
                if(map_location_px.getY() > 0 && canMove() && canMove())
                 map_location_px.setLocation(map_location_px.getX(), map_location_px.getY() - delta);
                break;
                
            case DOWN:
                if(map_location_px.getY() < stage.getHeight() && canMove())
                map_location_px.setLocation(map_location_px.getX(), map_location_px.getY() + delta);
                break;
                
            case LEFT:
                if(map_location_px.getX() > 0 && canMove() && canMove())
                map_location_px.setLocation(map_location_px.getX() - delta, map_location_px.getY());
                break;
                
            case RIGHT:
                if(map_location_px.getY() < stage.getWidth() && canMove())
                map_location_px.setLocation(map_location_px.getX() + delta, map_location_px.getY());
                break;
        }
        //System.out.println("square size px " + squares_size_px);
        //System.out.println("square size px " + squares_size_px);
        //System.out.println("map location px " + map_location_px.getX());
        //System.out.println("map location px " + map_location_px.getY());
    }
    
    public void acumulate_delta(){
        acumulated_delta += delta;
        if(acumulated_delta > 1){
            acumulated_delta = 0;
        }
    }
    
    public float get_acumulated_delta(){
        return acumulated_delta;
    }
    
    
    public void read_config(String path){
        //config_file = path;
        try{
            // Open the file that is the first 
            // command line parameter
            FileInputStream fstream = new FileInputStream(path);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            //Read File Line By Line
            int animation_type = 0;
            int image_index = 0;
            int no_animations = 0;
            int animation_length = 0;


            while (animation_type < 4)   {
                strLine = br.readLine();
                //System.out.println(strLine); 
                no_animations = Integer.parseInt(strLine);
                animation_length =  no_animations;
                image_index = 0;
                while(no_animations-- > 0){
                    strLine = br.readLine();
                    //System.out.println(strLine);            
                    switch(animation_type){
                        case 0:
                            if(up_animation == null || up_animation_length != animation_length){
                                up_animation =  new Image[animation_length];
                                up_animation_length = animation_length;
                            }
                            up_animation[image_index++] = new ImageIcon(strLine).getImage();
                            break;
                        case 1:
                            if(down_animation == null || down_animation_length != animation_length){
                                down_animation =  new Image[animation_length];
                                down_animation_length = animation_length;
                            }
                            down_animation[image_index++] = new ImageIcon(strLine).getImage();
                            break;
                        case 2:
                            if(left_animation == null || left_animation_length != animation_length){
                                left_animation =  new Image[animation_length];
                                left_animation_length = animation_length;
                            }
                            left_animation[image_index++] = new ImageIcon(strLine).getImage();
                            break;
                        case 3:
                            if(right_animation == null || right_animation_length != animation_length){
                                right_animation =  new Image[animation_length];
                                right_animation_length = animation_length;
                            }
                            right_animation[image_index++] = new ImageIcon(strLine).getImage();
                            break;
                    }
                    // Print the content on the console
                    //System.out.println (strLine);
                }
                animation_type++;
            }
            location =  new Point2D.Double(Integer.parseInt(br.readLine()), Integer.parseInt(br.readLine()));
            delta = (float) Float.parseFloat(br.readLine());
            //System.out.println(location.getX());
            //System.out.println(location.getY());
            //Close the input stream
            in.close();
            
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        
    }
    
    private void update_frame_animation(){
        switch(pacman_direction){
            case UP:
                current_animation_index = tmp_animation_index++ % up_animation_length;
                if(tmp_animation_index % up_animation_length == 0)tmp_animation_index = 0;
                break;
            case DOWN:
                current_animation_index = tmp_animation_index++ % down_animation_length;
                if(tmp_animation_index % down_animation_length == 0)tmp_animation_index = 0;
                break;
            case LEFT:
                current_animation_index = tmp_animation_index++ % left_animation_length;
                if(tmp_animation_index % left_animation_length == 0)tmp_animation_index = 0;
                break;
            case RIGHT:
                current_animation_index = tmp_animation_index++ % right_animation_length;
                if(tmp_animation_index % right_animation_length == 0)tmp_animation_index = 0;
                break;
            default:
                current_animation_index = current_animation_index++ % right_animation_length;
                if(tmp_animation_index % up_animation_length == 0)tmp_animation_index = 0;
        }
    }
    
    public void draw(Graphics2D g2d){
        //DrawPacManLeft(Graphics2D g2d)
        switch(pacman_direction){
            case UP:
                //current_animation_index = current_animation_index++ % up_animation_length;
                g2d.drawImage(up_animation[current_animation_index], (int)map_location_px.getX(),(int) map_location_px.getY(), null);
                break;
            case DOWN:
                //current_animation_index = current_animation_index++ % down_animation_length;
                g2d.drawImage(down_animation[current_animation_index], (int)map_location_px.getX(),(int) map_location_px.getY(), null);
                break;
            case LEFT:
                //current_animation_index = current_animation_index++ % left_animation_length;
                g2d.drawImage(left_animation[current_animation_index], (int)map_location_px.getX(),(int) map_location_px.getY(), null);
                break;
            case RIGHT:
                //current_animation_index = current_animation_index++ % right_animation_length;
                g2d.drawImage(right_animation[current_animation_index], (int)map_location_px.getX(),(int) map_location_px.getY(), null);
                break;
            default:
                //current_animation_index = current_animation_index++ % right_animation_length;
                g2d.drawImage(right_animation[current_animation_index], (int)map_location_px.getX(),(int) map_location_px.getY(), null);
        }
        //System.out.println("");
        //System.out.println(current_animation_index);
        //g2d.draw
    }
    
    
    
    @Override
    public void run() {
        while(true){
            move();
            update_frame_animation();
            //System.out.println(this.current_animation_index);
            try {
                runner.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Character.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
