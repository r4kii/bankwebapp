$(document).ready(function() {
	$("#form1").hide();
	$("#afterreg").hide();

	$("#afterregbut").hide();

	$("#register").click(function() {
		$("#form2").hide();
		$("#form1").show();
	});
	$("#login").click(function() {
		$("#form1").hide();
		$("#form2").show();
	});

	$("#existingUser").hide();
	$("#invaliduser").hide();
	$("#passwordinvalid").hide();
	$("#passcheck").hide();

	$("#register").css({
		cursor: "pointer",
	});
	$("#login").css({
		cursor: "pointer",
	});

	$("#form1").submit(function(e) {
		e.preventDefault();
		let name = $("input#namer").val();
		let email = $("input#emailr").val();
		let password = $("input#passwordr").val();
		let passwordCheck = $("input#passwordcheck").val();

		let validPassword = true; //validatePassword(password);
		let comparePassword = true;
		if (password != passwordCheck) comparePassword = false;

		//console.log(comparePassword+'');

		if (!validPassword) {
			$("#passwordinvalid").show();
		} else {
			$("#passwordinvalid").hide();
		}

		if (!comparePassword) {
			$("#passcheck").show();
			$("input#passwordcheck").val("");
		} else {
			$("#passcheck").hide();
		}

		if (validPassword && comparePassword) {
			$.ajax({
				method: "POST",
				url: "Register",
				data: JSON.stringify({
					name: name,
					email: email,
					password: password,
				}),

				//we are expecting a json data in response from a server
				dataType: "json",
			}).done(function(data) {
				if (data.check == "1") {
					$("#existingUser").hide();
					$("#afterreg").show();
					$("#form1").hide();
					//console.log(data)
					$("#afterreg").html(
						"Name:" +
						data.name +
						"<br>Email-Id:" +
						data.emailId +
						"<br>Your Customer ID:" +
						data.id +
						"<br>Your Account Number:" +
						data.accountNumber
					);
					$("#afterregbut").show();
				} else {
					$("#existingUser").show();
				}

				//alert("success")
			});
		}

		//console.log(name+' '+email+' '+password+' '+passwordCheck+'');
	});

	$("#afterregbut").click(function() {
		$("#form1").trigger("reset");
		$("#afterreg").hide();
		$("#form2").show();
		$("#afterregbut").hide();
	});

	$("#form2").submit(function(e) {
		e.preventDefault();

		let id = $("input#customerId").val();
		let password = $("input#password").val();

		$.ajax({
			method: "POST",
			url: "Login",
			data: JSON.stringify({
				id: id,
				password: password,
			}),
			dataType: "json",
		}).done(function(data) {
			console.log(data);
			if (data.check == "1") {
				$("#invaliduser").hide();
				window.location.href =
					"http://localhost:8080/BankingSystem/welcome.jsp";
			} else {
				$("#invaliduser").show();
			}
		});
	});

	function validatePassword(password) {
		var regex =
			"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$";
		if (password.match(regex)) return true;
		else return false;
	}
});