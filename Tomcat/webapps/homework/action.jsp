<%@ page contentType="text/javascript;charset=UTF-8"%>
<%@ page import="com.foungyu.un.Homework" %>
<%
out.clear();
String action = request.getParameter("action");
String value = request.getParameter("input");
if (action == null) {
    out.println("Failed, action can not be null.");
    return;
}
if (action.equals("SET_VALUE_FROM_ANDROID") && value != null) {
    Homework.setValueFromAndroid(value);
    out.println("Success");
} else if (action.equals("GET_VALUE_FROM_ANDROID")) {
    out.println(Homework.getValueFromAndroid());
} else if (action.equals("SET_VALUE_FROM_ARDUINO") && value != null) {
    Homework.setValueFromArduino(value);
    out.println("Success");
} else if (action.equals("GET_VALUE_FROM_ARDUINO")) {
    out.println(Homework.getValueFromArduino());
} else {
    out.println("Failed, action="+action+", input="+value+"");
}
%>