package com.chberndt.liferay.site.initializer;

import com.liferay.portal.kernel.util.StringUtil;

import java.io.IOException;

import org.osgi.service.component.annotations.Component;

/**
 * 
 * @author Christian Berndt
 *
 */
@Component(
	immediate=true,
	service=MySiteInitializerDependencyResolver.class
)
public class MySiteInitializerDependencyResolver {
	
	public String getDependenciesPath() {
		return _DEPENDENCIES_PATH;
	}

	public String getJSON(String name) throws IOException {
		return StringUtil.read(MySiteInitializerDependencyResolver.class.getClassLoader(), 
			_DEPENDENCIES_PATH + name);
	}

	private static final String _DEPENDENCIES_PATH =
		"com/chberndt/liferay/site/initializer/dependencies/";

}
