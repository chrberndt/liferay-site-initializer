package com.chberndt.liferay.site.initializer;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.service.ResourcePermissionLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ArrayUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * 
 * @author Christian Berndt
 * 
 * MyFileImporter is a stripped version of com.liferay.commerce.product.internal.importer.CPFileImporterImpl
 *
 */
@Component(
	immediate=true,
	service=MyFileImporter.class
)
public class MyFileImporter {

	public void createRoles(JSONArray jsonArray, ServiceContext serviceContext) throws PortalException {
		
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			JSONObject actionsJSONObject = jsonObject.getJSONObject("actions");
			String name = jsonObject.getString("name");
			int scope = jsonObject.getInt("scope");
			int type = jsonObject.getInt("type");

			_log.info("Adding or updating Role " + name);

			Role role = getRole(name, type, serviceContext);

			updateActions(role, actionsJSONObject, scope, serviceContext);
		}
	}

	protected Role getRole(String name, int type, ServiceContext serviceContext) throws PortalException {

		Map<Locale, String> titleMap = new HashMap<>();

		titleMap.put(serviceContext.getLocale(), name);

		Role role = _roleLocalService.fetchRole(serviceContext.getCompanyId(), name);

		if (role == null) {
			role = _roleLocalService.addRole(serviceContext.getUserId(), null, 0, name, titleMap, null, type, null,
					serviceContext);
		}

		return role;
	}

	protected void updateAction(Role role, String resource, String actionId, int scope, ServiceContext serviceContext)
			throws PortalException {

		if (scope == ResourceConstants.SCOPE_COMPANY) {
			_resourcePermissionLocalService.addResourcePermission(serviceContext.getCompanyId(), resource, scope,
					String.valueOf(role.getCompanyId()), role.getRoleId(), actionId);
		} else if (scope == ResourceConstants.SCOPE_GROUP_TEMPLATE) {
			_resourcePermissionLocalService.addResourcePermission(serviceContext.getCompanyId(), resource,
					ResourceConstants.SCOPE_GROUP_TEMPLATE, String.valueOf(GroupConstants.DEFAULT_PARENT_GROUP_ID),
					role.getRoleId(), actionId);
		} else if (scope == ResourceConstants.SCOPE_GROUP) {
			_resourcePermissionLocalService.removeResourcePermissions(serviceContext.getCompanyId(), resource,
					ResourceConstants.SCOPE_GROUP, role.getRoleId(), actionId);

			_resourcePermissionLocalService.addResourcePermission(serviceContext.getCompanyId(), resource,
					ResourceConstants.SCOPE_GROUP, String.valueOf(serviceContext.getScopeGroupId()), role.getRoleId(),
					actionId);
		}
	}

	protected void updateActions(Role role, JSONObject jsonObject, int scope, ServiceContext serviceContext)
			throws PortalException {

		String resource = jsonObject.getString("resource");
		JSONArray actionIdsJSONArray = jsonObject.getJSONArray("actionIds");

		for (int i = 0; i < actionIdsJSONArray.length(); i++) {
			String actionId = actionIdsJSONArray.getString(i);

			updateAction(role, resource, actionId, scope, serviceContext);
		}
	}

	protected void updatePermissions(long companyId, String name, String primKey, JSONArray jsonArray)
			throws PortalException {

		if (jsonArray == null) {
			jsonArray = JSONFactoryUtil
					.createJSONArray("[{\"actionIds\": [\"VIEW\"], \"roleName\": \"Site Member\"," + "\"scope\": 4}]");
		}

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			int scope = jsonObject.getInt("scope");

			String roleName = jsonObject.getString("roleName");

			Role role = _roleLocalService.getRole(companyId, roleName);

			String[] actionIds = new String[0];

			JSONArray actionIdsJSONArray = jsonObject.getJSONArray("actionIds");

			if (actionIdsJSONArray != null) {
				for (int j = 0; j < actionIdsJSONArray.length(); j++) {
					actionIds = ArrayUtil.append(actionIds, actionIdsJSONArray.getString(j));
				}
			}

			_resourcePermissionLocalService.setResourcePermissions(companyId, name, scope, primKey, role.getRoleId(),
					actionIds);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(MyFileImporter.class);

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private ResourcePermissionLocalService _resourcePermissionLocalService;

	@Reference
	private RoleLocalService _roleLocalService;

}
