/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paqman;
import java.awt.Graphics;
import java.awt.geom.Point2D;
/**
 *
 * @author itzcoatl90
 */
public class LineTrace {
   private Point2D initPoint;
   private Point2D endPoint;
   private int Tile_Len;
   public LineTrace(int X1, int Y1, int X2, int Y2, int TL) {
      initPoint=new Point2D.Float(X1, Y1);
      endPoint=new Point2D.Float(X2, Y2);
      Tile_Len=TL;
   }
   
   public void execute(Graphics g){
       g.drawLine(((int)initPoint.getX())*Tile_Len, ((int)initPoint.getY())*Tile_Len,
               ((int)endPoint.getX())*Tile_Len, ((int)endPoint.getY())*Tile_Len);
   }
   public String toString(){
       return "init X <"+initPoint.getX()+"> Y <"+initPoint.getY()+">  End X <"+endPoint.getX()+"> Y <"+endPoint.getY()+">";
   }
}
