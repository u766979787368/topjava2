<%--
  Created by IntelliJ IDEA.
  User: leoni
  Date: 17.06.2021
  Time: 22:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>
<form action="meals" method="post">
    <p>Дата <input    type="date" name="date" value="${date}"></p>
    <p>Время <input   type="time" name="time" value="${time}"></p>
    <p>Еда <input     type="text" name="description" value="${description}"></p>
    <p>Калории <input type="text" name="calories" value="${calories}"></p>
    <p><input type="hidden" name="id" value="${id}"></p>

    <p><input type="submit" value="Отправить"></p>
</form>
</body>
</html>
