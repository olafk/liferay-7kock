package de.olafkock.liferay.layout.wrapper;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.service.LayoutLocalServiceWrapper;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceWrapper;

import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * This wrapper ensures that whenever a top level "portlet" page is created, it is created 
 * as a link to its first child page with the same name. That child page is also
 * automatically created by this wrapper.
 * 
 * inspired by https://web.liferay.com/community/forums/-/message_boards/view_message/87968373
 * 
 * See caveats in the README coming with this project. This is PROOF OF CONCEPT level code.
 * 
 * @author olaf
 */

@Component(
	immediate = true,
	property = {
	},
	service = ServiceWrapper.class
)
public class HierarchyEnforcerWrapper extends LayoutLocalServiceWrapper {

	public HierarchyEnforcerWrapper() {
		super(null);
	}
	@Override
	public Layout addLayout(long userId, long groupId, boolean privateLayout, long parentLayoutId,
			Map<Locale, String> nameMap, Map<Locale, String> titleMap, Map<Locale, String> descriptionMap,
			Map<Locale, String> keywordsMap, Map<Locale, String> robotsMap, String type, String typeSettings,
			boolean hidden, Map<Locale, String> friendlyURLMap, ServiceContext serviceContext) throws PortalException {

		if (parentLayoutId == 0 && !hidden && "portlet".equals(type)) {
			Layout secondLevel = super.addLayout(userId, groupId, privateLayout, 0, nameMap,
					titleMap, descriptionMap, keywordsMap, robotsMap, type, typeSettings, hidden, friendlyURLMap,
					serviceContext);
			
			String topLevelTypeSettings = "linkToLayoutId=" + secondLevel.getLayoutId() + "\n" +
					"groupId=" + secondLevel.getGroupId() + "\n" + 
					"privateLayout=" + privateLayout;
			Layout topLevel = super.addLayout(userId, groupId, privateLayout, parentLayoutId, nameMap, titleMap,
					descriptionMap, keywordsMap, robotsMap, LayoutConstants.TYPE_LINK_TO_LAYOUT, topLevelTypeSettings, hidden,
					friendlyURLMap, serviceContext);
			
			secondLevel.setParentLayoutId(topLevel.getLayoutId());
			updateLayout(secondLevel);
			
			return topLevel;
		} else {
			return super.addLayout(userId, groupId, privateLayout, parentLayoutId, nameMap, titleMap, descriptionMap,
					keywordsMap, robotsMap, type, typeSettings, hidden, friendlyURLMap, serviceContext);
		}
	}
}