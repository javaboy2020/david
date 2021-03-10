<%--
  Created by IntelliJ IDEA.
  User: 80468
  Date: 2021/1/7
  Time: 15:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>主页</title>
</head>
<body>
    <h1>Hello,${userSession.userName}!</h1>
    <hr>
    <a href="/user/logout">注销</a>
</body>
</html>
