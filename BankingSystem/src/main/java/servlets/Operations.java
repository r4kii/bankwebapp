package servlets;

import dataAccess.CustomerDao;
import entity.Transactions;
import dataAccess.TransactionsDao;
import java.io.IOException;
import java.time.LocalDate;
//import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class Operations
 */
@WebServlet("/Operations")
public class Operations extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
  
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String reqdata  =request.getReader().lines().collect(Collectors.joining());
		JSONObject value = new JSONObject(reqdata);
		
		HttpSession session = request.getSession();
		int loginId = (int)session.getAttribute("cid");
		
		if(value.getString("operation").equals("withdraw")) withdraw(request,response,value,loginId);
		else if(value.getString("operation").equals("deposit")) deposit(request,response,value,loginId);
		else if(value.getString("operation").equals("accountTransfer")) accountTransfer(request,response,value,loginId);
		else transferHistory(request, response,loginId);

	}
	
	private void withdraw(HttpServletRequest request, HttpServletResponse response,JSONObject objJson, int loginId) throws ServletException, IOException {
	
		int amount = Integer.parseInt(objJson.getString("amount"));
	
		int currBalance = CustomerDao.getBalance(loginId);
		boolean check = amount<=currBalance-1000;

		if(check) {
			CustomerDao.updateBalance(currBalance-amount, loginId);
			currBalance = CustomerDao.getBalance(loginId);//c.getBalance();
		}

		Transactions t = new Transactions();
		
		t.setTid(TransactionsDao.getTransID(loginId));
        t.setAmount(amount);
        t.setType("AtmWithdrawal");
        t.setBalance(currBalance);
        t.setTransferAccNum(0);
        t.setTransDate(LocalDate.now());
   
        TransactionsDao.insertTransaction(t,loginId);
		
		String  reqInfo= "{\"check\":"+check+",\"balance\":"+currBalance+"}";
		JSONObject info= new JSONObject(reqInfo);
		response.getWriter().write(info.toString());
	}

	private void deposit(HttpServletRequest request, HttpServletResponse response,JSONObject objJson,int loginId) throws ServletException, IOException {

		int amount = Integer.parseInt(objJson.getString("amount"));
	
		CustomerDao.updateBalance(CustomerDao.getBalance(loginId)+amount, loginId);
		
		Transactions t = new Transactions();
		t.setTid(TransactionsDao.getTransID(loginId));
        t.setAmount(amount);
        t.setType("CashDeposit");
        int currBalance =CustomerDao.getBalance(loginId);
        t.setBalance(currBalance);
        t.setTransferAccNum(0);
        t.setTransDate(LocalDate.now());

        TransactionsDao.insertTransaction(t,loginId);
  
		String  reqInfo= "{\"balance\":"+currBalance+"}";
		JSONObject info= new JSONObject(reqInfo);
		response.getWriter().write(info.toString());
	}
	
	public void accountTransfer(HttpServletRequest request, HttpServletResponse response,JSONObject objJson,int loginId)throws ServletException, IOException {

		int amount = Integer.parseInt(objJson.getString("amount"));
		int tacNum = Integer.parseInt(objJson.getString("tacNum"));

		int currBalance = CustomerDao.getBalance(loginId);
		boolean checkForBalance = currBalance-amount>=1000;
		
		int transToCusID = CustomerDao.getTransToCusID(tacNum);
		
		if((transToCusID==0 || transToCusID==loginId) || !checkForBalance) {
			//insufficient balance or invalid accnum
			String  reqInfo="";
			
			if(!checkForBalance)
			{
				 reqInfo= "{\"check\":"+false+",\"balance\":"+currBalance+"}";
			}
			else if(transToCusID==0 || transToCusID==loginId) {
				
				reqInfo= "{\"check\":"+false+",\"balance\":"+currBalance+"}";
			}
			
			JSONObject info= new JSONObject(reqInfo);
	        response.getWriter().write(info.toString());
		}
		else if(checkForBalance && transToCusID!=0) {
			//valid entry
			CustomerDao.updateBalance(CustomerDao.getBalance(loginId)-amount, loginId);
			CustomerDao.updateBalance(CustomerDao.getBalance(transToCusID)+amount, transToCusID);
	        
	        Transactions t1 = new Transactions();
	        
	        t1.setTid(TransactionsDao.getTransID(loginId));
	        t1.setAmount(amount);
	        t1.setType("Transfer to");
	        t1.setBalance(CustomerDao.getBalance(loginId));
	        t1.setTransferAccNum(tacNum);
	        t1.setTransDate(LocalDate.now());
	        
	        TransactionsDao.insertTransaction(t1,loginId);
	        
	        Transactions t2 = new Transactions();
	        
	        t2.setTid(TransactionsDao.getTransID(transToCusID));
	        t2.setAmount(amount);
	        t2.setType("Transfer from");
	        t2.setBalance(CustomerDao.getBalance(transToCusID));
	        t2.setTransferAccNum(CustomerDao.getAccNum(loginId));
	        t2.setTransDate(LocalDate.now());
	        
	        TransactionsDao.insertTransaction(t2,transToCusID);
	        
	        String  reqInfo= "{\"check\":"+true+",\"balance\":"+t1.getBalance()+"}";
	        JSONObject info= new JSONObject(reqInfo);
	        response.getWriter().write(info.toString());
	        
		}
	
	}
	
	public void transferHistory(HttpServletRequest request, HttpServletResponse response,int loginId) throws IOException {

		JSONArray jArr = new JSONArray();

		for(Transactions t : TransactionsDao.getTransactions(loginId)) {

			JSONObject temp = new JSONObject(t);
			jArr.put(temp);

		}
		response.getWriter().write(jArr.toString());
	}
	

}

