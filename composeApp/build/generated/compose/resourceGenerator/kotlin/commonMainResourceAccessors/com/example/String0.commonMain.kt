@file:OptIn(InternalResourceApi::class)

package com.example

import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.ResourceItem
import org.jetbrains.compose.resources.StringResource

private const val MD: String = "composeResources/com.example/"

public val Res.string.app_name: StringResource by lazy {
      StringResource("string:app_name", "app_name", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 10, 32),
      ))
    }

public val Res.string.copied: StringResource by lazy {
      StringResource("string:copied", "copied", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 43, 46),
      ))
    }

public val Res.string.copy: StringResource by lazy {
      StringResource("string:copy", "copy", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 90, 40),
      ))
    }

public val Res.string.floor_color: StringResource by lazy {
      StringResource("string:floor_color", "floor_color", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 131, 63),
      ))
    }

public val Res.string.light_color: StringResource by lazy {
      StringResource("string:light_color", "light_color", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 195, 59),
      ))
    }

public val Res.string.sphere_color: StringResource by lazy {
      StringResource("string:sphere_color", "sphere_color", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 255, 48),
      ))
    }

public val Res.string.title: StringResource by lazy {
      StringResource("string:title", "title", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 304, 29),
      ))
    }

@InternalResourceApi
internal fun _collectCommonMainString0Resources(map: MutableMap<String, StringResource>) {
  map.put("app_name", Res.string.app_name)
  map.put("copied", Res.string.copied)
  map.put("copy", Res.string.copy)
  map.put("floor_color", Res.string.floor_color)
  map.put("light_color", Res.string.light_color)
  map.put("sphere_color", Res.string.sphere_color)
  map.put("title", Res.string.title)
}
