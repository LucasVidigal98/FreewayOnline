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
        int id = 0;
        while (true){
            
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
                if(port > 10003) port = 10000;
                System.out.println("Erro " + e.getMessage());
            }
        }
        
        if (id == 0) port = 10100;
        else if (id == 1) port = 10101;
        else if (id == 2) port = 10102;
        else if (id == 3) port = 10103;
        
        boolean play = false;
        
        while (true){
            
            Socket client = new Socket("192.168.0.100", port);
            ObjectInputStream io = new ObjectInputStream(client.getInputStream());
            play = (boolean)io.readObject();
            
            System.out.println(play);
            
            if(play) break;
            
        }
        
        Road game = new Road(44, 600, id, port);
        game.initRoad();
        game.editComponents();
    }
}
