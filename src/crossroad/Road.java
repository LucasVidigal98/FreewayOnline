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
    private final JLabel lTime;
    private final JLabel lLife;
    private final ArrayList<JLabel> othersChickens;
    private final ArrayList<JLabel> cars;
    
    private int posChickenX, posChickenXInitial;
    private int posChickenY, posChickenYInitial;
    private int score, life, mode;
    private boolean canMove;
    private int[] velocity = new int[4];
    
    private int id, port;
    private Integer[][] posChickens = new Integer[4][4];
    
    private String ip;

    public Road(int posChickenX, int posChickenY, int id, int port, int mode, String path, String backGround, String ip) {
        
        this.iconFreeway = new ImageIcon(getClass().getResource(backGround));
        this.lRoad = new JLabel(iconFreeway);
        this.iconChicken = new ImageIcon(getClass().getResource(path));
        this.lChicken = new JLabel(iconChicken);
        this.explosion = new ImageIcon(getClass().getResource("..\\images\\explosion.gif"));
        this.lExplosion = new JLabel(explosion);
        this.lscore = new JLabel("Score: 0");
        this.lTime = new JLabel("3");
        this.lLife = new JLabel("Vida: 5");
        this.othersChickens = new ArrayList<>();
        this.posChickenX = posChickenX;
        this.posChickenY = posChickenY;
        this.posChickenXInitial = posChickenX;
        this.posChickenYInitial = posChickenY;
        this.canMove = true;
        this.score = 0;
        this.life = 5;
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.mode = mode;
        this.initOtherChickens();
        this.cars = new ArrayList<>();
        this.initCarsRight();
        this.initCarsLeft();
        this.initTime();
        this.velocity[0] = 2; this.velocity[1] = 3; this.velocity[2] = 7;; this.velocity[3] = 10;
        this.addMoviments();
        new MoveCarsRight().start();
        new MoveCarsLeft().start();
        new MoveChikens().start();
        new ThreadWin().start();
        new Dificulty().start();
        new PlaySound("..\\sounds\\soundBase.wav", 1).start();
    }
    
     /* 1 2 3 já */
    private void initTime(){
        
        Thread t = new Thread();
        
        for (int i=3; i>=0; i--){
           
            try {
                t.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(i != 0)
                lTime.setText(""+i);
            else
                lTime.setText("JÁ !!!");
        }
    }
    
    /* Inicia as galinhas com as posições iniciais */
    private void initOtherChickens(){
       
        for (int i=0; i<4; i++){

            ImageIcon image = new ImageIcon(getClass().getResource("..\\images\\chicken" + (i + 1) + ".png"));
            othersChickens.add(new JLabel(image));
            if (i == 0) {
                othersChickens.get(i).setBounds(44, 600, 50, 50);
            } else if (i == 1) {
                othersChickens.get(i).setBounds(364, 600, 50, 50);
            } else if (i == 2) {
                othersChickens.get(i).setBounds(626, 600, 50, 50);
            } else if (i == 3) {
                othersChickens.get(i).setBounds(940, 600, 50, 50);
            }
        }
    }
    
    /* inicia os carros da direita */
    private void initCarsRight(){
        
       // new PlaySound("..\\sounds\\buzinaBase.wav", 1).start();
        
        File dir = new File("src\\images\\carsright"); 
       
        String images[] = dir.list(); //lista todas as imagens do diretório
        int pos[] = new int[4];
        pos[0] = 556;
        pos[1] = 468;
        pos[2] = 424;
        pos[3] = 336;
        
        int count = 0;
        
        for(int k=0; k<4; k++){
            for(int i=0; i<2; i++){
                ImageIcon iconCar = new ImageIcon(getClass().getResource("..\\images\\carsright\\"+images[count])); //recebe uma imagem
                
                count++;
                if(count >= images.length) count = 0;
                
                JLabel car = new JLabel(iconCar);
                car.setBounds((-80-(i*500)), pos[k], 5, 5);
                cars.add(car);
            }
        }
    }
    
    /* inicia os carros da esquerda */
    private void initCarsLeft(){
        
        File dir = new File("src\\images\\carsleft");
       
        String images[] = dir.list();
        int pos[] = new int[40];
        pos[0] = 292;
        pos[1] = 204;
        pos[2] = 132;
        pos[3] = 72;
        
        int count = 0;
        
        for(int k=0; k<4; k++){
            for(int i=0; i<2; i++){
                ImageIcon iconCar = new ImageIcon(getClass().getResource("..\\images\\carsleft\\"+images[count]));
                
                count++;
                if(count >= images.length) count = 0;
                
                JLabel car = new JLabel(iconCar);
                car.setBounds((1280+(i*500)), pos[k], 5, 5);
                cars.add(car);
            }
        }
    }
    
    /* Inicia a tela e os labels */
    public void initRoad(){
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(null);
        setResizable(false);
        
        add(lTime);
        add(lscore);
        add(lLife);
        add(lChicken);
        
        for (int i=0; i<othersChickens.size(); i++){
            if(id != i)
                add(othersChickens.get(i));
        }
        
        for (int i=0; i<cars.size(); i++){
            add(cars.get(i));
        }
        
        add(lRoad);
    }
    
    /* altera o tamanho dos componentes */
    public void editComponents(){
        lRoad.setBounds(0, 0, 1280, 720);
        lscore.setBounds(0, 60, 100, 100);
        lscore.setForeground(Color.red);
        lscore.setSize(new Dimension(100, 100));
        lTime.setForeground(Color.red);
        lTime.setBounds(560, 60, 600, 600);
        lLife.setForeground(Color.red);
        lLife.setBounds(0, 80, 100, 100);
        lChicken.setBounds(posChickenXInitial, posChickenYInitial, 50, 50);
    }
    
    /* Recebe os movimentos */
    private void addMoviments(){
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
               // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                if(canMove){ // verifica se a galinha pode se mover
                    int auxY = posChickenY;
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    if(ke.getKeyCode() == 38){
                        posChickenY -= 44;
                        new SendChicken(id, posChickenX, posChickenY).start();
                    }

                    if(ke.getKeyCode() == 40){
                        if((auxY += 44) <= 600){
                            posChickenY += 44;
                            new SendChicken(id, posChickenX, posChickenY).start();
                        }
                    }

                    /* restringe os movimentos para dentro da tela e reinicia a posição da galinha quando atravessar a rua */
                    if((posChickenX > 0 && posChickenX < 1200) && (posChickenY > 0 && posChickenY <= 600)){ 
                        lChicken.setBounds(posChickenX, posChickenY, 50, 50);
                    }else if(posChickenY < 0){
                        new PlaySound("..\\sounds\\coins.wav", 0).start();
                        posChickenX = posChickenXInitial;
                        posChickenY = posChickenYInitial;
                        lChicken.setBounds(posChickenXInitial, posChickenYInitial, 50, 50);
                        score += 1;
                        lscore.setText("Score: " + score);
                        new SendChicken(id, posChickenXInitial, posChickenYInitial).start();
                    }

                    new Colision().start();
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
 
    /* Thread para música */
    public class PlaySound extends Thread{
        
        public String path;
        public int flag;
        
        public PlaySound(String path, int flag){
            this.path = path;
            this.flag = flag;
        }
        
        @Override
        public void run(){
            URL sound = getClass().getResource(path);
            AudioClip s = Applet.newAudioClip(sound);
            s.play();
            if(flag == 1)
                s.loop();
        }
    }
 
    /* verifica colisões */
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
                    life -= 1;
                    lLife.setText("Vida: " + (life));
                    lChicken.setBounds(posChickenXInitial, posChickenYInitial, 50, 50); //reiniia a posição da galinha
                    new PlaySound("..\\sounds\\chicken.wav", 0).start();
                    new SendChicken(id, posChickenXInitial, posChickenYInitial).start(); //envia posição para o servidor
                    
                    if(life == 0 && mode == 1){ //se o jogo é online e a galinha morrer, retira a galinha da tela e exibe a mensagem
                        canMove = false;
                        JOptionPane.showMessageDialog(null, "Você perdeu! Espere o jogo terminar");
                        lChicken.setBounds(-200, -200, 50, 50);
                        new SendChicken(id, -200, -200).start();
                    }
                }
            }
        }
    }
 
    /* movimentação dos carros */
    public class MoveCarsRight extends Thread{
        @Override
        public void run(){    
            while(true){
                
                try {
                    sleep(15);
                } catch (InterruptedException ex) {
                    JOptionPane.showMessageDialog(null, "Erro na Thread" + ex.getMessage());
                }
                
                for(int i=0; i<cars.size(); i++){ //altera som, posição e velocidade dos carros
                    
                    if(cars.get(i).getY() == 556){
                        if(cars.get(i).getX() > 1280)
                            cars.get(i).setBounds(-80, cars.get(i).getY(), 100, 100);

                        cars.get(i).setBounds((cars.get(i).getX() + velocity[0]), cars.get(i).getY(), 100, 100);
                    }
                    
                    if(cars.get(i).getY() == 468){
                        if(cars.get(i).getX() > 1280)
                            cars.get(i).setBounds(-80, cars.get(i).getY(), 100, 100);

                        cars.get(i).setBounds((cars.get(i).getX() + velocity[1]), cars.get(i).getY(), 100, 100);
                    }
                    
                    if(cars.get(i).getY() == 424){
                        if(cars.get(i).getX() > 1280)
                            cars.get(i).setBounds(-80, cars.get(i).getY(), 100, 100);

                        cars.get(i).setBounds((cars.get(i).getX() + velocity[2]), cars.get(i).getY(), 100, 100);
                    }
                    
                    if(cars.get(i).getY() == 336){
                        if(cars.get(i).getX() > 1280)
                            cars.get(i).setBounds(-80, cars.get(i).getY(), 100, 100);

                        cars.get(i).setBounds((cars.get(i).getX() + velocity[3]), cars.get(i).getY(), 100, 100);
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

                        cars.get(i).setBounds((cars.get(i).getX() - velocity[0]), cars.get(i).getY(), 100, 100);
                    }
                    
                    if(cars.get(i).getY() == 132){
                        if(cars.get(i).getX() < -80)
                            cars.get(i).setBounds(1280, cars.get(i).getY(), 100, 100);

                        cars.get(i).setBounds((cars.get(i).getX() - velocity[1]), cars.get(i).getY(), 100, 100);
                    }
                    
                    if(cars.get(i).getY() == 204){
                        if(cars.get(i).getX() < -80)
                            cars.get(i).setBounds(1280, cars.get(i).getY(), 100, 100);

                        cars.get(i).setBounds((cars.get(i).getX() - velocity[2]), cars.get(i).getY(), 100, 100);
                    }
                    
                    if(cars.get(i).getY() == 292){
                        if(cars.get(i).getX() < -80)
                            cars.get(i).setBounds(1280, cars.get(i).getY(), 100, 100);

                        cars.get(i).setBounds((cars.get(i).getX() - velocity[3]), cars.get(i).getY(), 100, 100);
                    }
                }    
            }
        }
    }
     
    /* recebe as posições das outras galinhas */
    public class MoveChikens extends Thread{
        
        @Override
        public void run(){
            
            while(true){
                
                try {
                    sleep(20);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Socket client = null;
                try {
                    client = new Socket(ip, port+200); //inicia a porta
                } catch (IOException ex) {
                    Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
                    port += 1000;
                    continue;
                }
                
                ObjectInputStream io = null;
                
                try {
                    io = new ObjectInputStream(client.getInputStream());
                } catch (IOException ex) {
                    Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    posChickens = (Integer[][]) io.readObject(); //recebe a matriz de posições
                } catch (IOException ex) {
                    Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                for (int i=0; i<posChickens.length; i++){ //atualiza as posições
                    if(i != id){
                        try{
                            othersChickens.get(i).setBounds(posChickens[i][1], posChickens[i][2], 50, 50);
                        }catch(Exception e){
                            continue;
                        }
                    }
                }
            }
        }
        
    }
    
    /* envia os movimentos da galinha */
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
            
            try {
                client = new Socket(ip, port+100);
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
    
    /* Recebe os status do jogo */
    public class ThreadWin extends Thread{
        
        @Override
        public void run(){

            String w = "";
            
            while(true){
                
                try {
                    sleep(20);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Socket client = null;
                try {
                    client = new Socket(ip, port+300);
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
                    w = (String) io.readObject();
                } catch (IOException ex) {
                    Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if(!w.equals("NULL")){
                    
                    canMove = false;
                    int option = JOptionPane.showConfirmDialog(null, w + "Deseja jogar novamente?");
                    
                    if(option == 0){
                        dispose();
                        new Main().onlineMode();
                    }else{
                        System.exit(1);
                    }
                }
            }
        }
    }
    
    /* Aumenta a velocidade dos carros a cada 1 minuto */
    public class Dificulty extends Thread{
        
        @Override
        public void run(){
            
            for(int i=0; i<3 ; i++){
                
                try {
                    sleep(60000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                velocity[0] += 2;
                velocity[1] += 2;
                velocity[2] += 2;
                velocity[3] += 2;
            }
        } 
    }
}
