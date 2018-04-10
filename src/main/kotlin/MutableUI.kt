import org.w3c.dom.HTMLTableElement
import kotlin.dom.clear

class MutableUI(private val data: List<Int>, private val table: HTMLTableElement) {
    private val indexBoxes = data.indices.map {
        val ret = SingleCell.create("index_$it")
        ret.text = it.toString()
        ret.setState(SingleCell.State.LOCKED)
        ret
    }

    private val decimalInputBoxes = data.map {
        val ret = SingleCell.create("input_raw_$it")
        ret.text = it.toString()
        ret.setState(SingleCell.State.LOCKED)
        ret
    }

    fun getKeyBox(index:Int) = keyBoxes.getOrNull(index)
    private val keyBoxes: List<SingleCell> = data.indices.map { index ->
        val ip = SingleCell.create("key_$index")
        ip.onChange {
            resultBoxes[index].text = if (it.text.isEmpty()) "" else {
                val myVal = ip.text[0].toInt()
                val result = myVal xor data[index]
                result.toChar().toString()
            }
        }

        ip.onDirection {
            when (this) {
                SingleCell.Direction.RIGHT -> getKeyBox(index + 1)?.focus()
                SingleCell.Direction.LEFT -> getKeyBox(index - 1)?.focus()
                SingleCell.Direction.DOWN -> getResultBox(index)?.focus()
            }
        }
        ip
    }

    fun getResultBox(index:Int) = resultBoxes.getOrNull(index)
    private val resultBoxes: List<SingleCell>  = data.indices.map { index ->
        val ip = SingleCell.create("result_$index")
        ip.onChange {
            keyBoxes[index].text = if (it.text.isEmpty()) "" else {
                val myVal = ip.text[0].toInt()
                val result = myVal xor data[index]
                result.toChar().toString()
            }
        }

        ip.onDirection {
            when (this) {
                SingleCell.Direction.RIGHT -> getResultBox(index + 1)?.focus()
                SingleCell.Direction.LEFT -> getResultBox(index - 1)?.focus()
                SingleCell.Direction.UP -> getKeyBox(index)?.focus()
            }
        }
        ip
    }

    init {
        table.clear()
        table.tr {
            th { textContent = "Index Number" }
            addAll(indexBoxes)
        }
        table.tr {
            th { textContent = "Decimal value" }
            addAll(decimalInputBoxes)
        }

        table.tr {
            th { textContent = "Key Guesses" }
            addAll(keyBoxes)
        }

        table.tr {
            th { textContent = "Result Character" }
            addAll(resultBoxes)
        }
    }
}