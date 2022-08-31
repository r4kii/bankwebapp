$(document).ready(function() {

	$('#depositForm').hide()
	$('#withdrawForm').hide()
	$('#accountTransForm').hide()
	$('#afterwithtext').hide()
	$('#afterdeptext').hide()
	$('#aftertranstext').hide()
	$('#insufficientBalance').hide()
	$('#insufficientBalanceTransfer').hide()
	$('#invalidAccNum').hide()
	$('#historyDiv').hide()
	$('#invalidRange').hide()

	$('#withdraw').css({
		'cursor': 'pointer'
	});

	$('#deposit').css({
		'cursor': 'pointer'
	});

	$('#accountTrans').css({
		'cursor': 'pointer'
	});

	$('#transferHistory').css({
		'cursor': 'pointer'
	});

	$('#logout').css({
		'cursor': 'pointer'
	});

	$('#withdraw').click(
		function() {
			$('#withdrawForm').show()
			$('#depositForm').hide()
			$('#accountTransForm').hide()
			$('#historyDiv').hide()
			$('#afterwithtext').hide()
			$('#afterdeptext').hide()
			$('#aftertranstext').hide()

			$(".container > .formContainer> h3").children('span').each(function() {
				$(this).removeClass('section');
			});

			$('#withdraw span').addClass('section')


		})

	$('#deposit').click(
		function() {
			$('#withdrawForm').hide()
			$('#depositForm').show()
			$('#accountTransForm').hide()
			$('#historyDiv').hide()
			$('#afterwithtext').hide()
			$('#afterdeptext').hide()
			$('#aftertranstext').hide()

			$(".container > .formContainer> h3").children('span').each(function() {
				$(this).removeClass('section');
			});

			$('#deposit span').addClass('section');
		})

	$('#accountTrans').click(
		function() {
			$('#withdrawForm').hide();
			$('#depositForm').hide()
			$('#accountTransForm').show()
			$('#historyDiv').hide()
			$('#afterwithtext').hide()
			$('#afterdeptext').hide()
			$('#aftertranstext').hide()


			$(".container > .formContainer> h3").children('span').each(function() {
				$(this).removeClass('section');
			});

			$('#accountTrans span').addClass('section');

		})

	$('#transferHistory').click(
		function() {

			$('#withdrawForm').hide();
			$('#depositForm').hide()
			$('#accountTransForm').hide()
			$('#historyDiv').show()
			$('#afterwithtext').hide()
			$('#afterdeptext').hide()
			$('#aftertranstext').hide()

			$(".container > .formContainer> h3").children('span').each(function() {
				$(this).removeClass('section');
			});

			$('#transferHistory span').addClass('section');

			var filterType = $('#transHisFilter')
				.find(":selected").text()
			if (filterType === 'None') {
				$('#transferAccNumForm').hide()
				$('#dates').hide()
				$("#historyTable tr").remove()
				ajaxHistory()
			}

		})

	$('#withdrawForm').submit(function(e) {
		e.preventDefault()
		let amount = $('#withamount').val();
		$.ajax({
			method: "POST",
			url: "Operations",
			data: JSON.stringify({
				"amount": amount,
				"operation": "withdraw"
			}),
			dataType: "json",
		}).done(function(data) {
			if (data.check == "1") {
				$('#withdrawForm').hide()
				$('#afterwithtext').show()
				$('#insufficientBalance').hide()
				$('#afterwithtext').html("Withdraw Successfull!<br>Updated Balance :" + data.balance)
				swal("Withdrawal Successfull!", "Updated Balance :" + data.balance, "success")
			} else {
				$('#insufficientBalance').show()
			}
		})
	})

	$("#depositForm").submit(function(e) {
		e.preventDefault();
		let amount = $("#depamount").val();

		$.ajax({
			method: "POST",
			url: "Operations",
			data: JSON.stringify({
				amount: amount,
				operation: "deposit",
			}),
			dataType: "json",
		}).done(function(data) {
			$("#depositForm").hide();
			$("#afterdeptext").show();
			$("#afterdeptext").html(
				"Deposit Successfull!<br>Updated Balance :" + data.balance
			);
			swal("Deposit Successfull!", "Updated Balance :" + data.balance, "success");
		});
	});

	$("#accountTransForm").submit(function(e) {
		e.preventDefault();
		let amount = $("#transferamount").val();
		let tacNum = $("#transfertoid").val();

		$.ajax({
			method: "POST",
			url: "Operations",
			data: JSON.stringify({
				amount: amount,
				tacNum: tacNum,
				operation: "accountTransfer",
			}),
			dataType: "json",
		}).done(function(data) {
			if (data.check == "1") {
				$("#accountTransForm").hide();
				$("#aftertranstext").show();
				$("#aftertranstext").html(
					"Transfer Success!<br>Updated Balance :" + data.balance
				);
				swal(
					"Transfer Successfull!",
					"Updated Balance :" + data.balance,
					"success"
				);
				$("#insufficientBalanceTransfer").hide();
				$("#invalidAccNum").hide();
			} else {
				if (data.balance - amount >= 1000) {
					$("#invalidAccNum").show();
					$("#insufficientBalanceTransfer").hide();
				} else {
					$("#insufficientBalanceTransfer").show();
					$("#invalidAccNum").hide();
				}
			}
		});
	});

	$('#dates').hide();
	$('#transferAccNumForm').hide();

	$("#dates").submit(function(e) {
		e.preventDefault();
		$("#historyTable tr").remove();

		const sdate = new Date($("#StartDate").val());
		const edate = new Date($("#EndDate").val());
		if (sdate.getTime() > edate.getTime()) $("#invalidRange").show();
		else $("#invalidRange").hide();
		$.ajax({
			method: "POST",
			url: "Operations",
			dataType: "json",
			data: JSON.stringify({
				operation: "as",
			}),
		}).done(function(data) {
			var transaction = "";

			$("#historyTable").append(
				"<tr>" +
				"<th>Transaction ID</th>" +
				"<th>Transaction Date</th>" +
				"<th>Transaction Type</th>" +
				"<th>Transfer Account Number</th>" +
				"<th>Transaction Amount</th>" +
				"<th>Updated Balance</th>" +
				"</tr>"
			);
			$.each(data, function(key, value) {
				if (value.transferAccNum == 0) {
					value.transferAccNum = "nil";
				}

				if (
					new Date(value.transDate).getTime() >= sdate.getTime() &&
					new Date(value.transDate).getTime() <= edate.getTime()
				) {
					transaction += "<tr>";
					transaction += "<td>" + value.tid + "</td>";
					transaction += "<td>" + value.transDate + "</td>";
					transaction += "<td>" + value.type + "</td>";
					transaction += "<td>" + value.transferAccNum + "</td>";
					transaction += "<td>" + value.amount + "</td>";
					transaction += "<td>" + value.balance + "</td>";
					transaction += "</tr>";
				}
			});
			$("#historyTable").append(transaction);
		});
	});

	$("#transferAccNumForm").submit(function(e) {
		e.preventDefault();
		$("#historyTable tr").remove();
		var tacNum = $("input#transferAccNum").val();

		$.ajax({
			method: "POST",
			url: "Operations",
			dataType: "json",
			data: JSON.stringify({
				operation: "as",
			}),
		}).done(function(data) {
			var transaction = "";

			$("#historyTable").append(
				"<tr>" +
				"<th>Transaction ID</th>" +
				"<th>Transaction Date</th>" +
				"<th>Transaction Type</th>" +
				"<th>Transfer Account Number</th>" +
				"<th>Transaction Amount</th>" +
				"<th>Updated Balance</th>" +
				"</tr>"
			);
			$.each(data, function(key, value) {
				if (value.transferAccNum == tacNum) {
					transaction += "<tr>";
					transaction += "<td>" + value.tid + "</td>";
					transaction += "<td>" + value.transDate + "</td>";
					transaction += "<td>" + value.type + "</td>";
					transaction += "<td>" + value.transferAccNum + "</td>";
					transaction += "<td>" + value.amount + "</td>";
					transaction += "<td>" + value.balance + "</td>";
					transaction += "</tr>";
				}
			});
			$("#historyTable").append(transaction);
		});
	});

})

