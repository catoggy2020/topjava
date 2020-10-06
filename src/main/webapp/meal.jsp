<%--
  Created by IntelliJ IDEA.
  User: Anton
  Date: 06.10.2020
  Time: 22:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>

<html>
<head>
    <title>Edit meal</title>
</head>
<body>
<h2>Edit meal</h2>
<form method="POST" action="meals?action=edit&id=<c:out value="${meal.id}"/>" name="formInsertOrEditMeal">
    <table>
        <tr>
            <td>
                DateTime :
            </td>
            <td>
                <input type="datetime-local" name="dateTime"
                       value="${meal.dateTime}"/>
            </td>
        </tr>
        <tr>
            <td>
                Description :
            </td>
            <td>
                <input type="text" name="description"
                       value="<c:out value="${meal.description}" />"/>
            </td>
        </tr>
        <tr>
            <td>
                Calories :
            </td>
            <td>
                <input type="text" name="calories"
                       value="<c:out value="${meal.calories}" />"/>
            </td>
        </tr>
    </table>

    <input type="submit" value="Save"/>
    <a href="meals?action=listMeals"><input type="button" value="Cancel"></a>
</form>
</body>
</html>
