package servlets;

import dataAccess.CustomerDao;
import java.io.IOException;
//import java.io.PrintWriter;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;


@WebServlet("/Login")
public class Login extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String loginDetails = request.getReader().lines().collect(Collectors.joining());
		JSONObject loginData = new JSONObject(loginDetails);
		
		int tempId = Integer.parseInt(loginData.getString("id"));
		String password = Register.passwordEncryption(loginData.getString("password"));

		boolean check = CustomerDao.verifyLogin(tempId, password);

		if(check) {
			HttpSession session = request.getSession();
			session.setAttribute("cid", tempId);
		}
		
		String  checkAuth= "{\"check\":"+check+"}";
		
		JSONObject validLogin = new JSONObject(checkAuth);

		response.getWriter().write(validLogin.toString());;

	}
	

}
