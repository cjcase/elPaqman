/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paqman;

// Code by cjcase

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JFrame;

public class RunGame{
    
    static Game pacman;
    private static int EXIT_ON_CLOSE;
    private static Component mainPanel;
    
    public static void main(String args[]){
        /*try{
            pacman = new Game();
            Thread go = new Thread(pacman);
            go.start();
        } catch (Exception e){
            System.out.println("Error: " + e);
        }*/
        
        
        Stage stage = new Stage("map.txt");
        //stage.setSize(300,300);
        stage.setVisible(true);
        stage.print_matrix();
        //stage.add_ghost("config.txt");
        
        stage.add_pacman(new Character("config.txt", stage));
        //Dimension d = new Dimension(300,300);
        //stage.setPreferredSize(d);
        //Thread nt = new Thread(new Character("config.txt", stage), "test");
        //nt.start();
        JFrame window = new JFrame();
        window.setSize(stage.getWidth(),stage.getHeight());
        window.setVisible(true);
        //window.setLayout(new BorderLayout());
        //window.add(stage, BorderLayout.CENTER);
        window.add(stage);
        //window.add
        //window.add(stage)
        //window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
   }    
    
}
