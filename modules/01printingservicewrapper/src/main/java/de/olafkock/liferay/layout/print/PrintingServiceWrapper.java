package de.olafkock.liferay.layout.print;

import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalServiceWrapper;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceWrapper;

@Component(
	immediate = true,
	property = {
	},
	service = ServiceWrapper.class
)
public class PrintingServiceWrapper extends LayoutLocalServiceWrapper {

	public PrintingServiceWrapper() {
		super(null);
	}

	@Override
	public Layout addLayout(long userId, long groupId, boolean privateLayout, long parentLayoutId,
			Map<Locale, String> nameMap, Map<Locale, String> titleMap, Map<Locale, String> descriptionMap,
			Map<Locale, String> keywordsMap, Map<Locale, String> robotsMap, String type, String typeSettings,
			boolean hidden, Map<Locale, String> friendlyURLMap, ServiceContext serviceContext) throws PortalException {

		System.out.println("*** addLayout");
		System.out.println("* parentLayoutId: " + parentLayoutId);
		System.out.println("* nameMap: " + nameMap.get(nameMap.keySet().iterator().next())); // any one value
		System.out.println("* type: " + type);
		System.out.println("* typeSettings: " + typeSettings);

		return super.addLayout(userId, groupId, privateLayout, parentLayoutId, nameMap, titleMap, descriptionMap, keywordsMap, robotsMap, type, typeSettings, hidden, friendlyURLMap, serviceContext);
	}

}