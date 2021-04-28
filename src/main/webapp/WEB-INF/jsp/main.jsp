<%@ page language="java" contentType="text/html; 
    charset=utf-8" 
    pageEncoding="utf-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>Insert title here</title>

		<style>
			header{
				height: 60px;
				background-color: lightgray;
				vertical-align: center;
			}


			.home-page{
				width: 40px;
				margin: 5px 20px;
			}

			.floating-button {
				text-decoration: none;
				display: inline-block;
				width: 140px;
				height: 45px;
				line-height: 45px;
				font-family: 'Montserrat', sans-serif;
				font-size: 11px;
				text-transform: uppercase;
				text-align: center;
				letter-spacing: 3px;
				font-weight: 600;
				color: #524f4e;
				background: lightgray;
				box-shadow: 0 8px 5px rgba(0, 0, 0, .1);
				transition: .3s;
			}
			.floating-button:hover {
				background: #2EE59D;
				box-shadow: 0 5px 10px rgba(46, 229, 157, .4);
				color: white;
				transform: translateY(+5px);
			}

			.found-books{
				position: absolute;
				left: 450px;
				top: 200px;
				font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
				border-collapse: collapse;
				color: #686461;
			}

			.found-books caption {
				padding: 10px;
				color: white;
				background: #8fd4c1;
				font-size: 18px;
				text-align: left;
				font-weight: bold;
			}
			.found-books th {
				border-bottom: 3px solid #B9B29F;
				padding: 10px;
				text-align: left;
			}
			.found-books td {
				padding: 10px;
			}
			.found-books tr:nth-child(odd) {
				background: white;
			}
			.found-books tr:nth-child(even) {
				background: #E8E6D1;
			}
			.floating-button-reset {
				text-decoration: none;
				display: inline-block;
				width: 180px;
				height: 45px;
				line-height: 45px;
				border-radius: 45px;
				margin: 5px 10px;
				font-family: 'Montserrat', sans-serif;
				font-size: 11px;
				text-transform: uppercase;
				text-align: center;
				letter-spacing: 3px;
				font-weight: 600;
				color: #524f4e;
				background: white;
				box-shadow: 0 8px 15px rgba(0, 0, 0, .1);
				transition: .3s;
				outline: none;
			}
			.floating-button-reset:hover {
				background: #2EE59D;
				box-shadow: 0 15px 20px rgba(46, 229, 157, .4);
				color: white;
				transform: translateY(-7px);
			}
			.modal {
				display: none; /* Hidden by default */
				position: fixed; /* Stay in place */
				z-index: 1; /* Sit on top */
				padding-top: 100px; /* Location of the box */
				left: 0;
				top: 0;
				width: 100%; /* Full width */
				height: 100%; /* Full height */
				overflow: auto; /* Enable scroll if needed */
				background-color: rgb(0,0,0); /* Fallback color */
				background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
			}

			.modal-content {
				position: relative;
				background-color: #fefefe;
				margin: auto;
				padding: 0;
				border: 1px solid #888;
				width: 60%;
				box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2),0 6px 20px 0 rgba(0,0,0,0.19);
			}

			.closeMessage {
				color: white;
				float: right;
				font-size: 28px;
				font-weight: bold;
			}

			.closeMessage:hover,
			.closeMessage:focus {
				color: #000;
				text-decoration: none;
				cursor: pointer;
			}

			.modal-header {
				padding: 2px 16px;
				background-color: #5cb85c;
				color: white;
			}

			.modal-body {padding: 2px 16px;}

			.floating-button-change{
				border: none;
				text-decoration: none;
				display: inline-block;
				width: 140px;
				height: 45px;
				line-height: 45px;
				margin: 30px 10px;
				font-family: 'Montserrat', sans-serif;
				font-size: 11px;
				text-transform: uppercase;
				text-align: center;
				letter-spacing: 3px;
				font-weight: 600;
				color: #524f4e;
				background: white;
				box-shadow: 0 8px 15px rgba(0, 0, 0, .1);
				transition: .3s;
				outline: none;
			}
			.floating-button-change:hover {
				background: #2EE59D;
				box-shadow: 0 15px 20px rgba(46, 229, 157, .4);
				color: white;
				transform: translateY(-7px);
			}
			.change-lang{
				font-family: 'Montserrat', sans-serif;
				font-size: 17px;
				font-weight: 500;
				text-transform: uppercase;
			}
		</style>

		<fmt:setLocale value="${sessionScope.local}" />
		<fmt:setBundle basename="localisation.local" var="loc" />

		<fmt:message bundle="${loc}" key="local.message" var="message" />
		<fmt:message bundle="${loc}" key="local.locbutton.name.ru" var="ru_button"/>
		<fmt:message bundle="${loc}" key="local.locbutton.name.en" var="en_button"/>
		<fmt:message bundle="${loc}" key="local.logout" var="logout"/>
		<fmt:message bundle="${loc}" key="local.submit" var="submit" />
		<fmt:message bundle="${loc}" key="local.findBook" var="find" />
		<fmt:message bundle="${loc}" key="local.signin" var="signin"/>
		<fmt:message bundle="${loc}" key="local.signup" var="signup"/>
		<fmt:message bundle="${loc}" key="local.account" var="account"/>
		<fmt:message bundle="${loc}" key="local.selectGenre" var="selectgenre"/>
		<fmt:message bundle="${loc}" key="local.notSelected" var="notselected"/>
		<fmt:message bundle="${loc}" key="local.resetSearch" var="resetsearch"/>
		<fmt:message bundle="${loc}" key="local.popular" var="popular"/>
		<fmt:message bundle="${loc}" key="local.author" var="author"/>
		<fmt:message bundle="${loc}" key="local.foundBooks" var="foundBooks"/>
		<fmt:message bundle="${loc}" key="local.name" var="name"/>
		<fmt:message bundle="${loc}" key="local.bookOrAuthor" var="bookOrAuthor"/>
	</head>
	<body>
	<header>
		<table border="0" align="left">
			<tr>
				<td>
					<a href="Controller?command=gotoindexpage"><img src="css/img/home.png" class="home-page"/></a>
				</td>
				<td>
					<a href="Controller?command=showpopular" class="floating-button" >${popular}</a>
				</td>
			</tr>
		</table>

		<table border="0" align="right" >
			<tr height="45">
				<td align="right" class="change-lang">
					<c:out value="${message}" />
				</td>
				<td>
					<a href="Controller?command=changelocalru">
						<img src="css/img/ru.jpg" width="40" height="30"> </a>
				</td>
				<td>
					<a href="Controller?command=changelocalen">
						<img src="css/img/en.jpg" width="40" height="30"></a>
				</td>
				<c:choose>
				<c:when test="${sessionScope.auth == null}">
				<td>
					<form action="Controller" method="post">
						<input type="hidden" name="command" value="signin" />
						<input type="submit" value="${signin}" class="floating-button" style="border: 0"/>
					</form>
				</td>
				</c:when>
				<c:when test="${sessionScope.auth == false}">
				<td>
					<form action="Controller" method="post">
						<input type="hidden" name="command" value="signin" />
						<input type="submit" value="${signin}" class="floating-button" style="border: 0"/>
					</form>
				</td>
				</c:when>
				<c:otherwise>
				<td>
					<form action="Controller" method="post">
						<input type="hidden" name="command" value="gotoreaderaccount" />
						<input type="submit" value="${account}" class="floating-button" style="border: 0"/>
					</form>
				</td>
				<td>
					<form action="Controller" method="post">
						<input type="hidden" name="command" value="logout"/>
						<input type="submit" value="${logout}" class="floating-button" style="border: 0"/>
					</form>
				</td>
				</c:otherwise>
				</c:choose>

		</table>
	</header>

		<h1 align="center">!!! Welcome !!!</h1>

		<br/>

		<p style="font-weight: bolder; margin: 5px 20px;font-size: 20px"><c:out value="${find} " /></p>
		<form action="Controller" method="post">
			<table class="found-books" style="left: 10px;">
				<tr>
					<td>
						<label for="selectedgenre">${selectgenre} </label>
					</td>
					<td>
						<select name="selectedgenre" id="selectedgenre" style="width: 187px">
							<option disabled>${selectgenre}</option>
							<option selected value="not selected">${notselected}</option>
							<c:forEach var="n" items="${requestScope.genres}">
								<option value="${n}">${n}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						<input type="hidden" name="command" value="findbookbyname" />
						<c:out value="${bookOrAuthor}" />
					</td>
					<td>
						<input type="text" name="nameToFind" autocomplete="off" value="" style="width: 180px"/>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<input type="submit" value="${submit}" class="floating-button-reset" style="margin: 10px 0px"/><br />
					</td>
				</tr>
			</table>
		</form>

	<div id="messageModal" class="modal">
		<!-- Modal content -->
		<div class="modal-content">
			<div class="modal-header">
				<span class="closeMessage">&times;</span>
				<h2>Message</h2>
			</div>
			<div class="modal-body">
				<p style="font-size: 15px; font-weight: bolder">${sessionScope.message}</p>
				<button type="button" class="floating-button-change" id="okMessage">OK</button>
			</div>
		</div>
	</div>

	<c:if test="${sessionScope.message != null}">
		<script>

			var messageModal = document.getElementById("messageModal");
			var closeMessage = document.getElementById("okMessage");
			var span = document.getElementsByClassName("closeMessage");

			messageModal.style.display = "block";

			closeMessage.onclick = function (){
				messageModal.style.display = "none";
			}
			span.onclick = function (){
				messageModal.style.display = "none";
			}
			window.onclick = function(event) {
				if (event.target == messageModal) {
					messageModal.style.display = "none";
				}
			}
		</script>
		${sessionScope.remove("message")}
	</c:if>

	<c:if test="${requestScope.foundBooks.size() > 0}">
		<form action="Controller" method="post" style="position: absolute; left: 450px; top: 140px">
			<input type="hidden" name="command" value="gotoindexpage" />
			<input type="submit" value="${resetsearch}" class="floating-button-reset"/>
		</form>
	</c:if>
	    <br />
	<c:if test="${requestScope.foundBooks.size() > 0}">
		<div>
			<table class="found-books">
				<caption>${foundBooks}</caption>
				<th></th>
				<th width="500">${name}</th>
				<th width="300">${author}</th>
				<c:forEach var="n" items="${requestScope.foundBooks}">
					<div>
						<tr>
							<td>
								<a href="Controller?command=gotobookpage&bookid=${n.id}" >
									<img src="css/img/${n.picture}" width="40" > </a>
							</td>
							<td>
								<a href="Controller?command=gotobookpage&bookid=${n.id}" ><c:out value="${n.bookName}" />  </a>
							</td>
							<td>
								<a href="Controller?command=findbookbyname&nameToFind=${n.authorName} ${n.authorSurname}" >
									<c:out value="${n.authorName}" /> <c:out value="${n.authorSurname}" /> </a>
							</td>
						</tr>
					</div>
				</c:forEach>
			</table>
		</div>
	</c:if>

	</body>
</html>