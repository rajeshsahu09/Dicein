package com.example.dicein

import android.widget.Button
import android.widget.TextView

class Player(var passdicebtn: Button, var rolldicebtn: Button, var score: TextView, var roundScore: TextView) {
    fun toggleButtons() {
        passdicebtn.isEnabled = !(passdicebtn.isEnabled)
        rolldicebtn.isEnabled = !(rolldicebtn.isEnabled)
    }
    fun intiPlayer(status: Boolean) {
        score.text = "0"
        roundScore.text = "0"

        this.passdicebtn.isEnabled = status
        this.rolldicebtn.isEnabled = status
    }
}