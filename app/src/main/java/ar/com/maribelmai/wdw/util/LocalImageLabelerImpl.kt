package ar.com.maribelmai.wdw.util

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.lang.Exception

class LocalImageLabelerImpl: LocalImageLabeler {

    override fun process(
        inputImage: InputImage,
        confidence: Float,
        onSuccess: (List<ImageLabel>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val options = ImageLabelerOptions.Builder()
            .setConfidenceThreshold(confidence)
            .build()
        val labeler = ImageLabeling.getClient(options)
        labeler.process(inputImage)
            .addOnSuccessListener { onSuccess(it) }
            .addOnFailureListener { onFailure(it) }
    }
}