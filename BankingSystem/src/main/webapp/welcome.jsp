<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/welcome.css">
    <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.js"></script>
    <script type="text/javascript" src="js/welcome.js"></script>
    <title>Welcome</title>
  </head>
  <body>
  <%
    response.setHeader("Cache-Control", "no-cache, no-store, must-reavalidate"); 
	System.out.println(request.getHeader("referer"));
	if (request.getHeader("referer") == null || session.getAttribute("cid") == null)
		response.sendRedirect("WelcomePage.html");
	%> 
	<div class="parentContainer">
	
      <div class="container">
        <div class="formContainer">
          <h3 id="withdraw">
            <span>Withdraw</span>
          </h3>
        </div>
        <div class="formContainer">
          <h3 id="deposit">
            <span>Deposit</span>
          </h3>
        </div>
        <div class="formContainer">
          <h3 id="accountTrans">
            <span>Account Transfer</span>
          </h3>
        </div>
        <div class="formContainer">
          <h3 id="transferHistory">
            <span>Transfer History</span>
          </h3>
        </div>
        
        <form class="formContainer" action="Logout" method="Post">
          <input type="submit" class="submit-btn" value="Logout">
        </form>
        
      </div>
      
      <form id="withdrawForm" class="withDrawStyle">
        <label>Withdrawal Amount:</label>
        <input type="text" id="withamount" name="withdrawAmount">
        <br>
        <input type="submit" id="withdrawBut" id="submit-btn" class="submit-btn" name="withdrawBut" value="Withdraw">
        <small id="insufficientBalance" style="color: red;">Insufficient Balance </small>
      </form>
      <p id="afterwithtext" class="formStyle"></p>
      <form id="depositForm" class="depositStyle">
        <label>Deposit Amount:</label>
        <input type="text" id="depamount" name="depAmount">
        <br>
        <input type="submit" id="submit-btn" class="submit-btn" value="Deposit">
      </form>
      <p id="afterdeptext" class="formStyle"></p>
      <form id="accountTransForm" class="accountStyle">
        <label>Transfer Amount :</label>
        <input type="number" id="transferamount" name="transferAmount">
        <br>
        <label>Transfer Account Number :</label>
        <input type="number" id="transfertoid" name="transferToId">
        <br>
        <input type="submit" id="submit-btn" class="submit-btn" value="Transfer">
        <small id="insufficientBalanceTransfer" style="color: red;">Insufficient Balance </small>
        <small id="invalidAccNum" style="color: red;">Invalid AccountNumber </small>
      </form>
      <p id="aftertranstext" class="formStyle"></p>
      <div id="historyDiv" class="history-table">
        <div class="filter">
          <h1>Transactions</h1>
          <label for="filter">Filter By</label>
          <select name="cars" id="transHisFilter" onchange="updateFilter()">
            <optgroup label="Choose"></optgroup>
            <option id="all">None</option>
            <option id="basedOnDate">TransactionDate</option>
            <option id="transferAccNum">TransferAccountNumber</option>
          </select>
          <br>
          <br>
          <form id="dates">
            <label for="StartDate">From:</label>
            <input type="date" id="StartDate" name="StartDate" required>
            <label for="EndDate">To:</label>
            <input type="date" id="EndDate" name="EndDate" required>
            <input type="submit" value="Search" class="search-btn">
            <br>
            <small id="invalidRange" style="color: red;">Enter Valid Range</small>
            <br>
          </form>
          <form id="transferAccNumForm">
            <label>TransferAccountNumber:</label>
            <input type="number" id="transferAccNum">
            <input type="submit" value="Search" class="search-btn">
          </form>
        </div>
        <table id='historyTable'></table>
      </div>
    </div>
  </body>
</html>