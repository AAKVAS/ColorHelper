package feature.imageBusket.domain

import feature.imageBusket.data.ImageData

interface ImageBusketRepository {
    suspend fun getImages(): List<ImageData>
    suspend fun saveImages(images: List<ImageData>)
    suspend fun deleteImages()
}