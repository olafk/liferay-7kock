package com.liferay.olafkock.sevencogs;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.model.PortletConstants;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.ResourceLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.permission.PortletPermissionUtil;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.StringPool;

public class LayoutExploration {
	/**
	 * add a page to the site with the following properties:
	 * @param group            site, where this layout is added as top level page.
	 * @param name             the name of the new page
	 * @param privateLayout    true for private, false for public pages
	 * @param friendlyURL      the friendlyURL you'd like for this page, e.g. "sample"
	 * @param layoutTemplateId the human readable layout template id, e.g. 2_columns_ii
	 * @return 
	 * @throws Exception
	 */
	public static Layout addLayoutToSite(LayoutLocalService layoutLocalService, ResourceLocalService resourceLocalService, Group group, String name, boolean privateLayout, String friendlyURL,
			String layoutTemplateId) throws Exception {
		ServiceContext serviceContext = new ServiceContext();
		if(!friendlyURL.startsWith("/")) {
			friendlyURL = "/" + friendlyURL;
		}
		Layout layout = layoutLocalService.addLayout(group.getCreatorUserId(), group.getGroupId(), privateLayout,
				LayoutConstants.DEFAULT_PARENT_LAYOUT_ID, name, StringPool.BLANK, StringPool.BLANK,
				LayoutConstants.TYPE_PORTLET, false, friendlyURL, serviceContext);
		LayoutTypePortlet layoutTypePortlet = (LayoutTypePortlet) layout.getLayoutType();
		layoutTypePortlet.setLayoutTemplateId(0, layoutTemplateId, false);
		layoutLocalService.updateLayout(layout);
		addResourceToLayout(resourceLocalService, layout, PortletKeys.PORTAL);
		return layout;
	}

	public static void addResourceToLayout(ResourceLocalService resourceLocalService, Layout layout, String portletId) throws PortalException {
		String rootPortletId = PortletConstants.getRootPortletId(portletId);
		String portletPrimaryKey = PortletPermissionUtil.getPrimaryKey(layout.getPlid(), portletId);
		resourceLocalService.addResources(layout.getCompanyId(), layout.getGroupId(), 0, rootPortletId,
				portletPrimaryKey, true, true, true);
	}



}