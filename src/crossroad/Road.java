/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package crossroad;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author lucas
 */
public class Road extends JFrame{
    
    private final ImageIcon iconFreeway;
    private final JLabel lRoad;
    private final ImageIcon iconChicken;
    private final JLabel lChicken;
    private final ImageIcon explosion;
    private final JLabel lExplosion;
    private final JLabel lscore;
    private final ArrayList<JLabel> othersChickens;
    private final ArrayList<JLabel> cars;
    private final int[] posCars;
    
    private int posChickenX, posChickenXInitial;
    private int posChickenY, posChickenYInitial;
    private int score;
    
    private int id, port;
    private Integer[][] posChickens = new Integer[4][4];

    public Road(int posChickenX, int posChickenY, int id, int port) {
        
        this.iconFreeway = new ImageIcon(getClass().getResource("..\\images\\background.png"));
        this.lRoad = new JLabel(iconFreeway);
        this.iconChicken = new ImageIcon(getClass().getResource("..\\images\\chicken.png"));
        this.lChicken = new JLabel(iconChicken);
        this.explosion = new ImageIcon(getClass().getResource("..\\images\\explosion.gif"));
        this.lExplosion = new JLabel(explosion);
        this.lscore = new JLabel("Score: 0");
        this.othersChickens = new ArrayList<>();
        this.posChickenX = posChickenX;
        this.posChickenY = posChickenY;
        this.posChickenXInitial = posChickenX;
        this.posChickenYInitial = posChickenY;
        this.score = 0;
        this.id = id;
        this.port = port;
        this.initOtherChickens();
        this.cars = new ArrayList<>();
        this.posCars = new int[160];
        this.initCarsRight();
        this.initCarsLeft();
        this.addMoviments();
        new MoveCarsRight().start();
        new MoveCarsLeft().start();
        new MoveChikens().start();
    }
    
    private void initOtherChickens(){
        
        for (int i=0; i<4; i++){
            othersChickens.add(new JLabel(iconChicken));
            othersChickens.get(i).setBounds(posChickenXInitial, posChickenYInitial, 50, 50);
        }
    }
    
    private void initCarsRight(){
        
        File dir = new File("src\\images\\carsright");
       
        String images[] = dir.list();
        int pos[] = new int[4];
        pos[0] = 556;
        pos[1] = 468;
        pos[2] = 424;
        pos[3] = 336;
        
        for(int k=0; k<4; k++){
            for(int i=0; i<2; i++){
                Random image = new Random();
                ImageIcon iconCar = new ImageIcon(getClass().getResource("..\\images\\carsright\\"+images[image.nextInt(images.length)]));
                JLabel car = new JLabel(iconCar);
                car.setBounds((-80-(i*500)), pos[k], 5, 5);
                cars.add(car);
            }
        }
    }
    
    private void initCarsLeft(){
        
        File dir = new File("src\\images\\carsleft");
       
        String images[] = dir.list();
        int pos[] = new int[40];
        pos[0] = 292;
        pos[1] = 204;
        pos[2] = 132;
        pos[3] = 72;
        
        for(int k=0; k<4; k++){
            for(int i=0; i<2; i++){
                Random image = new Random();
                ImageIcon iconCar = new ImageIcon(getClass().getResource("..\\images\\carsright\\"+images[image.nextInt(images.length)]));
                JLabel car = new JLabel(iconCar);
                car.setBounds((1280+(i*500)), pos[k], 5, 5);
                cars.add(car);
            }
        }
        //System.out.println(cars.size());
    }
    
    public void initRoad(){
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(null);
        setResizable(false);
        
        add(lscore);
        add(lChicken);
        
        for (int i=0; i<othersChickens.size(); i++){
            add(othersChickens.get(i));
        }
        
        for (int i=0; i<cars.size(); i++){
            add(cars.get(i));
        }
        
        add(lRoad);
    }
    
    public void editComponents(){
        lRoad.setBounds(0, 0, 1280, 720);
        lscore.setBounds(0, 60, 100, 100);
        lscore.setForeground(Color.red);
        lscore.setSize(new Dimension(100, 100));
        lChicken.setBounds(posChickenXInitial, posChickenYInitial, 50, 50);
    }
    
 private void addMoviments(){
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
                    if((auxX -= 64) > 0){
                        posChickenX -= 64;
                        new SendChicken(id, posChickenX, posChickenY).start();
                    }
                }
                
                if(ke.getKeyCode() == 38){
                    posChickenY -= 44;
                    new SendChicken(id, posChickenX, posChickenY).start();
                }
                
                if(ke.getKeyCode() == 39){
                    if((auxX += 64) < 1260){
                        posChickenX += 64;
                        new SendChicken(id, posChickenX, posChickenY).start();
                    }
                }
                
