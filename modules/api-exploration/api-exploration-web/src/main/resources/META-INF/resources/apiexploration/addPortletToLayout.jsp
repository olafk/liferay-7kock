<%@page import="com.liferay.portal.kernel.model.Portlet"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.kernel.service.PortletLocalServiceUtil"%>
<%@ include file="/init.jsp" %>
<portlet:actionURL name="addPortletToLayout" var="addPortletToLayout"/>

<h1>Add a portlet to *this* Layout</h1>
<aui:form action="<%=addPortletToLayout %>" method="POST" name="fm">
	<%-- aui:input type="hidden" name="redirect" value="redirect"/ --%>
	<aui:select name="portletId">
		<aui:option value=""/>
		<%
		List<Portlet> list = PortletLocalServiceUtil.getPortlets();
		for(Portlet thePortlet: list) {
			String id = thePortlet.getPortletId(); %>
			<aui:option value="<%=id %>">
				<%=thePortlet.getDisplayName() %> (<%=id %>)
			</aui:option>
		<% } %>
	</aui:select>
	<aui:button-row>
		<aui:button type="submit" />

		<aui:button type="cancel" onClick="<portlet:renderURL/>" />
	</aui:button-row>
</aui:form>