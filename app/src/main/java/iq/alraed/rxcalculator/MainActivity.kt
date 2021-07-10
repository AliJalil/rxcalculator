package iq.alraed.rxcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import net.objecthunter.exp4j.ExpressionBuilder
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var calTxt: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calTxt = findViewById(R.id.calTxt)

        Observable.create<String> { emitter ->
            calTxt.doOnTextChanged { text, _, _, _ ->
                emitter.onNext(text.toString())
            }
        }.debounce(1500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t ->
                Log.i("ALI_TAG", t)
                calTxt.setText(showResult())
            }

    }


    private fun showResult(): String {
        try {
            val text = calTxt.text.toString()
            val expBuild = ExpressionBuilder(text).build()
            val eval = expBuild.evaluate()
            val longEval = eval.toLong()
            return if (eval == longEval.toDouble()) {
                longEval.toString()
            } else {
                eval.toString()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
        return "0"
    }
}