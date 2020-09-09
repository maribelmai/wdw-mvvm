package ar.com.maribelmai.wdw.repositories

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import ar.com.maribelmai.wdw.model.Picture

class PicturesRepositoryImpl : PicturesRepository {

    private val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.SIZE
    )

    private val sortOrder = "${MediaStore.Images.Media.DISPLAY_NAME} ASC"

    override fun loadPictures(contentResolver: ContentResolver): List<Picture> {
        val pictures = mutableListOf<Picture>()
        val query = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            emptyArray<String>(),
            sortOrder
        )
        query?.use { cursor ->

            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val size = cursor.getInt(sizeColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                pictures += Picture(contentUri.toString(), name, size)
            }
        }
        return pictures
    }
}