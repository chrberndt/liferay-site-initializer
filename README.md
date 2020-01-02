# liferay-site-initializer
Explore the capabilities of Liferay's SiteInitializer interface

## How To Run

* ...
1. Create a directory `$WORKSPACE_HOME/configs/local/deploy`
1. Store your (developer) license in `$WORKSPACE_HOME/configs/local/deploy` so that it gets automatically deployed whenever you run the `initBundle` task

## Tools and Versions

### gradle.properties

```groovy
liferay.workspace.bundle.token.download=true
liferay.workspace.bundle.url=https://api.liferay.com/downloads/portal/7.2.10.1/liferay-dxp-tomcat-7.2.10.1-sp1-20191009103614075.7z
liferay.workspace.target.platform.version=7.2.10.1
```


