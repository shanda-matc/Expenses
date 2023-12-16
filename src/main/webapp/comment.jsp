<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Comments</title>
    <link href="css/custom.css" rel="stylesheet" />
    <link href="css/styles.css" rel="stylesheet" />
</head>
<body>

<!-- Comment Box -->
<form class="set" action="comment" method="post">
    <div>
        <label>
            <textarea class="comment" name="comment" rows="4" placeholder="Enter your comment here"></textarea>
        </label><br>
    </div>

        <input type="hidden" name="storyId" value="${story.storyId}"><br>
    <div class="col-md-12">
        <input type="submit" class="btn btn-primary" value="Add Comment">
    </div>
</form>

<!-- Display existing comments for the story -->
<h4>Comments:</h4>
<c:forEach var="storyComment" items="${story.comments}">
    <p>${storyComment.user.username}: ${storyComment.content} on ${storyComment.creationDate}</p>
</c:forEach>

</body>
</html>
