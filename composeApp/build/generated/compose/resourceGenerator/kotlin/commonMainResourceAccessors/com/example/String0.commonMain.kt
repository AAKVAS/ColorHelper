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

public val Res.string.buffered_image: StringResource by lazy {
      StringResource("string:buffered_image", "buffered_image", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 59, 42),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 79, 74),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 59, 42),
      ))
    }

public val Res.string.cancel: StringResource by lazy {
      StringResource("string:cancel", "cancel", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 102, 22),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 154, 30),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 102, 22),
      ))
    }

public val Res.string.clear: StringResource by lazy {
      StringResource("string:clear", "clear", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 125, 21),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 185, 45),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 125, 21),
      ))
    }

public val Res.string.close_with_saving: StringResource by lazy {
      StringResource("string:close_with_saving", "close_with_saving", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 147, 45),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 231, 69),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 147, 45),
      ))
    }

public val Res.string.close_without_saving: StringResource by lazy {
      StringResource("string:close_without_saving", "close_without_saving", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 193, 56),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 301, 80),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 193, 56),
      ))
    }

public val Res.string.color_count: StringResource by lazy {
      StringResource("string:color_count", "color_count", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 250, 43),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 382, 71),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 250, 43),
      ))
    }

public val Res.string.color_thief_algorithm: StringResource by lazy {
      StringResource("string:color_thief_algorithm", "color_thief_algorithm", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 294, 101),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 454, 129),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 294, 101),
      ))
    }

public val Res.string.confirm_delete: StringResource by lazy {
      StringResource("string:confirm_delete", "confirm_delete", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 396, 46),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 584, 74),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 396, 46),
      ))
    }

public val Res.string.`continue`: StringResource by lazy {
      StringResource("string:continue", "continue", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 443, 28),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 659, 32),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 443, 28),
      ))
    }

public val Res.string.copied: StringResource by lazy {
      StringResource("string:copied", "copied", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 472, 22),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 692, 46),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 472, 22),
      ))
    }

public val Res.string.copy: StringResource by lazy {
      StringResource("string:copy", "copy", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 495, 20),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 739, 40),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 495, 20),
      ))
    }

public val Res.string.delete: StringResource by lazy {
      StringResource("string:delete", "delete", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 616, 22),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 916, 34),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 616, 22),
      ))
    }

public val Res.string.delete_color: StringResource by lazy {
      StringResource("string:delete_color", "delete_color", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 516, 44),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 780, 60),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 516, 44),
      ))
    }

public val Res.string.delete_palette: StringResource by lazy {
      StringResource("string:delete_palette", "delete_palette", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 561, 54),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 841, 74),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 561, 54),
      ))
    }

public val Res.string.dominant_colors: StringResource by lazy {
      StringResource("string:dominant_colors", "dominant_colors", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 639, 95),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 951, 175),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 639, 95),
      ))
    }

public val Res.string.extract_palette: StringResource by lazy {
      StringResource("string:extract_palette", "extract_palette", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 735, 43),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1127, 63),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 735, 43),
      ))
    }

public val Res.string.extraction_method: StringResource by lazy {
      StringResource("string:extraction_method", "extraction_method", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 779, 49),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1191, 69),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 779, 49),
      ))
    }

public val Res.string.floor_color: StringResource by lazy {
      StringResource("string:floor_color", "floor_color", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 829, 39),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1261, 63),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 829, 39),
      ))
    }

public val Res.string.harmonious_colors: StringResource by lazy {
      StringResource("string:harmonious_colors", "harmonious_colors", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 869, 49),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1325, 77),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 869, 49),
      ))
    }

public val Res.string.image_busket: StringResource by lazy {
      StringResource("string:image_busket", "image_busket", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 919, 36),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1403, 44),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 919, 36),
      ))
    }

public val Res.string.k_means_clustering: StringResource by lazy {
      StringResource("string:k_means_clustering", "k_means_clustering", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 956, 106),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1448, 146),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 956, 106),
      ))
    }

