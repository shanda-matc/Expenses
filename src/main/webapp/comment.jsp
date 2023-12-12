<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Comments</title>
</head>
<body>

<c:if test="${not empty requestScope.commentSuccessMessage}">
    <p>${requestScope.commentSuccessMessage}</p>
</c:if>

<%-- Display existing comments
<c:forEach var="comment" items="${comments}">
    <p>${comment.user.username} said: ${comment.content} on ${comment.creationDate}</p>
</c:forEach>
--%>

</body>
</html>
