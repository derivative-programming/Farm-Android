package com.app.farm.utilities

import android.content.Context
import android.view.View
import android.widget.TextView
import com.app.farm.utilities.Utils.checkNull
import com.app.farm.utilities.Utils.isEditTextEmpty
import com.app.farm.utilities.Utils.validateEmail
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

class UtilTest {
    @Test
    fun `should return true and set error message when editText is empty`() {
        // Arrange
        val context = mock(Context::class.java)
        val editText = mock(MyEditText::class.java)
        val errorMessage = mock(TextView::class.java)
        val string = "Error message"
        val expected = true


        // Act
        val result = isEditTextEmpty(context, editText, errorMessage, string)

        // Assert
        assertEquals(expected, result)
        verify(editText).text
        verify(errorMessage).text = string
        verify(errorMessage).visibility = View.VISIBLE
    }

    @Test
    fun `should return false and not set error message when editText is not empty`() {
        // Arrange
        val context = mock(Context::class.java)
        val editText = mock(MyEditText::class.java)
        editText.setText("Not empty")
        val errorMessage = mock(TextView::class.java)
        val string = "Error message"
        val expected = false

        // Act
        val result = isEditTextEmpty(context, editText, errorMessage, string)

        // Assert
        assertEquals(expected, result)
        verify(editText).text
        verify(errorMessage, never()).text = string
        verify(errorMessage, never()).visibility = View.VISIBLE
    }

    @Test
    fun testNullInput() {
        val input: String? = null
        val expectedOutput = "NA"
        assertEquals(expectedOutput, checkNull(input))
    }

    @Test
    fun testEmptyInput() {
        val input: String? = ""
        val expectedOutput = ""
        assertEquals(expectedOutput, checkNull(input))
    }

    @Test
    fun testNonEmptyInput() {
        val input: String? = "hello"
        val expectedOutput = "hello"
        assertEquals(expectedOutput, checkNull(input))
    }


    @Test
    fun testValidEmail() {
        val context = mock(Context::class.java)
        val editText = mock(MyEditText::class.java)
        val errorMessage = TextView(context)
        val email = "john.doe@example.com"

        val isValid = validateEmail(context, editText, errorMessage, email)

        assertEquals(false, isValid)
    }

    @Test
    fun testInvalidEmail() {
        val context = mock(Context::class.java)
        val editText = mock(MyEditText::class.java)
        val errorMessage = TextView(context)
        val email = "invalid email"

        val isValid = validateEmail(context, editText, errorMessage, email)

        assertEquals(true, isValid)
    }

}

