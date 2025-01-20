<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<nav class="navbar navbar-expand-md navbar-dark bg-dark py-0">
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <div class="container">
            <a href="meals" class="navbar-brand"><img src="resources/images/icon-meal.png"> <spring:message code="app.title"/></a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <ul class="navbar-nav ml-auto">
                <li class="nav-item">
                    <sec:authorize access="isAuthenticated()">
                        <form:form class="form-inline my-2" action="logout" method="post">
                            <sec:authorize access="hasRole('ADMIN')">
                                <a class="btn btn-info mr-1" href="users"><spring:message code="user.title"/></a>
                            </sec:authorize>
                            <a class="btn btn-info mr-1" href="profile">${userTo.name} <spring:message code="app.profile"/></a>
                            <button class="btn btn-primary my-1" type="submit">
                                <span class="fa fa-sign-out"></span>
                            </button>
                        </form:form>
                    </sec:authorize>
                    <sec:authorize access="isAnonymous()">
                        <form:form class="form-inline my-2" id="login_form" action="spring_security_check" method="post">
                            <input class="form-control mr-1" type="text" placeholder="Email" name="username">
                            <input class="form-control mr-1" type="password" placeholder="Password" name="password">
                            <button class="btn btn-success" type="submit">
                                <span class="fa fa-sign-in"></span>
                            </button>
                        </form:form>
                    </sec:authorize>
                </li>
                <li class="nav-item dropdown px-3">
                    <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownLocaleButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        ${pageContext.response.locale}
                    </button>
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                        <a class="dropdown-item" href="${requestScope['javax.servlet.forward.request_uri']}?lang=en">English</a>
                        <a class="dropdown-item" href="${requestScope['javax.servlet.forward.request_uri']}?lang=ru">Русский</a>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</nav>

<script type="text/javascript">
    var localeCode = "${pageContext.response.locale}";
</script>
