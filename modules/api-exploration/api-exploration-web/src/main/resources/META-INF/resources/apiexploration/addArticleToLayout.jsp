<%@page import="com.liferay.portal.kernel.model.Portlet"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.kernel.service.PortletLocalServiceUtil"%>
<%@ include file="/init.jsp" %>
<portlet:actionURL name="addArticleToLayout" var="addArticleToLayout"/>

<h1>Add a Web Content Display portlet with an article to *this* Layout</h1>
<aui:form action="<%=addArticleToLayout %>" method="POST" name="fm">
	<aui:input name="title"/>
	<aui:input type="textarea" name="article"/>
	
	<aui:button-row>
		<aui:button type="submit" />


		<aui:button type="cancel" onClick="<portlet:renderURL/>" />
	</aui:button-row>
</aui:form>