package jp.ac.asojuku.st.myminislot

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    private var myCoin = 1000
    private var betCoin = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_reset.setOnClickListener { resetCoin() }
        btn_up.setOnClickListener { upCoin() }
        btn_up.setOnLongClickListener { upMaxCoin(); true }
        btn_down.setOnClickListener { downCoin() }
        btn_down.setOnLongClickListener { downMaxCoin(); true }
        btn_start.setOnClickListener { startGame() }

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()
        editor.clear().apply()
    }

    override fun onResume() {
        super.onResume()
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        myCoin = pref.getInt("MY_COIN", 1000)
        // betCoin = pref.getInt("BET_COIN", 0)
        betCoin = 0
        text_myCoinValue.text = myCoin.toString()
        text_betCoinValue.text = 10.toString()
    }

    private fun resetCoin() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()
        editor.clear().apply()
        myCoin = 1000
        betCoin = 0

        text_myCoinValue.text = 1000.toString()
        text_betCoinValue.text = 10.toString()
    }

    private fun upCoin() {
        if (myCoin > 0) {
            myCoin -= 10
            betCoin += 10
            text_myCoinValue.text = myCoin.toString()
            text_betCoinValue.text = betCoin.toString()
        }
    }

    private fun upMaxCoin() {
        if (myCoin > 10) {
            betCoin += myCoin - 10
            myCoin = 10
            text_myCoinValue.text = myCoin.toString()
            text_betCoinValue.text = betCoin.toString()
        }
    }

    private fun downCoin() {
        if (betCoin > 10) {
            myCoin += 10
            betCoin -= 10
            text_myCoinValue.text = myCoin.toString()
            text_betCoinValue.text = betCoin.toString()
        }
    }

    private fun downMaxCoin() {
        if (betCoin > 10) {
            myCoin += betCoin - 10
            betCoin = 10
            text_myCoinValue.text = myCoin.toString()
            text_betCoinValue.text = 10.toString()
        }
    }

    private fun startGame() {
        if (myCoin <= 0) {
            return Toast.makeText(applicationContext, "所持金が0になっています！", Toast.LENGTH_SHORT).show()
        } else if (betCoin == 0) {
            betCoin = 10
            myCoin -= 10
        }

        startActivity<GameActivity>("MY_COIN" to myCoin, "BET_COIN" to betCoin)
    }
}
