package org.luciano.bazas

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.HandlerThread
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {


    private lateinit var player1Button: Button
    private lateinit var player2Button: Button
    private lateinit var player3Button: Button
    private lateinit var player4Button: Button
    private lateinit var minusCallButton: Button
    private lateinit var plusCallButton: Button
    private lateinit var commitCallsButton: Button
    private lateinit var minusWinButton: Button
    private lateinit var plusWinButton: Button
    private lateinit var commitWinsButton: Button
    private lateinit var setNameButton: Button
    private lateinit var unCommitCallsButton: Button
    private lateinit var unCommitWinsButton: Button
    private lateinit var nextRoundButton: Button
    private lateinit var currentRoundTV: TextView;
    private lateinit var player1Pts: TextView
    private lateinit var player2Pts: TextView
    private lateinit var player3Pts: TextView
    private lateinit var player4Pts: TextView
    private lateinit var callTitle: TextView
    private lateinit var winTitle: TextView
    private lateinit var currentCallTV: TextView
    private lateinit var currentWinTV: TextView
    private lateinit var playersSpinner: Spinner
    private lateinit var modeSpinner: Spinner
    private lateinit var hLineBazas: View
    private lateinit var hLinePlayers: View
    private lateinit var hLineCalled: View
    private lateinit var hLineWin: View

    private lateinit var game: Game
    private var backgroundThread: HandlerThread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        hideAll()
        game = Game()
        initButtons()
        initSpinners()
    }

    override fun onResume() {
        super.onResume()
        startBackgroundThread()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onStop() {
        super.onStop()
        stopBackgroundThread()
    }


    private fun startBackgroundThread() {
        backgroundThread = HandlerThread("BackgroundThread")
        backgroundThread!!.start()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun stopBackgroundThread() {
        backgroundThread!!.quitSafely()
        try {
            backgroundThread!!.join()
            backgroundThread = null
            //backgroundHandler = null;
        } catch (e: InterruptedException) {
            Log.e("Main","Interrupted when stopping background thread", e)
        }
    }

    private fun findViews() {
        playersSpinner =findViewById(R.id.playersSpinner)
        modeSpinner=findViewById(R.id.modeSpinner)
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
        commitWinsButton=findViewById(R.id.commitWinsButton)
        minusWinButton=findViewById(R.id.minusWinButton)
        plusWinButton=findViewById(R.id.plusWinButton)
        currentCallTV=findViewById(R.id.currentCallTV)
        currentWinTV=findViewById(R.id.currentWinTV)
        setNameButton=findViewById(R.id.setNameButton)
        hLineBazas=findViewById(R.id.hline_bazas)
        hLinePlayers=findViewById(R.id.hline_players)
        hLineCalled=findViewById(R.id.hline_called)
        hLineWin=findViewById(R.id.hline_win)
        callTitle=findViewById(R.id.callTitle)
        winTitle=findViewById(R.id.winTitle)
        nextRoundButton=findViewById(R.id.nextRoundButton)
        unCommitCallsButton=findViewById(R.id.unCommitCallsButton)
        unCommitWinsButton=findViewById(R.id.unCommitWinsButton)
    }

    private fun hideAll() {
        val viewsToHide = mutableListOf(
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
            commitWinsButton,
            hLineBazas,
            hLinePlayers,
            hLineCalled,
            hLineWin,
            callTitle,
            winTitle,
            nextRoundButton,
            unCommitCallsButton,
            unCommitWinsButton
        )

        viewsToHide.forEach{
            it.visibility=View.INVISIBLE
        }
    }

    private fun initSpinners() {
        // Mode Spinner:
        val modeAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this, R.array.modes, android.R.layout.simple_spinner_item)
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.adapter = modeAdapter
        modeSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val itemSelected = parent!!.getItemAtPosition(pos).toString()
                game.setMode(itemSelected)
            }
        }

        // Players Spinner:
        val playerAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this, R.array.players, android.R.layout.simple_spinner_item)
        playerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playersSpinner.adapter = playerAdapter
        playersSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val itemSelected = parent!!.getItemAtPosition(pos).toString()
                if (itemSelected == "Select Number of Players") {
                    //Do nothing
                } else {
                    game.setRounds(itemSelected.toInt())
                    startApp()
                }
            }
        }
    }

    private fun startApp(){
        modeSpinner.isEnabled = false
        playersSpinner.isEnabled = false
        enableCallStage(true)
        enableWinStage(false)
        initPlayers()
        showAll()
        updateUI()
        onNextRound()
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
        if (game.currentPlayers==4){
            viewsToShow.add(player4Button)
            viewsToShow.add(player4Pts)
        }
        viewsToShow.forEach{
            it.visibility=View.VISIBLE
        }

        // Store new players:
        for (index in 0 until game.currentPlayers) {
            val playerName: String = "Player "+(index+1)
            game.players.add(Player(playerName, index))
        }
        setPlayerFocus(Player.Players.PLAYER_1)
    }

    private fun showAll() {
        val viewsToShow = mutableListOf(
            currentRoundTV,
            minusCallButton,
            plusCallButton,
            commitCallsButton,
            setNameButton,
            minusWinButton,
            plusWinButton,
            currentCallTV,
            currentWinTV,
            commitWinsButton,
            hLineBazas,
            hLinePlayers,
            hLineCalled,
            hLineWin,
            callTitle,
            winTitle,
            nextRoundButton,
            unCommitCallsButton,
            unCommitWinsButton
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
        commitWinsButton.setOnClickListener { commitWonButtonClicked() }
        player1Button.setOnClickListener { setPlayerFocus(Player.Players.PLAYER_1)}
        player2Button.setOnClickListener { setPlayerFocus(Player.Players.PLAYER_2)}
        player3Button.setOnClickListener { setPlayerFocus(Player.Players.PLAYER_3)}
        player4Button.setOnClickListener { setPlayerFocus(Player.Players.PLAYER_4)}
        unCommitCallsButton.setOnClickListener { unCommitCallsClicked() }
        unCommitWinsButton.setOnClickListener { unCommitWinsClicked() }
        nextRoundButton.setOnClickListener { nextRoundButtonClicked() }
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
            setPositiveButton("OK") { _, _ ->
                game.currentPlayer.name = editText.text.toString()
                Log.i("Main", "Positive Button Clicked")
                updateUI()
            }
            setNegativeButton("Cancel") { dialog, which ->
                Log.i("Main", "Negative Button Clicked")
            }
            setView(dialogLayout)
            show()
        }
    }

    private fun plusWinButtonClicked() {
        Log.d("Main", "plusWinButtonClicked")
        game.currentPlayer.currentWin +=1
        updateUI()
    }

    private fun plusCallButtonClicked() {
        Log.d("Main", "plusCallButtonClicked")
        game.currentPlayer.currentCall +=1
        updateUI()
    }

    private fun minusWinButtonClicked() {
        Log.d("Main", "minusWinButtonClicked")
        game.currentPlayer.currentWin -=1
        updateUI()
    }

    private fun minusCallButtonClicked() {
        Log.d("Main", "minusCallButtonClicked")
        game.currentPlayer.currentCall -=1
        updateUI()
    }

    @SuppressLint("ShowToast")
    private fun commitCallsButtonClicked() {
        Log.d("Main", "commitCallsButtonClicked")
        val isValid = game.checkValidCall()
        if (isValid){
            game.commitCalls()
            updateUI()
            enableCallStage(false)
            enableWinStage(true)
            unCommitCallsButton.isEnabled = true
        } else {
            Toast.makeText(this@MainActivity, "Invalid call. Try again", Toast.LENGTH_SHORT)
            Log.d("Main","Invalid call. Try again" )
        }
    }

    private fun unCommitCallsClicked() {
        game.unCommitCalls()
        updateUI()
        enableCallStage(true)
        enableWinStage(false)
        unCommitCallsButton.isEnabled = false
    }

    @SuppressLint("ShowToast")
    private fun commitWonButtonClicked() {
        Log.d("Main", "commitWonButtonClicked")
        val isValid = game.checkValidWon()
        if (isValid){
            game.commitWins()
            updateUI()
            enableWinStage(false)
            unCommitWinsButton.isEnabled = true
            nextRoundButton.isEnabled = true
        } else {
            Toast.makeText(this@MainActivity, "Invalid won. Try again", Toast.LENGTH_SHORT)
            Log.d("Main","Invalid won. Try again" )
        }
    }

    private fun unCommitWinsClicked() {
        game.unCommitWins()
        updateUI()
        enableWinStage(true)
        unCommitWinsButton.isEnabled = false
        nextRoundButton.isEnabled = false
    }

    private fun nextRoundButtonClicked() {
        Log.d("Main","nextRoundButtonClicked")
        game.computePoints()
        if (game.isFinished()) {
            onGameFinished()
        } else {
            onNextRound()
        }
    }

    private fun onNextRound() {
        enableCallStage(true)
        enableWinStage(false)
        game.nextRound()
        updateUI()
        nextRoundButton.isEnabled = false
    }

    private fun onGameFinished() {
        enableCallStage(false)
        enableWinStage(false)
        Log.d("Main", "onGameFinished")
        updateUI()
    }


    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        // Update Points:
        val playerScores = mutableListOf<TextView>(
            player1Pts,
            player2Pts,
            player3Pts
        )
        if (game.currentPlayers==4)
            playerScores.add(player4Pts)

        playerScores.forEachIndexed{index, tv ->
            tv.text = game.players[index].points.toString()
        }

        // Update Titles:
        callTitle.text = "Calls (Table: ${game.getCallsSum()})"
        winTitle.text = "Wins (Table: ${game.getWinsSum()})"

        if (game.isFinished()) {
            val winner = game.findWinnerPlayer()
            val count = winner.countCorrect()
            val msg = "${winner.name} won with $count correct hands"
            currentRoundTV.text = msg
            currentRoundTV.setTextColor(resources.getColor(R.color.colorPrimary))
            nextRoundButton.isEnabled = false
            Log.d("Main", msg)
        } else {
            // Update texts:
            val triumphSymbol = if(game.currentTriumph==1) {"⦿"} else {"⦾" }
            currentRoundTV.text = "Round: ${game.currentRound}  $triumphSymbol"
            currentCallTV.text = game.currentPlayer.currentCall.toString()
            currentWinTV.text = game.currentPlayer.currentWin.toString()
        }

        // Update Button Appearance:
        val buttons = mutableListOf<Button>(
            player1Button,
            player2Button,
            player3Button
        )
        if (game.currentPlayers==4)
            buttons.add(player4Button)

        buttons.forEachIndexed{ index, button ->
            // Names
            button.text = game.players[index].name
            // Idle Behaviour
            button.setBackgroundColor(resources.getColor(R.color.colorIdle))
            button.setTextColor(resources.getColor(R.color.colorBlack))
            // Show hand
            if(index+1 == game.currentHand) {
                button.setTextColor(resources.getColor(R.color.colorPrimary))
            }
            // Selected
            if (game.currentPlayer.index == index) {
                button.setTextColor(resources.getColor(R.color.colorWhite))
                button.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                // Hand And Selected
                if(index+1 == game.currentHand) {
                    button.setTextColor(resources.getColor(R.color.colorHandLight))
                }
            }
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
        unCommitCallsButton.isEnabled = false
    }


    private fun enableWinStage(enable: Boolean) {
        val winStageViews = mutableListOf(
            minusWinButton,
            plusWinButton,
            currentWinTV,
            commitWinsButton
        )

        winStageViews.forEach {
            it.isEnabled = enable
        }
        unCommitWinsButton.isEnabled = false
    }

    private fun setPlayerFocus(playerID: Player.Players) {
        Log.d("Main", "setPlayerFocus on $playerID")
        game.setCurrentPlayer(playerID)
        updateUI()
    }

    // TODO: add logic for preventing total call == round

}