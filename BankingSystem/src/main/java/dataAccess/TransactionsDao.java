package dataAccess;

import entity.Transactions;
import java.sql.Connection;
import java.sql.Date;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class TransactionsDao {
	

	static Connection con = DBConnection.getCon();
	
	public static void insertTransaction(Transactions t,int id) {
    	
		System.out.println(Date.valueOf(t.getTransDate()));
		Date d =Date.valueOf(t.getTransDate());
    	String query = "insert into Transactions(TransID,TransDate,CustomerID,TransType,TransferAccNum,TransAmount,Balance) values(?,?,?,?,?,?,?)";
    	PreparedStatement pst;
		try {
			pst = con.prepareStatement(query);
	    	pst.setInt(1, t.getTid());
	    	pst.setDate(2,d );
	    	pst.setInt(3,id);
	    	pst.setString(4,t.getType());
	    	pst.setInt(5, t.getTransferAccNum());
	    	pst.setInt(6, t.getAmount());
	    	pst.setInt(7, t.getBalance());
	    	
	    	
	    	pst.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public static int getTransID(int id) {
		
		int tid=0;
		String query ="Select Count(*) as transId  from Transactions where CustomerID=?";
		PreparedStatement pst ;
		try {
			pst= con.prepareStatement(query);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			rs.next();
			tid = rs.getInt("transId");
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return tid+1;
	}
	
	public static ArrayList<Transactions> getTransactions(int id){
		
		ArrayList<Transactions> allTransactions = new ArrayList<>();
		

    	String query = "select * from Transactions where CustomerID=? ";
    	PreparedStatement  pst;
    	try {
    		pst = con.prepareStatement(query);
    		pst.setInt(1, id);
    		ResultSet rs = pst.executeQuery();
    		while(rs.next()) {
    			int tid = rs.getInt("TransID");
    			String transType = rs.getString("TransType");
    			int amount = rs.getInt("TransAmount");
    			int balance = rs.getInt("Balance");
    			LocalDate date = rs.getDate("TransDate").toLocalDate();
    			int transferAccNum = rs.getInt("TransferAccNum");
    			
    			Transactions t = new Transactions(tid,transType,amount,balance,date,transferAccNum);
    			allTransactions.add(t);
    		}
    		
    	}
    	catch (SQLException e){
    		e.printStackTrace();
    	}
		return allTransactions;
		
	}
}
