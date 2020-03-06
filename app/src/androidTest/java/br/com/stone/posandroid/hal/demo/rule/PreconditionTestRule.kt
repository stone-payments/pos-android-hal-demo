package br.com.stone.posandroid.hal.demo.rule

import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import br.com.stone.posandroid.hal.demo.HALConfig
import br.com.stone.posandroid.hal.demo.MainActivity
import br.com.stone.posandroid.hal.demo.R
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.Semaphore

/**
 * This rule provides a way of blocking the execution of test cases by showing a screen with a
 * [Precondition] message and a button to proceed with the execution.
 *
 * The use case of this rule is when executing tests on physical devices. Since some tests
 * depends on "hard to emulate" device states (printer without paper, low battery etc.) we need to
 * ensure that the device meet these conditions before proceeding with test execution, preventing
 * test failures.
 *
 */
class PreconditionTestRule :
    ActivityTestRule<MainActivity>(MainActivity::class.java, false, false) {

    /**
     * Block the execution of the test and show a screen informing the precondition that must be met
     * and a button to proceed with the execution.
     */
    override fun apply(base: Statement?, description: Description): Statement {

        val precondition = description.getAnnotation(Precondition::class.java)

        if (!HALConfig.runningOnEmulator && precondition != null) {

            val semaphore = Semaphore(0)

            launchActivity(
                Intent(
                    getInstrumentation().targetContext,
                    MainActivity::class.java
                )
            )

            runOnUiThread {
                activity.apply {
                    findViewById<TextView>(R.id.textView_test_title).text = description.methodName
                    findViewById<TextView>(R.id.textView_message).text =
                        String.format(getString(R.string.test_message_format), precondition.text)
                    findViewById<Button>(R.id.button_proceed).setOnClickListener {
                        semaphore.release()
                    }
                }
            }
            semaphore.acquire()
        }

        return super.apply(base, description)
    }
}