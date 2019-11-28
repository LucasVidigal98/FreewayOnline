/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package crossroad;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.YES_OPTION;

/**
 *
 * @author lucas
 */
public class Road extends JFrame{
    
    private ImageIcon iconFreeway = new ImageIcon(getClass().getResource("..\\images\\background.png"));
    private JLabel lRoad = new JLabel(iconFreeway);
    private ImageIcon iconChicken = new ImageIcon(getClass().getResource("..\\images\\chicken.gif"));
    private JLabel lChicken = new JLabel(iconChicken);
    private ArrayList<JLabel> othersChickens = new ArrayList<>();
    private ArrayList<JLabel> cars;
    
    
    private int posChickenX, posChickenXInitial;
    private int posChickenY, posChickenYInitial;

    public Road(int posChickenX, int posChickenY) {
        
        this.posChickenX = posChickenX;
        this.posChickenY = posChickenY;
        this.posChickenXInitial = posChickenX;
        this.posChickenYInitial = posChickenY;
        this.initCarsRight();
        this.addMoviments();
        new MoveCarsRight().start();
    }
    
    public void initCarsRight(){
        
        cars = new ArrayList<>();
        File dir = new File("..\\images\\carsright");
        
        File files[] = dir.listFiles();
        
        if(!dir.exists()) System.out.println("oiiiiiiiii");
        
        //String images[] = dir.list();
        int pos[] = new int[4];
        pos[0] = 556;
        pos[1] = 468;
        pos[2] = 424;
        pos[3] = 336;
        
        //System.out.println(images[0]);
        
        for(int i=0; i<30; i++){
            
            Random image = new Random();
            ImageIcon iconCar = new ImageIcon(getClass().getResource("..\\images\\carsright\\formula.png"));
            JLabel car = new JLabel(iconCar);
            car.setBounds((-80-i-(image.nextInt(6000))), pos[image.nextInt(pos.length)], 100, 100);
            cars.add(car);
        }
    }
    
    public void initRoad(){
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(null);
        
        add(lChicken);
        
        for (int i=0; i<cars.size(); i++){
            add(cars.get(i));
        }
        
        add(lRoad);
    }
    
    public void editComponents(){
        lRoad.setBounds(0, 0, 1280, 720);
        lChicken.setBounds(posChickenXInitial, posChickenYInitial, 64, 64);
    }
    
 public void addMoviments(){
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
               // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                if(ke.getKeyCode() == 37){
                    posChickenX -= 64;
                }
                
                if(ke.getKeyCode() == 38){
                    posChickenY -= 44;
                }
                
                if(ke.getKeyCode() == 39){
                    posChickenX += 64;
                }
                
                if(ke.getKeyCode() == 40){
                    posChickenY += 44;
                }
                
                System.out.println("x " + posChickenX);
                System.out.println("y " + posChickenY);
                
                if((posChickenX > 0 && posChickenX < 1200) && (posChickenY > 0 && posChickenY < 600)){
                    lChicken.setBounds(posChickenX, posChickenY, 64, 64);
                }else{
                    posChickenX = posChickenXInitial;
                    posChickenY = posChickenYInitial;
                    lChicken.setBounds(posChickenXInitial, posChickenYInitial, 64, 64);
                }
            }
//556
            @Override
            public void keyReleased(KeyEvent ke) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
 
    public class MoveCarsRight extends Thread{
        @Override
        public void run(){
            Random gerador = new Random();
            int aleatorio;
            int pontos = 0, op, op2;
            
            while(true){
                
                try {
                    sleep(15);
                } catch (InterruptedException ex) {
                    JOptionPane.showMessageDialog(null, "Erro na Thread" + ex.getMessage());
                }
                
                for(int i=0; i<cars.size(); i++){
                    
                    if(cars.get(i).getY() == 556){
                        if(cars.get(i).getX() > 1280)
                            cars.get(i).setBounds(-80, cars.get(i).getY(), 100, 100);

                        cars.get(i).setBounds((cars.get(i).getX() + 2), cars.get(i).getY(), 100, 100);
                    }
                    
                    if(cars.get(i).getY() == 468){
                        if(cars.get(i).getX() > 1280)
                            cars.get(i).setBounds(-80, cars.get(i).getY(), 100, 100);

                        cars.get(i).setBounds((cars.get(i).getX() + 3), cars.get(i).getY(), 100, 100);
                    }
                    
                    if(cars.get(i).getY() == 424){
                        if(cars.get(i).getX() > 1280)
                            cars.get(i).setBounds(-80, cars.get(i).getY(), 100, 100);

                        cars.get(i).setBounds((cars.get(i).getX() + 7), cars.get(i).getY(), 100, 100);
                    }
                    
                    if(cars.get(i).getY() == 336){
                        if(cars.get(i).getX() > 1280)
                            cars.get(i).setBounds(-80, cars.get(i).getY(), 100, 100);

                        cars.get(i).setBounds((cars.get(i).getX() + 10), cars.get(i).getY(), 100, 100);
                    }
                }    
            }
        }
    }
    
    public static void main(String[] args) {
        
        Road game = new Road(44, 600);
        game.initRoad();
        game.editComponents();
    }
    
}
