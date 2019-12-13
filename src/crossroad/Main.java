/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crossroad;

import crossroad.view.Loading;
import crossroad.view.Menu;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 *
 * @author lucas
 */
public class Main {
    
    public static String backGournd = "..\\images\\background.png";
    public static String ip;
    
    public class ThreadConnection extends Thread{
        
        private int port, id;
        private Loading l;
        
        public ThreadConnection(int port, Loading l){
            this.port = port;
            this.id = 0;
            this.l = l;
        }
        
        @Override
        public void run(){
            while (true) {
   
                try {

                    Socket client = new Socket(Main.ip, port);
                    ObjectInputStream io = new ObjectInputStream(client.getInputStream());
                    Integer[] vec = (Integer[]) io.readObject();
                   
                    id = vec[0];
                    port = vec[1];
                    io.close();
                    break;

                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Erro " + e.getMessage());
                }
            }
           
            if(id == 0) port+=100;
            else if(id == 1) port+=101;
            else if(id == 2) port+=102;
            else if(id==3) port+= 103;
           
            boolean play = false;

            while (true) {

                try {
                    Socket client = new Socket(Main.ip, port);
                    ObjectInputStream io = new ObjectInputStream(client.getInputStream());
                    play = (boolean) io.readObject();
                } catch (Exception e) {
                    System.out.println("Erro " + e.getMessage());
                }

                if (play) {
                    break;
                }

            }
            
            String path = "";
            int x = 44;
            
            if(id == 0) {
                path = "..\\images\\chicken1.png";
                x = 44;
            } else if(id == 1){
                path = "..\\images\\chicken2.png";
                x = 364;
            } else if(id == 2) {
                path = "..\\images\\chicken3.png";
                x = 626;
            } else if(id == 3) {
                path = "..\\images\\chicken4.png";
                x = 940;
            }
            
            l.dispose();
            Road game = new Road(x, 600, id, port, 1, path, Main.backGournd, Main.ip);
            game.initRoad();
            game.editComponents();
        }
    }
    
    public void onlineMode(){
        
        Loading l = new Loading();
        int port = 6790;
        new ThreadConnection(port, l).start();
        
    }
    
    public static void offlineMode(){
        
        Road game = new Road(44, 600, 0, 0, 0, "..\\images\\chicken1.png", Main.backGournd, "");
        game.initRoad();
        game.editComponents();
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
       
        Menu m = new Menu();
        m.setVisible(true);
    }
}
