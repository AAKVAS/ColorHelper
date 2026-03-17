package feature.perspectiveBuilder.domain

import core.model.Image
import feature.perspectiveBuilder.model.PerspectivePoint

interface PerspectiveSceneExtractor {
    suspend fun extractPerspectiveScene(image: Image): List<PerspectivePoint>
}