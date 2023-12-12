<%--
  Created by IntelliJ IDEA.
  User: shand
  Date: 12/5/2023
  Time: 3:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="contentType.jsp" />
<jsp:include page="navigation.jsp" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update Story</title>
    <link href="css/styles.css" rel="stylesheet" />

</head>
<body>

<h2>Update Story</h2>

<form action="updateStory" method="post">
    <input type="hidden" name="storyId" value="${existingStory.storyId}">

    <label for="title">Title:</label>
    <input type="text" id="title" name="title" value="${existingStory.title}" required>
    <br>

    <label for="content">Content:</label>
    <textarea id="content" name="content" rows="4"  required>${existingStory.content}</textarea>
    <br>

    <input type="submit" value="Update Story">
    <a href="index.jsp">Go back to Home</a>
</form>

</body>
</html>

