package feature.imageBusket.domain

import feature.imageBusket.data.ImageData

interface ImageBusketRepository {
    fun getImages(): List<ImageData>
    fun addImage(image: ImageData)
    fun deleteImage(index: Int)
}