package converter

fun main() {
    val conv = Converter()
    while (conv.status != Status.EXIT) {
        print(conv.message)
        conv.conv(readLine()!!)
    }
}

