package servlets;


import dataAccess.CustomerDao;
import entity.Transactions;
import dataAccess.TransactionsDao;
import entity.Customer;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

@WebServlet("/Register")
public class Register extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String formDetails =request.getReader().lines().collect(Collectors.joining());		
		JSONObject formData = new JSONObject(formDetails);

		Customer c = new Customer();
		
		int tempSize =CustomerDao.numOfCustomers();
		
		c.setName(formData.getString("name"));
		c.setEmailId(formData.getString("email"));
		c.setId(tempSize+1);
		c.setAccountNumber(tempSize+10000+1);
		c.setBalance(10000);
		c.setPassword(passwordEncryption(formData.getString("password")));
		
		ArrayList<Transactions> transactionsList = new ArrayList<>();
        c.setTransactions(transactionsList);

        
        Transactions t = new Transactions();
        t.setTid(1);t.setType("Opening");
        t.setTransDate(LocalDate.now());
        t.setAmount(10000);
        t.setBalance(c.getBalance());
        t.setTransferAccNum(0);

        //c.getTransactions().add(t);
        boolean check =CustomerDao.insertCustomer(c);
		TransactionsDao.insertTransaction(t,c.getId());
		
		
		
		if(check) {
			JSONObject user = new JSONObject(c);
			user.accumulate("check",check);
			
			response.getWriter().write(user.toString());
		}
		else {
			String  reqInfo= "{\"check\":"+check+"}";
			JSONObject info= new JSONObject(reqInfo);
			
			response.getWriter().write(info.toString());
		}
    
	}
	
	public static String passwordEncryption(String password){

        StringBuilder encryptedPass = new StringBuilder();

        for(int i =0; i<password.length();i++){

            char ch= password.charAt(i);

            if(ch>=97&&ch<=122) encryptedPass.append((char)(96+((int)ch+1-96)%26));
            else if(ch>=65&&ch<=90) encryptedPass.append((char)(64+((int)ch+1-64)%26));
            else encryptedPass.append((char)(47+((int)ch+1-47)%10));

        }

        return encryptedPass.toString();

    }
	




}
