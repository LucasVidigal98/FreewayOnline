/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crossroad;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 *
 * @author lucas
 */
public class Main {
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        int port = 10000;
        int oldPort = port;
        int id = 0;
        int numberConections = 0;
        while (true){
            System.out.println("Aqui");
            try{

                Socket client = new Socket("192.168.0.100", port);
                ObjectInputStream io = new ObjectInputStream(client.getInputStream());
                id = (int)io.readObject();
                System.out.println(id);
                System.out.println("Fim");
                io.close();
                break;
               
            }catch(IOException | ClassNotFoundException e){
                port += 1;
                numberConections++;
                System.out.println("Erro " + e.getMessage());
            }
        }
        
        System.out.println(port);
        port += 100;
        System.out.println(port);
        
        boolean play = false;
        
        while (true){
            
            try{
                Socket client = new Socket("192.168.0.100", port);
                ObjectInputStream io = new ObjectInputStream(client.getInputStream());
                play = (boolean)io.readObject();
            }catch (Exception e){
                System.out.println("Erro " + e.getMessage());
            }
            
            //System.out.println(play);
            
            if(play) break;
            
        }
        
        Road game = new Road(44, 600, id, port);
        game.initRoad();
        game.editComponents();
    }
}
