package br.com.stone.posandroid.hal.demo.printer

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.printer.Printer
import br.com.stone.posandroid.hal.api.printer.PrinterBuffer
import br.com.stone.posandroid.hal.api.printer.PrinterErrorCode
import br.com.stone.posandroid.hal.demo.HALConfig
import br.com.stone.posandroid.hal.demo.rule.Precondition
import br.com.stone.posandroid.hal.demo.rule.PreconditionTestRule
import br.com.stone.posandroid.hal.demo.util.blockingPrinterAssertions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class PrinterTest {

    private val stubResultsFolder = "printer/printer-test"
    private lateinit var subject: Printer

    @get:Rule
    val preconditionsTestRule = PreconditionTestRule()

    @Test
    @Precondition("Printer must have paper")
    fun printOk() {
        subject = HALConfig.deviceProvider.getPrinter(
            mapOf(RESULTS_FILE_KEY to "$stubResultsFolder/print-ok.json")
        )

        val param = PrinterBuffer()
        param.addLine(Printer::class.simpleName.toString())

        blockingPrinterAssertions(
            successAssertions = {
                assertTrue(true)
            },
            errorAssertions = {
                fail()
            },
            functionAssertions = {
                subject.print(param, it)
            }
        )
    }

    @Test
    fun printWithoutContent() {

        subject = HALConfig.deviceProvider.getPrinter(
            mapOf(RESULTS_FILE_KEY to "$stubResultsFolder/print-ok.json")
        )

        blockingPrinterAssertions(
            successAssertions = {
                fail("No Content to Print, but callback was success")
            },
            errorAssertions = {
                fail("No Content to Print, but callback was error $it")
            },
            functionAssertions = {
                subject.print(PrinterBuffer(), it)
            }
        )
    }

    @Test
    @Precondition("Printer must have no paper")
    fun printNoPaper() {
        subject = HALConfig.deviceProvider.getPrinter(
            mapOf(RESULTS_FILE_KEY to "$stubResultsFolder/print-no-paper.json")
        )

        val param = PrinterBuffer()
        param.addLine(Printer::class.simpleName.toString())

        blockingPrinterAssertions(
            successAssertions = {
                fail()
            },
            errorAssertions = {
                assertEquals(PrinterErrorCode.PRINTER_OUT_OF_PAPER, it)
            },
            functionAssertions = {
                subject.print(PrinterBuffer(), it)
            }
        )
    }
}