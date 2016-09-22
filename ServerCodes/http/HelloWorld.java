import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.gson.Gson;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class HelloWorld extends HttpServlet {

    private String message;

    public void init() throws ServletException
    {
        message = "Hello World";
	//Server server = new Server();
	//server.start();
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h1>" + message + "</h1>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String id = null;
        String password = null;
        String type = request.getParameter("type");
        Info mInfo = new Info();
        //---------login-----------------
        if(type.equals("login")){
            id = request.getParameter("id");
            password = request.getParameter("password");
            DbHelper db = new DbHelper();
            db.connect();
            String sql = "select * from chat where account = '"+id+"' and password = '"+password+"';";
            ResultSet rs = db.select(sql);

            try{
                if(rs.next() == true){
                    mInfo.setId(id);
                    mInfo.setResult("success");

                    setFriendList(mInfo, id, db); //--------

                    Gson gson = new Gson();
                    String data = gson.toJson(mInfo);
                    out.write(data+"\n");
                    out.flush();
                }else{
                    Gson gson = new Gson();
                    String data = gson.toJson(mInfo);
                    out.write(data);
                }
            }catch(SQLException e){e.printStackTrace();}

            //---------select-----------------
        }else if(type.equals("select")){
            id = request.getParameter("id");
            DbHelper db = new DbHelper();
            db.connect();
            String sql = "select * from chat where account = '"+id+"';";
            ResultSet rs = db.select(sql);
            try {
                if (rs.next() == true) {
                    out.write("exist" + "\n");
                    out.flush();
                } else {
                    out.write("notexist" + "\n");
                    out.flush();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            //---------register-----------------
        }else if(type.equals("register")){
            id = request.getParameter("id");
            password = request.getParameter("password");
            DbHelper db = new DbHelper();
            db.connect();
            String sql2 = "insert into chat(account,password) values('"+id+"','"+password+"')";
            if(db.insert(sql2)){
                out.write("registersuccess" + "\n");
                out.flush();
            }else{
                out.write("registerfail" + "\n");
                out.flush();
            }
            //---------watch-----------------
        }else if(type.equals("watch")){
            String myId = request.getParameter("myId");
            String hisId = request.getParameter("hisId");
            DbHelper db = new DbHelper();
            db.connect();
            String sql = "select 1 from friends where account1 = '"+myId+"' and account2 = '"+hisId+"';";
            ResultSet rs = db.select(sql);
            try {
                if (rs.next() == true) { //already watched
                    out.write("exist" + "\n");
                    out.flush();
                } else {
                    String sql2 = "select * from friends where account1 = '"+hisId+"' and account2 = '"+myId+"';";
                    ResultSet rs2 = db.select(sql2);
                    if (rs2.next() == true) { //he was already watched me
                        int frdType = rs2.getInt("type");
                        if(frdType == 2){ //we watched each other
                            out.write("exist" + "\n");
                            out.flush();

                        }else{ // he already watched me,but i has not watched him
                            //update type = 2;
                            String sql3 = "update friends set type = 2 where account1 = '"+hisId+"' and account2 = '"+myId+"';";
                            if(db.update(sql3)){
                                Info info = new Info();
                                info.setResult("success");
                                setFriendList(info, myId, db);
                                Gson gson = new Gson();
                                String data = gson.toJson(info);
                                out.write(data + "\n");//success watch
                                out.flush();
                            }
                        }
                    }else{  // i and he first meet
                        // insert (id id 1)
                        String sql4 = "insert into friends(account1,account2,type) values('"+myId+"','"+hisId+"',1);";
                        if(db.insert(sql4)){
                            Info info = new Info();
                            info.setResult("success");
                            setFriendList(info, myId, db);
                            Gson gson = new Gson();
                            String data = gson.toJson(info);
                            out.write(data + "\n");//success watch
                            out.flush();
                        }
                    }


                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        //----- cancel watch--------------
        }else if(type.equals("delete")){

	}

        out.close();
    }



    public void destroy()
    {

    }


    private void setFriendList(Info mInfo, String id, DbHelper db){
        
        
        try {
            String sql2 = "select * from friends where account1 ='" + id + "';";
            ResultSet rs2 = db.select(sql2);
            while (rs2.next()) {
                Friend friend = new Friend();
                friend.id = rs2.getString("account2");
                if (rs2.getInt("type") == 2) {
                    friend.type = 2;
                } else {
                    friend.type = 1;
                } //i single watch him
                mInfo.getFriends().add(friend);
            }
            String sql3 = "select * from friends where account2 = '" + id + "';";
            ResultSet rs3 = db.select(sql3);
            while (rs3.next()) {
                Friend friend = new Friend();
                friend.id = rs3.getString("account1");
                if (rs3.getInt("type") == 2) {
                    friend.type = 2;
                } else {
                    friend.type = 0;
                } //he singe watch me
                mInfo.getFriends().add(friend);
            }
        }catch(SQLException e){e.printStackTrace();}
        
        
        
    }


}
