package com.example.dicein

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    var roundScore = 0
    var activePlayer = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        diceImg.visibility = View.INVISIBLE;
        activePlayer = (1 until 3).random()

        currentScore1Text.text = roundScore.toString()
        currentScore2Text.text = roundScore.toString()
        score1Text.text = roundScore.toString()
        score2Text.text = roundScore.toString()

        if(activePlayer == 1) {
            rollDice2btn.isClickable = false
            passDice2btn.isClickable = false
            rollDice1btn.isClickable = true
            passDice1btn.isClickable = true
        } else {
            rollDice2btn.isClickable = true
            passDice2btn.isClickable = true
            rollDice1btn.isClickable = false
            passDice1btn.isClickable = false
        }
    }

    fun onRoll1BtnClick(view: View) {
        // 1 roll the dice
        val dice = (1 until 7).random()

        // 2 display outcome
        diceImg.visibility = View.VISIBLE;
        val resourceId = resources.getIdentifier("dice${dice}", "drawable", packageName)
        diceImg.setImageResource(resourceId)

        // 3 update score
        if(dice == 1) {
            // reset values and pass to other player
            roundScore = 0
            currentScore1Text.text = roundScore.toString()
            diceImg.visibility = View.INVISIBLE;
            activePlayer = 2
            rollDice2btn.isClickable = true
            passDice2btn.isClickable = true
            rollDice1btn.isClickable = false
            passDice1btn.isClickable = false
        } else {
            // update roundScore
            roundScore += dice
            currentScore1Text.text = roundScore.toString()
        }
    }

    fun onPass1BtnClick(view: View) {
        score1Text.text = (score1Text.text.toString().toInt() + roundScore).toString()
        if(score1Text.text.toString().toInt() >= 100) {
            Toast.makeText(this, "player 1 wins", Toast.LENGTH_SHORT).show()
        }
        roundScore = 0
        currentScore1Text.text = roundScore.toString()
        diceImg.visibility = View.INVISIBLE;
        activePlayer = 2
        rollDice2btn.isClickable = true
        passDice2btn.isClickable = true
        rollDice1btn.isClickable = false
        passDice1btn.isClickable = false

    }

    fun onRoll2BtnClick(view: View) {
        // 1 roll the dice
        val dice = (1 until 7).random()

        // 2 display outcome
        diceImg.visibility = View.VISIBLE;
        val resourceId = resources.getIdentifier("dice${dice}", "drawable", packageName)
        diceImg.setImageResource(resourceId)

        // 3 update score
        if(dice == 1) {
            // reset values and pass to other player
            roundScore = 0
            currentScore2Text.text = roundScore.toString()
            diceImg.visibility = View.INVISIBLE;
            activePlayer = 1
            rollDice2btn.isClickable = false
            passDice2btn.isClickable = false
            rollDice1btn.isClickable = true
            passDice1btn.isClickable = true
        } else {
            // update roundScore
            roundScore += dice
            currentScore2Text.text = roundScore.toString()
        }

    }

    fun onPass2BtnClick(view: View) {
        score2Text.text = (score2Text.text.toString().toInt() + roundScore).toString()
        if(score2Text.text.toString().toInt() >= 100) {
            Toast.makeText(this, "player 2 wins", Toast.LENGTH_SHORT).show()
            return;
        }
        roundScore = 0
        currentScore2Text.text = roundScore.toString()
        diceImg.visibility = View.INVISIBLE;
        activePlayer = 1
        rollDice2btn.isClickable = false
        passDice2btn.isClickable = false
        rollDice1btn.isClickable = true
        passDice1btn.isClickable = true
    }
}