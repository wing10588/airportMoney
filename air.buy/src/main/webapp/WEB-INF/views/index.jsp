<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<title>LCC airport lowest-prices</title>

<!-- jQuery -->
<script src="resources/js/jquery.js"></script>
<script src="resources/js/jquery-ui.min.js"></script>
<script src="resources/js/ajax.js"></script>
<!-- Bootstrap Core JavaScript -->
<script src="resources/js/bootstrap.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.6/moment.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/js/bootstrap-datetimepicker.min.js"></script>

<script src='resources/js/moment.min.js'></script>
<script src='resources/js/fullcalendar.min.js'></script>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>


<link href='resources/css/fullcalendar.css' rel='stylesheet' />
<link href='resources/css/fullcalendar.print.css' rel='stylesheet'
	media='print' />

<!-- Bootstrap Core CSS -->
<link href="resources/css/bootstrap.min.css" rel="stylesheet">

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/css/bootstrap-datetimepicker.min.css" />

<!-- Custom CSS -->
<link href="resources/css/landing-page.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="resources/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" type="text/css">
<link
	href="https://fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic"
	rel="stylesheet" type="text/css">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
<style type="text/css">
.panel-transparent {
	background: none;
}

.panel-transparent .panel-heading {
	background: rgba(122, 130, 136, 0.2) !important;
}

.panel-transparent .panel-body {
	background: rgba(46, 51, 56, 0.2) !important;
}
</style>
<script type="text/javascript">
	$(function() {
		$body = $("body");
		$(document).on({
			ajaxStart : function() {
				$body.addClass("loading");
			},
			ajaxStop : function() {
				$body.removeClass("loading");
			}
		});
		$('#outboundDate').datetimepicker({
			format : 'YYYY/MM/DD'
		});
		$('#returnDate').datetimepicker({
			format : 'YYYY/MM/DD'
		});
		$("#from").change(function() {

			//	 setTimeout(function(){
			$("#to").find("option").show();
			//    },5000);

			$("#to").focus();

		});
		$('select').focus(function() {
			$(this).attr("size", $(this).attr("expandto"));

		});
		$('select').blur(function() {
			$(this).attr("size", 1);

		});
		$("#to").change(function() {
			//	 setTimeout(function(){
			$("#outboundDate").focus();
			//	 },5000);

		});
		var date = "";
		$("#outboundDate").on("dp.change", function(e) {
			if (date != "") {

				if ($('input[name="structure"]:checked').val() == "RoundTrip") {
					$("#returnDate").focus();

				}
			} else {
				date = $("#outboundDate").val();
			}
		});
		$("#find")
				.click(
						function() {
							if ($("#outboundDate").val() == ""
									|| $("#to").find(":selected").val() == ""
									|| $("#from").find(":selected").val() == "") {
								alert("請輸入資料");
								return false;
							}
							if ($('input[name="structure"]:checked').val() == "RoundTrip") {
								if ($("#returnDate").val() == "") {
									alert("請輸入資料");
									return false;
								}
								if ($("#returnDate").val() < $("#outboundDate")
										.val()) {
									alert("回程需大於去程日");
									return false;
								}
							}

							search($("#outboundDate").val(), $("#returnDate")
									.val(), $("#from").find(":selected").val(),
									$("#to").find(":selected").val(), $(
											'input[name="structure"]:checked')
											.val(), $("#from")
											.find(":selected").text(), $("#to")
											.find(":selected").text());

						});

		$('input[type=radio][name=structure]').change(function() {

			if (this.value != "RoundTrip") {
				$("#returnDate").attr("disabled", "disabled");
				$("#returnDate").val("");
			} else {
				$("#returnDate").removeAttr("disabled");
			}

		});

		$("#back").click(function() {
			$('#moneyFind').show();
			$('#money').hide();
			$('#chart').hide();
			stat = "0";
		});

	});
