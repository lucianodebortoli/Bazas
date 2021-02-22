package org.luciano.bazas

import android.util.Log

class Game {

    var currentPlayers = 0
    var currentMode = ""
    var players = mutableListOf<Player>()
    lateinit var currentPlayer: Player
    var rounds = mutableListOf<Int>()
    private var triumph = mutableListOf<Int>()
    private var weights = mutableListOf<Int>()
    private var hand = mutableListOf<Int>()
    var currentRound = 1
    var currentTriumph = 1
    var currentWeight = 0
    var currentHand = 1
    private var totalRounds = 0

    fun setMode(mode: String) {
        currentMode = mode
    }

    fun setRounds(numberOfPlayers: Int) {
        currentPlayers = numberOfPlayers
        if (currentPlayers == 3) {
            rounds =  mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 8, 7, 6, 5, 4, 3, 2, 1)
            triumph = mutableListOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1)
            hand =    mutableListOf(1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3)
            val classicWeights = mutableListOf(10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10)
            val linearWeights =  mutableListOf(11, 12, 13, 14, 15, 16, 17, 18, 19, 19, 18, 17, 16, 15, 14, 13, 12, 11)
            val stepWeights =    mutableListOf(5,  5,  5,  5,  5,  10, 10, 10, 10, 10, 10, 10, 10, 5,  5,  5,  5,  5)
            when (currentMode) {
                "Classic" -> weights = classicWeights
                "Linear" -> weights = linearWeights
                "Step" -> weights = stepWeights
            }
        } else if (currentPlayers == 4) {
            rounds =  mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5, 4, 3, 2, 1)
            triumph = mutableListOf(1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1)
            hand =    mutableListOf(1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3)
            val classicWeights = mutableListOf(10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10)
            val linearWeights =  mutableListOf(11, 12, 13, 14, 15, 16, 17, 18, 17, 16, 15, 14, 13, 12, 11)
            val stepWeights =    mutableListOf(5 , 5 , 5 , 5 , 5,  10 ,10, 10, 10, 10, 5, 5,  5,  5,  5)
            when (currentMode) {
                "Classic" -> weights = classicWeights
                "Linear" -> weights = linearWeights
                "Step" -> weights = stepWeights
            }
        }
        totalRounds = rounds.size
        Log.d("Main", "playing $currentMode mode with $currentPlayers players")
    }

    fun setCurrentPlayer(playerID: Player.Players) {
        currentPlayer = players[playerID.ordinal]
    }

    fun checkValidCall(): Boolean {
        // Check total calls:
        var totalCalls = 0
        players.forEach {
            totalCalls += it.currentCall
        }
        return totalCalls != currentRound
    }

    fun checkValidWon(): Boolean {
        // Check total won:
        var checkSum = 0
        players.forEach {
            if (it.currentWin > currentRound) {
                checkSum += 1
            }
        }
        return checkSum == 0
    }

    fun commitCalls() {
        players.forEach{
            it.pushCall()
        }
    }

    fun unCommitCalls() {
        players.forEach{
            it.removeCall()
        }
    }

    fun commitWins(){
        players.forEach{
            it.pushWin()
        }
    }

    fun unCommitWins() {
        players.forEach{
            it.removeWin()
        }
    }

    fun nextRound(){
        players.forEach{
            it.reset()
        }
        currentRound = rounds.removeAt(0)
        currentTriumph = triumph.removeAt(0)
        currentWeight = weights.removeAt(0)
        currentHand = hand.removeAt(0)
        Log.d("Main", "round $currentRound | triumph $currentTriumph | weight $currentWeight | hand $currentHand")
    }

    fun computePoints() {
        players.forEach{
            var pts = 0
            pts += it.currentWin
            if (it.currentCall==it.currentWin)
                pts += currentWeight
            it.addPoints(pts)
        }
    }

    fun isFinished(): Boolean {
        val playedHands = players[0].pointsArray.size
        Log.d("Main", "played hands: $playedHands")
        return playedHands == totalRounds
    }

    fun findWinnerPlayer(): Player {
        var maxPoints = players[0].points
        var winnerIndex = 0
        players.forEach{
            if (it.points> maxPoints)
                maxPoints = it.points
        }
        players.forEach{
            if(it.points == maxPoints) {
                winnerIndex = it.index
            }
        }
        return players[winnerIndex]
    }

    fun getCallsSum(): Int {
        var totalCalls = 0
        players.forEach{
            totalCalls += it.currentCall
        }
        return totalCalls
    }

    fun getWinsSum(): Int {
        var totalWins = 0
        players.forEach{
            totalWins += it.currentWin
        }
        return totalWins
    }

}