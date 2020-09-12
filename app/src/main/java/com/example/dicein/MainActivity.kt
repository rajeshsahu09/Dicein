package com.example.dicein

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var activePlayer = -1

    lateinit var player1 : Player
    lateinit var player2 : Player
    lateinit var player : List<Player>

    fun initGame() {
        player1 = Player(passDice1btn, rollDice1btn, score1Text, currentScore1Text, Player1Text, roundMessage1Text)
        player2 = Player(passDice2btn, rollDice2btn, score2Text, currentScore2Text, Player2Text, roundMessage2Text)
        player = listOf<Player>(player1, player2)

        diceImg.visibility = View.INVISIBLE;
        newGameBtn.visibility = View.INVISIBLE;
        activePlayer = (0 until 2).random()
        roundScore = 0

        player[activePlayer].intiPlayer(true)
        player[1 - activePlayer].intiPlayer(false)
    }
    /**
     * when we change an activity from potrait to landscape or vice versa
     * The current activity is destroyed.
     * So we need to store the current activities values to pass onto other.
     * onSaveInstanceState(outState: Bundle) --> save state in this fun
     *  onRestoreInstanceState(savedInstanceState: Bundle) or "onCreate(savedInstanceState: Bundle?)"--> restore the values on this state
     */
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putParcelable(EXTRA_PLAYER, player)
//    }

//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        if(savedInstanceState != null) player = savedInstanceState.getParcelable(EXTRA_PLAYER)!!
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initGame()
    }

    fun toggleActivePlayer(actPlayer: Int) : Int {
        return (1 - actPlayer)
    }

    fun changePlayer(actPlayer: Int) : Int {
        // reset values and pass to other player
        roundScore = 0
        player[activePlayer].roundScore.text = roundScore.toString()
        diceImg.visibility = View.INVISIBLE;

        // toggle active player buttons
        player[activePlayer].toggleButtons()
        player[1 - activePlayer].toggleButtons()

        // change player
        activePlayer = toggleActivePlayer(actPlayer)
        // remove the message
        player[activePlayer].roundMessage.visibility = View.INVISIBLE

        return activePlayer
    }

    fun rollDice(score: Int, currScore: Int): Int { // generates random integers from 1 to 6
        val dice = (1 until 7).random()
        val threshold = (70 until 80).random()
        var outcome = 1
        if(score <= threshold) {
            if(dice > 1) outcome = dice
            else if(currScore == 0 && dice == 1) {
                val luck = (1 until 6).random() // [1-5]
                if(luck > 2) outcome = rollDice(score, currScore)
                else outcome = 1
            } else outcome = 1
        } else {
            if(1 < (2 * dice) && (2 * dice) < 10) outcome = dice; // 2, 3, 4
            else { // dice = [1, 5, 6]
                val idx = (0 until 2).random()
                if(idx == 0) outcome = 1 // if
                else {
                    outcome = (5 until 7).random()
                }
            }
        }
        return outcome
    }

    fun getResourceId(diceOutcome: Int): Int {
        diceImg.visibility = View.VISIBLE;
        // fetch the resource images using the res.drawable.imagesPackageNames
        return resources.getIdentifier("dice${diceOutcome}", "drawable", packageName)
    }

    // BUTTON PRESSED
    fun onRollBtnClick(view: View) {
        // 1 roll the dice
        val dice = rollDice(player[activePlayer].score.text.toString().toInt(), roundScore)

        // 2 display outcome in the image view
        diceImg.setImageResource(getResourceId(dice))

        // 3 update score
        if(dice == 1) {
            setRoundMessage("OPPs You Got 1", activePlayer)
            // change player
            activePlayer = changePlayer(activePlayer)
        } else {
            // update roundScore
            roundScore += dice
            player[activePlayer].roundScore.text = roundScore.toString()
        }
    }

    fun onNewGameClick(view: View) {
        Player1Text.text = "Player 1"
        Player2Text.text = "Player 2"
        initGame()
    }

    fun setRoundMessage(msg: String, actPlayer: Int) {
        player[actPlayer].roundMessage.typeface = ResourcesCompat.getFont(this.applicationContext, R.font.actor)
        player[actPlayer].roundMessage.visibility = View.VISIBLE
        player[actPlayer].roundMessage.text = msg
    }

    fun onPassBtnClick(view: View) {
        var score = player[activePlayer].score.text.toString().toInt();
        player[activePlayer].score.text = (score + roundScore).toString()
        score = player[activePlayer].score.text.toString().toInt();

        if(score >= 100) {
            // active player wins
            player[activePlayer].playerText.text = "Winner \uD83C\uDFC6 \uD83E\uDD73"
            player[1-activePlayer].playerText.text = "Looser \uD83D\uDE25"
            setRoundMessage("YOO-HOO!!!", activePlayer)
            setRoundMessage("HARD-LUCK", 1-activePlayer)

            // ui changes
            player[activePlayer].disableButtons()
            player[1-activePlayer].disableButtons()
            newGameBtn.visibility = View.VISIBLE
            diceImg.visibility = View.INVISIBLE
            return
        }

        // set message
        setRoundMessage("Round Score ${roundScore}", activePlayer)
        player[1-activePlayer].roundMessage.visibility = View.INVISIBLE

        // change player
        activePlayer = changePlayer(activePlayer);
    }
}
