package com.liferay.olafkock.sevencogs;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.PortletConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.LayoutSetLocalService;
import com.liferay.portal.kernel.service.OrganizationLocalService;
import com.liferay.portal.kernel.service.ResourceLocalService;
import com.liferay.portal.kernel.service.UserGroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.service.permission.PortletPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;

@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=api-exploration-web Portlet",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/apiexploration/view.jsp",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)

/**
 * API exploration portlet
 * Purpose: 
 *   - Port content manipulation from old 7cogs to DXP/Liferay7
 *   - run wild experiments
 *   - explore built-in data structures
 * Shortcomings: 
 *   - No permission checks at all - using justing LocalService. 
 *   - Very poor (if at all) error handling
 *   
 * This is used for frequent deployment, running various code examples
 * towards Liferay's API. I've fallen flat on my face with some of them
 * and hope that the remainders can teach someone how to properly access
 * Liferay's API for the purposes documented here.
 * 
 * This class is implementing a lot of functionality itself, but 
 * eventually this code shall be moved to an API-call-simplifying service.
 * E.g. the goal is to condense several of the large API calls (e.g. 7 
 * and more parameters) to the bare minimum, replacing all the rest with
 * sensible default values. If you need more: Just use the full blown API!
 */

public class ApiExplorationPortlet extends MVCPortlet {

	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		Organization everyone = organizationLocalService.fetchOrganization(themeDisplay.getCompanyId(), "Everyone");
		String text = "";
		if(everyone != null) {
			text = "Organization Everyone: " + everyone.getOrganizationId();
			try {
				Group group = groupLocalService.getGroup(everyone.getGroupId());
				text += "<br/>Group(Everyone): " + (group==null ? "null" : group.getGroupId() + " " + group.getName());
			} catch (PortalException e) {
				text += "<br/>" + e.getMessage();
			}
			try {
				UserGroup admins = userGroupLocalService.getUserGroup(themeDisplay.getCompanyId(), "Admins");
				text += "<br/>UserGroup Admins: " + (admins==null ? "null" : admins.getUserGroupId());
			} catch (PortalException e) {
				text += "<br/>" + e.getMessage();
			}
		}

