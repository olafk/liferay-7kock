<%@ include file="../init.jsp" %>

<c:if test="${not empty infoPortletName }">
	<%-- Render information for a specific portlet --%>
	<h1>${infoPortletName }</h1>
	<h2>${infoPortletId }</h2>
	<c:if test="${not empty infoPreferences.map }">
		<h3><liferay-ui:message key="preferences"/></h3>
		<table border="1">
			<thead>
				<tr>
					<td>
						<liferay-ui:message key="key"/>
					</td>
					<td>
						<liferay-ui:message key="value"/>
					</td>
				</tr>
			</thead>
			<tbody>
	
			<c:forEach items="${infoPreferences.map}" var="pref">
		  		<tr>
		  			<td class="prefsKey">
							<c:out value="${pref.key}"/>
					</td>
					<td class="prefsValue">
						<c:forEach items="${pref.value}" var="singlePref">
							<c:out value="${singlePref }"/>
							<br/>
						</c:forEach>
					</td>
				</tr>
			</c:forEach>
			
			</tbody>
		</table>
	</c:if>
	<c:if test="${empty infoPreferences.map }">
		<p><liferay-ui:message key="no-configured-preferences"/></p>
	</c:if>
</c:if>

<%-- c:if test="${empty infoPortletName }" --%>
	<%-- Render form to choose the portlet for which information should be displayed --%>
	<portlet:renderURL var="moreInfo"/>
	<liferay-ui:message key="show-info-for-portlet"/>
	<form action="${moreInfo }" method="POST">
		<select name="<portlet:namespace/>portletId">
			<c:forEach items="${infoPortlets}" var="onePortlet">
				<option value="${onePortlet.key}">${onePortlet.value}</option>
			</c:forEach>
		</select>
		<input type="submit"/>
	</form>
<%-- /c:if --%>
