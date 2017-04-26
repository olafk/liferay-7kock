<%@page import="com.liferay.portal.kernel.model.User"%>
<%@page import="com.liferay.portal.kernel.dao.orm.QueryUtil"%>
<%@page import="com.liferay.portal.kernel.service.UserLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.model.Portlet"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.kernel.service.PortletLocalServiceUtil"%>
<%@ include file="/init.jsp" %>
<portlet:actionURL name="addNavigationToUserProfile" var="addNavigationToUserProfile"/>

<h1>Add a Navigation Portlet to the given user's profile</h1>
<aui:form action="<%=addNavigationToUserProfile %>" method="POST" name="fm">
	<aui:select name="userId">
	<%
	List<User> users = UserLocalServiceUtil.getUsers(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
	for(User theUser: users) {
		%>
		<aui:option value="<%=theUser.getUserId() %>"><%=theUser.getFullName() %></aui:option>
		<%
	}
	%>
	</aui:select>
	
	<aui:button-row>
		<aui:button type="submit" />


		<aui:button type="cancel" onClick="<portlet:renderURL/>" />
	</aui:button-row>
</aui:form>