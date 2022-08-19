package br.com.stone.posandroid.hal.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import br.com.stone.posandroid.hal.api.Properties
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button: Button = findViewById(R.id.button_toggle_home)
        button.setOnClickListener {
            thread {
                val subject =
                    HALConfig.deviceProvider.getSettings(mapOf(Properties.KEY_CONTEXT to applicationContext))
                val toggleHomeKeyFunction = subject.toggleHomeKeyFunction(true)
                runOnUiThread {
                    Toast.makeText(
                        applicationContext,
                        "Desativado $toggleHomeKeyFunction",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
