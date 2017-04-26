package com.liferay.olafkock.sevencogs;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

/**
 * This portlet displays internal configuration information (currently only 
 * PortletPreferences) for a given portlet, passed through the parameter 
 * <code>portletId</code>. When none is given, it renders a form with all
 * portlets on the current page, to choose from.
 *
 * Purpose: If you're wondering how to programmatically set up a portlet with
 * configuration values, it's good to manually configure one, then inspect what
 * actually ends up in the portlet's configuration.
 * 
 * @author Olaf Kock
 */

@Component(
		immediate = true,
		property = {
	        "com.liferay.portlet.display-category=category.sevencogs",
	        "com.liferay.portlet.instanceable=false",
	        "com.liferay.portlet.footer-portlet-css=/portletinformation/portletinformation.css",
	        "javax.portlet.security-role-ref=power-user,user",
	        "javax.portlet.display-name=Portlet Information",
	        "javax.portlet.name=" + PortletInformationPortlet.PORTLET_NAME,
			"javax.portlet.init-param.template-path=/",
			"javax.portlet.init-param.view-template=/portletinformation/view.jsp",
			"javax.portlet.resource-bundle=content.Language"
	   },
	    service = Portlet.class
	)
	public class PortletInformationPortlet extends MVCPortlet {
	
	public static final String PORTLET_NAME="com_liferay_olafkock_sevencogs_PortletInformationPortlet";

	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		Layout layout = themeDisplay.getLayout();
		
		String portletId = renderRequest.getParameter("portletId");

		if(portletId != null && ! portletId.equals("")) {
			initializePortletInformation(renderRequest, portletId, layout);
		}
		initializeForm(renderRequest, layout);
	
		super.doView(renderRequest, renderResponse);
	}

	/**
	 * Render information for one specific portlet
	 * 
	 * @param renderRequest
	 * @param portletId
	 * @param layout
	 */

	private void initializePortletInformation(RenderRequest renderRequest, String portletId, Layout layout) {
		System.out.println("fetching info for portlet " + portletId);
		PortletPreferences preferences = PortletPreferencesFactoryUtil.getLayoutPortletSetup(layout, portletId);
		com.liferay.portal.kernel.model.Portlet portlet = portletLocalService.getPortletById(portletId);

		renderRequest.setAttribute("infoPortletName", portlet.getDisplayName());
		renderRequest.setAttribute("infoPortletId", portletId);
		renderRequest.setAttribute("infoPreferences", preferences);
	}

	/**
	 * initialize the request with information about all portlets on the current page
	 *
	 * @param renderRequest
	 * @param layout
	 */
	
	private void initializeForm(RenderRequest renderRequest, Layout layout) {
		// This portlet is placed on the current page, thus it's semi-safe to assume that 
		// it's a page that can hold portlets... (at least for this POC style code)
		LayoutTypePortlet layoutTypePortlet =
				(LayoutTypePortlet)layout.getLayoutType();
		
		List<com.liferay.portal.kernel.model.Portlet> allPortlets = layoutTypePortlet.getAllPortlets();
		Map<String, String> portlets = new HashMap<String, String>();
		for (com.liferay.portal.kernel.model.Portlet portlet : allPortlets) {
			if(portlet.isActive()) {
				portlets.put(portlet.getPortletId(), portlet.getDisplayName());
			}
			
		}
		renderRequest.setAttribute("infoPortlets", portlets);
	}

		
	@Reference
	public void setPortletLocalService(PortletLocalService portletLocalService) {
		this.portletLocalService = portletLocalService;
	}
	
	protected PortletLocalService portletLocalService;
	
}