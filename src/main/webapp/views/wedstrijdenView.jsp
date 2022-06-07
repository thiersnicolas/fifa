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
    <title>Wedstrijden</title>
</head>
<body>
<h1>FIFA World Cup Qatar 2022</h1>
<h2>Stadion: "${stadionNaam}"</h2>

<table width="70%">
    <tr>
        <th align="left">Nr</th>
        <th align="left">Voetbalmatch</th>
        <th align="left">Datum</th>
        <th align="left">Aftrap</th>
        <th align="left">Tickets</th>
    </tr>
    <c:forEach items="${wedstrijden}" var="wedstrijd">
        <tr>
            <td>
                <spring:url value="/fifa/${wedstrijd.id}" var="url">
                    <spring:param name="id" value="${wedstrijden.indexOf(wedstrijd)}"/>
                </spring:url>
                <a href="${url}">${wedstrijden.indexOf(wedstrijd)}</a>
            </td>
            <td>${wedstrijd.land1}-${wedstrijd.land2}</td>
            <td><fmt:formatNumber value="${wedstrijd.tijdstip.getDayOfMonth()}"
                                  minIntegerDigits="2"/> ${wedstrijd.tijdstip.getMonth().name().toLowerCase()}</td>
            <td><fmt:formatNumber value="${wedstrijd.tijdstip.getHour()}" minIntegerDigits="2"/>:<fmt:formatNumber
                    value="${wedstrijd.tijdstip.getMinute()}" minIntegerDigits="2"/></td>
            <td>${wedstrijd.getAantalBeschikbarePlaatsen()}</td>
        </tr>
    </c:forEach>
</table>
<br>
</body>
</html>