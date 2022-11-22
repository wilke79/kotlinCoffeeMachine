package machine

class CoffeeMachine(var water: Int, var milk: Int, var beans: Int, var cups: Int, var money: Int) {
    private var state: State
    private var innerState: Int

    init {
        state = State.GREETING
        innerState = 0
        update("")
    }

    enum class State {
        WAITING, SELLING, FILLING, GREETING
    }

    enum class Specialities(val water: Int, val milk: Int, val beans: Int, val money: Int) {
        ESPRESSO(250, 0, 16, 4),
        LATTE(350, 75, 20, 7),
        CAPPUCCINO(200, 100, 12, 6)
    }

    fun update(action: String) {
        when (state) {
            State.SELLING -> sellCoffee(action)
            State.FILLING -> fillMachine(action)
            State.WAITING -> {
                when (action) {
                    "buy" -> sellCoffee("")
                    "fill" -> fillMachine("")
                    "take" -> takeMoney()
                    "remaining" -> sumUp()
                }
            }
            else -> {
                println("Write action (buy, fill, take, remaining, exit)")
                state = State.WAITING
            }
        }
    }

    private fun sellCoffee(action: String) {
        when (innerState++) {
            0 -> {
                println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:")
                state = State.SELLING
            }
            1 -> {
                if (action != "back") {
                    val speciality = Specialities.values()[action.toInt() - 1]
                    println(speciality)
                    if (speciality.water > water) {
                        println("Sorry, not enough water!")
                    } else if (speciality.milk > milk) {
                        println("Sorry, not enough milk!")
                    } else if (speciality.beans > beans) {
                        println("Sorry, not enough coffee beans!")
                    } else if (cups < 1) {
                        println("Sorry, not enough disposable cups!")
                    } else {
                        println("I have enough resources, making you a coffee!")
                        water -= speciality.water
                        milk -= speciality.milk
                        beans -= speciality.beans
                        cups--
                        money += speciality.money
                    }
                }
                innerState = 0
                state = State.GREETING
                update("")
            }
        }
    }

    private fun fillMachine(action: String) {
        when (innerState++) {
            0 -> {
                println("Write how many ml of water you want to add:")
                state = State.FILLING
            }
            1 -> {
                water += action.toInt()
                println("Write how many ml of milk you want to add:")
            }

            2 -> {
                milk += action.toInt()
                println("Write how many grams of coffee beans you want to add:")
            }

            3 -> {
                beans += action.toInt()
                println("Write how disposable cups you want to add:")
            }

            4 -> {
                cups += action.toInt()
                innerState = 0
                state = State.GREETING
                update("")
            }
        }
    }

    private fun takeMoney() {
        println("I gave you \$$money")
        money = 0
        state = State.GREETING
        update("")
    }

    private fun sumUp() {
        println(
            "The coffee machine has:\n" +
                    "$water ml of water\n" +
                    "$milk ml of milk\n" +
                    "$beans g of coffee beans\n" +
                    "$cups disposable cups\n" +
                    "\$$money of money\n"
        )
        state = State.GREETING
        update("")
    }
}


fun main() {
    val machine = CoffeeMachine(400, 540, 120, 9, 550)
    do {
        val action = readln()
        machine.update(action)
    } while (action != "exit")
}
