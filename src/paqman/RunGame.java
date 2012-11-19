/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paqman;

/**
 *
 * @author itzcoatl90
 */
import java.io.IOException;
import javax.swing.JFrame;

public class RunGame{
    
    public static final int TILE_LEN=40;
    public static String mappath;
    public static String pacmanpath;
    public static String ghostspath;
    public static JFrame window;
    public static JFrame windowExit;
    public static Menu menu;
    public static Game game;
    
    public static void startGame(){
        game.setFirstStage(mappath, ghostspath, pacmanpath);
        game.stage.setVisible(true);
        game.resize(window);
        window.remove(menu);
        window.add(game.stage);
        game.stage.requestFocus();
    }
    
    public static void playAgain(){
        
    }
    
    public static void endGame() throws IOException{
        GameOver over= new GameOver();
        windowExit=new JFrame();
        windowExit.add(over);
        window.setVisible(false);
        over.setVisible(true);
        windowExit.setVisible(true);
    }
    
    public static void main(String args[]) throws IOException{
        window = new JFrame();
        menu = new Menu();
        game = game.getInstance();
        menu.setVisible(true);
        window.setSize(12*TILE_LEN,12*TILE_LEN);
        window.setVisible(true);
        window.add(menu);
   }
}

/*COMENTARIOS eliminados de arriba, por si se requieren usar aquí siguen.
 * Borrar cuando no se utilicen.
 * En declaraciones
 *
 * //private static Component mainPanel;
 * static Game pacman;
    private static int EXIT_ON_CLOSE;
 * //import java.awt.BorderLayout;
//import java.awt.Component;
//import java.awt.Dimension;
 * 
 *
 * TODO EN MAIN
     
 * /*try{
            pacman = new Game();
            Thread go = new Thread(pacman);
            go.start();
        } catch (Exception e){
            System.out.println("Error: " + e);
        }
 * 
 * //Stage stage = new Stage("map.txt");
        //stage.setVisible(true);
        ////stage.print_matrix();
        //stage.add_ghost("ghost_config.txt");
        
        //stage.add_pacman(new Character("pacman_config.txt", stage));
 * 
 * 
 * //window.setSize(stage.getMatrix_width()*40,stage.getMatrix_height()*40);
        //window.setVisible(true);
        ////window.setLayout(new BorderLayout());
        ////window.add(stage, BorderLayout.CENTER);
        //window.add(stage);
        //window.remove(stage);
        ////window.add
        ////window.add(stage)
        ////window.setDefaultCloseOperation(EXIT_ON_CLOSE);
 * 
 * 
     */