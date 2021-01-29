package org.luciano.bazas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView

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
    private lateinit var minusCallButton: Button
    private lateinit var plusCallButton: Button
    private lateinit var startButton: Button
    private lateinit var minusMadeButton: Button
    private lateinit var plusMadeButton: Button
    private lateinit var currentCallTV: TextView
    private lateinit var currentMadeTV: TextView
    private lateinit var nextRoundButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
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
           startButton=findViewById(R.id.startButton)
           minusMadeButton=findViewById(R.id.minusMadeButton)
           plusMadeButton=findViewById(R.id.plusMadeButton)
           currentCallTV=findViewById(R.id.currentCallTV)
           currentMadeTV=findViewById(R.id.currentMadeTV)
           nextRoundButton=findViewById(R.id.nextRoundButton)
    }

}