<jsp:include page="contentType.jsp" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%--
  Created by IntelliJ IDEA.
  User: shanda
  Date: 11/30/23
  Time: 6:00 PM
  To change this template use File | Settings | File Templates.
--%>
<html>
<jsp:include page="header.jsp" />
<body id="page-top">
<!-- Navigation -->
<jsp:include page="navigation.jsp" />

<!-- Masthead -->
<header class="masthead">
    <div class="container px-4 px-lg-5 h-100">
        <div class="row gx-4 gx-lg-5 h-100 align-items-center justify-content-center text-center">
            <div class="col-lg-8 align-self-end">
                <h1 class="text-white font-weight-bold">Add Story</h1>
                <hr class="divider" />
            </div>
            <div class="col-lg-8 align-self-baseline">
                <form action="addStory" method="post" class="row g-3 needs-validation" novalidate>
                    <div class="col-md-12">
                        <label for="title" class="form-label text-white">Title:</label>
                        <input type="text" id="title" class="form-control" name="title" required>
                        <div class="invalid-feedback">Please enter a title.</div>
                    </div>
                    <div class="col-md-12">
                        <label for="category" class="form-label text-white">Category:</label>
                        <select id="category" class="form-select" name="category" required>
                            <option value="" disabled selected>Select a category</option>
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
                        </select>
                        <div class="invalid-feedback">Please select a category.</div>
                    </div>
                    <div>
                        <%--@declare id="contentsource"--%>
                        <label for="contentSource" class="form-label text-white">Content Source:</label>
                        <input class="form-label text-white" type="radio" name="contentSource" value="api" checked> Fetch from API
                        <input class="form-label text-white" type="radio" name="contentSource" value="user"> Write my own
                    </div>

                    <div>
                        <%--@declare id="usercontent"--%>
                        <label class="form-label text-white" for="userContent">Content:</label>
                        <textarea name="userContent" rows="4" cols="50"></textarea>
                    </div>

                    <div class="col-12">
                        <button type="submit" class="btn btn-primary">Submit</button>
                    </div>

                </form>
            </div>
        </div>
    </div>
</header>

<jsp:include page="services.jsp" />
<%-- <%@ include file="contact.jsp" %>  --%>
<jsp:include page="footer.jsp" />
<jsp:include page="scripts.jsp" />

<!-- Bootstrap core JS-->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- Core theme JS-->
<script src="js/scripts.js"></script>
</body>
</html>
