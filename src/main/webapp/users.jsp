<%@ page import="java.time.LocalDateTime" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Users</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Users</h2>
<%! private int accessCount = 0; %>
<h2>Количество обращений к странице с момента загрузки сервера: <%= ++accessCount %></h2>
<%! private final LocalDateTime date = LocalDateTime.now(); %>
<h2>Текущее время: <%= date %></h2>
<h2>Имя вашего хоста: <%= request.getRemoteHost() %></h2>
</body>
</html>