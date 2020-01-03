package com.chberndt.liferay.site.initializer;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.site.exception.InitializationException;
import com.liferay.site.initializer.SiteInitializer;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Christian Berndt
 */
@Component (
	immediate=true,
	property="site.initializer.key=" + MySiteInitalizer.KEY,
	service=SiteInitializer.class
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
		} catch (Exception e) {
			_log.error(e, e);

			throw new InitializationException(e);
		}		
	}

	@Override
	public boolean isActive(long companyId) {
		return true;
	}
	
	protected void createRoles(ServiceContext serviceContext) throws Exception {
		JSONArray jsonArray = _getJSONArray("roles.json");

		_myFileImporter.createRoles(jsonArray, serviceContext);

	}
	
	private JSONArray _getJSONArray(String name) throws Exception {
		return _jsonFactory.createJSONArray(
			_siteInitializerDependencyResolver.getJSON(name));
	}
	
	private static final String _THEME_NAME = "My Theme";
	
	private static final Log _log = LogFactoryUtil.getLog(MySiteInitalizer.class);
	
	@Reference
	private MyFileImporter _myFileImporter;
	
	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private ResourcePermissionLocalService _resourcePermissionLocalService;
	

	@Reference
	private RoleLocalService _roleLocalService;
	
	@Reference(
		target = "(site.initializer.key=" + MySiteInitalizer.KEY + ")"
	)
	private MySiteInitializerDependencyResolver
		_siteInitializerDependencyResolver;

}