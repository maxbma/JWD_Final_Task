<%@ page language="java" contentType="text/html;
    charset=utf-8"
         pageEncoding="utf-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
        .floating-button-body {
            text-decoration: none;
            display: inline-block;
            width: 180px;
            height: 45px;
            line-height: 45px;
            border-radius: 45px;
            margin: 10px 24px;
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
        .floating-button-body:hover {
            background: #2EE59D;
            box-shadow: 0 15px 20px rgba(46, 229, 157, .4);
            color: white;
            transform: translateY(-7px);
            outline: none;
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
    <fmt:message bundle="${loc}" key="local.account" var="account"/>
    <fmt:message bundle="${loc}" key="local.showOrders" var="orders"/>
    <fmt:message bundle="${loc}" key="local.returnedBooks" var="returnedBooks"/>
    <fmt:message bundle="${loc}" key="local.addBook" var="addBook"/>
    <fmt:message bundle="${loc}" key="local.authorName" var="authorName"/>
    <fmt:message bundle="${loc}" key="local.authorSurname" var="authorSurname"/>
    <fmt:message bundle="${loc}" key="local.image" var="image"/>
    <fmt:message bundle="${loc}" key="local.bookList" var="bookList"/>
    <fmt:message bundle="${loc}" key="local.name" var="name"/>
    <fmt:message bundle="${loc}" key="local.description" var="description"/>
    <fmt:message bundle="${loc}" key="local.releaseYear" var="year"/>
    <fmt:message bundle="${loc}" key="local.genres" var="genres"/>
</head>
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
<br/>
<a href="Controller?command=showorderstolib" class="floating-button-body"> ${orders} </a>
<br/>
<a href="Controller?command=showreturnstolib" class="floating-button-body"> ${returnedBooks}</a>
<br/>
<a href="Controller?command=showbookstolib" class="floating-button-body"> ${bookList}</a>
<br/>
<button onclick="myFunction()" class="floating-button-body" style="border: white;">${addBook}</button>
<div id="addBook" style="display: none" >
    <form action="Controller" method="post" enctype="multipart/form-data">
        <table>
            <tr>
                <td style="width: 120px">
                    <input type="hidden" name="command" value="addnewbook" />
                    <c:out value="${name}" />
                </td>
                <td >
                    <input type="text" required name="bookname" style="width: 200px;"/>
                </td>
            </tr>
            <tr>
                <td>
                    <c:out value="${authorName}" />
                </td>
                <td>
                    <input type="text" required name="authorname" style="width: 200px;"/>
                </td>
            </tr>
            <tr>
                <td>
                    <c:out value="${authorSurname}" />
                </td>
                <td>
                    <input type="text" required name="authorsurname" style="width: 200px;"/>
                </td>
            </tr>
            <tr>
                <td>
                    <c:out value="${year}" />
                </td>
                <td>
                    <input type="number" required name="releaseyear" style="width: 200px"/>
                </td>
            </tr>
            <tr>
                <td><c:out value="${genres}" /></td>
                <td>
                    <c:forEach var="n" items="${requestScope.genres}">
                        <input type="checkbox" name="${n}" value="${n}" />
                        <c:out value="${n}"/>
                        <br/>
                    </c:forEach>
                </td>
            </tr>
            <tr>
                <td>
                    <c:out value="${description}" />
                </td>
                <td>
                    <input type="text" required name="description" style="height: 100px; width: 200px"/>
                </td>
            </tr>
            <tr>
                <td>
                    <c:out value="${image}" />
                </td>
                <td>
                    <input type="file" accept=".jpg" name="photo" />
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="submit" value="submit">
                </td>
            </tr>
        </table>
    </form>
</div>

<script>
    function myFunction() {
        var x = document.getElementById("addBook");
        if (x.style.display === "none") {
            x.style.display = "block";
        } else {
            x.style.display = "none";
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
