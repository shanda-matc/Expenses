<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/contentType.jsp" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add a New Story</title>
</head>
<body>

<h1>Add a New Story</h1>
<form action="addStory" method="post">
    <%--@declare id="title"--%><label for="title">Title:</label>
    <input type="text" name="title" required><br>

    <label for="userName">Author:</label>
    <input type="text" name="userName" required><br>

    <label for="category">Category:</label>
    <select name="category" required>
        <option value="Anecdote">Anecdote</option>
        <option value="Drabble">Drabble</option>
        <option value="Fable">Fable</option>
        <option value="Feghoot">Feghoot</option>
        <option value="Flash Fiction">Flash Fiction</option>
        <option value="Frame Story">Frame Story</option>
        <option value="Mini-saga">Mini-saga</option>
        <option value="Story Sequence">Story Sequence</option>
        <option value="Sketch Story">Sketch Story</option>
        <option value="Vignette">Vignette</option>
    </select><br>

    <label for="content">Content:</label><br>
    <textarea name="content" rows="5" cols="40" required></textarea><br>
    <input type="submit" value="Submit">
</form>
</body>
</html>
