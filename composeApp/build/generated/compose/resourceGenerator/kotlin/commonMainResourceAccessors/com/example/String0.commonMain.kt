@file:OptIn(InternalResourceApi::class)

package com.example

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.ResourceItem
import org.jetbrains.compose.resources.StringResource

private const val MD: String = "composeResources/com.example/"

public val Res.string.app_name: StringResource by lazy {
      StringResource("string:app_name", "app_name", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 10, 32),
      ))
    }

public val Res.string.cancel: StringResource by lazy {
      StringResource("string:cancel", "cancel", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 43, 30),
      ))
    }

public val Res.string.confirm_delete: StringResource by lazy {
      StringResource("string:confirm_delete", "confirm_delete", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 74, 74),
      ))
    }

public val Res.string.copied: StringResource by lazy {
      StringResource("string:copied", "copied", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 149, 46),
      ))
    }

public val Res.string.copy: StringResource by lazy {
      StringResource("string:copy", "copy", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 196, 40),
      ))
    }

public val Res.string.delete: StringResource by lazy {
      StringResource("string:delete", "delete", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 373, 34),
      ))
    }

public val Res.string.delete_color: StringResource by lazy {
      StringResource("string:delete_color", "delete_color", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 237, 60),
      ))
    }

public val Res.string.delete_palette: StringResource by lazy {
      StringResource("string:delete_palette", "delete_palette", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 298, 74),
      ))
    }

public val Res.string.floor_color: StringResource by lazy {
      StringResource("string:floor_color", "floor_color", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 408, 63),
      ))
    }

public val Res.string.harmonious_colors: StringResource by lazy {
      StringResource("string:harmonious_colors", "harmonious_colors", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 472, 77),
      ))
    }

public val Res.string.lab: StringResource by lazy {
      StringResource("string:lab", "lab", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 550, 43),
      ))
    }

public val Res.string.light_color: StringResource by lazy {
      StringResource("string:light_color", "light_color", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 594, 59),
      ))
    }

public val Res.string.not_canceled_action: StringResource by lazy {
      StringResource("string:not_canceled_action", "not_canceled_action", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 654, 99),
      ))
    }

public val Res.string.palettes: StringResource by lazy {
      StringResource("string:palettes", "palettes", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 754, 36),
      ))
    }

public val Res.string.sphere_color: StringResource by lazy {
      StringResource("string:sphere_color", "sphere_color", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 791, 48),
      ))
    }

public val Res.string.title: StringResource by lazy {
      StringResource("string:title", "title", setOf(
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 840, 29),
      ))
    }

@InternalResourceApi
internal fun _collectCommonMainString0Resources(map: MutableMap<String, StringResource>) {
  map.put("app_name", Res.string.app_name)
  map.put("cancel", Res.string.cancel)
  map.put("confirm_delete", Res.string.confirm_delete)
  map.put("copied", Res.string.copied)
  map.put("copy", Res.string.copy)
  map.put("delete", Res.string.delete)
  map.put("delete_color", Res.string.delete_color)
  map.put("delete_palette", Res.string.delete_palette)
  map.put("floor_color", Res.string.floor_color)
  map.put("harmonious_colors", Res.string.harmonious_colors)
  map.put("lab", Res.string.lab)
  map.put("light_color", Res.string.light_color)
  map.put("not_canceled_action", Res.string.not_canceled_action)
  map.put("palettes", Res.string.palettes)
  map.put("sphere_color", Res.string.sphere_color)
  map.put("title", Res.string.title)
}
