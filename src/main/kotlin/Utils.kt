import org.w3c.dom.HTMLTableCellElement
import org.w3c.dom.HTMLTableElement
import org.w3c.dom.HTMLTableRowElement
import kotlin.browser.document

fun HTMLTableElement.tr(id: String?=null, block: HTMLTableRowElement.() -> Unit) {
    val row = document.createElement("tr") as HTMLTableRowElement
    if (id != null) {
        row.id = id
    }
    row.block()
    append(row)
}

fun HTMLTableRowElement.td(id: String?=null, block: HTMLTableCellElement.() -> Unit={}) {
    val cell = document.createElement("td") as HTMLTableCellElement
    if (id != null) {
        cell.id = id
    }
    cell.block()
    append(cell)
}

fun HTMLTableRowElement.th(id: String?=null, block: HTMLTableCellElement.() -> Unit={}) {
    val cell = document.createElement("th") as HTMLTableCellElement
    if (id != null) {
        cell.id = id
    }
    cell.block()
    append(cell)
}

fun HTMLTableRowElement.addAll(list: Iterable<SingleCell>) {
    list.forEach { cell ->
        td {
            cell.appendTo(this)
        }
    }
}