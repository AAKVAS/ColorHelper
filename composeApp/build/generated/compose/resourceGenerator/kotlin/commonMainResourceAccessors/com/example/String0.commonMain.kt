@file:OptIn(InternalResourceApi::class)

package com.example

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

public val Res.string.color_count: StringResource by lazy {
      StringResource("string:color_count", "color_count", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 82, 43),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 110, 71),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 82, 43),
      ))
    }

public val Res.string.color_thief_algorithm: StringResource by lazy {
      StringResource("string:color_thief_algorithm", "color_thief_algorithm", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 126, 101),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 182, 129),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 126, 101),
      ))
    }

public val Res.string.confirm_delete: StringResource by lazy {
      StringResource("string:confirm_delete", "confirm_delete", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 228, 46),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 312, 74),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 228, 46),
      ))
    }

public val Res.string.`continue`: StringResource by lazy {
      StringResource("string:continue", "continue", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 275, 28),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 387, 32),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 275, 28),
      ))
    }

public val Res.string.copied: StringResource by lazy {
      StringResource("string:copied", "copied", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 304, 22),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 420, 46),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 304, 22),
      ))
    }

public val Res.string.copy: StringResource by lazy {
      StringResource("string:copy", "copy", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 327, 20),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 467, 40),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 327, 20),
      ))
    }

public val Res.string.delete: StringResource by lazy {
      StringResource("string:delete", "delete", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 448, 22),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 644, 34),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 448, 22),
      ))
    }

public val Res.string.delete_color: StringResource by lazy {
      StringResource("string:delete_color", "delete_color", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 348, 44),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 508, 60),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 348, 44),
      ))
    }

public val Res.string.delete_palette: StringResource by lazy {
      StringResource("string:delete_palette", "delete_palette", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 393, 54),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 569, 74),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 393, 54),
      ))
    }

public val Res.string.dominant_colors: StringResource by lazy {
      StringResource("string:dominant_colors", "dominant_colors", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 471, 95),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 679, 175),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 471, 95),
      ))
    }

public val Res.string.extract_palette: StringResource by lazy {
      StringResource("string:extract_palette", "extract_palette", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 567, 43),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 855, 63),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 567, 43),
      ))
    }

public val Res.string.extraction_method: StringResource by lazy {
      StringResource("string:extraction_method", "extraction_method", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 611, 49),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 919, 69),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 611, 49),
      ))
    }

public val Res.string.floor_color: StringResource by lazy {
      StringResource("string:floor_color", "floor_color", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 661, 39),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 989, 63),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 661, 39),
      ))
    }

public val Res.string.harmonious_colors: StringResource by lazy {
      StringResource("string:harmonious_colors", "harmonious_colors", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 701, 49),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1053, 77),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 701, 49),
      ))
    }

public val Res.string.k_means_clustering: StringResource by lazy {
      StringResource("string:k_means_clustering", "k_means_clustering", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 751, 106),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1131, 146),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 751, 106),
      ))
    }

public val Res.string.lab: StringResource by lazy {
      StringResource("string:lab", "lab", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 858, 23),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1278, 43),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 858, 23),
      ))
    }

public val Res.string.light_color: StringResource by lazy {
      StringResource("string:light_color", "light_color", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 882, 39),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1322, 59),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 882, 39),
      ))
    }

public val Res.string.median_cut: StringResource by lazy {
      StringResource("string:median_cut", "median_cut", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 922, 94),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1382, 166),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 922, 94),
      ))
    }

public val Res.string.not_canceled_action: StringResource by lazy {
      StringResource("string:not_canceled_action", "not_canceled_action", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1017, 67),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1549, 99),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1017, 67),
      ))
    }

public val Res.string.palette_generated: StringResource by lazy {
      StringResource("string:palette_generated", "palette_generated", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1085, 49),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1649, 57),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1085, 49),
      ))
    }

public val Res.string.palettes: StringResource by lazy {
      StringResource("string:palettes", "palettes", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1135, 28),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1707, 36),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1135, 28),
      ))
    }

public val Res.string.select_photo: StringResource by lazy {
      StringResource("string:select_photo", "select_photo", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1164, 36),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1744, 52),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1164, 36),
      ))
    }

public val Res.string.selected_photo: StringResource by lazy {
      StringResource("string:selected_photo", "selected_photo", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1201, 42),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1797, 58),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1201, 42),
      ))
    }

public val Res.string.sphere_color: StringResource by lazy {
      StringResource("string:sphere_color", "sphere_color", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1244, 40),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1856, 48),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1244, 40),
      ))
    }

public val Res.string.title: StringResource by lazy {
      StringResource("string:title", "title", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1285, 29),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1905, 29),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1285, 29),
      ))
    }

@InternalResourceApi
internal fun _collectCommonMainString0Resources(map: MutableMap<String, StringResource>) {
  map.put("add", Res.string.add)
  map.put("app_name", Res.string.app_name)
  map.put("cancel", Res.string.cancel)
  map.put("color_count", Res.string.color_count)
  map.put("color_thief_algorithm", Res.string.color_thief_algorithm)
  map.put("confirm_delete", Res.string.confirm_delete)
  map.put("continue", Res.string.`continue`)
  map.put("copied", Res.string.copied)
  map.put("copy", Res.string.copy)
  map.put("delete", Res.string.delete)
  map.put("delete_color", Res.string.delete_color)
  map.put("delete_palette", Res.string.delete_palette)
  map.put("dominant_colors", Res.string.dominant_colors)
  map.put("extract_palette", Res.string.extract_palette)
  map.put("extraction_method", Res.string.extraction_method)
  map.put("floor_color", Res.string.floor_color)
  map.put("harmonious_colors", Res.string.harmonious_colors)
  map.put("k_means_clustering", Res.string.k_means_clustering)
  map.put("lab", Res.string.lab)
  map.put("light_color", Res.string.light_color)
  map.put("median_cut", Res.string.median_cut)
  map.put("not_canceled_action", Res.string.not_canceled_action)
  map.put("palette_generated", Res.string.palette_generated)
  map.put("palettes", Res.string.palettes)
  map.put("select_photo", Res.string.select_photo)
  map.put("selected_photo", Res.string.selected_photo)
  map.put("sphere_color", Res.string.sphere_color)
  map.put("title", Res.string.title)
}
