<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: shand
  Date: 11/25/2023
  Time: 7:24 PM
  To change this template use File | Settings | File Templates.
--%>
<jsp:include page="contentType.jsp" />
<html>
<head>
    <title>Display Stories</title>
    <link href="css/custom.css" rel="stylesheet" />
    <link href="css/styles.css" rel="stylesheet" />
</head>
<body role="document">

<c:if test="${empty categoryStoriesMap}">
    <p>No stories available.</p>
</c:if>

<c:choose>
    <c:when test="${not empty param.category}">
        <c:set var="stories" value="${categoryStoriesMap[param.category]}" />
    </c:when>
    <c:otherwise>
        <c:set var="stories" value="${categoryStoriesMap}" />
    </c:otherwise>
</c:choose>

<c:forEach var="entry" items="${stories}">
    <c:if test="${not empty entry.value}">
    <h2>${entry.key.name}</h2>

    <c:forEach var="story" items="${entry.value}">
        <div class="story-container">
            <h3 class="post-title">${story.title}</h3>
            <p>${story.content}</p>
            <p class="post-meta">Posted by ${story.author.username} on ${story.publicationDate}&nbsp;</p>

                <%-- Display Comment Box --%>
            <form class="comment-form" action="comment" method="post">
                <textarea class="comment-textarea" name="comment" rows="4" placeholder="Enter your comment here"></textarea><br>
                <input class="comment-button" type="hidden" name="storyId" value="${story.storyId}">
                <input class="comment-button" type="submit" value="Add Comment">
                <c:if test="${story.author.username eq sessionScope.userName}">
                    <a class="comment-button" href="updateStory?storyId=${story.storyId}">Update Story</a>
                    <a class="comment-button" href="deleteStory?storyId=${story.storyId}">Delete Story</a>
                </c:if>
            </form>

                <%-- Display existing comments for the story --%>
            <h4>Comments:</h4>
            <c:forEach var="storyComment" items="${story.comments}">
                <p>${storyComment.user.username} said: ${storyComment.content} on ${storyComment.creationDate}</p>
            </c:forEach>

        </div>
    </c:forEach>
    <hr>
</c:if>
</c:forEach>

</body>
</html>


