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
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Update Story</title>
    <link href="css/styles.css" rel="stylesheet" />

</head>
<body>

<h2>Update Story</h2>
<div class="col-lg-8 align-self-baseline">
    <form action="updateStory" method="post">
    <input type="hidden" name="storyId" value="${existingStory.storyId}">

        <div class="col-md-12">
            <label for="title">Title:</label>
            <input type="text" id="title" name="title" value="${existingStory.title}" required>
            <br>
        </div>

        <div class="col-md-12">
            <label for="content" class="form-label text-white">Content:</label>
            <textarea id="content" class="form-control" name="content" rows="5"  required>${existingStory.content}</textarea>
            <br>
        </div>

        <div class="col-md-12">
            <input type="submit" class="btn btn-primary" value="Update Story">
        </div>
            <a href="index.jsp">Go back to Home</a>
    </form>
</div>
</body>
</html>

