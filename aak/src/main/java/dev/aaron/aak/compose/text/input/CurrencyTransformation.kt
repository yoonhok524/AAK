package dev.aaron.aak.compose.text.input

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import dev.aaron.aak.number.toSafeDouble
import java.text.DecimalFormat

object CurrencyTransformation : VisualTransformation {

    private val decimalFormat: DecimalFormat by lazy { DecimalFormat() }

    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = AnnotatedString(text.text.toCurrencyFormat()),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return text.text.toCurrencyFormat().length
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return text.length
                }
            }
        )
    }

    private fun String.toCurrencyFormat(): String {
        val numOfPoint = this.count { it == '.' }
        return if(this.endsWith(".") && numOfPoint > 1) {
            this.substringBeforeLast(".")
        } else {
            this
        }.let {
            decimalFormat.format(it.toSafeDouble())
        }.let {
            if (numOfPoint == 1 && this.endsWith(".")) "$it." else it
        }
    }
}