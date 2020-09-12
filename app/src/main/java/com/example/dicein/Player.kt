package com.example.dicein

import android.view.View
import android.widget.Button
import android.widget.TextView

class Player(
    var passdicebtn: Button,
    var rolldicebtn: Button,
    var score: TextView,
    var roundScore: TextView,
    var playerText: TextView,
    var roundMessage: TextView
) {
    fun intiPlayer(status: Boolean) {
        score.text = "0"
        roundScore.text = "0"

        passdicebtn.visibility = if(status) View.VISIBLE else View.INVISIBLE
        rolldicebtn.visibility = if(status) View.VISIBLE else View.INVISIBLE
        roundMessage.visibility = if(!status) {
            if(playerText.text.toString() == "Player 1") roundMessage.text = "Player-2 Starts"
            else roundMessage.text = "Player-1 Starts"
            View.VISIBLE
        } else View.INVISIBLE
    }

    fun toggleButtons() {
        passdicebtn.visibility = if(passdicebtn.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
        rolldicebtn.visibility = if(rolldicebtn.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
    }

    fun disableButtons() {
        passdicebtn.visibility = View.INVISIBLE
        rolldicebtn.visibility = View.INVISIBLE
    }

    fun restoreButtons(status: Boolean) {
        passdicebtn.visibility = if(status) View.VISIBLE else View.INVISIBLE
        rolldicebtn.visibility = if(status) View.VISIBLE else View.INVISIBLE
    }

    fun enableButtons() {
        passdicebtn.visibility = View.VISIBLE
        rolldicebtn.visibility = View.VISIBLE
    }

    fun clearTexts() {
        score.text = "0"
        roundScore.text = "0"
    }
}