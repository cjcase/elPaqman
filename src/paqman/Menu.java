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
//import java.applet.*;
//import javax.sound.sampled.AudioFormat;
//import javax.sound.sampled.AudioInputStream;
//import javax.sound.sampled.AudioSystem;
//import javax.sound.sampled.Clip;
//import javax.sound.sampled.DataLine;

/**
 *
 * @author itzcoatl90
 */
public class Menu extends JPanel implements ActionListener{
    
    private JButton ini;
    private JTextField ghostConf;
    private JTextField pacmanConf;
    private JTextField mapConf;
    public static BufferedImage image;
    //Clip clip;
    
    public Menu() throws IOException{
        initComponents();
    }
    
    private void initComponents() throws IOException{
        mapConf = new JTextField("map.txt",20);
        pacmanConf = new JTextField("pacman_config.txt",20);
        ghostConf = new JTextField("ghost_config.txt",20);
        ini = new JButton("Iniciar Juego");
        image = ImageIO.read(new File("img/Menu.png"));
        ini.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IniciarClick(evt);
            }
        });
        /*try {
            AudioInputStream soundStream = AudioSystem.getAudioInputStream(new File("Sounds/PacmanMenu.wav"));
            AudioFormat audioFormat = soundStream.getFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(
                    Clip.class, AudioSystem.getTargetFormats(
                            AudioFormat.Encoding.PCM_SIGNED, audioFormat ),
                            audioFormat.getFrameSize(),
                            audioFormat.getFrameSize() * 2 );
            if ( !AudioSystem.isLineSupported( dataLineInfo ) ) {
                System.err.println( "Unsupported Clip File!" );
                return;
            }
            clip = ( Clip ) AudioSystem.getLine( dataLineInfo );
            clip.open( soundStream );
        } catch ( Exception e) { e.printStackTrace(); }*/
        this.setLayout(null);
        mapConf.setBounds(6*RunGame.TILE_LEN, 2*RunGame.TILE_LEN, 3*RunGame.TILE_LEN, (int)(0.7*RunGame.TILE_LEN));
        ghostConf.setBounds(6*RunGame.TILE_LEN, 4*RunGame.TILE_LEN+10, 3*RunGame.TILE_LEN, (int)(0.7*RunGame.TILE_LEN));
        pacmanConf.setBounds(6*RunGame.TILE_LEN, 6*RunGame.TILE_LEN+20, 3*RunGame.TILE_LEN, (int)(0.7*RunGame.TILE_LEN));
        ini.setBounds(7*RunGame.TILE_LEN, 9*RunGame.TILE_LEN, 4*RunGame.TILE_LEN, (int)(0.7*RunGame.TILE_LEN));
        this.setBackground(Color.BLACK);
        this.add(mapConf);
        this.add(pacmanConf);
        this.add(ghostConf);
        this.add(ini);
        //clip.start();
    }
    
    public void IniciarClick(ActionEvent e) {
        RunGame.ghostspath=ghostConf.getText();
        RunGame.pacmanpath=pacmanConf.getText();
        RunGame.mappath=mapConf.getText();
        RunGame.startGame();
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
