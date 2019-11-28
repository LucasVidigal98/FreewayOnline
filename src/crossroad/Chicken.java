/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package crossroad;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author lucas
 */
public class Chicken extends JFrame{
    
    private ImageIcon iconChicken = new ImageIcon(getClass().getResource("chicken.gif"));
    private JLabel lChicken = new JLabel(iconChicken);
    
    private int posChicken;
    
    public void Chiken(){
        
    }
    
    public void addMovimento(){
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
               // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                if(ke.getKeyCode() == 37){
                    posChicken -= 60;
                    
                }
                if(ke.getKeyCode() == 39){
                    posChicken += 60;
                }
                
                if(posChicken > 0 && posChicken < 500){
                    lChicken.setBounds(posChicken, 750, 100, 100);
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
    
}
