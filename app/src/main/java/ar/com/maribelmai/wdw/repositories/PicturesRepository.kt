package ar.com.maribelmai.wdw.repositories

import android.content.ContentResolver
import ar.com.maribelmai.wdw.model.Picture

interface PicturesRepository {

    fun loadPictures(contentResolver: ContentResolver): List<Picture>
}