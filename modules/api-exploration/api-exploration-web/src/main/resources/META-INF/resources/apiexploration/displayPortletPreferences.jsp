<%@page import="com.liferay.portal.kernel.model.LayoutTypePortlet"%>
<%@page import="com.liferay.portal.kernel.theme.ThemeDisplay"%>
<%@page import="com.liferay.portal.kernel.model.Portlet"%>
<%@page import="java.util.Map"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="javax.portlet.PortletPreferences"%>
<%@page import="com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.model.LayoutTemplate"%>
<%@page import="java.util.List"%>
<%@page import="com.liferay.portal.kernel.service.LayoutTemplateLocalServiceUtil"%>
<%@ include file="/init.jsp" %>
<a href="<portlet:renderURL />">back</a>

<h1>Display Portlet Preferences</h1>
<h2>Portlets on page</h2>
<%
List<com.liferay.portal.kernel.model.Portlet> regularPortlets = layoutTypePortlet.getAllPortlets();
for(Portlet portlet: regularPortlets) {
	out.write("<h2>"+portlet.getDisplayName()+"</h2>");
	out.write("<table border=\"1\">");
	PortletPreferences pref = PortletPreferencesFactoryUtil.getLayoutPortletSetup(layout, portlet.getPortletId());
	Map<String, String[]> map = pref.getMap();
	for (String key : map.keySet()) {
		String[] value = map.get(key);
		out.write("<tr><td>" + key + "</td><td>");
		for(int i=0; i < value.length; i++) {
			out.write(HtmlUtil.escape(value[i]) + "<br/>");
		}
		out.write("</td></tr>");
	}
	out.write("</table>");
}
%>

<!-- duplicated code from above... not pretty -->

<h2>Embeded Portlets</h2>

<%
List<com.liferay.portal.kernel.model.Portlet> embeddedPortlets = layout.getEmbeddedPortlets();
for(Portlet portlet: embeddedPortlets) {
	out.write("<h2>"+portlet.getDisplayName()+"</h2>");
	out.write("<table border=\"1\">");
	PortletPreferences pref = PortletPreferencesFactoryUtil.getLayoutPortletSetup(layout, portlet.getPortletId());
	Map<String, String[]> map = pref.getMap();
	for (String key : map.keySet()) {
		String[] value = map.get(key);
		out.write("<tr><td>" + key + "</td><td>");
		for(int i=0; i < value.length; i++) {
			out.write(HtmlUtil.escape(value[i]) + "<br/>");
		}
		out.write("</td></tr>");
	}
	out.write("</table>");
}  %>

