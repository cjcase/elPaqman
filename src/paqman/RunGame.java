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
        
        stage.setVisible(true);
        //stage.print_matrix();
        stage.add_ghost("ghost_config.txt");
        
        stage.add_pacman(new Character("pacman_config.txt", stage));
        JFrame window = new JFrame();
        window.setSize(stage.getMatrix_width()*40,stage.getMatrix_height()*40);
        window.setVisible(true);
        //window.setLayout(new BorderLayout());
        //window.add(stage, BorderLayout.CENTER);
        window.add(stage);
        //window.add
        //window.add(stage)
        //window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
   }    
    
}
