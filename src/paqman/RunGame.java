/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paqman;

// Code by cjcase

import javax.swing.JFrame;

public class RunGame{
    
    static Game pacman;
    private static int EXIT_ON_CLOSE;
    
    public static void main(String args[]){
        /*try{
            pacman = new Game();
            Thread go = new Thread(pacman);
            go.start();
        } catch (Exception e){
            System.out.println("Error: " + e);
        }*/
        
        
        Stage stage = new Stage("map.txt");
        ///stage.setSize(300,300);
        stage.setVisible(true);
        stage.print_matrix();
        stage.add_ghost("config.txt");
        //Thread nt = new Thread(new Character("config.txt", stage), "test");
        //nt.start();
        JFrame window = new JFrame();
        window.setSize(700,700);
        window.setVisible(true);
        window.add(stage,0);
        //window.add
        //window.add(stage)
        //window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
   }    
    
}
