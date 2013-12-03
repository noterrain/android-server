<%@ page contentType="text/javascript;charset=UTF-8"%>
<%
out.clear();
String email = request.getParameter("email");
String password = request.getParameter("password");

if (email == null || password == null) {
    out.println("Fail, (email:"+email+", password:"+password+")");
} else {
    out.println("Success, (email:"+email+", password:"+password+")");
}
%>