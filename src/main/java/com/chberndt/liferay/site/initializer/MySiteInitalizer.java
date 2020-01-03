package com.chberndt.liferay.site.initializer;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.site.exception.InitializationException;
import com.liferay.site.initializer.SiteInitializer;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;

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
	
	private static final String _THEME_NAME = "My Theme";
	
	private static final Log _log = LogFactoryUtil.getLog(MySiteInitalizer.class);

}