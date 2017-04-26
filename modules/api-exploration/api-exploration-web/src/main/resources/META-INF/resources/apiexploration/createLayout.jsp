<%@page import="com.liferay.portal.kernel.model.LayoutTemplate"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.kernel.service.LayoutTemplateLocalServiceUtil"%>
<%@ include file="/init.jsp" %>
<portlet:actionURL name="createLayout" var="createLayout"/>

<h1>Create Layout</h1>
<aui:form action="<%=createLayout %>" method="POST" name="fm">
	<%-- aui:input type="hidden" name="redirect" value="redirect"/ --%>
	<aui:input name="friendlyURL" value="">
		<aui:validator name="required" errorMessage="You must enter a FriendlyURL"/>
	</aui:input>
	<aui:input name="pageName" value="">
		<aui:validator name="required" errorMessage="You must enter a Page Name"/>
	</aui:input>
	<aui:select name="layoutTemplateId">
		<%
		List<LayoutTemplate> list = LayoutTemplateLocalServiceUtil.getLayoutTemplates();
		for(LayoutTemplate theTemplate: list) {
			String id = theTemplate.getLayoutTemplateId(); %>
			<aui:option value="<%=id %>" selected="<%=id.equals("2_columns_ii") %>" >
				<%=theTemplate.getLayoutTemplateId() %>
			</aui:option>
		<% } %>
	</aui:select>
	<aui:button-row>
		<aui:button type="submit" />

		<aui:button type="cancel" onClick="<portlet:renderURL/>" />
	</aui:button-row>
</aui:form>