public val Res.string.lab: StringResource by lazy {
      StringResource("string:lab", "lab", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1063, 23),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1595, 43),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1063, 23),
      ))
    }

public val Res.string.light_color: StringResource by lazy {
      StringResource("string:light_color", "light_color", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1087, 39),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1639, 59),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1087, 39),
      ))
    }

public val Res.string.median_cut: StringResource by lazy {
      StringResource("string:median_cut", "median_cut", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1127, 94),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1699, 166),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1127, 94),
      ))
    }

public val Res.string.not_canceled_action: StringResource by lazy {
      StringResource("string:not_canceled_action", "not_canceled_action", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1222, 67),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1866, 99),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1222, 67),
      ))
    }

public val Res.string.palette_generated: StringResource by lazy {
      StringResource("string:palette_generated", "palette_generated", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1290, 49),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 1966, 57),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1290, 49),
      ))
    }

public val Res.string.palettes: StringResource by lazy {
      StringResource("string:palettes", "palettes", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1340, 28),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 2024, 36),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1340, 28),
      ))
    }

public val Res.string.preview: StringResource by lazy {
      StringResource("string:preview", "preview", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1369, 27),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 2061, 47),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1369, 27),
      ))
    }

public val Res.string.save_image_busket_message: StringResource by lazy {
      StringResource("string:save_image_busket_message", "save_image_busket_message", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1397, 125),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 2109, 217),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1397, 125),
      ))
    }

public val Res.string.select_photo: StringResource by lazy {
      StringResource("string:select_photo", "select_photo", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1523, 36),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 2327, 52),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1523, 36),
      ))
    }

public val Res.string.selected_photo: StringResource by lazy {
      StringResource("string:selected_photo", "selected_photo", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1560, 42),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 2380, 58),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1560, 42),
      ))
    }

public val Res.string.sphere_color: StringResource by lazy {
      StringResource("string:sphere_color", "sphere_color", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1603, 40),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 2439, 48),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1603, 40),
      ))
    }

public val Res.string.title: StringResource by lazy {
      StringResource("string:title", "title", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1644, 29),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 2488, 29),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1644, 29),
      ))
    }

public val Res.string.total: StringResource by lazy {
      StringResource("string:total", "total", setOf(
        ResourceItem(setOf(LanguageQualifier("en"), ), "${MD}values-en/strings.commonMain.cvr", 1674, 29),
        ResourceItem(setOf(LanguageQualifier("ru"), ), "${MD}values-ru/strings.commonMain.cvr", 2518, 37),
        ResourceItem(setOf(), "${MD}values/strings.commonMain.cvr", 1674, 29),
      ))
    }

@InternalResourceApi
internal fun _collectCommonMainString0Resources(map: MutableMap<String, StringResource>) {
  map.put("add", Res.string.add)
  map.put("app_name", Res.string.app_name)
  map.put("buffered_image", Res.string.buffered_image)
  map.put("cancel", Res.string.cancel)
  map.put("clear", Res.string.clear)
  map.put("close_with_saving", Res.string.close_with_saving)
  map.put("close_without_saving", Res.string.close_without_saving)
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
  map.put("image_busket", Res.string.image_busket)
  map.put("k_means_clustering", Res.string.k_means_clustering)
  map.put("lab", Res.string.lab)
  map.put("light_color", Res.string.light_color)
  map.put("median_cut", Res.string.median_cut)
  map.put("not_canceled_action", Res.string.not_canceled_action)
  map.put("palette_generated", Res.string.palette_generated)
  map.put("palettes", Res.string.palettes)
  map.put("preview", Res.string.preview)
  map.put("save_image_busket_message", Res.string.save_image_busket_message)
  map.put("select_photo", Res.string.select_photo)
  map.put("selected_photo", Res.string.selected_photo)
  map.put("sphere_color", Res.string.sphere_color)
  map.put("title", Res.string.title)
  map.put("total", Res.string.total)
}
