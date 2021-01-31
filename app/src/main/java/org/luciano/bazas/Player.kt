package org.luciano.bazas

class Player(name: String, index: Int) {

    enum class Players {
        PLAYER_1,
        PLAYER_2,
        PLAYER_3,
        PLAYER_4
    }

    var index: Int = 0
    var name: String = "player"
    var points: Int  = 0
    var currentCall: Int = 0
    var currentWin: Int  = 0
    var calledArray = mutableListOf<Int>()
    var winArray = mutableListOf<Int>()

    init {
        this.name = name
        this.index = index
    }

    fun pushCall() {
        calledArray.add(currentCall)
    }

    fun pushWin() {
        winArray.add(currentWin)
    }

    fun reset(){
        currentCall = 0
        currentWin = 0
    }

    fun addPoints(pts: Int) {
        points += pts
    }

    fun countCorrect(): Int{
        var correctAnswers = 0
        for (i in 0 until winArray.size) {
            if (calledArray[i] == winArray[i]) {
                correctAnswers += 1
            }
        }
        return correctAnswers
    }

}