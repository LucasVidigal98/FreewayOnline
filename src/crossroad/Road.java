/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package crossroad;

import java.awt.Dimension;
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
    private int[] posCars;
    
    
    private int posChickenX, posChickenXInitial;
    private int posChickenY, posChickenYInitial;

    public Road(int posChickenX, int posChickenY) {
        
        this.posChickenX = posChickenX;
        this.posChickenY = posChickenY;
        this.posChickenXInitial = posChickenX;
        this.posChickenYInitial = posChickenY;
        this.cars = new ArrayList<>();
        this.posCars = new int[160];
        this.initCarsRight();
        this.initCarsLeft();
        this.addMoviments();
        new MoveCarsRight().start();
        new MoveCarsLeft().start();
    }
    
    public void initCarsRight(){
        
        File dir = new File("src\\images\\carsright");
       
        String images[] = dir.list();
        int pos[] = new int[4];
        pos[0] = 556;
        pos[1] = 468;
        pos[2] = 424;
        pos[3] = 336;
        
        int countCars = 0;
        for(int k=0; k<4; k++){
            for(int i=0; i<4; i++){
                Random image = new Random();
                ImageIcon iconCar = new ImageIcon(getClass().getResource("..\\images\\carsright\\"+images[image.nextInt(images.length)]));
                JLabel car = new JLabel(iconCar);
                car.setBounds((-80-(i*500)), pos[k], 100, 100);
                pos[countCars] = (-80-(i*500));
                cars.add(car);
            }
        }
    }
    
    public void initCarsLeft(){
        
        File dir = new File("src\\images\\carsleft");
       
        String images[] = dir.list();
        int pos[] = new int[40];
        pos[0] = 292;
        pos[1] = 204;
        pos[2] = 132;
        pos[3] = 72;
        
        int countCars = 0;
        for(int k=0; k<4; k++){
            for(int i=0; i<4; i++){
                Random image = new Random();
                ImageIcon iconCar = new ImageIcon(getClass().getResource("..\\images\\carsright\\"+images[image.nextInt(images.length)]));
                JLabel car = new JLabel(iconCar);
                car.setBounds((1280+(i*500)), pos[k], 100, 100);
                pos[countCars] = (1280+(i*500));
                cars.add(car);
            }
        }
        System.out.println(cars.size());
    }
    
    public void initRoad(){
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(null);
        setResizable(false);
        
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
                int auxX = posChickenX;
                int auxY = posChickenY;
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                if(ke.getKeyCode() == 37){
                    if((auxX -= 64) > 0)
                        posChickenX -= 64;
                }
                
                if(ke.getKeyCode() == 38){
                    posChickenY -= 44;
                }
                
                if(ke.getKeyCode() == 39){
                    if((auxX += 64) < 1260)
                        posChickenX += 64;
                }
                
                if(ke.getKeyCode() == 40){
                    if((auxY += 44) <= 600)
                        posChickenY += 44;
                }
                
                System.out.println("x " + posChickenX);
                System.out.println("y " + posChickenY);
                
                if((posChickenX > 0 && posChickenX < 1200) && (posChickenY > 0 && posChickenY <= 600)){
                    lChicken.setBounds(posChickenX, posChickenY, 64, 64);
                }else if(posChickenY < 0){
                    posChickenX = posChickenXInitial;
                    posChickenY = posChickenYInitial;
                    lChicken.setBounds(posChickenXInitial, posChickenYInitial, 64, 64);
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
 
    public class MoveCarsRight extends Thread{
        @Override
        public void run(){    
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
    
     public class MoveCarsLeft extends Thread{
        @Override
        public void run(){
            
            while(true){
                
                try {
                    sleep(15);
                } catch (InterruptedException ex) {
                    JOptionPane.showMessageDialog(null, "Erro na Thread" + ex.getMessage());
                }
                
                for(int i=(cars.size()/2); i<cars.size(); i++){
                    
                    if(cars.get(i).getY() == 72){
                        if(cars.get(i).getX() < -80)
                            cars.get(i).setBounds(1280, cars.get(i).getY(), 100, 100);

                        cars.get(i).setBounds((cars.get(i).getX() - 2), cars.get(i).getY(), 100, 100);
                    }
                    
                    if(cars.get(i).getY() == 132){
                        if(cars.get(i).getX() < -80)
                            cars.get(i).setBounds(1280, cars.get(i).getY(), 100, 100);

                        cars.get(i).setBounds((cars.get(i).getX() - 3), cars.get(i).getY(), 100, 100);
                    }
                    
                    if(cars.get(i).getY() == 204){
                        if(cars.get(i).getX() < -80)
                            cars.get(i).setBounds(1280, cars.get(i).getY(), 100, 100);

                        cars.get(i).setBounds((cars.get(i).getX() - 7), cars.get(i).getY(), 100, 100);
                    }
                    
                    if(cars.get(i).getY() == 292){
                        if(cars.get(i).getX() < -80)
                            cars.get(i).setBounds(1280, cars.get(i).getY(), 100, 100);

                        cars.get(i).setBounds((cars.get(i).getX() - 10), cars.get(i).getY(), 100, 100);
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
