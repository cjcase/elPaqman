/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paqman;

// Code by cjcase
public class RunGame{
    
    static Game pacman;
    
    public static void main(String args[]){
        try{
            pacman = new Game();
            Thread go = new Thread(pacman);
            go.start();
        } catch (Exception e){
            System.out.println("Error: " + e);
        }
   }    
    
}
