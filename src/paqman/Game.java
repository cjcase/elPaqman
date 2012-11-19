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

public class Game {

    public static Stage stage;
    public static int score;
    private Game() {}
    
    private static class GameHolder {
        private static final Game INSTANCE = new Game();
    }
    
    public static Game getInstance(){
        return GameHolder.INSTANCE;
    }
    
    public void setFirstStage(String mappath,String ghostpath, String pacmanpath){
        score = 0;
        stage = new Stage(mappath);
        stage.setVisible(true);
        stage.add_ghost(ghostpath);
        stage.add_pacman(new Pacman(pacmanpath, stage));
    }
    
    public void resize(JFrame window){
        window.setExtendedState(window.getExtendedState()|JFrame.MAXIMIZED_BOTH);
    }
    public static void pointToScore(){
        score+=10;
    }
    public static void ghostToScore(){
        score-=100;
    }
    public static void endToScore(int lifes){
        score+=100*(lifes+1);
    }
}

/*
COMENTARIOS
 *
 * en el resize
 * window.setSize((stage.getMatrix_width()+2)*RunGame.TILE_LEN,(stage.getMatrix_height()+2)*RunGame.TILE_LEN);
 */