		renderRequest.setAttribute("debugOut", text);
		super.doView(renderRequest, renderResponse);
	}
	
	//////////////////// ActionHandlers ///////////////////////////////////////
	
	public void createLayout(ActionRequest request, ActionResponse response) throws PortalException, Exception {
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

		long groupId = themeDisplay.getScopeGroupId();
		String pageName = ParamUtil.getString(request, "pageName");
		String friendlyURL = ParamUtil.getString(request, "friendlyURL");
		String layoutTemplateId = ParamUtil.getString(request, "layoutTemplateId");

		try {
			LayoutExploration.addLayoutToSite(layoutLocalService, resourceLocalService, groupLocalService.getGroup(groupId), pageName, false, friendlyURL, layoutTemplateId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addUserGroupToGroup(ActionRequest request, ActionResponse response) throws PortalException, Exception {
		long groupId = ParamUtil.getLong(request, "groupId");
		long userGroupId = ParamUtil.getLong(request, "userGroupId");
		groupLocalService.addUserGroupGroup(userGroupId, groupId);
	}
	
	public void resetTermsOfUse(ActionRequest request, ActionResponse response) throws PortalException {
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		boolean really = ParamUtil.getBoolean(request, "really");
		if(really) {
			resetTermsOfUse(themeDisplay.getUserId()); // not those of the current user
		}
	}

	public void createRandomUsers(ActionRequest request, ActionResponse response) {
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		
		int numberOfUsers = ParamUtil.getInteger(request, "numberOfUsers");
		List<User> users = new LinkedList<User>();
		while(numberOfUsers-- > 0) {
			users.add(RandomUserHelper.createRandomUser(userLocalService, themeDisplay.getCompanyId()));
		}
	}
	
	public void addPortletToLayout(ActionRequest request, ActionResponse response) throws Exception {
		String portletId = ParamUtil.getString(request, "portletId");
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		Layout layout = themeDisplay.getLayout();
		System.out.println("adding portlet " + portletId + " to page " + layout.getTitle());
		addPortletId(layout, portletId, "column-1");
	}
	
	
	
	/**
	 * Adapted from original SevenCogs sample code
	 * 
	 * @param layout
	 * @param portletId
	 * @param columnId
	 * @return
	 * @throws Exception
	 */
	public String addPortletId(Layout layout, String portletId, String columnId) throws Exception {

		LayoutTypePortlet layoutTypePortlet = (LayoutTypePortlet) layout.getLayoutType();

		portletId = layoutTypePortlet.addPortletId(0, portletId, columnId, -1, false);

		addResources(layout, portletId);
		updateLayout(layout);

		return portletId;
	}

	/**
	 * Adapted from original SevenCogs sample code
	 * 
	 * @param layout
	 * @param portletId
	 * @throws Exception
	 */
	public void addResources(Layout layout, String portletId) throws Exception {

		String rootPortletId = PortletConstants.getRootPortletId(portletId);

		String portletPrimaryKey = PortletPermissionUtil.getPrimaryKey(layout.getPlid(), portletId);

		resourceLocalService.addResources(layout.getCompanyId(), layout.getGroupId(), 0, rootPortletId,
				portletPrimaryKey, true, true, true);
	}


	/**
	 * Adapted from original SevenCogs sample code
	 * 
	 * @param layout
	 * @throws Exception
	 */
	protected void updateLayout(Layout layout) throws Exception {
		layoutLocalService.updateLayout(
			layout.getGroupId(), layout.isPrivateLayout(), layout.getLayoutId(),
			layout.getTypeSettings());
	}
	
	
	protected void resetTermsOfUse(long notOfThisUserId) throws PortalException {
		List<User> users = userLocalService.getUsers(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		for(User user: users) {
			if(user.getUserId() != notOfThisUserId && ! user.isDefaultUser()) {
				userLocalService.updateAgreedToTermsOfUse(user.getUserId(), false);
			}
		}
	}
	
	
	
	
	
	@Reference(unbind="-")
	public void setUserGroupLocalService(UserGroupLocalService userGroupLocalService) {
		this.userGroupLocalService = userGroupLocalService;
	}
	
	@Reference(unbind="-")
	public void setOrganizationLocalService(OrganizationLocalService organizationLocalService) {
		this.organizationLocalService = organizationLocalService;
	}
	
	@Reference(unbind="-")
	public void setLayoutLocalService(LayoutLocalService layoutLocalService) {
		this.layoutLocalService = layoutLocalService;
	}

	@Reference(unbind="-")
	public void setLayoutSetLocalService(LayoutSetLocalService layoutSetLocalService) {
		this.layoutSetLocalService = layoutSetLocalService;
	}

	@Reference(unbind="-")
	public void setResourceLocalService(ResourceLocalService resourceLocalService) {
		this.resourceLocalService = resourceLocalService;
	}
	
	@Reference(unbind="-")
	public void setJournalArticleLocalService(JournalArticleLocalService journalArticleLocalService) {
		this.journalArticleLocalService = journalArticleLocalService;
	}

	@Reference(unbind="-")
	public void setUserLocalService(UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	@Reference(unbind="-")
	public void setGroupLocalService(GroupLocalService groupLocalService) {
		this.groupLocalService = groupLocalService;
	}

	private GroupLocalService groupLocalService;
	private JournalArticleLocalService journalArticleLocalService;
	private LayoutLocalService layoutLocalService;
	private LayoutSetLocalService layoutSetLocalService;
	private ResourceLocalService resourceLocalService;
	private UserLocalService userLocalService;
	private OrganizationLocalService organizationLocalService;
	private UserGroupLocalService userGroupLocalService;
}