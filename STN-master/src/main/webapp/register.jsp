<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="structure/meta.jsp" %>
    <title>Register</title>
    <%@ include file="structure/header.jsp" %>
</head>
<body>
<jsp:useBean id="user" class="com.stn.helpers.UserHelper"/>

${user.verifyAcces(pageContext.request,pageContext.response)}

<form name="register" action="RegisterProcess" method="post">
<table class="black" style="margin-top: 30pt;">
    <tr>
        <td colspan="2" class="center" style="padding-top: 12pt">All fields are required!</td>
    </tr>
    <tr>
        <td class="right" style="padding-left: 30pt; padding-top: 8pt">First Name:</td>
        <td style="padding-right: 30pt; padding-top: 8pt"><input type="text" name="firstname" value="${sessionScope.firstname}" size="35"></td>
    </tr>
    <tr>
        <td class="right" style="padding-left: 30pt; padding-top: 8pt">Last Name:</td>
        <td style="padding-right: 30pt; padding-top: 8pt"><input type="text" name="lastname" value="${sessionScope.lastname}" size="35"></td>
    </tr>
    <tr>
        <td class="right" style="padding-left: 30pt; padding-top: 8pt">User:</td>
        <td style="padding-right: 30pt; padding-top: 8pt"><input type="text" name="user" value="${sessionScope.user}" size="35"></td>
    </tr>
    <tr>
        <td class="right" style="padding-left: 30pt; padding-top: 8pt">Password:</td>
        <td style="padding-right: 30pt; padding-top: 8pt"><input type="password" name="password1" value="" size="35"></td>
    </tr>
    <tr>
        <td class="right" style="padding-left: 30pt; padding-top: 8pt">Confirm Password:</td>
        <td style="padding-right: 30pt; padding-top: 8pt"><input type="password" name="password2" value="" size="35"></td>
    </tr>
    <tr>
        <td class="right" style="padding-left: 30pt; padding-top: 8pt">Email:</td>
        <td style="padding-right: 30pt; padding-top: 8pt"><input type="text" name="email" value="${sessionScope.email}" size="35"></td>
    </tr>
    <tr>
        <td class="center" colspan="2" style="padding-top: 12pt">
            <input type="checkbox" name="terms" value="1">I have read the site <a class="ui" href="terms.jsp"><b>Terms of service</b></a> and rules page.<br>
            <input type="checkbox" name="faq" value="1">I agree to read the FAQ before asking any questions.
        </td>
    </tr>
    <tr>
        <td class="center" colspan="2"><b style="color: red; display: inline">${sessionScope.error}</b></td>
    </tr>
    <tr>
        <td colspan="2" class="center" style="padding-top: 20pt">
            <input type="hidden" name="invitecode" value="${param.invite}">
            <input name="Register" type="submit" value="Register">
        </td>
    </tr>
    <tr>
        <td colspan="2" height="24"></td>
    </tr>
</table>
</form>

<c:remove var="error" scope="session" />
<c:remove var="firstname" scope="session" />
<c:remove var="lastname" scope="session" />
<c:remove var="user" scope="session" />
<c:remove var="email" scope="session" />

<%@ include file="structure/footer.jsp" %>
</body>
</html>
