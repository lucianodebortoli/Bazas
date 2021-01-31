package org.luciano.bazas

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var playersSpinner: Spinner;
    private lateinit var currentRoundTV: TextView;
    private lateinit var player1Button: Button
    private lateinit var player2Button: Button
    private lateinit var player3Button: Button
    private lateinit var player4Button: Button
    private lateinit var player1Pts: TextView
    private lateinit var player2Pts: TextView
    private lateinit var player3Pts: TextView
    private lateinit var player4Pts: TextView
    private lateinit var playerTitle: TextView
    private lateinit var roundTitle: TextView
    private lateinit var callTitle: TextView
    private lateinit var winTitle: TextView
    private lateinit var minusCallButton: Button
    private lateinit var plusCallButton: Button
    private lateinit var commitCallsButton: Button
    private lateinit var minusWinButton: Button
    private lateinit var plusWinButton: Button
    private lateinit var currentCallTV: TextView
    private lateinit var currentWinTV: TextView
    private lateinit var commitWonButton: Button
    private lateinit var setNameButton: Button
    private lateinit var hLineBazas: View
    private lateinit var hLineRound: View
    private lateinit var hLinePlayers: View
    private lateinit var hLineCalled: View
    private lateinit var hLineWin: View

    private var currentPlayers = 0
    private var players = mutableListOf<Player>()
    private lateinit var currentPlayer: Player
    private var rounds = mutableListOf<Int>()
    private var currentRound = 1
    private var gameFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        hideAll()
        initButtons()
        initSpinner()
    }

    private fun findViews() {
        playersSpinner =findViewById(R.id.playersSpinner)
        currentRoundTV=findViewById(R.id.currentRoundTV)
        player1Button=findViewById(R.id.player1Button)
        player2Button=findViewById(R.id.player2Button)
        player3Button=findViewById(R.id.player3Button)
        player4Button=findViewById(R.id.player4Button)
        player1Pts=findViewById(R.id.player1Pts)
        player2Pts=findViewById(R.id.player2Pts)
        player3Pts=findViewById(R.id.player3Pts)
        player4Pts=findViewById(R.id.player4Pts)
        minusCallButton=findViewById(R.id.minusCallButton)
        plusCallButton=findViewById(R.id. plusCallButton)
        commitCallsButton=findViewById(R.id.commitCallsButton)
        commitWonButton=findViewById(R.id.commitWonButton)
        minusWinButton=findViewById(R.id.minusWinButton)
        plusWinButton=findViewById(R.id.plusWinButton)
        currentCallTV=findViewById(R.id.currentCallTV)
        currentWinTV=findViewById(R.id.currentWinTV)
        setNameButton=findViewById(R.id.setNameButton)
        hLineBazas=findViewById(R.id.hline_bazas)
        hLineRound=findViewById(R.id.hline_round)
        hLinePlayers=findViewById(R.id.hline_players)
        hLineCalled=findViewById(R.id.hline_called)
        hLineWin=findViewById(R.id.hline_win)
        playerTitle=findViewById(R.id.playersTitle)
        roundTitle=findViewById(R.id.roundTitle)
        callTitle=findViewById(R.id.callTitle)
        winTitle=findViewById(R.id.winTitle)
    }

    private fun hideAll() {
        val viewsToHide = mutableListOf(
            playersTitle,
            currentRoundTV,
            player1Button,
            player2Button,
            player3Button,
            player4Button,
            player1Pts,
            player2Pts,
            player3Pts,
            player4Pts,
            minusCallButton,
            plusCallButton,
            commitCallsButton,
            setNameButton,
            minusWinButton,
            plusWinButton,
            currentCallTV,
            currentWinTV,
            commitWonButton,
            hLineBazas,
            hLineRound,
            hLinePlayers,
            hLineCalled,
            hLineWin,
            roundTitle,
            callTitle,
            winTitle
        )

        viewsToHide.forEach{
            it.visibility=View.INVISIBLE
        }
    }

    private fun initSpinner() {
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this, R.array.players, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playersSpinner.adapter = adapter
        playersSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val itemSelected = parent!!.getItemAtPosition(pos).toString()
                if (itemSelected == "Select Number of Players") {
                    //Do nothing
                } else {
                    setRounds(itemSelected.toInt())
                }
            }
        }
    }

    private fun setRounds(numberOfPlayers: Int) {
        currentPlayers = numberOfPlayers
        if (currentPlayers==3)
            rounds = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 8, 7, 6, 5, 4, 3, 2, 1)
        else if (currentPlayers==4)
            rounds = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 7, 6, 5, 4, 3, 2, 1)
        startApp()
        playersSpinner.isEnabled = false
    }

    private fun startApp(){
        initPlayers()
        showAll()
        updateUI()
        enableCallStage(true)
        enableWinStage(false)
        nextRound()
    }

    private fun initPlayers(){
        // Manage UI:
        val viewsToShow = mutableListOf(
            player1Button,
            player2Button,
            player3Button,
            player1Pts,
            player2Pts,
            player3Pts)
        if (currentPlayers==4){
            viewsToShow.add(player4Button)
            viewsToShow.add(player4Pts)
        }
        viewsToShow.forEach{
            it.visibility=View.VISIBLE
        }

        // Store new players:
        for (index in 0..currentPlayers) {
            val playerName: String = "Player "+(index+1)
            players.add(Player(playerName, index))
        }
        setPlayerFocus(Player.Players.PLAYER_1)
    }

    private fun showAll() {
        val viewsToShow = mutableListOf(
            playersTitle,
            currentRoundTV,
            minusCallButton,
            plusCallButton,
            commitCallsButton,
            setNameButton,
            minusWinButton,
            plusWinButton,
            currentCallTV,
            currentWinTV,
            commitWonButton,
            hLineBazas,
            hLineRound,
            hLinePlayers,
            hLineCalled,
            hLineWin,
            callTitle,
            winTitle,
            roundTitle
        )

        viewsToShow.forEach{
            it.visibility=View.VISIBLE
        }
    }

    private fun initButtons() {
        minusCallButton.setOnClickListener { minusCallButtonClicked() }
        minusWinButton.setOnClickListener { minusWinButtonClicked() }
        plusCallButton.setOnClickListener { plusCallButtonClicked() }
        plusWinButton.setOnClickListener { plusWinButtonClicked() }
        setNameButton.setOnClickListener { setNameButtonClicked() }
        commitCallsButton.setOnClickListener { commitCallsButtonClicked() }
        commitWonButton.setOnClickListener { commitWonButtonClicked() }
        player1Button.setOnClickListener { setPlayerFocus(Player.Players.PLAYER_1)}
        player2Button.setOnClickListener { setPlayerFocus(Player.Players.PLAYER_2)}
        player3Button.setOnClickListener { setPlayerFocus(Player.Players.PLAYER_3)}
        player4Button.setOnClickListener { setPlayerFocus(Player.Players.PLAYER_4)}
    }


    @SuppressLint("InflateParams")
    private fun setNameButtonClicked() {
        Log.d("Main", "setNameButtonClicked")
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.popup_edit_text_layout,null)
        val editText = dialogLayout.findViewById<EditText>(R.id.popupTV)

        with(builder) {
            setTitle("Edit Player")
            setPositiveButton("OK") { dialog, which ->
                currentPlayer.name = editText.text.toString()
                Log.i("Main", "Positive Button Clicked")
            }
            setNegativeButton("Cancel") { dialog, which ->
                Log.i("Main", "Negative Button Clicked")
            }
            setView(dialogLayout)
            show()
        }
        updateUI()
    }

    private fun plusWinButtonClicked() {
        Log.d("Main", "plusWinButtonClicked")
        currentPlayer.currentWin +=1
        updateUI()
    }

    private fun plusCallButtonClicked() {
        Log.d("Main", "plusCallButtonClicked")
        currentPlayer.currentCall +=1
        updateUI()
    }

    private fun minusWinButtonClicked() {
        Log.d("Main", "minusWinButtonClicked")
        currentPlayer.currentWin -=1
        updateUI()
    }

    private fun minusCallButtonClicked() {
        Log.d("Main", "minusCallButtonClicked")
        currentPlayer.currentCall -=1
        updateUI()
    }

    @SuppressLint("ShowToast")
    private fun commitCallsButtonClicked() {
        Log.d("Main", "commitCallsButtonClicked")
        val isValid = checkValidCall()
        if (isValid){
            players.forEach{
                it.pushCall()
            }

            // Go to Win Stage:
            enableCallStage(false)
            enableWinStage(true)
            updateUI()
        } else {
            Toast.makeText(this@MainActivity, "Invalid call. Try again", Toast.LENGTH_SHORT)
            Log.d("Main","Invalid call. Try again" )
        }
    }

    private fun commitWonButtonClicked() {
        Log.d("Main", "commitWonButtonClicked")
        val isValid = checkValidWon()
        if (isValid){
            computePoints()
            players.forEach{
                it.pushWin()
                it.reset()
            }
            nextRound()
            // Go to Call Stage:
            enableCallStage(true)
            enableWinStage(false)
            updateUI()
        } else {
            Toast.makeText(this@MainActivity, "Invalid won. Try again", Toast.LENGTH_SHORT)
            Log.d("Main","Invalid won. Try again" )
        }
    }

    private fun checkValidCall(): Boolean {
        if (gameFinished)
            return false

        // Check total calls:
        var totalCalls = 0
        players.forEach{
            totalCalls += it.currentCall
        }
        return totalCalls != currentRound
    }

    private fun checkValidWon(): Boolean {
        if (gameFinished)
            return false

        // Check total won:
        var checkSum = 0
        players.forEach{
            if (it.currentWin>currentRound) {
                checkSum += 1
            }
        }
        return checkSum == 0
    }

    private fun computePoints() {
        // Add common points:
        players.forEach{
            it.addPoints(it.currentWin)
        }

        // Add bonus points:
        val bonusPts = if (currentRound<=5) { 5 } else { 10 }
        players.forEach{
            if (it.currentCall==it.currentWin) {
                it.addPoints(bonusPts)
            }
        }
    }

    private fun nextRound() {
        if(rounds.isNotEmpty()){
            currentRound = rounds.removeAt(0)
            Log.d("Main", "currentRound on $currentRound")
        }
        else{
            gameFinished()
        }
    }


    private fun gameFinished()
    {
        enableCallStage(false)
        enableWinStage(false)
        currentRoundTV.text="Game Finished"
        gameFinished = true
        Log.d("Main", "Game Finished")
    }

    private fun updateUI() {
        // Update texts:
        currentRoundTV.text = "$currentRound"
        currentCallTV.text = currentPlayer.currentCall.toString()
        currentWinTV.text = currentPlayer.currentWin.toString()

        // Update Buttons:
        val buttons = mutableListOf<Button>(
            player1Button,
            player2Button,
            player3Button,
            player4Button
        )
        buttons.forEachIndexed{ index, button ->
            button.setBackgroundColor(resources.getColor(R.color.colorIdle))
            button.setTextColor(resources.getColor(R.color.colorBlack))
            button.text = players[index].name
            if (currentPlayer.index == index) {
                button.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                button.setTextColor(resources.getColor(R.color.colorWhite))
            }
        }

        // Update Points:
        val scores = mutableListOf<TextView>(
            player1Pts,
            player2Pts,
            player3Pts,
            player4Pts
        )
        scores.forEachIndexed{index, tv ->
            tv.text = players[index].points.toString()
        }
    }


    private fun enableCallStage(enable: Boolean) {
        val callStageViews = mutableListOf(
            minusCallButton,
            plusCallButton,
            currentCallTV,
            commitCallsButton
        )
        callStageViews.forEach {
            it.isEnabled = enable
        }
    }


    private fun enableWinStage(enable: Boolean) {
        val winStageViews = mutableListOf(
            minusWinButton,
            plusWinButton,
            currentWinTV,
            commitWonButton
        )

        winStageViews.forEach {
            it.isEnabled = enable
        }
    }

    private fun setPlayerFocus(playerID: Player.Players) {
        Log.d("Main", "setPlayerFocus on $playerID")
        currentPlayer = players[playerID.ordinal]
        updateUI()
    }


    // TODO: add logic for preventing total call == round

}