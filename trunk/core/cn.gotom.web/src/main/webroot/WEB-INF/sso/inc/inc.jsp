<%@page import="cn.gotom.sso.util.CommonUtils"%>
<%@ page language="java"%>
<%@ page pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String serviceParameterName = getInitParameter(this.getServletContext(), "serviceParameterName", "service");
	request.setAttribute("serviceParameterName", serviceParameterName);
%>
<%!//String service = getInitParameter(request., "serviceParameterName", "service")
	public final String getInitParameter(final ServletContext servletContext, final String propertyName, final String defaultValue)
	{
		String value = servletContext.getInitParameter(propertyName);
		if (CommonUtils.isNotBlank(value))
		{
			return value;
		}
		if (CommonUtils.isEmpty(value))
		{
			value = defaultValue;
		}
		return defaultValue;
	}%>
<c:set var="ctxp" value="${pageContext.request.contextPath}" />