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
            width: 200px;
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
            border: none;
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
    <fmt:message bundle="${loc}" key="local.popular" var="popular"/>
    <fmt:message bundle="${loc}" key="local.booksToReturn" var="bookstoreturn"/>
    <fmt:message bundle="${loc}" key="local.myOrders" var="orders"/>
    <fmt:message bundle="${loc}" key="local.changePassword" var="changepass"/>
    <fmt:message bundle="${loc}" key="local.oldPassword" var="oldpass"/>
    <fmt:message bundle="${loc}" key="local.newPassword" var="newpass"/>
    <fmt:message bundle="${loc}" key="local.confirmPassword" var="confirmpass"/>
    <fmt:message bundle="${loc}" key="local.orderHistory" var="orderhistory"/>
</head>
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
        </tr>
    </table>
</header>

<a href="Controller?command=showreaderorders" class="floating-button-body">
    <c:out value="${orders}" /></a>
    <br/>
<a href="Controller?command=showbookstoreturn" class="floating-button-body">
    <c:out value="${bookstoreturn}" /></a>
<br />
<a href="Controller?command=showreaderhistory" class="floating-button-body" >
    <c:out value="${orderhistory}" />
</a>
<br/>
<button onclick="myFunction()" class="floating-button-body">${changepass}</button>

<div id="myDIV" style="display: none">
    <form action="Controller" method="post">
        <table>
            <tr>
                <td>
                    <input type="hidden" name="command" value="changepassword" />
                    <c:out value="${oldpass}" />
                </td>
                <td>
                    <input required minlength="3" type="password" name="oldpassword" />
                </td>
            </tr>
            <tr>
                <td>
                    <c:out value="${newpass}" />
                </td>
                <td>
                    <input required minlength="3" maxlength="50" type="password" name="newpassword" id="password1"/>
                </td>
            </tr>
            <tr>
                <td>
                    <c:out value="${confirmpass}" />
                </td>
                <td>
                    <input required minlength="3" maxlength="50" type="password" name="newpassword1" id="password2"/>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="submit" value="${changepass}" style="margin-left: 50px"/>
                </td>
            </tr>
        </table>
    </form>
</div>
<script>
    function myFunction() {
        var x = document.getElementById("myDIV");
        if (x.style.display === "none") {
            x.style.display = "block";
        } else {
            x.style.display = "none";
        }
    }
</script>
<script type="text/javascript">
    window.onload = function () {
        document.getElementById("password1").onchange = validatePassword;
        document.getElementById("password2").onchange = validatePassword;
    }
    function validatePassword(){
        var pass2=document.getElementById("password2").value;
        var pass1=document.getElementById("password1").value;
        if(pass1!=pass2)
            document.getElementById("password2").setCustomValidity("Passwords Don't Match");
        else
            document.getElementById("password2").setCustomValidity('');
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
