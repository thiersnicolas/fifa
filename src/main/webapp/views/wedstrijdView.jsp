<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <spring:url value="/css/style.css" var="urlCss"/>
    <link rel="stylesheet" href="${urlCss}" type="text/css"/>
    <title>Wedstrijd</title>
</head>
<body>
<h1>FIFA World Cup Qatar 2022</h1>
<h2>Stadion: ${wedstrijd.stadion.naam}</h2>
<h3>${wedstrijd.land1} vs ${wedstrijd.land2}</h3>
<h3>aantal tickets beschikbaar: ${wedstrijd.getAantalBeschikbarePlaatsen()}</h3>

<c:if test="${wedstrijd.getAantalBeschikbarePlaatsen() lt 1}">
    <c:url var="uitverkocht" value="/fifa?uitverkocht=true"/>
    <c:redirect url="${uitverkocht}"/>
</c:if>

<c:url var="post_url" value="/fifa/${wedstrijd.id}/tickets"/>
<form:form method="post" action="${post_url}" modelAttribute="ticketsCommand">
    <p>
        <label>email :</label>
        <form:input path="email" size="20"/>
        <form:errors path="email" cssClass="error"/>
    </p>
    <p>
        <label>aantal tickets: </label>
        <form:input path="aantal" size="20" value="1"/>
        <form:errors path="aantal" cssClass="error"/>
    </p>
    <p>
        <label>voetbalCode1 :</label>
        <form:input path="voetbalCode1" size="20" value="10"/>
        <form:errors path="voetbalCode1" cssClass="error"/>
    </p>
    <p>
        <label>voetbalCode2 :</label>
        <form:input path="voetbalCode2" size="20" value="20"/>
        <form:errors path="voetbalCode2" cssClass="error"/>
    </p>

    <p>
        <input type="submit" value="Koop"/>
    </p>

</form:form>

</body>
</html>