function updateFilter() {
	console.log($('#transHisFilter').find(":selected").val())
	var filterType = $('#transHisFilter').find(":selected").text()
	if (filterType === 'TransactionDate') {
		$('#dates').show()
		$('#transferAccNumForm').hide()
	} else if (filterType === 'TransferAccountNumber') {
		$('#transferAccNumForm').show()
		$('#dates').hide()
	} else if (filterType === 'None') {
		$('#transferAccNumForm').hide()
		$('#dates').hide()
		$("#historyTable tr").remove()
		ajaxHistory();
	}
}

function ajaxHistory() {
	$.ajax({
		method: "POST",
		url: "Operations",
		dataType: "json",
		data: JSON.stringify({
			operation: "as",
		}),
	}).done(function(data) {
		var transaction = "";
		$("#historyTable").append(
			"<tr>" +
			"<th>Transaction ID</th>" +
			"<th>Transaction Date</th>" +
			"<th>Transaction Type</th>" +
			"<th>Transfer Account Number</th>" +
			"<th>Transaction Amount</th>" +
			"<th>Updated Balance</th>" +
			"</tr>"
		);
		$.each(data, function(key, value) {
			console.log(typeof value.transDate);
			const date = new Date(value.transDate);
			console.log(typeof date);
			if (value.transferAccNum == 0) {
				value.transferAccNum = "nil";
			}
			/* console.log(value.transDate+'') */
			transaction += "<tr>";
			transaction += "<td>" + value.tid + "</td>";
			transaction += "<td>" + value.transDate + "</td>";
			transaction += "<td>" + value.type + "</td>";
			transaction += "<td>" + value.transferAccNum + "</td>";
			transaction += "<td>" + value.amount + "</td>";
			transaction += "<td>" + value.balance + "</td>";
			transaction += "</tr>";
		});
		$("#historyTable").append(transaction);
	});
}