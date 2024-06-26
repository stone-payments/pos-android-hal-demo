package br.com.stone.posandroid.hal.demo.rule

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import br.com.stone.posandroid.hal.demo.HALConfig
import br.com.stone.posandroid.hal.demo.MainActivity
import br.com.stone.posandroid.hal.demo.R
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.Semaphore

/**
 * This rule provides a way of blocking the execution of test cases by showing a screen with a
 * [Precondition] or/and [Postcondition] message and a button to proceed with the execution.
 *
 * The use case of this rule is when executing tests on physical devices. Since some tests
 * depends on "hard to emulate" device states (printer without paper, low battery etc.) we need to
 * ensure that the device meet these conditions before proceeding with test execution, preventing
 * test failures.
 *
 */
class ConditionTestRule: ActivityTestRule<MainActivity>(MainActivity::class.java, false, false) {
    /**
     * Block the execution of the test and show a screen informing the precondition or/and postcondition that must be met
     * and a button to proceed with the execution.
     */
    override fun apply(base: Statement?, description: Description): Statement {

        val postcondition: Postcondition? = description.getAnnotation(Postcondition::class.java)
        val precondition: Precondition? = description.getAnnotation(Precondition::class.java)

        if (!HALConfig.runningOnEmulator && (postcondition != null || precondition != null)) {

            val semaphore = Semaphore(0)

            launchActivity(
                Intent(
                    InstrumentationRegistry.getInstrumentation().targetContext,
                    MainActivity::class.java
                )
            )

            runOnUiThread {
                activity.apply {
                    val preconditionText = precondition?.let{
                        String.format(getString(R.string.test_message_format_before), it.text)
                    }
                    val postconditionText = postcondition?.let {
                        String.format(getString(R.string.test_message_format_after), it.text)
                    }
                    val message = preconditionText.orEmpty() + postconditionText.orEmpty()

                    findViewById<TextView>(R.id.textView_test_title).text = description.methodName
                    findViewById<TextView>(R.id.textView_message).text = message
                    findViewById<Button>(R.id.button_proceed).apply{
                        visibility = View.VISIBLE
                        setOnClickListener {
                            semaphore.release()
                            it.visibility = View.GONE
                        }
                    }
                }
            }
            semaphore.acquire()
        }

        return super.apply(base, description)
    }
}