<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <spring:url value="/css/style.css" var="urlCss"/>
    <link rel="stylesheet" href="${urlCss}" type="text/css"/>
    <title>Stadions</title>
</head>
<body>
<h1>FIFA World Cup Qatar 2022</h1>

<c:if test="${not empty verkocht}">
    <c:if test="${verkocht eq 1}">
        <p><small>${verkocht} ticket werd aangekocht</small></p>
    </c:if>
    <c:if test="${verkocht gt 1}">
        <p><small>${verkocht} tickets werden aangekocht</small></p>
    </c:if>
</c:if>

<c:if test="${uitverkocht}">
    <p><small>De voetbalmatch is uitverkocht</small></p>
</c:if>

<form:form method="post" action="fifa" modelAttribute="stadionCommand">
    <table>
        <tr>
            <td>Stadion :</td>
            <td><form:select path="stadionNaam">
                <form:options items="${stadions}"/>
            </form:select></td>
        </tr>

        <tr>
            <td colspan="3"><input type="submit" value="Voer uit"/></td>
        </tr>
    </table>
</form:form>

</body>
</html>