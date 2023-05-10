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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val printer: Printer by lazy {
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

        val buttonPrintLargeInvoice = findViewById<Button>(R.id.button_print_large_invoice)
        buttonPrintLargeInvoice.setOnClickListener {
            printLargeInvoice()
        }
    }

    private fun printAndroid() {
        GlobalScope.launch(Dispatchers.Default) {
            val androidBitmap = BitmapFactory.decodeResource(resources, R.raw.android)
            printBitmap(androidBitmap)
        }
    }

    private fun printInvoice() {
        GlobalScope.launch(Dispatchers.Default) {
            val invoiceBitmap = BitmapFactory.decodeResource(resources, R.raw.invoice)
            printBitmap(invoiceBitmap)
        }
    }

    private fun printLargeInvoice() {
        GlobalScope.launch(Dispatchers.Default) {
            val invoiceLargeBitmap = BitmapFactory.decodeResource(resources, R.raw.invoice_large)
            printBitmap(invoiceLargeBitmap)
        }
    }

    private suspend fun printBitmap(bitmap: Bitmap) {

        val steps = printer.getStepsToCut()

        val printerBuffer = PrinterBuffer()
        printerBuffer.step = steps
        Log.d("Printing", "bitmap steps=$steps, bw=${bitmap.width}, bh=${bitmap.height}")

        val resizedBitmap = getResizedBitmap(image = bitmap)
        Log.d(
            "Printing",
            "bitmap(resized)steps=$steps, bw=${resizedBitmap.width}, bh=${resizedBitmap.height}"
        )

        printerBuffer.addImage(resizedBitmap)

        try {
            printer.printOrThrows(printerBuffer = printerBuffer)
        } catch (printerException: PrinterException) {
            Log.e("Printing", printerException.message.orEmpty())
        } catch (exception: Exception) {
            Log.e("Printing", exception.message.orEmpty())
        }
    }

    private fun getResizedBitmap(image: Bitmap): Bitmap {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        width = WIDTH_DEFAULT
        height = (width / bitmapRatio).toInt()
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    companion object{
        private const val WIDTH_DEFAULT = 384
    }

}