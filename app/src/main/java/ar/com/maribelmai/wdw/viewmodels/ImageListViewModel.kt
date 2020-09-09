package ar.com.maribelmai.wdw.viewmodels

import android.content.ContentResolver
import androidx.lifecycle.*
import ar.com.maribelmai.wdw.model.Picture
import ar.com.maribelmai.wdw.repositories.PicturesRepository
import ar.com.maribelmai.wdw.repositories.PicturesRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageListViewModel(
    private val picturesRepository: PicturesRepository = PicturesRepositoryImpl(),
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    init {
        _state.value = State.Loading()
    }

    fun loadFiles(contentResolver: ContentResolver) {
        viewModelScope.launch(coroutineDispatcher) {
            _state.postValue(
                State.Success(
                    picturesRepository.loadPictures(contentResolver)
                )
            )
        }
    }

    sealed class State {
        class Loading : State()
        class Success(val pictures: List<Picture>) : State()
    }
}
