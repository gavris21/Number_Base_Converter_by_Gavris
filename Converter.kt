package converter

import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

class Converter {
    var status = Status.MAIN
    private val code = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private var sourceBase = 10
    private var targetBase = 2
    private val message1 = "Enter two numbers in format: {source base} {target base} (To quit type /exit) > "
    var message = message1

    fun conv(input: String) {
        when {
            status == Status.MAIN && input == "/exit" -> {
                status = Status.EXIT
            }
            status == Status.MAIN -> {
                sourceBase = input.split(" ")[0].toInt()
                targetBase = input.split(" ")[1].toInt()
                status = Status.SUB_MENU
                message = "Enter number in base $sourceBase to convert to base $targetBase (To go back type /back) > "
            }
            status == Status.SUB_MENU && input == "/back" -> {
                status = Status.MAIN
                message = message1
            }
            status == Status.SUB_MENU -> {
                if (!input.contains(".")) {
                    val inpNumber = convToDec(input, sourceBase)
                    val result = convFromDec(inpNumber, targetBase)
                    print("Conversion result: $result\n\n")
                } else {
                    val a = input.substring(0, input.indexOf("."))
                    val b = input.substring(input.indexOf(".") + 1)
                    val inpNumberInt = convToDec(a, sourceBase)
                    var resultInt = convFromDec(inpNumberInt, targetBase)
                    val inpNumberFract: BigDecimal = convToDecFract(b, sourceBase)
                    val resultFract = convFromDecFract(inpNumberFract, targetBase)
                    if (resultInt == "") {
                        resultInt = "0"
                    }
                    print("Conversion result: $resultInt.${resultFract.substring(0, 5)}\n\n")
                }
            }
        }

    }

    private fun convFromDecFract(inpNumberFract: BigDecimal, targetBase: Int): String {
        var quotient = inpNumberFract
        val list = mutableListOf<String>()
        repeat(15) {
            var temp = quotient * targetBase.toBigDecimal()
            list.add(parseTo(temp.toInt()))
            var c = temp.setScale(0, RoundingMode.DOWN)
            quotient = temp - c
        }
        return list.joinToString("")
    }

    private fun convToDecFract(number: String, base: Int): BigDecimal {
        val list = mutableListOf<String>()
        for (i in number.indices) {
            list.add(number[i].toString())
        }
        val list1 = parseFrom(list).toMutableList()
        var result = 0.toBigDecimal()
        for (i in list1.indices) {
            result += list1[i].toBigDecimal() * base.toBigDecimal().pow(-(i+1), MathContext.DECIMAL64)
        }
        result = result.setScale(15, RoundingMode.CEILING)
        return result
    }

    private fun convToDec(number: String, base: Int): BigInteger {
        val list = mutableListOf<String>()
        for (i in number.indices) {
            list.add(number[i].toString())
        }
        val list1 = parseFrom(list).reversed().toMutableList()
        var result = 0.toBigInteger()
        for (i in list1.indices) {
            result += list1[i].toBigInteger() * base.toBigInteger().pow(i)
        }
        return result
    }

    private fun convFromDec(number: BigInteger, base: Int): String {
        var quotient = number
        val list = mutableListOf<String>()
        while (quotient != 0.toBigInteger()) {
            list.add(parseTo((quotient % base.toBigInteger()).toInt()))
            quotient /= base.toBigInteger()
        }
        return list.reversed().joinToString("")
    }

    // parse digits to Hex
    private fun parseTo(digits: Int) = code[digits].toString()

    // parse digits to Dec
    private fun parseFrom(list: MutableList<String>): MutableList<Int> {
        val list1 = mutableListOf<Int>()
        for (element in list){
            list1.add(code.indexOf(element.uppercase()))
        }
        return list1
    }
}
enum class Status {
    MAIN,
    SUB_MENU,
    EXIT
}