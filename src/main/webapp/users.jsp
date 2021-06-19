<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html lang="ru">
<head>
    <title>Users</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Users</h2>
<form method="post" action="users">
    <label for="userId">Current User</label>
    <input type="hidden">
    <select id="userId" name="userId">
        <%--<jsp:useBean id="users" scope="request" type="java.util.List"/>--%>
        <c:forEach items="${users}" var="user">
            <jsp:useBean id="user" type="ru.javawebinar.topjava.to.UserTo"/>
            <option value="${user.id}">${user.name}</option>
        </c:forEach>
    </select>
    <input type="submit">
</form>
</body>
</html>