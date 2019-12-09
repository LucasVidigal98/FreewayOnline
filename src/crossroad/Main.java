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
    
    public class ThreadConnection extends Thread{
        
        private int port, oldPort, id, numberConnections;
        private Loading l;
        
        public ThreadConnection(int port, Loading l){
            this.port = port;
            this.oldPort = this.port;
            this.id = 0;
            this.l = l;
        }
        
        @Override
        public void run(){
            while (true) {
                System.out.println("Aqui");
                try {

                    Socket client = new Socket("192.168.0.105", port);
                    ObjectInputStream io = new ObjectInputStream(client.getInputStream());
                    id = (int) io.readObject();
                    System.out.println(id);
                    System.out.println("Fim");
                    io.close();
                    break;

                } catch (IOException | ClassNotFoundException e) {
                    port += 1;
                    numberConnections++;
                    System.out.println("Erro " + e.getMessage());
                }
            }

            System.out.println(port);
            port += 100;
            System.out.println(port);

            boolean play = false;

            while (true) {

                try {
                    Socket client = new Socket("192.168.0.105", port);
                    ObjectInputStream io = new ObjectInputStream(client.getInputStream());
                    play = (boolean) io.readObject();
                } catch (Exception e) {
                    System.out.println("Erro " + e.getMessage());
                }

                //System.out.println(play);
                if (play) {
                    break;
                }

            }

            l.dispose();
            Road game = new Road(44, 600, id, port);
            game.initRoad();
            game.editComponents();
        }
    }
    
    public void onlineMode(){
        
        Loading l = new Loading();
        int port = 10000;
        new ThreadConnection(port, l).start();
        
    }
    
    public static void offlineMode(){
        
        Road game = new Road(44, 600, 0, 0);
        game.initRoad();
        game.editComponents();
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
       
        Menu m = new Menu();
        m.setVisible(true);
    }
}
