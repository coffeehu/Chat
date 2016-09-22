import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hc on 2016/8/31.
 */
public class ConnectionThread extends Thread {
    private Socket socket;
    public BufferedReader reader;
    //public BufferedWriter writer;
    public OutputStream writer;
    private List<OnMessageListener> onMessageListenerList;

    public ConnectionThread(Socket socket){
        super();
        this.socket = socket;
        onMessageListenerList = new ArrayList<OnMessageListener>();
	System.out.println("ct constructor");
    }

    public void connect(){
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
            //writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
            writer = socket.getOutputStream();
	    System.out.println("success connect()");
            start();
        }catch (IOException e){e.printStackTrace();}
    }

    /**
     * 
     * @param myMessage
     */
    public void sendMessage(MyMessage myMessage){
        try {
            Gson gson = new Gson();
            String data = gson.toJson(myMessage);
            //writer.write((data+"\r\n").getBytes("UTF-8"));
            writer.write((data+"\n").getBytes("UTF-8"));
            System.out.println("sendmessage(mymessage),data ="+data);
            //writer.write(myMessage.content+"\r\n");
        }catch (IOException e){e.printStackTrace();}
    }

    public void sendMessage(String jsonData){
        try {
            System.out.println("sendmessage(String),json ="+jsonData);
            //writer.write(jsonData+"\r\n");
            writer.write((jsonData+"\n").getBytes("UTF-8"));
        }catch (IOException e){e.printStackTrace();}
    }

    @Override
    public void run(){
	System.out.println("connectionthread run()");
        while (true){
            try {
                if (reader.ready()) {
                    String data = reader.readLine();
              	    System.out.println("run() is ready()"+data+",listenerlist size ="+onMessageListenerList.size());
                    Gson gson = new Gson();
                    MyMessage myMessage = gson.fromJson(data, MyMessage.class);
		    System.out.println("ct run() is ready()  myMessage ="+myMessage.toString());
                    if(onMessageListenerList.size() > 0){
                        for(OnMessageListener listener : onMessageListenerList){
                            listener.onReceive(myMessage);
                        }
                    }
                }
            }catch (IOException e){
		System.out.println("connectionthread run() try failed");
		e.printStackTrace();}
        }
    }


    public static interface OnMessageListener{
        public void onReceive(MyMessage myMessage);
    }

    public void addMessageListener(OnMessageListener onMessageListener){
        onMessageListenerList.add(onMessageListener);
	System.out.println("add listener");
    }

    public void removeMessageListener(OnMessageListener onMessageListener){
        onMessageListenerList.remove(onMessageListener);
    }
}
