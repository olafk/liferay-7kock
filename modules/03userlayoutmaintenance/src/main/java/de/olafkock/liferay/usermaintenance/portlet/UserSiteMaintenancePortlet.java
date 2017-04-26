package de.olafkock.liferay.usermaintenance.portlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalArticleConstants;
import com.liferay.journal.model.JournalFolderConstants;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.model.PortletConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.LayoutSetLocalService;
import com.liferay.portal.kernel.service.OrganizationLocalService;
import com.liferay.portal.kernel.service.ResourceLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserGroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.service.permission.PortletPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;


/**
 * Inspired by the original sevencogs code, this is one usecase (largely hard coded) to 
 * demonstrate the use of Liferay's API to manipulate a particular site, in this case a 
 * user's site. While you can manipulate them uniformly through User Groups, some times 
 * you need more flexibility. And the API can deliver.
 * 
 * Careful: If you change a large number of user accounts this way, migrating them to 
 * next version might take a long time. See more caveats in the README coming with this 
 * project. This is PROOF OF CONCEPT level code.
 * 
 * @author olaf
 */


@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.instanceable=false",
		"javax.portlet.display-name=User Layout Maintenance Portlet",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class UserSiteMaintenancePortlet extends MVCPortlet {

	public void addLayouts(ActionRequest actionRequest, ActionResponse actionResponse)
			throws Exception {
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

		String pageName = ParamUtil.getString(actionRequest, "pageName");
		System.out.println("ACTION with page name " + pageName);
//		List<User> users = userLocalService.getUsers(0, 10); // simple Demo
		List<User> users = new LinkedList<User>();
		users.add(userLocalService.getUserByEmailAddress(themeDisplay.getCompanyId(), "horst@horstmann.de"));

		for (User user : users) {
			if(!user.isDefaultUser()) {
				Group userSite;
				try {
					userSite = user.getGroup(); // groupLocalService.getUserGroup(themeDisplay.getCompanyId(), user.getUserId());
					userSite = groupLocalService.getUserGroup(themeDisplay.getCompanyId(), user.getUserId());
					System.out.println("User " + user.getFullName() + " has site id " + userSite.getGroupId());

					Layout layout = addLayout(
							userSite, pageName, false, "/" + pageName, "1_2_1_columns");
					System.out.println("created layout " + layout.getLayoutId() + " for user " + user.getFullName());
					
					// Let's show a custom PortletInformation portlet on the page, in column 1:
					
					addPortletId(layout, "com_liferay_olafkock_sevencogs_PortletInformationPortlet", "column-1");
					
					// And a JournalContentDisplay portlet, with a new article
					
					String portletId = addPortletId(
						layout, "com_liferay_journal_content_web_portlet_JournalContentPortlet", "column-2");

					System.out.println("configured portletId " + portletId);

					removePortletBorder(layout, portletId);

					ServiceContext serviceContext = new ServiceContext();
					// These tags will be applied to the new journalArticle
					serviceContext.setAssetTagNames(new String[] {"tag1", "tag2"});
					// figured out this is required due to NPE if not set.
					serviceContext.setScopeGroupId(userSite.getGroupId());

					JournalArticle journalArticle = addJournalArticle(
						user.getUserId(), userSite.getGroupId(),
						"Public Pages " + user.getScreenName(), DEMOARTICLE, serviceContext);

					System.out.println("added journal article " + journalArticle.getArticleId());
					
					configureJournalContent(
						layout, portletId, journalArticle.getArticleId());

					System.out.println("...and configured the portlet to display it");
					
					// And page information in column 4
					
					addPortletId(layout, "com_liferay_olafkock_sevencogs_PageInformationPortlet", "column-4");
					
				} catch (PortalException e) {
					System.err.println(e.getClass().getName() + " " + e.getMessage());
				}
			}
		}
	}

	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
	
		super.doView(renderRequest, renderResponse);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Helper methods. All stolen & adapted from the old sevencogs samples

	/**
	 * Adapted from original SevenCogs sample code
	 * 
	 * @param group
	 * @param name
	 * @param privateLayout
	 * @param friendlyURL
	 * @param layouteTemplateId
	 * @return
	 * @throws Exception
	 */
	
	protected Layout addLayout(Group group, String name, boolean privateLayout, String friendlyURL,
			String layouteTemplateId) throws Exception {

		ServiceContext serviceContext = new ServiceContext();

		Layout layout = layoutLocalService.addLayout(group.getCreatorUserId(), group.getGroupId(), privateLayout,
				LayoutConstants.DEFAULT_PARENT_LAYOUT_ID, name, name, name,
				LayoutConstants.TYPE_PORTLET, false, friendlyURL, serviceContext);

		LayoutTypePortlet layoutTypePortlet = (LayoutTypePortlet) layout.getLayoutType();

		layoutTypePortlet.setLayoutTemplateId(0, layouteTemplateId, false);

		return layout;
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
	protected String addPortletId(Layout layout, String portletId, String columnId) throws Exception {

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
	protected void addResources(Layout layout, String portletId) throws Exception {

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

	protected void removePortletBorder(Layout layout, String portletId)
		throws Exception {

		PortletPreferences portletSetup =
			PortletPreferencesFactoryUtil.getLayoutPortletSetup(
				layout, portletId);

		portletSetup.setValue(
			"portletSetupShowBorders", String.valueOf(Boolean.FALSE));

		portletSetup.store();
	}

	protected JournalArticle addJournalArticle(
			long userId, long groupId, String title, String content,
			ServiceContext serviceContext)
		throws Exception {

		return addJournalArticle(
			userId, groupId, title, content, "BASIC-WEB-CONTENT",
			"BASIC-WEB-CONTENT", serviceContext);
	}


	protected JournalArticle addJournalArticle(
			long userId, long groupId, String title, String content,
			String structureId, String templateId,
			ServiceContext serviceContext)
		throws Exception {

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);

		Map<Locale, String> titleMap = new HashMap<Locale, String>();

		setLocalizedValue(titleMap, title);

		JournalArticle journalArticle =
			journalArticleLocalService.addArticle(
				userId, groupId, JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID, 
				0, 0, StringPool.BLANK, true, 
				JournalArticleConstants.VERSION_DEFAULT, titleMap, null, 
				content, structureId, templateId, StringPool.BLANK,
				1, 1, 2017, 0, 0, 0, 0, 0, 0, 0, true, 0, 0, 0, 0, 0, true,
				true, false, StringPool.BLANK, null, null, StringPool.BLANK, 
				serviceContext);
		
		
		journalArticleLocalService.updateStatus(userId, groupId, journalArticle.getArticleId(),
				journalArticle.getVersion(), WorkflowConstants.STATUS_APPROVED, StringPool.BLANK,
				new HashMap<String, Serializable>(), serviceContext);

		return journalArticle;
	}

	protected void setLocalizedValue(Map<Locale, String> map, String value) {
		Locale locale = LocaleUtil.getDefault();

		map.put(locale, value);

		if (!locale.equals(Locale.US)) {
			map.put(Locale.US, value);
		}
	}

	protected void configureJournalContent(
			Layout layout, String portletId, String articleId)
		throws Exception {

		PortletPreferences portletSetup =
			PortletPreferencesFactoryUtil.getLayoutPortletSetup(
				layout, portletId);

		portletSetup.setValue("groupId", String.valueOf(layout.getGroupId()));
		portletSetup.setValue("articleId", articleId);

		portletSetup.store();
	}

	///////////////////////////////////
	// Services that we might use
	
	@Reference(unbind="-")
	public void setUserLocalService(UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
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

	private final static String DEMOARTICLE = "<?xml version=\"1.0\"?>\n" + 
			"\n" + 
			"<root available-locales=\"en_US,\" default-locale=\"\">\n" + 
			"	<dynamic-element name=\"content\" type=\"text_area\" index-type=\"keyword\" instance-id=\"hnut\">\n" +
			"     <dynamic-content language=\"en-US\">" +
			"		<![CDATA[\n" + 
			"			<h2>\n" + 
			"				This is just some static demo article\n" + 
			"			</h2>\n" + 
			"\n" + 
			"			<p>\n" + 
			"				There's nothing else to see here but a random number: " + Math.random() +  
			"			</p>\n" + 
			"		]]>\n" + 
			"     </dynamic-content>\n" + 
			"   </dynamic-element>\n" +
			"</root>";
}