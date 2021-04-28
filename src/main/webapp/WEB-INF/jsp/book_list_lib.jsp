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

        a{
            text-decoration: underline;
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
            left: 200px;
            top: 100px;
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
        .floating-button-change{
            border: none;
            text-decoration: none;
            display: inline-block;
            width: 100px;
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
        .floating-button-delete{
            border: none;
            text-decoration: none;
            display: inline-block;
            width: 100px;
            height: 45px;
            line-height: 45px;
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
        .floating-button-delete:hover {
            background: #ff0000;
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

        .close {
            color: white;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }

        .close:hover,
        .close:focus {
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
    <fmt:message bundle="${loc}" key="local.name" var="name"/>
    <fmt:message bundle="${loc}" key="local.authorName" var="authorName"/>
    <fmt:message bundle="${loc}" key="local.authorSurname" var="authorSurname"/>
<body>
<header>
    <a href="Controller?command=gotoindexpage">
        <img src="css/img/home.png" class="home-page" style="position: absolute;top: 10px;left: 10px;"/></a>
    <table border="0" align="right">
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
            <td>
                <form action="Controller" method="post">
                    <input type="hidden" name="command" value="logout"/>
                    <input type="submit" value="${logout}" class="floating-button" style="border: 0"/>
                </form>
            </td>
    </table>
</header>
<table class="found-books">
    <th></th>
    <th width="500">${name}</th>
    <th width="150">${authorName}</th>
    <th width="150">${authorSurname}</th>
    <c:forEach var="n" items="${requestScope.books}">
        <tr>
            <td>
                <img src="css/img/${n.picture}" width="40" >
            </td>
            <td>
                <a id="bookName" href="Controller?command=gotobookpage&bookid=${n.id}">
                    <c:out value="${n.bookName}" /></a>
            </td>
            <td>
                <c:out value="${n.authorName}" />
            </td>
            <td>
                <c:out value="${n.authorSurname}" />
            </td>
        </tr>
    </c:forEach>
</table>

<div id="actionModal" class="modal">
    <!-- Modal content -->
    <div class="modal-content">
        <div class="modal-header">
            <span class="close">&times;</span>
            <h2>Modal Header</h2>
        </div>
        <div class="modal-body">
            <button type="button" id="change" class="floating-button-change">Change</button>
            <button type="button" id="deleteBtn" class="floating-button-delete">Delete</button>
        </div>
    </div>
</div>

<div id="deleteModal" class="modal">
    <!-- Modal content -->
    <div class="modal-content">
        <div class="modal-header">
            <span class="close">&times;</span>
            <h2>Modal Header</h2>
        </div>
        <div class="modal-body">
            <p style="font-size: 15px; font-weight: bolder">Are you sure you want to delete this book?</p>
            <a class="floating-button-delete" href="Controller?command=deletebook">Yes</a>
            <button class="floating-button-change" id="declineDelete">No</button>
        </div>
    </div>
</div>

<script>
    // Get the modal
    var modal = document.getElementById("deleteModal");
    var actionModal = document.getElementById("actionModal");
    // Get the button that opens the modal
    var btn = document.getElementById("deleteBtn");
    var bookName = document.getElementById("bookName");
    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];
    var declineDelete = document.getElementById("declineDelete");
    // When the user clicks the button, open the modal
    function displayBlock(){
        actionModal.style.display = "block";
    }
    btn.onclick = function() {
        modal.style.display = "block";
    }
    declineDelete.onclick = function() {
        modal.style.display = "none";
    }
    // When the user clicks on <span> (x), close the modal
    span.onclick = function() {
        modal.style.display = "none";
    }
    span.onclick = function() {
        actionModal.style.display = "none";
    }
    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
    window.onclick = function(event) {
        if (event.target == modal) {
            actionModal.style.display = "none";
        }
    }
</script>
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
</body>
</html>
