<%@ include file="/init.jsp" %>

<p>
	<b><liferay-ui:message key="UserSiteMaintenance.caption"/></b>
</p>

This portlet simulates programmatic maintenance of user profiles. 
There's no fancy UI here - just give a page name to create for
whoever this activity is executed on.
 
<portlet:actionURL var="actionURL" name="addLayouts"/>
<form action="${actionURL}" method="POST">
	<input type="text" name="<portlet:namespace/>pageName">
	<input type="submit">
</form>