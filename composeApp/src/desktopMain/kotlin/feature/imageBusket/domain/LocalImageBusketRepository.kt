package feature.imageBusket.domain

import feature.imageBusket.data.ImageData
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Paths
import java.util.UUID
import javax.imageio.ImageIO

class LocalImageBusketRepository : ImageBusketRepository {
    override suspend fun saveImages(images: List<ImageData>) {
        val savedMetadata = mutableListOf<ImageMetadata>()

        for (imageData in images) {
            val imageId = UUID.randomUUID().toString()

            val success = saveImageToDisk(imageData, imageId)

            if (success) {
                savedMetadata.add(
                    ImageMetadata(
                        id = imageId,
                        width = imageData.width,
                        height = imageData.height,
                        savedAt = System.currentTimeMillis()
                    )
                )
            }
        }

        val state = BucketState(savedMetadata)
        val json = Json { prettyPrint = true }
        getMetadataFile().writeText(json.encodeToString(state))
    }

    override suspend fun getImages(): List<ImageData> {
        val metadataFile = getMetadataFile()
        if (!metadataFile.exists()) {
            return emptyList()
        }

        val json = Json { ignoreUnknownKeys = true }
        val state = json.decodeFromString<BucketState>(metadataFile.readText())

        return state.images.mapNotNull { metadata ->
            loadImageFromDisk(metadata.id)
        }
    }

    override suspend fun deleteImages() {
        val metadataFile = getMetadataFile()
        if (metadataFile.exists()) {
            val json = Json { ignoreUnknownKeys = true }
            val state = json.decodeFromString<BucketState>(metadataFile.readText())

            state.images.forEach { metadata ->
                val imageFile = getImageFile(metadata.id)
                if (imageFile.exists()) {
                    imageFile.delete()
                }
            }
        }

        if (metadataFile.exists()) {
            metadataFile.delete()
        }

        val directory = getAppDataDirectory()
        directory.listFiles()?.let { files ->
            if (files.isEmpty()) {
                directory.delete()
            }
        }
    }

    private fun getMetadataFile(): File {
        return File(getAppDataDirectory(), "bucket_metadata.json")
    }

    private fun getImageFile(imageId: String): File {
        return File(getAppDataDirectory(), "$imageId.png")
    }

    @Serializable
    data class ImageMetadata(
        val id: String,
        val width: Int,
        val height: Int,
        val savedAt: Long
    )

    @Serializable
    data class BucketState(
        val images: List<ImageMetadata> = emptyList(),
        val savedAt: Long = System.currentTimeMillis()
    )

    private fun saveImageToDisk(image: BufferedImage, imageId: String): Boolean {
        return try {
            val file = getImageFile(imageId)
            ImageIO.write(image, "PNG", file)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun loadImageFromDisk(imageId: String): BufferedImage? {
        return try {
            val file = getImageFile(imageId)
            if (file.exists()) {
                ImageIO.read(file)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun getAppDataDirectory(appName: String = "ColorHelper"): File {
        return Paths.get(
            System.getProperty("user.home"),
            "AppData", "Local", appName, "ImageBucket"
        ).toFile().apply {
            mkdirs()
        }
    }
}