import org.w3c.dom.HTMLInputElement
import org.w3c.dom.ParentNode
import org.w3c.dom.events.KeyboardEvent
import kotlin.browser.document
import kotlin.dom.addClass

data class SingleCell(private val views: HTMLInputElement) {

    enum class State { EMPTY, TYPED, COMPUTED, LOCKED}
    var text: String
        get() = views.value
       set(value) {
           if (value.isEmpty()) {
               setState(State.EMPTY)
           } else {
               setState(State.COMPUTED)
           }
            views.value = value
       }

    companion object {
        fun create(id: String): SingleCell {
            val input = document.createElement("input") as HTMLInputElement
            input.setAttribute("maxlength", "1")
            input.setAttribute("text", "")
            input.classList.add("singleInput")
            input.id = id
            return SingleCell(input)
        }
    }

    fun typeState() {
        if (views.value.isEmpty()) {
            setState(State.EMPTY)
        } else {
            setState(State.TYPED)
        }
    }

    init {
        views.addClass("autogen")
        setState(State.EMPTY)
        views.onchange = {
            typeState()
            views.setAttribute("text", views.value)
        }
    }

    fun onChange(block:(SingleCell)->Unit) {
        views.onchange = {
            typeState()
            block(this)
        }
    }

    fun appendTo(node: ParentNode) = node.append(views)

    fun setState(state: State) {
        views.setAttribute("inputSource", state.name)
        views.readOnly = state == State.LOCKED
    }

    enum class Direction { UP, LEFT, DOWN, RIGHT }
    fun KeyboardEvent.getArrow() = when (this.key) {
        "ArrowRight" -> Direction.RIGHT
        "ArrowUp" -> Direction.UP
        "ArrowLeft" -> Direction.LEFT
        "ArrowDown" -> Direction.DOWN
        else -> null
    }

    fun onDirection(block: Direction.()->Unit) {
        views.onkeyup = { it.let { it as? KeyboardEvent }?.getArrow()?.block() }
    }

    fun focus() = views.focus()
}