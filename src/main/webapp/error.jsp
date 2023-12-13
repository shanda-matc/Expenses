<%@ page language="java" contentType="text/html; charset=US-ASCII"
         pageEncoding="US-ASCII" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "https://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
    <title>Error Page</title>
        <link href="css/custom.css" rel="stylesheet" />
        <link href="css/styles.css" rel="stylesheet" />
</head>
<body>
<% if(response.getStatus() == 500){ %>
<div class="error-container">
    <span class="error-code">Error Code: <%=response.getStatus() %></span>
    <h1>Error</h1>
    <p>Error: <%=exception.getMessage() %></p>
    <p>Please try again later or contact support.</p>
    <a href="index.jsp">Go back to Home</a>
</div>
<%} else {%>
<div class="error-container">
    <span class="error-code">Error Code: <%=response.getStatus() %></span>
    <p>Sorry, an error occurred while processing your request.</p>
    <p>Please try again later.</p>
    <a href="index.jsp">Go back to Home</a>
</div>
<%} %>
</body>
</html>
