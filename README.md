# MagicLib

English | [中文](./README_ZH_CN.md)

⚠️**Warning: The project is still in the early development stage.**

❗Before reporting a problem, be sure to try the latest [beta version](https://github.com/Hendrix-Shen/MagicLib/releases) to check if the problem still exists.

## Description

A library of versatile mod dependencies.

## Dependencies

| Dependency | Type     | Environment     | Link                                                                                                                                                                                              |
|------------|----------|-----------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| MaFgLib    | Optional | Client          | [CurseForge](https://www.curseforge.com/minecraft/mc-mods/mafglib) &#124; [Modrinth](https://modrinth.com/mod/mafglib) &#124; [GitHub](https://github.com/ThinkingStudios/MaLiLib-Forge/releases) |

## Feature

### Compat API
- Multi-version compatibility, yes, we have used a number of means to make it compatible with all the latest releases of Minecraft (1.16+) running on the (Neo)Forge. How is this done? Magic :(
- We have written compatibility APIs for balancing the differences between versions. For developers maintaining multiple MC versions of mods at the same time, it is possible to ignore Mojang's changes to MC to a certain extent. You don't have to care about what Mojang has done, using the API you will easily be able to use the same code between MC versions.

### Dependency check

We have added a complete dependency checking system to verify dependency availability by means of expressions and even custom predicates, who can be applied in a variety of scenarios, e.g.
- MagicLibMixinPlugin provides additional dependency checking for the mod, for example by setting different dependencies for the client side and the server side.
- Dependency checking can also be applied to Mixin, and Mixin will only be applied if the conditions are met.
- Dependency checking can also be applied to configuration management so that the configuration item is only displayed if the conditions are met.
- Further features may be included in future developments.

### I18n
- We have reimplemented I18n independent of MC and can set the list of alternate languages for MagicLib I18n. Mojang uses some tricks with I18n text to make some features of `String.format` unavailable, which is usually annoying.

### MaFgLib extensions
- We have written a very useful configuration management module for MaFgLib where we will use basic data types with Java annotations to generate configuration lists wherever possible. Also, for configuration files we have included a configuration versioning system which will help you to quickly write your own custom migration solution in the event of a major change to the configuration file structure.
- We have ported some features from a higher version of MaFgLib to make it easier to use the same features when using MaFgLib adapted to a lower version of Minecraft

## Development

### Support

Current main development for Minecraft version: 1.20.1

And use `preprocess` to be compatible with all versions.

**Note: I only accept the following versions of issues. Please note that this information is time-sensitive and any version of the issue not listed here will be closed**

- Minecraft 1.16.5
- Minecraft 1.18.2
- Minecraft 1.19.2
- Minecraft 1.20.1
- Minecraft 1.20.4 (WIP)

### Mappings

We are using the **Mojang official** mappings to de-obfuscate Minecraft and insert patches.

### Document

The English doc and the Chinese doc are aligned line by line.

## License

This project is available under the LGPLv3 license. Feel free to learn from it and incorporate it in your own projects.
