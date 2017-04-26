package com.liferay.olafkock.sevencogs;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

@Component(
		immediate = true,
		property = {
	        "com.liferay.portlet.display-category=category.sevencogs",
	        "com.liferay.portlet.instanceable=false",
	        "com.liferay.portlet.footer-portlet-css=/pageinformation/pageinformation.css",
	        "javax.portlet.security-role-ref=power-user,user",
	        "javax.portlet.display-name=Page Information",
	        "javax.portlet.name=" + PageInformationPortlet.PORTLET_NAME,
			"javax.portlet.init-param.template-path=/",
			"javax.portlet.init-param.view-template=/pageinformation/view.jsp",
			"javax.portlet.resource-bundle=content.Language"
	   },
	    service = Portlet.class
	)

public class PageInformationPortlet extends MVCPortlet {

	public static final String PORTLET_NAME = "com_liferay_olafkock_sevencogs_PageInformationPortlet";
	
	
	
	
}
