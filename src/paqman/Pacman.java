/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paqman;

/**
 *
 * @author itzcoatl90
 */

import java.awt.geom.Point2D;

public class Pacman extends Character {
    public int lifes;
    private Point2D inicialLocation;
    private Point2D inicialLocationpx;
            
    public Pacman(String path, Stage stage) {
        super(path, stage);
        lifes = 3;
        inicialLocation = new Point2D.Double(location.getX(),location.getY());
        inicialLocationpx = new Point2D.Double(map_location_px.getX(),map_location_px.getY());
    }

    public int getLifes() {
        return lifes;
    }

    public void setLifes(int lifes) {
        this.lifes = lifes;
    }
    
    public void resetLocation(){
        location.setLocation(inicialLocation);
        map_location_px.setLocation(inicialLocationpx);
    }
    
}
