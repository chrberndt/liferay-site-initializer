# liferay-site-initializer

## Introduction


Explore the capabilities of Liferay's SiteInitializer interface. Implementation based on the model of 

* https://github.com/liferay/liferay-portal/tree/master/modules/apps/frontend-theme-fjord/frontend-theme-fjord-site-initializer

## Implementation

### Add required dependencies in `build.gradle`

1. Add 	`compileOnly group: "com.liferay", name: "com.liferay.site.api"`dependency in `build.gradle` in order to use the `com.liferay.site.initializer.SiteInitializer` interface
1. Add `compileOnly group: "com.liferay.portal", name: "com.liferay.portal.kernel"`dependency in `build.gradle`in order to use `com.liferay.site.exception.InitializationException` etc.
1. Add `compileOnly group: "com.liferay", name: "com.liferay.petra.string"`dependency in `build.gradle`in order to use `com.liferay.petra.string.StringPool` et al.
1. (Optional) Add `compileOnly group: "javax.servlet", name: "javax.servlet-api"` in order to use `javax.servlet.ServletContext`
1. `+` any Liferay API modules you need to initialize your theme site, e.g. `com.liferay.fragment.api`, `com.liferay.layout.api`, etc.

### Implement `initialize()` method

1. Break down initialization tasks into manageable private helper methods, e.g. `_createServiceContext`, ...
1. Wrap initialization code with a try-catch block, catch any `Exception` that might occur and throw an `InitializationException`.

### Deploy

After you have deployed the module to your server, you should see a message like the following in your server's log: 

```
	2020-01-03 10:16:09.676 INFO  [pipe-start 1113][BundleStartStopLogger:39] STARTED com.chberndt.liferay.site.initializer_1.0.0 [1113]
```




## How To Run

1. `git clone git@github.com:chrberndt/liferay-site-initializer.git` into `$WORKSPACE_HOME/modules`
1. Refresh the liferay-workspace with _Gradle → Refresh Gradle Project_ `CTRL + F5`
1. ...
1. Create a directory `$WORKSPACE_HOME/configs/local/deploy`
1. Store your (developer) license in `$WORKSPACE_HOME/configs/local/deploy` so that it gets automatically deployed whenever you run the `initBundle` task

## Tools and Versions

### Liferay Developer Studio

Version: 3.7.1.201910160309-ga2

### `gradle.properties`

```groovy
liferay.workspace.bundle.token.download=true
liferay.workspace.bundle.url=https://api.liferay.com/downloads/portal/7.2.10.1/liferay-dxp-tomcat-7.2.10.1-sp1-20191009103614075.7z
liferay.workspace.target.platform.version=7.2.10.1
```

### `settings.gradle`

```
buildscript {
	dependencies {
		classpath group: "com.liferay", name: "com.liferay.gradle.plugins.workspace", version: "2.1.5"
		classpath group: "net.saliman", name: "gradle-properties-plugin", version: "1.4.6"
	}

	repositories {
		maven {
			url "https://repository-cdn.liferay.com/nexus/content/groups/public"
		}
	}
}

apply plugin: "net.saliman.properties"

apply plugin: "com.liferay.workspace"
```


