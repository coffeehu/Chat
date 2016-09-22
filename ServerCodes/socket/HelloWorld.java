import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.gson.Gson;
import java.sql.*;
import java.util.ArrayList;


public class HelloWorld extends HttpServlet {
 
  private String message;

  public void init() throws ServletException
  {

      message = "Hello World";
  }

  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {

      response.setContentType("text/html");


      PrintWriter out = response.getWriter();
      out.println("<h1>" + message + "</h1>");
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String type = request.getParameter("type");
        String id = request.getParameter("id");
        String password = request.getParameter("password");
	String result = fail;

	if(type.equals("login")){
	    DbHelper db = new DbHelper();
	    db.connect();
	    String sql = "select * from chat where account = '"+id+"' and password = '"+password+"';";
            ResultSet rs = db.select(sql);
	    Info info = new Info(); //id, result,List<Friend> friends		

	    try{
		  if(rs.next() == true){
			info.id = id;
			info.result = "success";
		        String sql2 = "select * from hc";
			ResultSet rs2 = db.select(sql2);
			while(rs2.next()){
				Friend friend = new Friend();
				friend.id = rs2.getString("account");
				info.friends.add(friend); 
			}
			Gson gson = new Gson();
			String data = gson.toJson(info);
	                out.write(data);
		   }else{
			info.type = "fail";
			Gson gson = new Gson();
			String data = gson.toJson(info);
			out.write(data);
		   }
	    }catch(SQLException e){e.printStackTrace();}
	    
	}


 

        out.flush();
        out.close();
    }


  
  public void destroy()
  {

  }
}
