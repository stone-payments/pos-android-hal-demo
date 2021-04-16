package br.com.stone.posandroid.hal.demo.printer

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import br.com.stone.posandroid.hal.api.Properties.KEY_CONTEXT
import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.printer.Printer
import br.com.stone.posandroid.hal.api.printer.PrinterBuffer
import br.com.stone.posandroid.hal.api.printer.PrinterBuffer.Companion.NO_PRINTER_STEP
import br.com.stone.posandroid.hal.api.printer.PrinterErrorCode
import br.com.stone.posandroid.hal.api.printer.customize.Alignment
import br.com.stone.posandroid.hal.api.printer.customize.CustomizedTextSize
import br.com.stone.posandroid.hal.api.printer.customize.PrinterCustomizedText
import br.com.stone.posandroid.hal.api.printer.exception.PrinterException
import br.com.stone.posandroid.hal.api.printer.ext.printOrThrows
import br.com.stone.posandroid.hal.demo.HALConfig
import br.com.stone.posandroid.hal.demo.rule.ConditionTestRule
import br.com.stone.posandroid.hal.demo.rule.Precondition
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class PrinterTest {

    private val stubResultsFolder = "resources/printer/printer-test"
    private lateinit var subject: Printer
    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }

    @get:Rule
    val conditionTestRule = ConditionTestRule()

    @Test
    @Precondition("Printer must have paper")
    fun printOk() = runBlocking {
        subject = HALConfig.deviceProvider.getPrinter(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/print-ok.json",
                KEY_CONTEXT to context
            )
        )

        val param = PrinterBuffer()
        param.addLine(Printer::class.simpleName.toString())

        try {
            subject.printOrThrows(param)
        } catch (e: PrinterException) {
            fail("Codigo de erro: ${e.code}")
        }
    }

    @Test
    @Precondition("Printer must have paper")
    fun printCustomizedText() = runBlocking {
        subject = HALConfig.deviceProvider.getPrinter(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/print-ok.json",
                KEY_CONTEXT to context
            )
        )

        val param = PrinterBuffer()
        for (align in Alignment.values()) {
            param.addLine(object : PrinterCustomizedText {
                override fun getText(): String = align.name

                override fun getAlignment(): Alignment = align

                override fun getTextSize(): CustomizedTextSize =
                    CustomizedTextSize.MEDIUM_32_COLUMNS
            })
        }

        for (textSize in CustomizedTextSize.values()) {
            param.addLine(object : PrinterCustomizedText {
                override fun getText(): String = textSize.name + ("*".repeat(48))

                override fun getAlignment(): Alignment = Alignment.LEFT

                override fun getTextSize(): CustomizedTextSize = textSize
            })
        }

        param.addLine("-".repeat(48))

        try {
            subject.printOrThrows(param)
        } catch (e: PrinterException) {
            fail("Codigo de erro: ${e.code}")
        }
    }

    @Test
    @Precondition("Printer must have paper")
    fun printWithoutContent() = runBlocking {

        subject = HALConfig.deviceProvider.getPrinter(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/print-ok.json",
                KEY_CONTEXT to context
            )
        )

        try {
            subject.printOrThrows(PrinterBuffer())
        } catch (e: PrinterException) {
            fail("Codigo de erro: ${e.code}")
        }
    }

    @Test
    @Precondition("Printer must have no paper")
    fun printNoPaper() = runBlocking {
        subject = HALConfig.deviceProvider.getPrinter(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/print-no-paper.json",
                KEY_CONTEXT to context
            )
        )

        val param = PrinterBuffer()
        param.addLine(Printer::class.simpleName.toString())

        try {
            subject.printOrThrows(param)
            fail("Should fail printing")
        } catch (e: PrinterException) {
            assertEquals(PrinterErrorCode.PRINTER_OUT_OF_PAPER, e.code)
        }
    }

    @Test
    @Precondition("Printer not busy ")
    fun printUnsupportedFormat() = runBlocking {
        subject = HALConfig.deviceProvider.getPrinter(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/print-unsupported-format.json",
                KEY_CONTEXT to context
            )
        )

        val printerBuffer = PrinterBuffer(mutableListOf(0, 1, 2, 3), NO_PRINTER_STEP)
        try {

            subject.printOrThrows(printerBuffer)
            fail("Should fail printing")
        } catch (e: Throwable) {
            if (e is PrinterException) {
                val result =
                    (e.code == PrinterErrorCode.PRINTER_UNSUPPORTED_FORMAT || e.code == PrinterErrorCode.PRINTER_INVALID_DATA)
                assertTrue(result)
            }
        }
    }

    @Test
    fun printWithPrinterBusy() {
        subject = HALConfig.deviceProvider.getPrinter(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/print-with-printer-busy.json",
                KEY_CONTEXT to context
            )
        )
        val param = PrinterBuffer()
        param.addLine(Printer::class.simpleName.toString())
        param.addLine(Printer::class.simpleName.toString())
        param.addLine(Printer::class.simpleName.toString())


        try {
            runBlocking {
                launch {
                    subject.printOrThrows(param)
                }
                launch {
                    subject.printOrThrows(param)
                }
            }
            fail("Should fail printing")
        } catch (e: PrinterException) {
            assertEquals(PrinterErrorCode.PRINTER_BUSY, e.code)
        }
    }

    @Test
    @Ignore("Test available only for specific POS assessments")
    fun printWithPrinterOverheating() = runBlocking {
        subject = HALConfig.deviceProvider.getPrinter(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/print-with-printer-overheating.json",
                KEY_CONTEXT to context
            )
        )

        val param = PrinterBuffer()
        param.addLine(Printer::class.simpleName.toString())

        try {
            subject.printOrThrows(param)
            fail("Should fail printing")
        } catch (e: PrinterException) {
            assertEquals(PrinterErrorCode.PRINTER_OVERHEATING, e.code)
        }
    }

    @Test
    @Ignore("Test available only for specific POS assessments")
    fun printWithLowEnergy() = runBlocking {
        subject = HALConfig.deviceProvider.getPrinter(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/print-with-low-energy.json",
                KEY_CONTEXT to context
            )
        )

        val param = PrinterBuffer()
        param.addLine(Printer::class.simpleName.toString())

        try {
            subject.printOrThrows(param)
            fail("Should fail printing")
        } catch (e: PrinterException) {
            assertEquals(PrinterErrorCode.PRINTER_LOW_ENERGY, e.code)
        }
    }

    @Test
    @Ignore("Test available only for specific POS assessments")
    fun printWithInitializingError() = runBlocking {
        subject = HALConfig.deviceProvider.getPrinter(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/print-with-initializing-error.json",
                KEY_CONTEXT to context
            )
        )

        val param = PrinterBuffer()
        param.addLine(Printer::class.simpleName.toString())

        try {
            subject.printOrThrows(param)
            fail("Should fail printing")
        } catch (e: PrinterException) {
            assertEquals(PrinterErrorCode.PRINTER_INIT_ERROR, e.code)
        }
    }

    @Test
    @Ignore("Test available only for specific POS assessments")
    fun printWithPrinterPrintError() = runBlocking {
        subject = HALConfig.deviceProvider.getPrinter(
            mapOf(
                RESULTS_FILE_KEY to "$stubResultsFolder/print-with-printer-print-error.json",
                KEY_CONTEXT to context
            )
        )

        val param = PrinterBuffer()
        param.addLine(Printer::class.simpleName.toString())

        try {
            subject.printOrThrows(param)
            fail("Should fail printing")
        } catch (e: PrinterException) {
            assertEquals(PrinterErrorCode.PRINTER_PRINT_ERROR, e.code)
        }
    }
}