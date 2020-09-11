package com.example.dicein

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var activePlayer = -1

    lateinit var player1 : Player
    lateinit var player2 : Player
    lateinit var player : List<Player>

    fun toggleActivePlayer(actPlayer: Int) : Int {
        return (1 - actPlayer)
    }

    fun initGame() {
        player1 = Player(passDice1btn, rollDice1btn, score1Text, currentScore1Text, Player1Text)
        player2 = Player(passDice2btn, rollDice2btn, score2Text, currentScore2Text, Player2Text)
        player = listOf<Player>(player1, player2)

        diceImg.visibility = View.INVISIBLE;
        newGameBtn.visibility = View.INVISIBLE;
        activePlayer = (0 until 2).random()
        roundScore = 0

        player[activePlayer].intiPlayer(true)
        player[1 - activePlayer].intiPlayer(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initGame()
    }

    fun changePlayer(actPlayer: Int) {
        // reset values and pass to other player
        roundScore = 0
        player[activePlayer].roundScore.text = roundScore.toString()
        diceImg.visibility = View.INVISIBLE;

        // toggle active player buttons
        player[activePlayer].toggleButtons()
        player[1 - activePlayer].toggleButtons()
        activePlayer = toggleActivePlayer(activePlayer)
    }

    fun rollDice() : Int { // generates random integers from 1 to 6
        return (1 until 7).random()
    }
    fun getResourceId(diceOutcome: Int): Int {
        diceImg.visibility = View.VISIBLE;
        // fetch the resource images using the res.drawable.imagesPackageNames
        return resources.getIdentifier("dice${diceOutcome}", "drawable", packageName)
    }
    fun onRollBtnClick(view: View) {
        // 1 roll the dice
        val dice = rollDice()

        // 2 display outcome in the image view
        diceImg.setImageResource(getResourceId(dice))

        // 3 update score
        if(dice == 1) {
            // change player
            changePlayer(activePlayer)
        } else {
            // update roundScore
            roundScore += dice
            player[activePlayer].roundScore.text = roundScore.toString()
        }
    }

    fun onnewGameClick(view: View) {
        Player1Text.text = "Player 1"
        Player2Text.text = "Player 2"
        initGame()
    }

    fun onPassBtnClick(view: View) {
        var score = player[activePlayer].score.text.toString().toInt();
        player[activePlayer].score.text = (score + roundScore).toString()
        score = player[activePlayer].score.text.toString().toInt();

        if(score >= 10) {
            // active player wins
            player[activePlayer].playerText.text = "Winner \uD83C\uDFC6 \uD83E\uDD73"
            player[1-activePlayer].playerText.text = "Looser \uD83D\uDE25"

            // ui changes
            player[activePlayer].disableButtons()
            player[1-activePlayer].disableButtons()
            newGameBtn.visibility = View.VISIBLE
            diceImg.visibility = View.INVISIBLE
            return
        }

        // change player
        changePlayer(activePlayer);
    }
}
