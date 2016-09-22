import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.sql.*;

/**
 * Created by hc on 2016/8/30.
 */
public class Server extends Thread{
    private ConnectionThread connectionThread;
    private ArrayList<MyMessage> messageList = new ArrayList<>();
    public static HashMap<String, ConnectionThread> conns = new HashMap<String, ConnectionThread>();
    private static final int PORT = 55555;
    private ServerSocket serverSocket = null;


    private ConnectionThread.OnMessageListener msgListener = new ConnectionThread.OnMessageListener() {
        @Override
        public void onReceive(MyMessage myMessage) {
	    System.out.println("msglistener onreceive(),myMessage ="+myMessage.toString());
            if(myMessage.type.equals("message")){
                //try {
	    	System.out.println("--is message!msglistener onreceive() type ="+myMessage.type+", destid ="+myMessage.destId+",id ="+myMessage.id);
                    ConnectionThread connectionThread = (ConnectionThread) conns.get(myMessage.getDestId());
		    System.out.println("--get conn ="+conns.get(myMessage.getDestId().toString()));
		    myMessage.localType = "frd";
                    connectionThread.sendMessage(myMessage);
		    //System.out.println("--write data ="+data);
                //}catch (IOException e){e.printStackTrace();}
            }else if(myMessage.type.equals("login")){
         	    conns.put(myMessage.id, connectionThread);
                    connectionThread.sendMessage(myMessage);
	    }
        }
    };

    public void run(){
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("start server, port: "+PORT);

            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("client has connect");
                connectionThread = new ConnectionThread(socket);
                connectionThread.addMessageListener(msgListener);
                connectionThread.connect();

            }

        }catch (IOException e){e.printStackTrace();}
    }



}

