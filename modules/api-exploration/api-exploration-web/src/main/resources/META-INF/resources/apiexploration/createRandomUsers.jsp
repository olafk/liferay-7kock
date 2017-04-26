
<%@page import="com.liferay.portal.kernel.model.User"%>
<%@page import="java.util.List"%>
<%@ include file="/init.jsp" %>
<portlet:actionURL name="createRandomUsers" var="createRandomUsers"/>

<h1>Create Users</h1>
<aui:form action="<%=createRandomUsers %>" method="POST" name="fm">
	<%-- aui:input type="hidden" name="redirect" value="redirect"/ --%>
	<aui:input name="numberOfUsers" value="3">
	</aui:input>
	<aui:button-row>
		<aui:button type="submit" />

		<aui:button type="cancel" onClick="<portlet:renderURL/>" />
	</aui:button-row>
</aui:form>

<% List<User> users = (List<User>)renderRequest.getAttribute("users");
if(users != null) {
	out.write("Created " + users.size() + " users");
} %>