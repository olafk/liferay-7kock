<%@ include file="../init.jsp" %>

<p>
	<b><liferay-ui:message key="7cogs_portlet_Sevencogs.caption"/></b>

	<portlet:renderURL var="createLayout">
       <portlet:param name="mvcPath" value="/apiexploration/createLayout.jsp" />
	</portlet:renderURL>
	<portlet:renderURL var="addPortletToLayout">
		<portlet:param name="mvcPath" value="/apiexploration/addPortletToLayout.jsp"/>
	</portlet:renderURL>
	<portlet:renderURL var="displayPortletPreferences">
		<portlet:param name="mvcPath" value="/apiexploration/displayPortletPreferences.jsp"/>
	</portlet:renderURL>
	<portlet:renderURL var="addArticleToLayout">
		<portlet:param name="mvcPath" value="/apiexploration/addArticleToLayout.jsp"/>
	</portlet:renderURL>
	<portlet:renderURL var="createRandomUsers">
		<portlet:param name="mvcPath" value="/apiexploration/createRandomUsers.jsp"/>
	</portlet:renderURL>	
	<portlet:renderURL var="resetTermsOfUse">
		<portlet:param name="mvcPath" value="/apiexploration/resetTermsOfUse.jsp"/>
	</portlet:renderURL>	
	<portlet:renderURL var="addNavigationToUserProfile">
		<portlet:param name="mvcPath" value="/apiexploration/addNavigationToUserProfile.jsp"/>
	</portlet:renderURL>	
	<portlet:renderURL var="addUserGroupToGroup">
		<portlet:param name="mvcPath" value="/apiexploration/addUserGroupToGroup.jsp"/>
	</portlet:renderURL>	
		
	<% String placeholder=""; %>
	<ul>
		<li><a href="<%=createLayout %>">Create Layout</a></li>
		<li><a href="<%=addPortletToLayout%>">Add a portlet to this page</a></li>
		<li><a href="<%=displayPortletPreferences%>">Display portletPreferences</a></li>
		<li><a href="<%=createRandomUsers %>">Create Random Users</a></li>
		<li><a href="<%=resetTermsOfUse %>">Reset Terms Of Use</a></li>
	</ul>
	And now for something completely different
	<ul>
		<li><a href="<%=addUserGroupToGroup %>">Add UserGroup To Group</a></li>
	</ul>
</p>

 <%-- 
		<li><a href="<%=placeholder %>">   ***   </a></li>
--%> <%-- 
		<li><a href="<%=placeholder %>">   ***   </a></li>
--%>

${debugOut}