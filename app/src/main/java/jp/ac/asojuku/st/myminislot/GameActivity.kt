package jp.ac.asojuku.st.myminislot

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {
    private var myCoin = 0
    private var betCoin = 0

    private var imageButton01: Int? = null
    private var imageButton02: Int? = null
    private var imageButton03: Int? = null

    private var cheatMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        myCoin = intent.getIntExtra("MY_COIN", 1000)
        betCoin = intent.getIntExtra("BET_COIN", 10)

        text_myCoinValueGame.text = myCoin.toString()
        text_betCoinValueGame.text = betCoin.toString()

        btn_back.setOnClickListener { finish() }
        btn_img01.setOnClickListener { imageChange(btn_img01) }
        btn_img02.setOnClickListener { imageChange(btn_img02) }
        btn_img03.setOnClickListener { imageChange(btn_img03) }

        image_icon.setOnClickListener {
            if (imageButton01 != null && imageButton02 != null && imageButton03 != null && cheatMode) myCoinUpdate(gameJudgment())
        }
        image_icon.setOnLongClickListener {
            cheatMode = if (cheatMode) {
                Toast.makeText(applicationContext, "チートモードを無効にしました！！", Toast.LENGTH_SHORT).show()
                false
            } else {
                Toast.makeText(applicationContext, "チートモードを有効にしました！！", Toast.LENGTH_SHORT).show()
                true
            }
            true
        }
    }

    private fun imageChange(imageButton: ImageButton) {
        if (!cheatMode) {
            if ((imageButton == btn_img01 && imageButton01 != null) || (imageButton == btn_img02 && imageButton02 != null) || (imageButton == btn_img03 && imageButton03 != null)) {
                return
            }
        }
        val random = (Math.random() * 10).toInt()
        val nowImage: Int
        when (random) {
            0 -> imageButton.setImageResource(R.drawable.banana)
            1 -> imageButton.setImageResource(R.drawable.bar)
            2 -> imageButton.setImageResource(R.drawable.bigwin)
            3 -> imageButton.setImageResource(R.drawable.cherry)
            4 -> imageButton.setImageResource(R.drawable.grape)
            5 -> imageButton.setImageResource(R.drawable.lemon)
            6 -> imageButton.setImageResource(R.drawable.orange)
            7 -> imageButton.setImageResource(R.drawable.seven)
            8 -> imageButton.setImageResource(R.drawable.waltermelon)
            9 -> imageButton.setImageResource(R.drawable.kin)
        }
        when (random) {
            in 0..9 -> nowImage = random
            else -> nowImage = 0
        }
        when (imageButton) {
            btn_img01 -> imageButton01 = nowImage
            btn_img02 -> imageButton02 = nowImage
            btn_img03 -> imageButton03 = nowImage
        }
        if (!cheatMode) {
            if ((imageButton01 != null) && (imageButton02 != null) && (imageButton03 != null)) {
                val judgmentResult = gameJudgment()
                myCoinUpdate(judgmentResult)
            }
        }
    }

    private fun gameJudgment(): Int {
        var magnification = 0
        if (imageButton01 == imageButton02 && imageButton01 == imageButton03 && imageButton02 == imageButton03) {
            if (imageButton01 == 9) {
                magnification = 999
            } else if (imageButton01 == 7) {
                magnification = 20
            } else if (imageButton01 == 2) {
                magnification = 10
            } else if (imageButton03 == 1) {
                magnification = 5
            } else {
                magnification = 2
            }
        } else if (imageButton01 == imageButton02 || imageButton01 == imageButton03 || imageButton02 == imageButton03) {
            if (imageButton01 == imageButton02) {
                when (imageButton01) {
                    7 -> magnification = 3
                    else -> magnification = 1
                }
            }
            if (imageButton01 == imageButton03) {
                when (imageButton01) {
                    7 -> magnification = 3
                    else -> magnification = 1
                }
            }
            if (imageButton02 == imageButton03) {
                when (imageButton02) {
                    7 -> magnification = 3
                    else -> magnification = 1
                }
            }
        } else {
            if (imageButton01 == 6 && imageButton02 == 3 && imageButton03 == 5) {
                magnification = 30
            } else if (imageButton01 == 8 && imageButton02 == 0 && imageButton03 == 4) {
                magnification = 10
            } else if (imageButton01 == 8 || imageButton02 == 8 || imageButton03 == 8) {
                magnification = 1
            } else {
                magnification = -1
            }
        }
        Toast.makeText(applicationContext, "倍率" + magnification +"！！", Toast.LENGTH_SHORT).show()
        return magnification
    }

    private fun myCoinUpdate(magnification: Int) {
        if (magnification == -1) {
            myCoin -= betCoin * (magnification * -1)
        } else {
            myCoin += betCoin * magnification
        }
        text_myCoinValueGame.text = myCoin.toString()
        text_betCoinValueGame.text = betCoin.toString()
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()
        editor.putInt("MY_COIN", myCoin).putInt("BET_COIN", betCoin).apply()
    }
}
