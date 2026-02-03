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
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 126, 57),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 182, 69),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 126, 57),
      ))
    }

public val Res.string.confirm_delete: StringResource by lazy {
      StringResource("string:confirm_delete", "confirm_delete", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 184, 46),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 252, 74),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 184, 46),
      ))
    }

public val Res.string.`continue`: StringResource by lazy {
      StringResource("string:continue", "continue", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 231, 28),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 327, 32),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 231, 28),
      ))
    }

public val Res.string.copied: StringResource by lazy {
      StringResource("string:copied", "copied", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 260, 22),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 360, 46),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 260, 22),
      ))
    }

public val Res.string.copy: StringResource by lazy {
      StringResource("string:copy", "copy", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 283, 20),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 407, 40),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 283, 20),
      ))
    }

public val Res.string.delete: StringResource by lazy {
      StringResource("string:delete", "delete", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 404, 22),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 584, 34),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 404, 22),
      ))
    }

public val Res.string.delete_color: StringResource by lazy {
      StringResource("string:delete_color", "delete_color", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 304, 44),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 448, 60),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 304, 44),
      ))
    }

public val Res.string.delete_palette: StringResource by lazy {
      StringResource("string:delete_palette", "delete_palette", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 349, 54),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 509, 74),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 349, 54),
      ))
    }

public val Res.string.dominant_colors: StringResource by lazy {
      StringResource("string:dominant_colors", "dominant_colors", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 427, 43),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 619, 71),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 427, 43),
      ))
    }

public val Res.string.extract_palette: StringResource by lazy {
      StringResource("string:extract_palette", "extract_palette", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 471, 43),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 691, 63),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 471, 43),
      ))
    }

public val Res.string.extraction_method: StringResource by lazy {
      StringResource("string:extraction_method", "extraction_method", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 515, 49),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 755, 69),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 515, 49),
      ))
    }

public val Res.string.floor_color: StringResource by lazy {
      StringResource("string:floor_color", "floor_color", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 565, 39),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 825, 63),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 565, 39),
      ))
    }

public val Res.string.harmonious_colors: StringResource by lazy {
      StringResource("string:harmonious_colors", "harmonious_colors", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 605, 49),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 889, 77),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 605, 49),
      ))
    }

public val Res.string.k_means_clustering: StringResource by lazy {
      StringResource("string:k_means_clustering", "k_means_clustering", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 655, 50),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 967, 86),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 655, 50),
      ))
    }

public val Res.string.lab: StringResource by lazy {
      StringResource("string:lab", "lab", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 706, 23),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1054, 43),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 706, 23),
      ))
    }

public val Res.string.light_color: StringResource by lazy {
      StringResource("string:light_color", "light_color", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 730, 39),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1098, 59),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 730, 39),
      ))
    }

public val Res.string.median_cut: StringResource by lazy {
      StringResource("string:median_cut", "median_cut", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 770, 34),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1158, 54),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 770, 34),
      ))
    }

public val Res.string.not_canceled_action: StringResource by lazy {
      StringResource("string:not_canceled_action", "not_canceled_action", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 805, 67),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1213, 99),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 805, 67),
      ))
    }

public val Res.string.palettes: StringResource by lazy {
      StringResource("string:palettes", "palettes", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 873, 28),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1313, 36),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 873, 28),
      ))
    }

public val Res.string.select_photo: StringResource by lazy {
      StringResource("string:select_photo", "select_photo", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 902, 36),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1350, 52),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 902, 36),
      ))
    }

public val Res.string.selected_photo: StringResource by lazy {
      StringResource("string:selected_photo", "selected_photo", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 939, 42),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1403, 58),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 939, 42),
      ))
    }

public val Res.string.sphere_color: StringResource by lazy {
      StringResource("string:sphere_color", "sphere_color", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 982, 40),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1462, 48),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 982, 40),
      ))
    }

public val Res.string.title: StringResource by lazy {
      StringResource("string:title", "title", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1023, 29),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1511, 29),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1023, 29),
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
  map.put("palettes", Res.string.palettes)
  map.put("select_photo", Res.string.select_photo)
  map.put("selected_photo", Res.string.selected_photo)
  map.put("sphere_color", Res.string.sphere_color)
  map.put("title", Res.string.title)
}
