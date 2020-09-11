package com.example.dicein

import android.widget.Button
import android.widget.TextView

class Player(var passdicebtn: Button, var rolldicebtn: Button, var score: TextView, var roundScore: TextView, var playerText: TextView) {
    fun toggleButtons() {
        passdicebtn.isEnabled = !(passdicebtn.isEnabled)
        rolldicebtn.isEnabled = !(rolldicebtn.isEnabled)
    }
    fun intiPlayer(status: Boolean) {
        score.text = "0"
        roundScore.text = "0"

        passdicebtn.isEnabled = status
        rolldicebtn.isEnabled = status
    }
    fun disableButtons() {
        passdicebtn.isEnabled = false
        rolldicebtn.isEnabled = false
    }
    fun enableButtons() {
        passdicebtn.isEnabled = true
        rolldicebtn.isEnabled = true
    }
    fun clearTexts() {
        score.text = "0"
        roundScore.text = "0"
    }
}