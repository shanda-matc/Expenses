<%@ page import="java.util.Objects" %><%--
  Created by IntelliJ IDEA.
  User: shand
  Date: 12/10/2023
  Time: 12:23 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Success</title>
    <link href="css/custom.css" rel="stylesheet" />
    <link href="css/styles.css" rel="stylesheet" />
</head>
<body>
<% if (Objects.equals(request.getAttribute("storyAddedSuccessfully"), true)) { %>
    <div class="container">
        <h1>Success</h1>
        <p>Your story has been added successfully!</p>
        <a href="index.jsp">Go to Home</a>
    </div>
<% } %>
</body>
</html>
