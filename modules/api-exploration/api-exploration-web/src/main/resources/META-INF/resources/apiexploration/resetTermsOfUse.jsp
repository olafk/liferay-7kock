<%@ include file="/init.jsp" %>
<portlet:actionURL name="resetTermsOfUse" var="resetTermsOfUse"/>

<h1>Reset everybody else's Terms of Use agreement flag</h1>
<aui:form action="<%=resetTermsOfUse %>" method="POST" name="fm">
	<aui:input type="checkbox" name="really"/>
	
	<aui:button-row>
		<aui:button type="submit" />


		<aui:button type="cancel" onClick="<portlet:renderURL/>" />
	</aui:button-row>
</aui:form>