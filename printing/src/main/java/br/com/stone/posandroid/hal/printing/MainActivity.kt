package br.com.stone.posandroid.hal.printing

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import br.com.stone.posandroid.hal.api.Properties
import br.com.stone.posandroid.hal.api.printer.Printer
import br.com.stone.posandroid.hal.api.printer.PrinterBuffer
import br.com.stone.posandroid.hal.api.printer.exception.PrinterException
import br.com.stone.posandroid.hal.api.printer.ext.printOrThrows
import br.com.stone.posandroid.hal.api.provider.AutoProvider
import br.com.stone.posandroid.hal.demo.printing.R
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val printer : Printer by lazy {
        AutoProvider.provider.getPrinter(mapOf(Properties.KEY_CONTEXT to this.applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonPrintAndroid = findViewById<Button>(R.id.button_print_android)
        buttonPrintAndroid.setOnClickListener {
            printAndroid()
        }

        val buttonPrintInvoice = findViewById<Button>(R.id.button_print_invoice)
        buttonPrintInvoice.setOnClickListener {
            printInvoice()
        }
    }

    private fun printAndroid() {
        val bitmapUrl = ""

        GlobalScope.launch(Dispatchers.Default) {

            val invoiceBitmapUrl = URL(bitmapUrl)

            val invoiceBitmap = BitmapFactory.decodeStream(
                invoiceBitmapUrl.openConnection().getInputStream()
            )

            printBitmap(invoiceBitmap)
        }
    }

    private fun printInvoice() {

        val bitmapUrl = "https://tms.stone.com.br/Addons/v1/prints/or_Gv2M665UlrtkYMOY-NFE.png"

        GlobalScope.launch(Dispatchers.Default) {

            val invoiceBitmapUrl = URL(bitmapUrl)

            val invoiceBitmap = BitmapFactory.decodeStream(
                invoiceBitmapUrl.openConnection().getInputStream()
            )

            printBitmap(invoiceBitmap)
        }
    }

    private suspend fun printBitmap(bitmap: Bitmap) {
        val printerBuffer = PrinterBuffer()
        printerBuffer.step = printer.getStepsToCut()

        printerBuffer.addImage(bitmap)

        try {
            printer.printOrThrows(printerBuffer = printerBuffer)
        } catch (printerException: PrinterException) {
            Log.e("Printing", printerException.message.orEmpty())
        } catch (exception: Exception) {
            Log.e("Printing", exception.message.orEmpty())
        }
    }
}