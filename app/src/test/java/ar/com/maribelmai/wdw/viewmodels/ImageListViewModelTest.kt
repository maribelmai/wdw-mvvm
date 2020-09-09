package ar.com.maribelmai.wdw.viewmodels

import android.content.ContentResolver
import androidx.lifecycle.Observer
import ar.com.maribelmai.wdw.InstantExecutorExtension
import ar.com.maribelmai.wdw.model.Picture
import ar.com.maribelmai.wdw.repositories.PicturesRepository
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class ImageListViewModelTest {

    private var contentResolver = mock<ContentResolver>()
    private val result = listOf(
        Picture("/path/1.jpg", "1.jpg", 200),
        Picture("/path/2.jpg", "2.jpg", 300),
        Picture("/path/3.jpg", "3.jpg", 400)
    )
    private var picturesRepository = mock<PicturesRepository> {
        on {this.loadPictures(contentResolver)} doReturn result
    }

    private var coroutineDispatcher = TestCoroutineDispatcher()

    @Test
    fun `when viewmodel is created state is loading`() = runBlockingTest {

        // Given, When
        val viewModel = ImageListViewModel(picturesRepository, coroutineDispatcher)

        // Then
        assertTrue(viewModel.state.value is ImageListViewModel.State.Loading)
    }

    @Test
    fun `when pictures are retrieved state is updated with result`() = runBlockingTest {

        // Given
        val viewModel = ImageListViewModel(picturesRepository, coroutineDispatcher)

        // When
        viewModel.loadFiles(contentResolver)

        // Then
        val value = viewModel.state.value
        assertTrue(value is ImageListViewModel.State.Success)
        value as ImageListViewModel.State.Success
        assertEquals(result, value.pictures)
    }

    @Test
    fun `when pictures are retrieved state updates two states`() = runBlockingTest {

        // Given
        val viewModel = ImageListViewModel(picturesRepository, coroutineDispatcher)
        val observer = mock<Observer<ImageListViewModel.State>>()
        viewModel.state.observeForever(observer)

        // When
        viewModel.loadFiles(contentResolver)

        // Then
        verify(observer, times(2)).onChanged(any())
        verifyNoMoreInteractions(observer)
    }
}