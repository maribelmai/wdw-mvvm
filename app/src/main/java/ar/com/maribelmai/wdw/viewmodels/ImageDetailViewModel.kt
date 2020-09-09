package ar.com.maribelmai.wdw.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ar.com.maribelmai.wdw.util.LocalImageLabeler
import ar.com.maribelmai.wdw.util.LocalImageLabelerImpl
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel

class ImageDetailViewModel(
    private val confidence: Float,
    private val imageLabeler: LocalImageLabeler = LocalImageLabelerImpl()) : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    init {
        _state.value = State.Loading()
    }

    fun retrieveInfo(inputImage: InputImage) {
        imageLabeler.process(inputImage, confidence, {
            _state.value = State.Success(it)
        }, {
            _state.value = State.Failure(it.message)
        } )
    }

    /**
     * Factory example
     */
    class Factory(private val confidence: Float = 0.7F) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            ImageDetailViewModel(confidence) as T
    }

    sealed class State {
        class Loading : State()
        class Success(val imageLabels: List<ImageLabel>) : State()
        class Failure(val message: String?) : State()
    }
}

