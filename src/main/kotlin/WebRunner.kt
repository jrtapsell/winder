import org.w3c.dom.*
import kotlin.browser.document

object FixedUI {
    val pythonicInput = document.getElementById("pythonicInput") as HTMLInputElement
    val table = document.getElementById("out") as HTMLTableElement
    val updateButton = document.getElementById("updateButton") as HTMLButtonElement
}

object WebRunner {
    val parserRegex = Regex("""(\\x[0-9a-f]{2}|\\.|.)""")


    fun onNewText(input: HTMLInputElement) {
        val text = input.value
        val indexes = parserRegex.findAll(text)
            .map { it.groupValues[1] }
            .map {
                when (it.length) {
                    1 -> it[0].toInt()
                    2 -> when (it[1]) {
                        'r' -> '\r'.toInt()
                        't' -> '\t'.toInt()
                        'n' -> '\n'.toInt()
                        else -> throw AssertionError(it)
                    }
                    4 -> it.drop(2).toInt(16)
                    else -> throw AssertionError(it)
                }
            }
            .toList()
        MutableUI(indexes, FixedUI.table)
    }

    fun onload() {
        FixedUI.updateButton.onclick = {
            onNewText(FixedUI.pythonicInput)
        }
    }
}

@Suppress("unused")
fun main(args: Array<String>) {
    WebRunner.onload()
}