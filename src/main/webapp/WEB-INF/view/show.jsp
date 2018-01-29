<%--
  Created by IntelliJ IDEA.
  User: Lenovo
  Date: 2018/1/26
  Time: 13:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>SpringDemo</title>
</head>
<body>
    <h1>这是SpringMVC的Demo</h1>
    <h1>传回的值是：${spring}</h1>
<form action="/mydemo/login" method="post">
    <input type="text" name="username"/></br>
    <input type="text" name="password"/></br>
    <input type="submit" value="login"/>
</form>
</body>
</html>
