# ConfigKeys [![Build Status](https://travis-ci.org/FerusTech/ConfigKeys.svg?branch=master)](https://travis-ci.org/FerusTech/ConfigKeys)
ConfigKeys is a simple wrapper for zml's [Configurate](https://github.com/zml2008/configurate), providing a more stream-lined way to access configuration on-the-fly.

## Using ConfigKeys
Using ConfigKeys is simple.

```java
private static final ConfigKey<String> VERSION = ConfigKey.of("version");
private static final HoconConfigFile CONFIG = HoconConfigFile.load(Paths.get("").resolve("config.conf"));

public static String getVersion() {
    return VERSION.get(CONFIG);
}
```

## Download
Latest Version: [![Maven Central](https://img.shields.io/maven-central/v/tech.ferus.util/ConfigKeys.svg)]()

Replace **VERSION** key below with latest version shown in button above.

**Maven**
```xml
<dependency>
    <groupId>tech.ferus.util</groupId>
    <artifactId>ConfigKeys</artifactId>
    <version>VERSION</version>
</dependency>
```

**Gradle**
```groovy
repositories {
    mavenCentral()
}

dependencies {
    compile 'tech.ferus.util:ConfigKeys:VERSION'
}
```

Builds are distributed using JCenter's Bintray [ConfigKeys JCenter Bintray](https://bintray.com/ferustech/maven/configkeys/)

## Javadocs
Can be located [here](https://ferustech.github.io/ConfigKeys/).

## Discord

If you need assistance with or have any questions about ConfigKeys, join [FerusTech's Discord](https://discord.gg/wKapeAk) server.

## Dependencies
ConfigKeys _requires_ **Java 8**.

Dependencies are managed automatically via Gradle. For a detailed list, view below.

| Name | Version | Website | Repository |
| ---- | ------- | ------- | ---------- |
| FindBugs | 3.0.2 | http://findbugs.sourceforge.net/ | https://github.com/findbugsproject/findbugs |
| Configurate | 3.2 | http://zml2008.github.io/ | https://github.com/zml2008/configurate |