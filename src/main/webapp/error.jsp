<%@ page language="java" contentType="text/html; charset=US-ASCII"
         pageEncoding="US-ASCII" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "https://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
    <title>Error Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            text-align: center;
            padding: 20px;
        }

        .error-container {
            max-width: 600px;
            margin: 50px auto;
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 5px;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        h1 {
            color: #d9534f;
        }

        p {
            color: #333;
        }

        .error-code {
            font-size: 24px;
            margin-bottom: 10px;
            color: #d9534f;
        }
    </style>
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
