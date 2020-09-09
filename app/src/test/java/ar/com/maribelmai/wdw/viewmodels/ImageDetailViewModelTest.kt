package ar.com.maribelmai.wdw.viewmodels

import ar.com.maribelmai.wdw.InstantExecutorExtension
import ar.com.maribelmai.wdw.util.LocalImageLabeler
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class ImageDetailViewModelTest {

    private val imageLabeler = mock<LocalImageLabeler>()
    private val inputImage = mock<InputImage>()

    @Test
    fun `when image starts retrieving info state is loading`() {

        // Given, When
        val viewModel = ImageDetailViewModel(0.7f, imageLabeler)

        // Then
        val value = viewModel.state.value
        assertTrue(value is ImageDetailViewModel.State.Loading)
    }

    @Test
    fun `when tags are retrieved state is success`() {

        // Given
        val tagList = listOf(ImageLabel("TAG 1", 0.8f, 1), ImageLabel("TAG 2", 0.8f, 2))
        val viewModel = ImageDetailViewModel(0.7f, imageLabeler)
        val successArgCaptor = argumentCaptor<(List<ImageLabel>) -> Unit>()
        whenever(imageLabeler.process(any(), any(), successArgCaptor.capture(), any())).then {
            successArgCaptor.firstValue.invoke(tagList)
        }

        // When
        viewModel.retrieveInfo(inputImage)

        // Then
        val value = viewModel.state.value
        assertTrue(value is ImageDetailViewModel.State.Success)
        value as ImageDetailViewModel.State.Success
        assertEquals(tagList, value.imageLabels)
    }

    @Test
    fun `when tags fails on being retrieved state is failure`() {

        // Given
        val viewModel = ImageDetailViewModel(0.7f, imageLabeler)
        val failureArgCaptor = argumentCaptor<(Exception) -> Unit>()
        whenever(imageLabeler.process(any(), any(), any(), failureArgCaptor.capture())).then {
            failureArgCaptor.firstValue.invoke(Exception("error message"))
        }

        // When
        viewModel.retrieveInfo(inputImage)

        // Then
        val value = viewModel.state.value
        assertTrue(value is ImageDetailViewModel.State.Failure)
        value as ImageDetailViewModel.State.Failure
        assertEquals("error message", value.message)
    }
}