package ar.com.maribelmai.wdw.util

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel

interface LocalImageLabeler {

    fun process(
        inputImage: InputImage,
        confidence: Float,
        onSuccess: (List<ImageLabel>) -> Unit,
        onFailure: (Exception) -> Unit
    )

}
