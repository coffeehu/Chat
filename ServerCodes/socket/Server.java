import com.google.gson.Gson;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.sql.*;

/**
 * Created by hc on 2016/8/30.
 */
public class Server {
    private ConnectionThread connectionThread;
    private List<MyMessage> messageList = new ArrayList<>();
    public  HashMap<String, List<MyMessage>> messagesMap = new HashMap<String, List<MyMessage>>();
    public static HashMap<String, ConnectionThread> conns = new HashMap<String, ConnectionThread>();
    private static final int PORT = 55555;
    private ServerSocket serverSocket = null;

//--------------------------
/*    private ConnectionThread.OnMessageListener imgListener = new ConnectionThread.OnMessageListener() {
        @Override
        public void onReceive(MyMessage m) {
		System.out.println("---img listening mymessage");
	}

};*/
//--------------------------


    private ConnectionThread.OnMessageListener logListener = new ConnectionThread.OnMessageListener() {
        public void onReceive(byte[] bytes) {}
        @Override
        public void onReceive(MyMessage myMessage) {
	    System.out.println("loglistener onreceive(),myMessage ="+myMessage.toString());
	    //----------------login-----------------------
            if(myMessage.type.equals("login")){
                    conns.put(myMessage.id, connectionThread);
	    	    System.out.println("login success! id :"+myMessage.id);
		    if(messagesMap.get(myMessage.getId())!=null){
			try{
			    Thread.sleep(2500);
			}catch(InterruptedException e){e.printStackTrace();}
			List<MyMessage> messages = messagesMap.get(myMessage.getId());
                        ConnectionThread connectionThread = conns.get(myMessage.id);
			for(int i=0;i<messages.size();i++){
		            messages.get(i).localType = "frd";
                            connectionThread.sendMessage(messages.get(i));
			}
			messagesMap.remove(myMessage.getId());
		    }
	    }else if(myMessage.type.equals("close")){
		try{
	    	        System.out.println("close! id :"+myMessage.id);
                        conns.get(myMessage.id).disconnect();
			conns.remove(myMessage.id);
		}catch(IOException e){e.printStackTrace();}
		
	    }
        }
    };

    private ConnectionThread.OnMessageListener msgListener = new ConnectionThread.OnMessageListener() {
        public void onReceive(byte[] bytes) {}
        @Override
        public void onReceive(MyMessage myMessage) {
	    System.out.println("msglistener onreceive(),myMessage ="+myMessage.toString());
            if(myMessage.type.equals("message")){
                //try {
	    	System.out.println("--is message!msglistener onreceive() type ="+myMessage.type+", destid ="+myMessage.destId+",id ="+myMessage.id);
		if(conns.get(myMessage.getDestId()) != null){
                    ConnectionThread connectionThread = (ConnectionThread) conns.get(myMessage.getDestId());
		    System.out.println("--get conn ="+conns.get(myMessage.getDestId().toString()));
		    myMessage.localType = "frd";
                    connectionThread.sendMessage(myMessage);
		    //System.out.println("--write data ="+data);
                //}catch (IOException e){e.printStackTrace();}
                }else{
		    System.out.println("he is offline");
		    if(messagesMap.get(myMessage.getDestId())==null){
    			List<MyMessage> messageList = new ArrayList<>();
			messageList.add(myMessage);
			messagesMap.put(myMessage.getDestId(),messageList);
		    }else{
			messagesMap.get(myMessage.getDestId()).add(myMessage);
		    }
		}
            }
        }
    };

    private void start(){
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("start server, port: "+PORT);

            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("client has connect");
                connectionThread = new ConnectionThread(socket);
                connectionThread.addMessageListener(logListener);
                connectionThread.addMessageListener(msgListener);
                connectionThread.connect();

            }

        }catch (IOException e){e.printStackTrace();}
    }


    public static void main(String args[]){
        Server server = new Server();
        server.start();
    }

}

