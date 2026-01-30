@file:OptIn(InternalResourceApi::class)

package com.example

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.ResourceItem

private const val MD: String = "composeResources/com.example/"

public val Res.drawable.add_icon: DrawableResource by lazy {
      DrawableResource("drawable:add_icon", setOf(
        ResourceItem(setOf(), "${MD}drawable/add_icon.xml", -1, -1),
      ))
    }

public val Res.drawable.back_arrow: DrawableResource by lazy {
      DrawableResource("drawable:back_arrow", setOf(
        ResourceItem(setOf(), "${MD}drawable/back_arrow.xml", -1, -1),
      ))
    }

public val Res.drawable.brush: DrawableResource by lazy {
      DrawableResource("drawable:brush", setOf(
        ResourceItem(setOf(), "${MD}drawable/brush.xml", -1, -1),
      ))
    }

public val Res.drawable.camera_icon: DrawableResource by lazy {
      DrawableResource("drawable:camera_icon", setOf(
        ResourceItem(setOf(), "${MD}drawable/camera_icon.xml", -1, -1),
      ))
    }

public val Res.drawable.copy_icon: DrawableResource by lazy {
      DrawableResource("drawable:copy_icon", setOf(
        ResourceItem(setOf(), "${MD}drawable/copy_icon.xml", -1, -1),
      ))
    }

public val Res.drawable.outline_delete: DrawableResource by lazy {
      DrawableResource("drawable:outline_delete", setOf(
        ResourceItem(setOf(), "${MD}drawable/outline_delete.xml", -1, -1),
      ))
    }

@InternalResourceApi
internal fun _collectCommonMainDrawable0Resources(map: MutableMap<String, DrawableResource>) {
  map.put("add_icon", Res.drawable.add_icon)
  map.put("back_arrow", Res.drawable.back_arrow)
  map.put("brush", Res.drawable.brush)
  map.put("camera_icon", Res.drawable.camera_icon)
  map.put("copy_icon", Res.drawable.copy_icon)
  map.put("outline_delete", Res.drawable.outline_delete)
}
