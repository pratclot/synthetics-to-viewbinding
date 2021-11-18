# synthetics-to-viewbinding

![Build](https://github.com/pratclot/synthetics-to-viewbinding/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

## What this is for

This plugin allows to convert Android synthetic references in kt files to View Binding ones with just a shortcut.
It is not planned to be universal or totally reconfigurable, rather an exercise, but reasonably hard improvements could be done.

## TODO

- [ ] Have a configuration to the plugin, like let user choose how to name the *binding*, how the view's layout referenced in the fragment etc.
- [ ] Learn how to add the View Binding import directive.
- [ ] Add ktlint. 
- [ ] Add View Binding tag generation. 













## Template ToDo list
- [x] Create a new [IntelliJ Platform Plugin Template][template] project.
- [ ] Get known with the [template documentation][template].
- [x] Verify the [pluginGroup](/gradle.properties), [plugin ID](/src/main/resources/META-INF/plugin.xml) and [sources package](/src/main/kotlin).
- [ ] Review the [Legal Agreements](https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html).
- [ ] [Publish a plugin manually](https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate) for the first time.
- [ ] Set the Plugin ID in the above README badges.
- [ ] Set the [Deployment Token](https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html).
- [ ] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be notified about releases containing new features and fixes.

<!-- Plugin description -->
A simple opinionated plugin to automate migration from Kotlin synthetic extensions to View Binding.
Not all cases are covered, do not hesitate to raise an issue on GitHub with an example! 

Use "Generate..." menu on the file to perform the conversion with "Migrate fragment to ViewBinding" action. 
Use "Add ViewBinding methods" action to insert methods to set and retrieve the binding using the view tag trick from [here](https://www.reddit.com/r/androiddev/comments/eo8rou/comment/fea42o9/?utm_source=share&utm_medium=web2x&context=3).

[link to github](https://github.com/pratclot/synthetics-to-viewbinding)
<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "synthetics-to-viewbinding"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/pratclot/synthetics-to-viewbinding/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
