package com.example.dicein

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var player1 : Player
    lateinit var player2 : Player
    lateinit var player : ArrayList<Player>
    var activePlayer = -1
    var winnerStatus = 0

    var mUri: Uri? = null

    fun initGame() {
        player1 = Player(
            passDice1btn,
            rollDice1btn,
            score1Text,
            currentScore1Text,
            Player1Text,
            roundMessage1Text,
            oponent1Score
        )
        player2 = Player(
            passDice2btn,
            rollDice2btn,
            score2Text,
            currentScore2Text,
            Player2Text,
            roundMessage2Text,
            oponent2Score
        )
        player = arrayListOf<Player>(player1, player2)

        diceImg.visibility = View.INVISIBLE;
        newGameBtn.visibility = View.INVISIBLE;
        activePlayer = (0 until 2).random()
        roundScore = 0
        winnerStatus = 0

        player[activePlayer].intiPlayer(true)
        player[1 - activePlayer].intiPlayer(false)
    }

    // save instance when orientation changes
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("score1", player[activePlayer].score.text.toString())
        outState.putString("oponentscore1", player[activePlayer].oponentScore.text.toString())
        outState.putString("roundscoretext1", player[activePlayer].roundScore.text.toString())
        outState.putString("player1", player[activePlayer].playerText.text.toString())
        outState.putString("roundmessage1", player[activePlayer].roundMessage.text.toString())

        outState.putString("score2", player[1 - activePlayer].score.text.toString())
        outState.putString("oponentscore2", player[1 - activePlayer].oponentScore.text.toString())
        outState.putString("roundscoretext2", player[1 - activePlayer].roundScore.text.toString())
        outState.putString("player2", player[1 - activePlayer].playerText.text.toString())
        outState.putString("roundmessage2", player[1 - activePlayer].roundMessage.text.toString())

        outState.putInt("roundscoreint", roundScore)
        outState.putInt("activeplayer", activePlayer)
        outState.putInt("winner", winnerStatus)

        outState.putParcelable("uri", mUri)
        outState.putBoolean("diceimagevisible", (diceImg.visibility == View.VISIBLE))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initGame()
    }

    // restore saved instance when orientation changes
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if(savedInstanceState != null) {
            roundScore = savedInstanceState.getInt("roundscoreint")
            activePlayer = savedInstanceState.getInt("activeplayer")
            winnerStatus = savedInstanceState.getInt("winner")

            player[activePlayer].score.text = savedInstanceState.getString("score1")
            player[activePlayer].oponentScore.text = savedInstanceState.getString("oponentscore1")
            player[activePlayer].roundScore.text = savedInstanceState.getString("roundscoretext1")
            player[activePlayer].playerText.text = savedInstanceState.getString("player1")
            player[activePlayer].roundMessage.text = savedInstanceState.getString("roundmessage1")

            player[1 - activePlayer].score.text = savedInstanceState.getString("score2")
            player[1 - activePlayer].oponentScore.text = savedInstanceState.getString("oponentscore2")
            player[1 - activePlayer].roundScore.text = savedInstanceState.getString("roundscoretext2")
            player[1 - activePlayer].playerText.text = savedInstanceState.getString("player2")
            player[1 - activePlayer].roundMessage.text = savedInstanceState.getString("roundmessage2")

            player[activePlayer].restoreUI(true, winnerStatus)
            player[1 - activePlayer].restoreUI(false, winnerStatus)

            newGameBtn.visibility = if(winnerStatus == 1) View.VISIBLE else View.INVISIBLE

            mUri = savedInstanceState.getParcelable("uri");
            diceImg.setImageURI(null)
            diceImg.setImageURI(mUri)
            val visible = savedInstanceState.getBoolean("diceimagevisible")
            diceImg.visibility = if(visible) View.VISIBLE else View.INVISIBLE
        }
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

    fun getResourceId(diceOutcome: Int): Uri? {
        diceImg.visibility = View.VISIBLE;
        // fetch the resource images using the res.drawable.imagesPackageNames
//        return resources.getIdentifier("dice${diceOutcome}", "drawable", packageName)
        mUri = Uri.parse("android.resource://$packageName/drawable/dice$diceOutcome")
        return mUri
    }

    // BUTTON PRESSED
    fun onRollBtnClick(view: View) {
        // 1 roll the dice
        val dice = rollDice(player[activePlayer].score.text.toString().toInt(), roundScore)

        // 2 display outcome in the image view
        diceImg.setImageURI(null)
        diceImg.setImageURI(getResourceId(dice))

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
        player[actPlayer].roundMessage.typeface = ResourcesCompat.getFont(
            this.applicationContext,
            R.font.actor
        )
        player[actPlayer].roundMessage.visibility = View.VISIBLE
        player[actPlayer].roundMessage.text = msg
    }

    fun onPassBtnClick(view: View) {
        var score = player[activePlayer].score.text.toString().toInt();
        player[activePlayer].score.text = (score + roundScore).toString()
        score = player[activePlayer].score.text.toString().toInt();

        player[activePlayer].oponentScore.text = player[activePlayer].score.text
        player[1 - activePlayer].oponentScore.text = player[1 - activePlayer].score.text

        if(score >= 10) {
            // set winner status
            winnerStatus = 1

            // active player wins
            player[activePlayer].playerText.text = "Winner \uD83C\uDFC6 \uD83E\uDD73"
            player[1 - activePlayer].playerText.text = "Looser \uD83D\uDE25"
            setRoundMessage("YOO-HOO!!!", activePlayer)
            setRoundMessage("HARD-LUCK", 1 - activePlayer)

            // ui changes
            player[activePlayer].disableButtons()
            player[1 - activePlayer].disableButtons()
            newGameBtn.visibility = View.VISIBLE
            diceImg.visibility = View.INVISIBLE
            return
        }

        // set message
        setRoundMessage("Round Score ${roundScore}", activePlayer)
        player[1 - activePlayer].roundMessage.visibility = View.INVISIBLE

        // change player
        activePlayer = changePlayer(activePlayer)
    }
}
