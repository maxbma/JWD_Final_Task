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
            width: 240px;
            height: 45px;
            line-height: 45px;
            border-radius: 45px;
            position: absolute;
            left: 200px;
            top: 100px;
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
        .found-books{
            position: absolute;
            left: 200px;
            top: 160px;
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
    <fmt:message bundle="${loc}" key="local.book" var="book"/>
    <fmt:message bundle="${loc}" key="local.author" var="author"/>
    <fmt:message bundle="${loc}" key="local.operStatus" var="status"/>
    <fmt:message bundle="${loc}" key="local.confirm" var="confirm"/>
    <fmt:message bundle="${loc}" key="local.account" var="account"/>
    <fmt:message bundle="${loc}" key="local.returnDate" var="date"/>
    <fmt:message bundle="${loc}" key="local.previousPage" var="previous"/>
    <fmt:message bundle="${loc}" key="local.popular" var="popular"/>
    <fmt:message bundle="${loc}" key="local.noOrders" var="noOrders"/>
</head>
<header>
    <c:if test="${sessionScope.user.userRole == \"READER\" || null == sessionScope.auth}">
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
    </c:if>
    <c:if test="${sessionScope.user.userRole == \"LIBRARIAN\"}">
        <a href="Controller?command=gotoindexpage">
            <img src="css/img/home.png" class="home-page" style="position: absolute;top: 10px;left: 10px;"/></a>
    </c:if>
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
            <c:if test="${sessionScope.user.userRole == \"READER\"}" >
            <td>
                <form action="Controller" method="post">
                    <input type="hidden" name="command" value="gotoreaderaccount" />
                    <input type="submit" value="${account}" class="floating-button" style="border: 0"/>
                </form>
            </td>
            </c:if>

            <td>
                <form action="Controller" method="post">
                    <input type="hidden" name="command" value="logout"/>
                    <input type="submit" value="${logout}" class="floating-button" style="border: 0"/>
                </form>
            </td>
    </table>
</header>

<c:if test="${requestScope.history.size() > 0}">
    <table border="1" class="found-books">
        <tr align="center">
            <th>${book}</th>
            <th>${date}</th>
        </tr>
        <c:forEach var="n" items="${requestScope.history}">
            <tr>
                <td>
                    <c:out value="${n.bookName}" />
                </td>
                <td>
                    <c:out value="${n.returnDate}" />
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<c:if test="${requestScope.history.size() == 0 }">
<table border="1" class="found-books" width="300">
    <caption>${noOrders}</caption>
</table>
</c:if>

<br/>
<c:if test="${sessionScope.user.userRole == \"READER\"}">
    <a href="Controller?command=gotoreaderaccount" class="floating-button-body">${previous}</a>
</c:if>
<c:if test="${sessionScope.user.userRole == \"LIBRARIAN\"}">
    <a href="Controller?command=gotoindexpage" class="floating-button-body">${previous}</a>
</c:if>
</body>
</html>
