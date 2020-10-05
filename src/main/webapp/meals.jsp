<%--
  Created by IntelliJ IDEA.
  User: Anton
  Date: 05.10.2020
  Time: 20:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time"%>

<html>
<head>
    <title>Meals</title>
</head>
<body>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h2>Meals</h2>
    <table border="1" cellspacing="0">
        <thead style="font-weight: bold">
            <td>Date</td>
            <td>Description</td>
            <td>Calories</td>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <c:choose>
                <c:when test="${meal.excess == true}">
                    <tr style="color: red">
                </c:when>
                <c:otherwise>
                    <tr style="color: green">
                </c:otherwise>
            </c:choose>
                <td><javatime:format value="${meal.dateTime}" pattern="yyyy-MM-dd hh:mm"/></td>
                <td><c:out value="${meal.description}"/></td>
                <td><c:out value="${meal.calories}"/></td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
