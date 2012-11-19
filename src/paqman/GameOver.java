/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paqman;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 *
 * @author itzcoatl90
 */
public class GameOver extends JPanel implements ActionListener{
    private JButton ini;
    public static BufferedImage image;
    
    public GameOver() throws IOException{
        initComponents();
    }
    
    private void initComponents() throws IOException{
        ini = new JButton("¡Otra vez!");
        image = ImageIO.read(new File("img/GameOver.png"));
        ini.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IniciarClick(evt);
            }
        });
        this.setLayout(null);
        ini.setBounds(7*RunGame.TILE_LEN, 9*RunGame.TILE_LEN, 4*RunGame.TILE_LEN, (int)(0.7*RunGame.TILE_LEN));
        this.setBackground(Color.BLACK);
        this.add(ini);
    }
    
    public void IniciarClick(ActionEvent e) {
        RunGame.playAgain();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public void paintComponent (Graphics g) {
        g.drawImage(image, 0, 0, null);
        repaint();
    }
}
