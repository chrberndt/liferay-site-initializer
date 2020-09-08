package com.chberndt.liferay.site.initializer;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.site.exception.InitializationException;
import com.liferay.site.initializer.SiteInitializer;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Christian Berndt
 */
@Component(
	immediate = true, property = "site.initializer.key=" + MySiteInitalizer.KEY,
	service = SiteInitializer.class
)
public class MySiteInitalizer implements SiteInitializer {

	public static final String KEY = "my-site-initializer";

	@Override
	public String getDescription(Locale locale) {
		return StringPool.BLANK;
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String getName(Locale locale) {
		return _THEME_NAME;
	}

	@Override
	public String getThumbnailSrc() {
		return null;
	}

	@Override
	public void initialize(long groupId) throws InitializationException {
		try {
			_log.info("MySiteInitializer.initialize()");

			ServiceContext serviceContext = getServiceContext(groupId);

			createRoles(serviceContext);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new InitializationException(e);
		}
	}

	@Override
	public boolean isActive(long companyId) {
		return true;
	}

	protected void createRoles(ServiceContext serviceContext) throws Exception {
		_log.info("MySiteInitializer.createRoles()");

		JSONArray jsonArray = _getJSONArray("roles.json");

		_myFileImporter.createRoles(jsonArray, serviceContext);
	}

	protected ServiceContext getServiceContext(long groupId)
		throws PortalException {

		User user = _userLocalService.getUser(PrincipalThreadLocal.getUserId());
		Group group = _groupLocalService.getGroup(groupId);

		Locale locale = LocaleUtil.getSiteDefault();

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setCompanyId(group.getCompanyId());
		serviceContext.setLanguageId(_language.getLanguageId(locale));
		serviceContext.setScopeGroupId(groupId);
		serviceContext.setTimeZone(user.getTimeZone());
		serviceContext.setUserId(user.getUserId());

		return serviceContext;
	}

	private JSONArray _getJSONArray(String name) throws Exception {
		return _jsonFactory.createJSONArray(
			_siteInitializerDependencyResolver.getJSON(name));
	}

	private static final String _THEME_NAME = "My Theme";

	private static final Log _log = LogFactoryUtil.getLog(
		MySiteInitalizer.class);

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private Language _language;

	@Reference
	private MyFileImporter _myFileImporter;

	@Reference
	private MySiteInitializerDependencyResolver
		_siteInitializerDependencyResolver;

	@Reference
	private UserLocalService _userLocalService;

}