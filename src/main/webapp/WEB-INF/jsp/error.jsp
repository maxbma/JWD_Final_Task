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
    <fmt:message bundle="${loc}" key="local.account" var="account"/>
    <fmt:message bundle="${loc}" key="local.popular" var="popular"/>
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
<h1>OOPS... WE ENCOUNTED AN ERROR</h1>
</body>
</html>
