<%@ page language="java" contentType="text/html;
    charset=utf-8"
         pageEncoding="utf-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
    <head>
        <meta charset="utf-8">
        <title>${requestScope.booktoshow.bookName}</title>

        <style>
            header{
                height: 60px;
                background-color: lightgray;
                vertical-align: center;
            }
            h1{
                size: 18px;
                color: gray;
                margin: 20px;
                text-align: center;
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
                border-radius: 45px;
                margin: 10px 24%;
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
                border: none;
            }
            .floating-button:hover {
                background: #2EE59D;
                box-shadow: 0 15px 20px rgba(46, 229, 157, .4);
                color: white;
                transform: translateY(-7px);
            }
            .floating-button-head {
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
            .floating-button-head:hover {
                background: #2EE59D;
                box-shadow: 0 5px 10px rgba(46, 229, 157, .4);
                color: white;
                transform: translateY(+5px);
            }

            .found-books{
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
            .floating-button-change{
                border: none;
                text-decoration: none;
                display: inline-block;
                width: 140px;
                height: 45px;
                line-height: 45px;
                margin: 10px;
                margin-left: 360px;
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
                width: 140px;
                height: 45px;
                line-height: 45px;
                margin: 10px 20px;
                margin-left: 30px;
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

            .closeChange, .closeDelete, .closeAuthMessage, .closeMessage {
                color: white;
                float: right;
                font-size: 28px;
                font-weight: bold;
            }

            .closeChange, .closeDelete, .closeAuthMessage,.closeMessage:hover,
            .closeChange, .closeDelete, .closeAuthMessage,.closeMessage:focus {
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

            input[type="text"]{
                white-space: normal;
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
        <fmt:message bundle="${loc}" key="local.signin" var="signin"/>
        <fmt:message bundle="${loc}" key="local.signup" var="signup"/>
        <fmt:message bundle="${loc}" key="local.author" var="author"/>
        <fmt:message bundle="${loc}" key="local.releaseYear" var="year"/>
        <fmt:message bundle="${loc}" key="local.orderBook" var="order"/>
        <fmt:message bundle="${loc}" key="local.description" var="description"/>
        <fmt:message bundle="${loc}" key="local.popular" var="popular"/>
        <fmt:message bundle="${loc}" key="local.signInToOrder" var="signInToOrder"/>
        <fmt:message bundle="${loc}" key="local.name" var="name"/>
        <fmt:message bundle="${loc}" key="local.authorName" var="authorName"/>
        <fmt:message bundle="${loc}" key="local.authorSurname" var="authorSurname"/>
        <fmt:message bundle="${loc}" key="local.yes" var="yes"/>
        <fmt:message bundle="${loc}" key="local.no" var="no"/>
        <fmt:message bundle="${loc}" key="local.delete" var="delete"/>
        <fmt:message bundle="${loc}" key="local.change" var="change"/>
        <fmt:message bundle="${loc}" key="local.confirmChanges" var="confirmChanges"/>
        <fmt:message bundle="${loc}" key="local.confirmDelete" var="confirmDelete"/>
        <fmt:message bundle="${loc}" key="local.selectGenre" var="selectGenres"/>
        <fmt:message bundle="${loc}" key="local.genres" var="genres"/>
    </head>
    <body>
    <header>
        <table border="0" align="left">
            <tr>
                <td>
                    <a href="Controller?command=gotoindexpage"><img src="css/img/home.png" class="home-page"/></a>
                </td>
                <c:if test="${sessionScope.auth == null or sessionScope.user.userRole == \"READER\"}">
                    <td>
                        <a href="Controller?command=showpopular" class="floating-button-head" >${popular}</a>
                    </td>
                </c:if>
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
                <c:choose>
                <c:when test="${sessionScope.auth == null}">
                <td>
                    <form action="Controller" method="post">
                        <input type="hidden" name="command" value="signin" />
                        <input type="submit" value="${signin}" class="floating-button-head" style="border: 0"/>
                    </form>
                </td>
                </c:when>
                <c:when test="${sessionScope.auth == false}">
                <td>
                    <form action="Controller" method="post">
                        <input type="hidden" name="command" value="signin" />
                        <input type="submit" value="${signin}" class="floating-button-head" style="border: 0"/>
                    </form>
                </td>
                </c:when>
                <c:when test="${sessionScope.user.userRole == \"LIBRARIAN\"}">
                <td>
                    <form action="Controller" method="post">
                        <input type="hidden" name="command" value="logout"/>
                        <input type="submit" value="${logout}" class="floating-button-head" style="border: 0"/>
                    </form>
                </td>
                </c:when>
                <c:otherwise>
                <td>
                    <form action="Controller" method="post">
                        <input type="hidden" name="command" value="gotoreaderaccount" />
                        <input type="submit" value="${account}" class="floating-button-head" style="border: 0"/>
                    </form>
                </td>
                <td>
                    <form action="Controller" method="post">
                        <input type="hidden" name="command" value="logout"/>
                        <input type="submit" value="${logout}" class="floating-button-head" style="border: 0"/>
                    </form>
                </td>
                </c:otherwise>
                </c:choose>

        </table>
    </header>

    <h1>${requestScope.booktoshow.bookName}</h1>
    <table width="900" border="1" align="center" class="found-books">
        <tr>
            <td rowspan="4" style="width: 256px">
                <img src="css/img/${requestScope.booktoshow.picture}" width="256">
            </td>
            <td width="100">
                <p>${author}</p>
            </td>
            <td>
                <p>${requestScope.booktoshow.authorName} ${requestScope.booktoshow.authorSurname}</p>
            </td>
        </tr>
        <tr>
            <td>
                <p>${year}</p>
            </td>
            <td>
                <p>${requestScope.booktoshow.releaseYear}</p>
            </td>
        </tr>
        <tr>
            <td>
                <p>${genres}</p>
            </td>
            <td>
                <c:if test="${requestScope.booktoshow.genres.size() == 0}">
                    <c:out value="-" />
                </c:if>
                <c:forEach var="n" items="${requestScope.booktoshow.genres}">
                    <c:out value="${n} " />
                </c:forEach>
            </td>
        </tr>
        <tr>
            <td>
                <p>${description}</p>
            </td>
            <td>
                <p>${requestScope.booktoshow.description}</p>
            </td>
        </tr>
    </table>

        <br />

    <br />

    <c:if test="${sessionScope.user.userRole != \"LIBRARIAN\"}">
        <a href="Controller?command=registrateorder&bookid=${requestScope.booktoshow.id}" onclick="checkAuth()"
           class="floating-button" > ${order}</a>
    </c:if>
    <c:if test="${sessionScope.user.userRole == \"LIBRARIAN\"}">
        <button type="button" id="changeBtn" class="floating-button-change">${change}</button>
        <button type="button" id="deleteBtn" class="floating-button-delete">${delete}</button>
    </c:if>

    <div id="deleteModal" class="modal">
        <!-- Modal content -->
        <div class="modal-content">
            <div class="modal-header">
                <span class="closeDelete">&times;</span>
                <h2>${delete}</h2>
            </div>
            <div class="modal-body">
                <p style="font-size: 15px; font-weight: bolder">${confirmDelete}</p>
                <form action="Controller" method="post">
                    <input type="hidden" name="command" value="deletebook" />
                    <input type="hidden" name="bookid" value="${requestScope.booktoshow.id}" />
                    <input type="hidden" name="picture" value="${requestScope.booktoshow.picture}" />
                    <input type="hidden" name="bookname" value="${requestScope.booktoshow.bookName}" />
                    <input type="hidden" name="authorname" value="${requestScope.booktoshow.authorName}" />
                    <input type="hidden" name="authorsurname" value="${requestScope.booktoshow.authorSurname}" />
                    <input type="hidden" name="releaseyear" value="${requestScope.booktoshow.releaseYear}" />
                    <input type="hidden" name="description" value="${requestScope.booktoshow.description}" />

                    <input type="submit" value="${yes}" class="floating-button-delete"/>
                    <button type="button" class="floating-button-change" id="declineDelete">${no}</button>
                </form>
            </div>
        </div>
    </div>

    <div id="changeModal" class="modal">
        <!-- Modal content -->
        <div class="modal-content">
            <div class="modal-header">
                <span class="closeChange">&times;</span>
                <h2>${change}</h2>
            </div>
            <div class="modal-body">
                <form action="Controller" method="post">
                    <table>
                        <tr>
                            <td style="width: 120px">
                                <input type="hidden" name="command" value="changebook" />
                                <input type="hidden" name="bookid" value="${requestScope.booktoshow.id}" />
                                <input type="hidden" name="picture" value="${requestScope.booktoshow.picture}" />
                                <c:out value="${name}" />
                            </td>
                            <td >
                                <input type="hidden" name="bookname" value="${requestScope.booktoshow.bookName}" />
                                <input type="text" name="newbookname" value="${requestScope.booktoshow.bookName}" style="width: 650px;"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <c:out value="${authorName}" />
                            </td>
                            <td>
                                <input type="hidden" name="authorname" value="${requestScope.booktoshow.authorName}" />
                                <input type="text" name="newauthorname" value="${requestScope.booktoshow.authorName}" style="width: 650px;"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <c:out value="${authorSurname}" />
                            </td>
                            <td>
                                <input type="hidden" name="authorsurname" value="${requestScope.booktoshow.authorSurname}" />
                                <input type="text" name="newauthorsurname" value="${requestScope.booktoshow.authorSurname}" style="width: 650px;"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <c:out value="${year}" />
                            </td>
                            <td>
                                <input type="hidden" name="releaseyear" value="${requestScope.booktoshow.releaseYear}" />
                                <input type="text" name="newreleaseyear" value="${requestScope.booktoshow.releaseYear}" style="width: 650px;"/>
                            </td>
                        </tr>
                        <tr>
                            <td><c:out value="${genres}" /></td>
                            <td>
                                <c:forEach var="n" items="${requestScope.genres}">
                                    <c:if test="${requestScope.booktoshow.genres.contains(n)}">
                                        <input type="checkbox" checked="checked" name="${n}" value="${n}" />
                                    </c:if>
                                    <c:if test="${!requestScope.booktoshow.genres.contains(n)}">
                                        <input type="checkbox" name="${n}" value="${n}" />
                                    </c:if>
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
                                <input type="hidden" name="description" value="${requestScope.booktoshow.description}" />
                                <input type="text" name="newdescription"
                                       value="${requestScope.booktoshow.description}"
                                       style="height: 100px; width: 650px;"/>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" align="center">
                                <br/>
                                <input type="submit" value="${confirmChanges}">
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
    </div>

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
            //alert('${sessionScope.message}');

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

    <script>
        var deleteModal = document.getElementById("deleteModal");
        var changeModal = document.getElementById("changeModal");

        var deleteBtn = document.getElementById("deleteBtn");
        var changeBtn = document.getElementById("changeBtn");
        var declineDelete = document.getElementById("declineDelete");

        var spanDelete = document.getElementsByClassName("closeDelete")[0];
        var spanChange = document.getElementsByClassName("closeChange")[0];

        deleteBtn.onclick = function() {
            deleteModal.style.display = "block";
        }
        changeBtn.onclick = function() {
            changeModal.style.display = "block";
        }

        declineDelete.onclick = function() {
            deleteModal.style.display = "none";
        }
        // When the user clicks on <span> (x), close the modal
        spanDelete.onclick = function() {
            deleteModal.style.display = "none";
        }
        spanChange.onclick = function() {
            changeModal.style.display = "none";
        }

        window.onclick = function(event) {
            if (event.target == deleteModal) {
                deleteModal.style.display = "none";
            }
        }
        window.onclick = function(event) {
            if (event.target == changeModal) {
                changeModal.style.display = "none";
            }
        }
    </script>

    <div id="authBeforeOrderModal" class="modal">
        <!-- Modal content -->
        <div class="modal-content">
            <div class="modal-header">
                <span class="closeAuthMessage">&times;</span>
                <h2>Message</h2>
            </div>
            <div class="modal-body">
                <p style="font-size: 15px; font-weight: bolder">${signInToOrder}</p>
                <a href="Controller?command=signin"><button type="button" class="floating-button-change" id="closeMessage">${signin}</button></a>
                <button type="button" class="floating-button-change" id="declineAuth">${no}</button>
            </div>
        </div>
    </div>

    <script>
        function checkAuth(){
            if(${sessionScope.user == null}){
                event.preventDefault();

                var messageModal = document.getElementById("authBeforeOrderModal");
                var closeMessage = document.getElementById("closeMessage");
                var declineAuth = document.getElementById("declineAuth");
                var span = document.getElementsByClassName("closeAuthMessage")[0];

                messageModal.style.display = "block";

                closeMessage.onclick = function (){
                    messageModal.style.display = "none";
                }
                declineAuth.onclick = function (){
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
            }
        }
    </script>
    </body>
</html>
