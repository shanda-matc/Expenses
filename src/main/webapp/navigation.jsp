<!-- Navigation-->
<nav class="navbar navbar-expand-lg navbar-light fixed-top py-3" id="mainNav">
    <div class="container px-4 px-lg-5">
        <c:choose>
            <c:when test="${signInSuccess}">
                <!-- Display username in navbar if signed in -->
                <a class="navbar-brand" href="#page-top">Hi, ${userName}</a>
            </c:when>
            <c:otherwise>
                <!-- Display default content if not signed in -->
                <a class="navbar-brand" href="#page-top">Scribble Amplify Knack</a>
            </c:otherwise>
        </c:choose>
        <button class="navbar-toggler navbar-toggler-right" type="button" data-bs-toggle="collapse" data-bs-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
        <div class="collapse navbar-collapse" id="navbarResponsive">
            <ul class="navbar-nav ms-auto my-2 my-lg-0">
                <li class="nav-item"><a class="nav-link" href="logIn">SignIn</a></li>
                <li class="nav-item"><a class="nav-link" href="startup">SignUp</a></li>
                <li class="nav-item"><a class="nav-link" href="#read">Read</a></li>
                <li class="nav-item"><a class="nav-link" href="addStory.jsp">Write</a></li>
            </ul>
        </div>
    </div>
</nav>