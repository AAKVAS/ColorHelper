@file:OptIn(InternalResourceApi::class)

package com.example

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.LanguageQualifier
import org.jetbrains.compose.resources.ResourceItem
import org.jetbrains.compose.resources.StringResource

private const val MD: String = "composeResources/com.example/"

public val Res.string.add: StringResource by lazy {
      StringResource("string:add", "add", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 10, 15),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 10, 35),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 10, 15),
      ))
    }

public val Res.string.app_name: StringResource by lazy {
      StringResource("string:app_name", "app_name", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 26, 32),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 46, 32),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 26, 32),
      ))
    }

public val Res.string.cancel: StringResource by lazy {
      StringResource("string:cancel", "cancel", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 59, 22),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 79, 30),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 59, 22),
      ))
    }

public val Res.string.confirm_delete: StringResource by lazy {
      StringResource("string:confirm_delete", "confirm_delete", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 82, 46),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 110, 74),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 82, 46),
      ))
    }

public val Res.string.copied: StringResource by lazy {
      StringResource("string:copied", "copied", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 129, 22),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 185, 46),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 129, 22),
      ))
    }

public val Res.string.copy: StringResource by lazy {
      StringResource("string:copy", "copy", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 152, 20),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 232, 40),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 152, 20),
      ))
    }

public val Res.string.delete: StringResource by lazy {
      StringResource("string:delete", "delete", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 273, 22),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 409, 34),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 273, 22),
      ))
    }

public val Res.string.delete_color: StringResource by lazy {
      StringResource("string:delete_color", "delete_color", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 173, 44),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 273, 60),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 173, 44),
      ))
    }

public val Res.string.delete_palette: StringResource by lazy {
      StringResource("string:delete_palette", "delete_palette", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 218, 54),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 334, 74),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 218, 54),
      ))
    }

public val Res.string.floor_color: StringResource by lazy {
      StringResource("string:floor_color", "floor_color", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 296, 39),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 444, 63),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 296, 39),
      ))
    }

public val Res.string.harmonious_colors: StringResource by lazy {
      StringResource("string:harmonious_colors", "harmonious_colors", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 336, 49),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 508, 77),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 336, 49),
      ))
    }

public val Res.string.lab: StringResource by lazy {
      StringResource("string:lab", "lab", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 386, 23),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 586, 43),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 386, 23),
      ))
    }

public val Res.string.light_color: StringResource by lazy {
      StringResource("string:light_color", "light_color", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 410, 39),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 630, 59),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 410, 39),
      ))
    }

public val Res.string.not_canceled_action: StringResource by lazy {
      StringResource("string:not_canceled_action", "not_canceled_action", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 450, 67),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 690, 99),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 450, 67),
      ))
    }

public val Res.string.palettes: StringResource by lazy {
      StringResource("string:palettes", "palettes", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 518, 28),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 790, 36),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 518, 28),
      ))
    }

public val Res.string.sphere_color: StringResource by lazy {
      StringResource("string:sphere_color", "sphere_color", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 547, 40),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 827, 48),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 547, 40),
      ))
    }

public val Res.string.title: StringResource by lazy {
      StringResource("string:title", "title", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 588, 29),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 876, 29),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 588, 29),
      ))
    }

@InternalResourceApi
internal fun _collectCommonMainString0Resources(map: MutableMap<String, StringResource>) {
  map.put("add", Res.string.add)
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
