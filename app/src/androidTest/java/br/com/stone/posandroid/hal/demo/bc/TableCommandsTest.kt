package br.com.stone.posandroid.hal.demo.bc

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import br.com.stone.posandroid.hal.api.Properties.RESULTS_FILE_KEY
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode.Companion.PP_OK
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode.Companion.PP_TABEXP
import br.com.stone.posandroid.hal.demo.bc.base.AutoOpenCloseTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random


@RunWith(AndroidJUnit4ClassRunner::class)
class TableCommandsTest : AutoOpenCloseTest() {

    private val stubResultsFolder = "resources/table-commands-test"

    @Test
    fun validateTableLoadProcess() {
        val loadInitInput = "080123456789"
        val tableLoadInitResult = pinpad.tableLoadInit(loadInitInput)
        assertTrue(tableLoadInitResult == PP_OK || tableLoadInitResult == PP_TABEXP)

        val tableRecords = arrayOf(
            "012841080110A0000000041010D0761300000000000001CREDITO         030002000200000769862020000080750001000000000000E0F0E8F000F0A00122F850ACF8000400000000FC50ACA00000000000000000000000000000050000000000000F9F02065F2A029A039C0195059F370400000000039F370400000000000000000000000000000000Y1Z1Y3Z3",
            "0161120815A00000002510001030000248CF98DFEDB3D3727965EE7797723355E0751C81D2D3DF4D18EBAB9FB9D49F38C8C4A826B99DC9DEA3F01043D4BF22AC3550E2962A59639B1332156422F788B9C16D40135EFD1BA94147750575E636B6EBC618734C91C1D1BF3EDC2A46A43901668E0FFC136774080E888044F6A1E65DC9AAA8928DACBEB0DB55EA3514686C6A732CEF55EE27CF877F110652694A0E3484C855D882AE191674E25C296205BBB599455176FDD7BBC549F27BA5FE35336F7E29E68D783973199436633C67EE5A680F05160ED12D1665EC83D1997F10FD05BBDBF9433E8F797AEE3E9F02A34228ACE927ABE62B8B9281AD08D3DF5C7379685045D7BA5FCDE586371C729CF2FD262394ABC4CC173506502446AA9B9FD000000000000000000000000000000000000000000"
        )

        tableRecords.forEach {
            assertEquals(PP_OK, pinpad.tableLoadRec(it))
        }

        assertEquals(PP_OK, pinpad.tableLoadEnd())
    }

    @Test
    fun validateTableExpired() {

        pinpad.runtimeProperties[RESULTS_FILE_KEY] =
            "$stubResultsFolder/validate_table_expired.json"

        val loadInitInput = Random(0L).nextLong().toString()

        assertEquals(PP_TABEXP, pinpad.tableLoadInit(loadInitInput))
    }

    @Test
    fun validateGetTimestamp() {
        val input = "00"
        val pinpadResult = pinpad.getTimeStamp(input)

        assertEquals(PP_OK, pinpadResult.resultCode)
        assertTrue(pinpadResult.output.isNotBlank())
    }
}