</script>
</head>
<body>

	<!-- Navigation -->
	<nav class="navbar navbar-default navbar-fixed-top topnav"
		role="navigation">
		<div class="container topnav">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target="#bs-example-navbar-collapse-1">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<!--	<a class="navbar-brand topnav" href="#">Start Bootstrap</a> -->
			</div>
			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav navbar-right">
					<li><a id="back">查詢票價</a></li>


				</ul>
			</div>
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container -->
	</nav>


	<!-- Header -->
	<a name="about"></a>
	<div class="intro-header" id="moneyFind">
		<div class="container">

			<div class="row">
				<div class="col-lg-12">
					<div class="intro-message">
						<h3>LCC機票比價</h3>
						<div class="panel-group">
							<div class="panel panel-transparent">

								<div class="panel-body">
									<div class="controls form-inline">
										從 <select id="from" name="from" class="form-control" code="">
											<jsp:include page="countryList.jsp" /></select>到 <select id="to"
											name="to" class="form-control" code="">
											<jsp:include page="countryList.jsp" /></select> 出發日
										<div class='input-group date'>
											<input type='text' class="form-control" size="10"
												id='outboundDate' /> <span class="input-group-addon">
												<span class="glyphicon glyphicon-calendar"></span>
											</span>
										</div>
										回程日
										<div class='input-group date'>
											<input type='text' class="form-control" size="10"
												id='returnDate' /> <span class="input-group-addon"> <span
												class="glyphicon glyphicon-calendar"></span>
											</span>
										</div>
									</div>

									<label class="radio-inline"><input type="radio"
										name="structure" checked value="RoundTrip">來回</label> <label
										class="radio-inline"><input type="radio"
										name="structure" value="OneWay">單程</label>

									<hr class="intro-divider">
									<ul class="list-inline intro-social-buttons">
										<li><a class="btn btn-default btn-lg" id="find"><span
												class="network-name"><b>查詢</b></span></a></li>
										<li><a onClick="$('input').val('');$('select').val('');"
											class="btn btn-default btn-lg"><span class="network-name"><b>清除</b></span></a></li>

									</ul>


								</div>

							</div>

						</div>

					</div>




				</div>
			</div>
		</div>

	</div>
	<!-- /.container -->

	</div>
	<!-- /.intro-header -->


	<!-- /.content-section-a -->
	<a name="chart"></a>
	<div class="content-section-a" hidden id="chart">

		<div class="container">
			<div class="row">
				<div style="text-align: right; margin-top: 2cm;">
					<ul class="list-inline intro-social-buttons">
						<li><a onClick="browserCal()" class="btn btn-default btn-lg"><span
								class="network-name"><b>日歷模式</b></span></a></li>


					</ul>
				</div>

				<div style="text-align: center">
					<ul class="list-inline intro-social-buttons">
						<li><div id="beforeMonth">
								<a onClick="beforeMonth(0)" class="btn btn-default btn-lg"><span
									class="network-name"><b>上個月</b></span></a>
							</div></li>

						<li><div id="before">
								<a onClick="beforeChart()" class="btn btn-default btn-lg"><span
									class="network-name"><b>上一頁(前15日)</b></span></a>
							</div></li>
						<li><div id="next">
								<a onClick="nextChart()" class="btn btn-default btn-lg"><span
									class="network-name"><b>下一頁(後15日)</b></span></a>
							</div></li>
						<li><div id="nextMonth">
								<a onClick="nextMonth(0)" class="btn btn-default btn-lg"><span
									class="network-name"><b>下個月</b></span></a>
							</div></li>
					</ul>
				</div>
				<div id="container"
					style="min-width: 310px; height: 400px; margin: 0 auto"></div>
				<hr class="intro-divider">
				<div style="text-align: center">
					<ul class="list-inline intro-social-buttons">
						<li><div id="before1Month">
								<a onClick="beforeMonth(1)" class="btn btn-default btn-lg"><span
									class="network-name"><b>上個月</b></span></a>
							</div></li>
						<li><div id="before1">
								<a onClick="beforeReturnChart()" class="btn btn-default btn-lg"><span
									class="network-name"><b>上一頁(前15日)</b></span></a>
							</div></li>
						<li><div id="next1">
								<a onClick="nextReturnChart()" class="btn btn-default btn-lg"><span
									class="network-name"><b>下一頁(後15日)</b></span></a>
							</div></li>
							<li><div id="next1Month">
								<a onClick="nextMonth(1)" class="btn btn-default btn-lg"><span
									class="network-name"><b>下個月</b></span></a>
							</div></li>

					</ul>
				</div>
				<div id="container1"
					style="min-width: 310px; height: 400px; margin: 0 auto"></div>
			</div>

		</div>
		<!-- /.container -->

	</div>

		<!-- Page Content -->

	<a name="services"></a>
	<div class="content-section-a" hidden id="money">

		<div class="container">
			<div class="row">
				<div style="text-align: right; margin-top: 2cm;">
					<ul class="list-inline intro-social-buttons">
						<li><a onClick="browserChart()"
							class="btn btn-default btn-lg"><span class="network-name"><b>柱狀圖模式</b></span></a></li>


					</ul>
				</div>

				<h3>
					<div id='name'></div>
				</h3>
				<div id='calendar'></div>
				<hr class="intro-divider">
				<h3>
					<div id='name1'></div>
				</h3>
				<div id='calendar1'></div>
			</div>

		</div>
		<!-- /.container -->

	</div>

	<div class="content-section-a navbar-fixed-bottom" id="selectTicket" hidden>

		<div class="container">
			<div class="row">
				<div class="panel-group">
					<div class="panel-body">
						<div class="controls form-inline">

							出發地
					
								<input type='text' class="form-control"  id='' />
					
							目的地
						
								<input type='text' class="form-control"  id='' />
						
							<ul class="list-inline intro-social-buttons">
							<li><a class="btn btn-default btn-lg" id=""><span
									class="network-name"><b>開啟訂票網站</b></span></a></li>


						</ul>
						</div>


					</div>
				</div>

			</div>
		</div>
	</div>



	<!-- /.banner -->

	<!-- Footer -->
	<footer>
		<div class="container">
			<div class="row">
				<div class="col-lg-12">
					<ul class="list-inline">
						<li><a href="#">Home</a></li>
						<li class="footer-menu-divider">&sdot;</li>
						<li><a href="#about">About</a></li>
						<li class="footer-menu-divider">&sdot;</li>
						<li><a href="#services">Services</a></li>
						<li class="footer-menu-divider">&sdot;</li>
						<li><a href="#contact">Contact</a></li>
					</ul>
					<p class="copyright text-muted small">Copyright &copy; Your
						Company 2014. All Rights Reserved</p>
				</div>
			</div>
		</div>
	</footer>



	<div class="modal">
		<!-- Place at bottom of page -->
	</div>

</body>
</html>