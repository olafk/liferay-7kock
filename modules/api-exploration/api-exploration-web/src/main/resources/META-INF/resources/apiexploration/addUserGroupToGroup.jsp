<%@page import="com.liferay.portal.kernel.model.LayoutTemplate"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.kernel.service.LayoutTemplateLocalServiceUtil"%>
<%@ include file="/init.jsp" %>
<portlet:actionURL name="addUserGroupToGroup" var="addUserGroupToGroup"/>

<h1>Add UserGroup to Group</h1>
<p>
	Problem: The UI does not allow to add UserGroups to Organizations. However, the API does.
</p>
<p>
	Caveat: I've not performance-tested it. Probably nobody has, as it's not easy to achieve
	this state without scripting...
</p>
<p>
	In order to add a UserGroup to an Organization, you'll have to find the GroupId of the 
	Organization (every Org is associated with a Group) and the UserGroup's id. Here are some 
	valuable values to start with... 
</p>
${debugOut}
<aui:form action="<%=addUserGroupToGroup %>" method="POST" name="fm">
	<%-- aui:input type="hidden" name="redirect" value="redirect"/ --%>
	<aui:input name="groupId" value="">
		<aui:validator name="required" errorMessage="You must enter a GroupId"/>
	</aui:input>
	<aui:input name="userGroupId" value="">
		<aui:validator name="required" errorMessage="You must enter a UserGroupId"/>
	</aui:input>
	<aui:button-row>
		<aui:button type="submit" />

		<aui:button type="cancel" onClick="<portlet:renderURL/>" />
	</aui:button-row>
</aui:form>

${debugOut}