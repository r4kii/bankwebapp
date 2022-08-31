package entity;
import java.time.LocalDate;

public class Transactions {

    
	private int tid ;
    private String type;
    private int amount ;
    private int balance;
    private LocalDate transDate;
    private int transferAccNum;
    

    public int getTransferAccNum() {
		return transferAccNum;
	}

	public void setTransferAccNum(int transferAccNum) {
		this.transferAccNum = transferAccNum;
	}

	public LocalDate getTransDate() {
		return transDate;
	}

	public void setTransDate(LocalDate transDate) {
		this.transDate = transDate;
	}

	public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public Transactions(int tid, String type, int amount, int balance, LocalDate transDate, int transferAccNum) {
		super();
		this.tid = tid;
		this.type = type;
		this.amount = amount;
		this.balance = balance;
		this.transDate = transDate;
		this.transferAccNum = transferAccNum;
	}

	@Override
	public String toString() {
		return "Transactions [tid=" + tid + ", type=" + type + ", amount=" + amount + ", balance=" + balance
				+ ", transDate=" + transDate + ", transferAccNum=" + transferAccNum + "]";
	}

	public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

   

    public Transactions() {
    	
    }
    
	public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
    
    
    
}