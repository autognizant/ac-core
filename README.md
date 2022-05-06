# ![image](https://user-images.githubusercontent.com/12494447/167223009-53630e45-3d87-4371-8f98-bbd22e54563a.png)

# Simplify web test automation testing !

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [Autognizant Framework](#autognizant-framework)
  - [Changelog](#changelog)
  - [Maven dependency](#maven-dependency)
- [Documentation](#documentation)
  - [Pre-requisites](#pre-requisites)
  - [Setting up Example Project](#setting-up-example-project)
  - [Running test](#running-test)
  - [Execution results](#execution-results)
- [Appendix](#appendix)
  - [Building](#building)
  - [License](#license)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Autognizant Framework

Autognizant Framework is a behavior driven development (BDD) approach to write automation test script to test Web application. It enables you to write and execute automated acceptance/functional tests. It is cross-platform, open source and free. Automate your test cases with minimal coding.

### Changelog

All changes can be seen in the linked [changelog](CHANGELOG.md).

### Maven dependency

```xml
<dependency>
    <groupId>com.autognizant</groupId>
    <artifactId>ac-core</artifactId>
    <version>Check the version number above</version>
</dependency>
```

## Documentation
### Pre-requisites
- <a href="https://java.com/en/download/manual.jsp" target="_blank">Java >16</a>
- <a href="https://maven.apache.org/download.cgi" target="_blank">Maven >3.8.1</a>
- <a href="https:https://eclipse.org/downloads/" target="_blank">Eclipse</a>
- Eclipse Plugins
  - <a href="http://download.eclipse.org/technology/m2e/releases/1.4" target="_blank">Maven</a> 
  - <a href="http://cucumber.github.io/cucumber-eclipse/update-site/" target="_blank">Cucumber</a>
  
### Setting up Example Project
- Install Java and set path.
- Install Maven and set path.
- Clone respective repository or download zip.
	- maven : https://github.com/autognizant/ac-core

### Running test

Go to your project directory from terminal and hit following commands

```
* Update autognizant.config file(src/test/resources/config) to configure framework level properties.
* Update configuration.properties file(src/test/resources/config) to configure execution parameters for test run.
* mvn clean verify (defualt will run cucumber features/tags methioned RunCucumberTest.java).
* mvn clean verify -Dcucumber.filter.tags="@ACAUTO-Scenario1" (this will run specified cucumber tags).
* mvn clean verify -Dcucumber.features="src/test/resources/features/login.feature" (this will run specified cucumber features).
```

### Execution results

The report is generated inside the target/cucumber-html-reports directory. Open overview-features.html file to view the reports

## Appendix

### Building

Autognizant requires Java >= 16 and Maven >= 3.8.1.

It is available in [Maven central](https://search.maven.org/search?q=g:com.autognizant%20AND%20a:ac-core).

### License

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