                if(ke.getKeyCode() == 40){
                    if((auxY += 44) <= 600){
                        posChickenY += 44;
                        new SendChicken(id, posChickenX, posChickenY).start();
                    }
                }
                
                if((posChickenX > 0 && posChickenX < 1200) && (posChickenY > 0 && posChickenY <= 600)){
                    lChicken.setBounds(posChickenX, posChickenY, 20, 20);
                }else if(posChickenY < 0){
                    new PlaySound("..\\sounds\\coins.wav").start();
                    posChickenX = posChickenXInitial;
                    posChickenY = posChickenYInitial;
                    lChicken.setBounds(posChickenXInitial, posChickenYInitial, 50, 50);
                    score += 10;
                    lscore.setText("Score: " + score);
                }
                
                new Colision().start();
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
 
    public class PlaySound extends Thread{
        
        public String path;
        
        public PlaySound(String path){
            this.path = path;
        }
        
        @Override
        public void run(){
            URL sound = getClass().getResource(path);
            AudioClip s = Applet.newAudioClip(sound);
            s.play();
            //s.stop();
        }
    }
 
    public class Colision extends Thread{
        
        @Override
        public void run(){
            
            for(int i=0; i<cars.size(); i++){
                if (bateu(lChicken, cars.get(i))) {
                    
                    add(lExplosion);
                    lExplosion.setBounds(posChickenX, posChickenY, 100, 100);
                    
                    try{
                        sleep(15);
                    }catch(InterruptedException e){
                        System.out.println("Erro " + e.getMessage());
                    }
                    
                    remove(lExplosion);
                    posChickenX = posChickenXInitial;
                    posChickenY = posChickenYInitial;
                    score -= 5;
                    lscore.setText("Score: " + score);
                    lChicken.setBounds(posChickenXInitial, posChickenYInitial, 50, 50);
                    new PlaySound("..\\sounds\\chicken.wav").start();
                }
            }
        }
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
     
    public class MoveChikens extends Thread{
        
        @Override
        public void run(){
            
            while(true){
                
                Socket client = null;
                
                try {
                    client = new Socket("192.168.0.100", port+200);
                } catch (IOException ex) {
                    Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                ObjectInputStream io = null;
                
                try {
                    io = new ObjectInputStream(client.getInputStream());
                } catch (IOException ex) {
                    Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    posChickens = (Integer[][]) io.readObject();
                } catch (IOException ex) {
                    Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                for (int i=0; i<posChickens.length; i++){
                    if(i != id){
                        try{
                            othersChickens.get(i).setBounds(posChickens[i][1], posChickens[i][2], 64, 64);
                        }catch(Exception e){
                            continue;
                        }
                    }
                }
            }
        }
        
    }
    
    public class SendChicken extends Thread{
        
        public int id, x, y;
        
        public SendChicken(int id, int x, int y){
            
            this. id = id;
            this.x = x;
            this.y = y;
        }
        
        @Override
        public void run(){
            
            Socket client = null;
            System.out.println("Port " + (port+100));
            
            try {
                client = new Socket("192.168.0.100", port+100);
            } catch (IOException ex) {
                Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Integer[] pos = new Integer[3];
            pos[0] = id;
            pos[1] = x;
            pos[2] = y;
            
            ObjectOutputStream io = null;
            try {
                io = new ObjectOutputStream(client.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                io.flush();
            } catch (IOException ex) {
                Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                io.writeObject(pos);
            } catch (IOException ex) {
                Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                io.close();
            } catch (IOException ex) {
                Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean bateu(Component a, Component b) {
		int aX = a.getX();
		int aY = a.getY();
		int ladoDireitoA = aX+a.getWidth();
		int ladoEsquerdoA= aX;
		int ladoBaixoA= aY+a.getHeight();
		int ladoCimaA= aY;
		
		int bX = b.getX();
		int bY = b.getY();
		int ladoDireitoB = bX+b.getWidth();
		int ladoEsquerdoB= bX;
		int ladoBaixoB= bY+b.getHeight();
		int ladoCimaB= bY;
		
		boolean bateu = false;
		
		boolean cDireita=false;
		boolean cCima=false;
		boolean cBaixo=false;
		boolean cEsquerda=false;
		
		if(ladoDireitoA>=ladoEsquerdoB) {
			cDireita=true;
		}
		if(ladoCimaA<=ladoBaixoB) {
			cCima=true;
		}
		if(ladoBaixoA>=ladoCimaB) {
			cBaixo=true;
		}
		if(ladoEsquerdoA<=ladoDireitoB) {
			cEsquerda=true;
		}
		
		if(cDireita && cEsquerda && cBaixo && cCima) {
			bateu = true;
		}
			
		return bateu;
        }
}
