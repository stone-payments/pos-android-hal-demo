package br.com.stone.posandroid.hal.demo.bc.base

import br.com.stone.posandroid.hal.api.bc.PinpadResult
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode
import br.com.stone.posandroid.hal.api.bc.constants.ResultCode.Companion.PP_TABEXP
import br.com.stone.posandroid.hal.mock.bc.PinpadStub
import org.junit.Before
import java.util.ArrayDeque


abstract class AutoLoadTableTest : AutoOpenCloseTest() {

    @Before
    override fun setup() {
        val queue =
            ArrayDeque(
                listOf(
                    PinpadStub.Companion.CombinedResult(PinpadResult(PinpadResult.OPN, ResultCode.PP_OK)),
                    PinpadStub.Companion.CombinedResult(PinpadResult(PinpadResult.TLI, ResultCode.PP_OK)),
                    PinpadStub.Companion.CombinedResult(PinpadResult(PinpadResult.CLO, ResultCode.PP_OK))
                )
            )
        initializePinpad(queue)
        loadTableIfNeeded()
    }

    private fun loadTableIfNeeded() {

        if (pinpad.tableLoadInit("$ACQUIRER_ID$TABLE_STUB_TIMESTAMP") == PP_TABEXP) {

            TABLE_STUB_RECORDS.forEach {
                pinpad.tableLoadRec(it)
            }

            pinpad.tableLoadEnd()
        }
    }

    companion object {
        const val ACQUIRER_ID = "08"
        const val TABLE_STUB_TIMESTAMP = "3615208604"

        //        val TABLE_STUB_TIMESTAMP = UUID.randomUUID().toString().substring(0, 10) //force load table
        val TABLE_STUB_RECORDS = arrayOf(
            "013401080110A0000000041010D0761300000000000001CREDITO         0300020002000007698620              00000       E0F8E8F000F0A00122F850ACF8000400000000FC50ACA0000000000001405f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F850ACF8000400000000FC50ACA000E060C81F000F0A00105f5e0ff1",
            "013401080210A0000000041010D0761200000000000002DEBITO          0300020002000007698620              00000       E0F0E8F000F0F00122F850ACF8000000800000FC50ACA0000000000001405f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F850ACF8000000800000FC50ACA000E060C81F000F0F00105f5e0ff1",
            "013401080307A000000004101000000000000000000001CREDITO         0300020002000007698620              00000       E0F8E8F000F0A00122F850ACF8000400000000FC50ACA0000000000001405f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F850ACF8000400000000FC50ACA000E060C81F000F0A00105f5e0ff1",
            "013401080407A000000004306000000000000000000002DEBITO          0300020002000007698620              00000       E0F0E8F000F0F00122F850ACF8000000800000FC50ACA0000000000001405f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F850ACF8000000800000FC50ACA000E040C81F000F0F00105f5e0ff1",
            "013401080507A000000003101000000000000000000001CREDITO         03008C0002000007698620              00000       E0F8C87000F0F00122D84004F8000010000000D84000A8000000000001205f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3D84004F8000010000000D84000A800E0F8C817000F0F00105f5e0ff1",
            "013401080607A000000003201000000000000000000002DEBITO          03008C0002000007698620              00000       E0F8C87000F0F00122D84004F8000010000000D84000A8000000000001205f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3D84004F8000010000000D84000A800E0F8C817000F0F00105f5e0ff1",
            "013401080707A00000038800B000000000000000000003TK RESTAURANTE  0300010002000007698620              00000       E0F0E87000F0F00122CC58BC80000000000000CC78FCF8000000000001005f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3CC58BC80000000000000CC78FCF800E0F0E817000F0F00105f5e0ff1",
            "013401080807A00000038800B100000000000000000003TK ALIMENTACAO  0300010002000007698620              00000       E0F0E87000F0F00122CC58BC80000000000000CC78FCF8000000000001005f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3CC58BC80000000000000CC78FCF800E0F0E817000F0F00105f5e0ff1",
            "013401080907A00000038800B300000000000000000003TK CULTURA      0300010002000007698620              00000       E0F0E87000F0F00122CC58BC80000000000000CC78FCF8000000000001005f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3CC58BC80000000000000CC78FCF800E0F0E817000F0F00105f5e0ff1",
            "013401081007A00000038800B500000000000000000003TK FLEX         0300010002000007698620              00000       E0F0E87000F0F00122CC58BC80000000000000CC78FCF8000000000001005f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3CC58BC80000000000000CC78FCF800E0F0E817000F0F00105f5e0ff1",
            "013401081107A000000537202000000000000000000003SODEXO REFEICAO 0300010002000007698620              00000       E0F0C86000F0F00122F87088E8001010000000F8F888F8000000000001405f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F87088E8001010000000F8F888F800E0F0C816000F0F00105f5e0ff1",
            "013401081207A000000537201000000000000000000003SODEXO ALIMENT  0300010002000007698620              00000       E0F0E86000F0F00122F87088E8001010000000F8F888F8000000000001405f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F87088E8001010000000F8F888F800E0F0E816000F0F00105f5e0ff1",
            "013401081307A000000537205000000000000000000003SODEXO COMBUST  0300010002000007698620              00000       E0F0E86000F0F00122F87088E8001010000000F8F888F8000000000001405f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F87088E8001010000000F8F888F800E0F0E816000F0F00105f5e0ff1",
            "013401081407A000000537204000000000000000000003SODEXO PREMIUM  0300010002000007698620              00000       E0F0E86000F0F00122F87088E8001010000000F8F888F8000000000001405f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F87088E8001010000000F8F888F800E0F0E816000F0F00105f5e0ff1",
            "013401081507A000000537203000000000000000000003SODEXO GIFT     0300010002000007698620              00000       E0F0E86000F0F00122F87088E8001010000000F8F888F8000000000001405f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F87088E8001010000000F8F888F800E0F0E816000F0F00105f5e0ff1",
            "013401081607A000000537206000000000000000000003SODEXO CULTURA  0300010002000007698620              00000       E0F0E86000F0F00122F87088E8001010000000F8F888F8000000000001405f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F87088E8001010000000F8F888F800E0F0E816000F0F00105f5e0ff1",
            "013401081707A000000557603100000000000000000003VR REFEICAO     0300010002000007698620              00000       E0F0E87000F0F00122CC58BC80000000000000CC78FCF8000000000001005f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3CC58BC80000000000000CC78FCF800E0F0E817000F0F00105f5e0ff1",
            "013401081807A000000557602700000000000000000003VR ALIMENTACAO  0300010002000007698620              00000       E0F0E87000F0F00122CC58BC80000000000000CC78FCF8000000000001005f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3CC58BC80000000000000CC78FCF800E0F0E817000F0F00105f5e0ff1",
            "013401081907A000000557602800000000000000000003VR AUTO         0300010002000007698620              00000       E0F0E87000F0F00122CC58BC80000000000000CC78FCF8000000000001005f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3CC58BC80000000000000CC78FCF800E0F0E817000F0F00105f5e0ff1",
            "013401082007A000000557603000000000000000000003VR CULTURA      0300010002000007698620              00000       E0F0E87000F0F00122CC58BC80000000000000CC78FCF8000000000001005f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3CC58BC80000000000000CC78FCF800E0F0E817000F0F00105f5e0ff1",
            "013401082107A000000557603400000000000000000003VR BENEFICIOS   0300010002000007698620              00000       E0F0E87000F0F00122CC58BC80000000000000CC78FCF8000000000001005f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3CC58BC80000000000000CC78FCF800E0F0E817000F0F00105f5e0ff1",
            "013401082207A000000494101000000000000000000001ELO CREDITO     0300020002000007698620              00000       E0D8C87000F0F00122FC408480000010000000FC608490000000000001805f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3FC408480000010000000FC60849000E048C817000F0F00105f5e0ff1",
            "013401082307A000000494201000000000000000000002ELO DEBITO      0300020002000007698620              00000       E0D8C87000F0F00122FC408480000010000000FC608490000000000101805f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3FC408480000010000000FC60849000E048C817000F0F00105f5e0ff1",
            "013401082409A000000003050760100000000000000001ELO CREDITO     0300020002000007698620              00000       E0D8C87000F0F00122FC408480000010000000FC608490000000000001805f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3FC408480000010000000FC60849000E048C817000F0F00105f5e0ff1",
            "013401082509A000000003050760200000000000000002ELO DEBITO      0300020002000007698620              00000       E0D8C87000F0F00122FC408480000010000000FC608490000000000001805f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3FC408480000010000000FC60849000E048C817000F0F00105f5e0ff1",
            "013401082609A000000152301010100000000000000001ELO CREDITO     0300020002000007698620              00000       E0D8C87000F0F00122FC408480000010000000FC608490000000000001805f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3FC408480000010000000FC60849000E048C817000F0F00105f5e0ff1",
            "013401082709A000000152301020100000000000000002ELO DEBITO      0300020002000007698620              00000       E0D8C87000F0F00122FC408480000010000000FC608490000000000001805f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3FC408480000010000000FC60849000E048C817000F0F00105f5e0ff1",
            "013401082807A000000494301000000000000000000002ELO MOEDEIRO    0300020002000007698620              00000       E0D8C87000F0F00122FC408480000010000000FC608490000000000001805f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3FC408480000010000000FC60849000E048C817000F0F00105f5e0ff1",
            "013401082907A000000494401000000000000000000001ELO PRE-PAGO    0300020002000007698620              00000       E0D8C87000F0F00122FC408480000010000000FC608490000000000001805f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3FC408480000010000000FC60849000E048C817000F0F00105f5e0ff1",
            "013401083007A000000494501000000000000000000003ALELO REFEICAO  0300020002000007698620              00000       E0F0E87000F0F00122FC408480000010000000FC608490000000000001005f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3FC408480000010000000FC60849000E0F0E817000F0F00105f5e0ff1",
            "013401083107A000000494502000000000000000000003ALELO ALIMENT   0300020002000007698620              00000       E0F0E87000F0F00122FC408480000010000000FC608490000000000001005f5e0ff0000000000002710000009F02065F2A029A039C0195059F370400000000009F37040000000000000000000000000000000000Y1Z1Y3Z3FC408480000010000000FC60849000E0F0E817000F0F00105f5e0ff1",
            "013401083206A000000025010000000000000000000001AMEX CREDIT     0300010002000007698620              00000       E0F8E87000F0F00122DC50FC98000010000000DE00FC98000000000001605f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3DC50FC98000010000000DE00FC9800E0F8E817000F0F00105f5e0ff1",
            "013401083307A000000664101000000000000000000001VERDECARD CREDIT0300010002000007698620              00000       E0F0E87000F0F001221068D4F88020102800001068D4F8800000000001005f5e0ff00000000000027100000000000000000000000000000000000000000000000000000000000000000000000000000000000000Y1Z1Y3Z31068D4F88020102800001068D4F880E0F0E817000F0F00105f5e0ff1",
            "013401083407A000000651000200000000000000000003VALECARD REFEICA0300010002000007698620              00000       E0F0E87000F0F00122CE70BC80002010280000CE70BC80000000000001005f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3CE70BC80002010280000CE70BC8000E0F0E817000F0F00105f5e0ff1",
            "013401083507A000000555E54000000000000000000003VEROCARD REFEICA0300010002000007698620              00000       E0F0E87000F0F00122F87088E8000000000000F8F8FCE8000000000001005f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F87088E8000000000000F8F8FCE800E0F0E817000F0F00105f5e0ff1",
            "013401083607A000000555E53000000000000000000003VEROCARD ALIMENT0300010002000007698620              00000       E0F0E87000F0F00122F87088E8000000000000F8F8FCE8000000000001005f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F87088E8000000000000F8F8FCE800E0F0E817000F0F00105f5e0ff1",
            "013401083707A000000555E56000000000000000000003VEROCARD BENEFIC0300010002000007698620              00000       E0F0E87000F0F00122F87088E8000000000000F8F8FCE8000000000001005f5e0ff0000000000002710000000000000000000000000000000000000000000000039F370400000000000000000000000000000000Y1Z1Y3Z3F87088E8000000000000F8F8FCE800E0F0E817000F0F00105f5e0ff1",
            "013401083807A000000555E55300000000000000000003VEROCARD VOUCHER0300010002000007698620              00000       E0F0E87000F0F00122F87088E8000000000000F8F8FCE8000000000001005f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F87088E8000000000000F8F8FCE800E0F0E817000F0F00105f5e0ff1",
            "013401083907A000000555E44000000000000000000003GREENCARD REFEIC0300010002000007698620              00000       E0F0E87000F0F00122F87088E8000000000000F8F8FCE8000000000001005f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F87088E8000000000000F8F8FCE800E0F0E817000F0F00105f5e0ff1",
            "013401084007A000000555E43000000000000000000003GREENCARD ALIMEN0300010002000007698620              00000       E0F0E87000F0F00122F87088E8000000000000F8F8FCE8000000000001005f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F87088E8000000000000F8F8FCE800E0F0E817000F0F00105f5e0ff1",
            "013401084107A000000555E45000000000000000000003GREENCARD CULTUR0300010002000007698620              00000       E0F0E87000F0F00122F87088E8000000000000F8F8FCE8000000000001005f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F87088E8000000000000F8F8FCE800E0F0E817000F0F00105f5e0ff1",
            "013401084207A000000555E45000000000000000000003GREENCARD COMBUS0300010002000007698620              00000       E0F0E87000F0F00122F87088E8000000000000F8F8FCE8000000000001005f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F87088E8000000000000F8F8FCE800E0F0E817000F0F00105f5e0ff1",
            "013401084307A000000555E45000000000000000000003GREENCARD BENEFI0300010002000007698620              00000       E0F0E87000F0F00122F87088E8000000000000F8F8FCE8000000000001005f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F87088E8000000000000F8F8FCE800E0F0E817000F0F00105f5e0ff1",
            "013401084407A000000459201000000000000000000002SOROCRED DEBITO 0300010002000007698620              00000       E0F0E87000F0F00122F87088E8000000000000F8F8FCE8000000000001005f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F87088E8000000000000F8F8FCE800E0F0E817000F0F00105f5e0ff1",
            "013401084507A000000459101000000000000000000001SOROCRED CREDITO0300010002000007698620              00000       E0F0E87000F0F00122F87088E8000000000000F8F8FCE8000000000001005f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F87088E8000000000000F8F8FCE800E0F0E817000F0F00105f5e0ff1",
            "013401084607A000000459301000000000000000000003SOROCRED VOUCHER0300010002000007698620              00000       E0F0E87000F0F00122F87088E8000000000000F8F8FCE8000000000001005f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3F87088E8000000000000F8F8FCE800E0F0E817000F0F00105f5e0ff1",
            "013401084707A000000589400100000000000000000001UPBrasil Credito0300010002000007698620              00000       E0F0E87000F0F00122DCF8FCF8F02000000000DCF8FCF8F00000000001005f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3DCF8FCF8F02000000000DCF8FCF8F0E0F0E817000F0F00105f5e0ff1",
            "013401084807A000000589300100000000000000000003UPBrasil Frete  0300010002000007698620              00000       E0F0E87000F0F00122DCF8FCF8F02000000000DCF8FCF8F00000000001005f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3DCF8FCF8F02000000000DCF8FCF8F0E0F0E817000F0F00105f5e0ff1",
            "013401084907A000000589500100000000000000000003UPBrasil Frota  0300010002000007698620              00000       E0F0E87000F0F00122DCF8FCF8F02000000000DCF8FCF8F00000000001005f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3DCF8FCF8F02000000000DCF8FCF8F0E0F0E817000F0F00105f5e0ff1",
            "013401085007A000000589100100000000000000000003UPBrasil Voucher0300010002000007698620              00000       E0F0E87000F0F00122DCF8FCF8F02000000000DCF8FCF8F00000000001005f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3DCF8FCF8F02000000000DCF8FCF8F0E0F0E817000F0F00105f5e0ff1",
            "013401085107A000000589200100000000000000000003UPBrasil Voucher0300010002000007698620              00000       E0F0E87000F0F00122DCF8FCF8F02000000000DCF8FCF8F00000000001005f5e0ff00000000000027100000000000000000000000000000000000000000000009F37040000000000000000000000000000000000Y1Z1Y3Z3DCF8FCF8F02000000000DCF8FCF8F0E0F0E817000F0F00105f5e0ff1",
            "0161120801A00000000301001030000128C696034213D7D8546984579D1D0F0EA519CFF8DEFFC429354CF3A871A6F7183F1228DA5C7470C055387100CB935A712C4E2864DF5D64BA93FE7E63E71F25B1E5F5298575EBE1C63AA617706917911DC2A75AC28B251C7EF40F2365912490B939BCA2124A30A28F54402C34AECA331AB67E1E79B285DD5771B5D9FF79EA630B750000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001D34A6A776011C7E7CE3AEC5F03AD2F8CFC5503CC000000000000000000000000000000000000000000",
            "0161120802A00000000307001030000144A89F25A56FA6DA258C8CA8B40427D927B4A1EB4D7EA326BBB12F97DED70AE5E4480FC9C5E8A972177110A1CC318D06D2F8F5C4844AC5FA79A4DC470BB11ED635699C17081B90F1B984F12E92C1C529276D8AF8EC7F28492097D8CD5BECEA16FE4088F6CFAB4A1B42328A1B996F9278B0B7E3311CA5EF856C2F888474B83612A82E4E00D0CD4069A6783140433D50725F00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001B4BC56CC4E88324932CBC643D6898F6FE593B172000000000000000000000000000000000000000000",
            "0161120803A00000000308001030000176D9FD6ED75D51D0E30664BD157023EAA1FFA871E4DA65672B863D255E81E137A51DE4F72BCC9E44ACE12127F87E263D3AF9DD9CF35CA4A7B01E907000BA85D24954C2FCA3074825DDD4C0C8F186CB020F683E02F2DEAD3969133F06F7845166ACEB57CA0FC2603445469811D293BFEFBAFAB57631B3DD91E796BF850A25012F1AE38F05AA5C4D6D03B1DC2E568612785938BBC9B3CD3A910C1DA55A5A9218ACE0F7A21287752682F15832A678D6E1ED0B000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000120D213126955DE205ADC2FD2822BD22DE21CF9A8000000000000000000000000000000000000000000",
            "0161120804A000000003090010300002489D912248DE0A4E39C1A7DDE3F6D2588992C1A4095AFBD1824D1BA74847F2BC4926D2EFD904B4B54954CD189A54C5D1179654F8F9B0D2AB5F0357EB642FEDA95D3912C6576945FAB897E7062CAA44A4AA06B8FE6E3DBA18AF6AE3738E30429EE9BE03427C9D64F695FA8CAB4BFE376853EA34AD1D76BFCAD15908C077FFE6DC5521ECEF5D278A96E26F57359FFAEDA19434B937F1AD999DC5C41EB11935B44C18100E857F431A4A5A6BB65114F174C2D7B59FDF237D6BB1DD0916E644D709DED56481477C75D95CDD68254615F7740EC07F330AC5D67BCD75BF23D28A140826C026DBDE971A37CD3EF9B8DF644AC385010501EFC6509D7A4111FF80A40173F52D7D27E0F26A146A1C8CCB29046000000000000000000000000000000000000000000",
            "0161120805A00000000403001030000128C2490747FE17EB0584C88D47B1602704150ADC88C5B998BD59CE043EDEBF0FFEE3093AC7956AD3B6AD4554C6DE19A178D6DA295BE15D5220645E3C8131666FA4BE5B84FE131EA44B039307638B9E74A8C42564F892A64DF1CB15712B736E3374F1BBB6819371602D8970E97B900793C7C2A89A4A1649A59BE680574DD0B6014500000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000015ADDF21D09278661141179CBEFF272EA384B13BB000000000000000000000000000000000000000000",
            "0161120806A00000000404001030000144A6DA428387A502D7DDFB7A74D3F412BE762627197B25435B7A81716A700157DDD06F7CC99D6CA28C2470527E2C03616B9C59217357C2674F583B3BA5C7DCF2838692D023E3562420B4615C439CA97C44DC9A249CFCE7B3BFB22F68228C3AF13329AA4A613CF8DD853502373D62E49AB256D2BC17120E54AEDCED6D96A4287ACC5C04677D4A5A320DB8BEE2F775E5FEC500000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001381A035DA58B482EE2AF75F4C3F2CA469BA4AA6C000000000000000000000000000000000000000000",
            "0161120807A00000000405001030000176B8048ABC30C90D976336543E3FD7091C8FE4800DF820ED55E7E94813ED00555B573FECA3D84AF6131A651D66CFF4284FB13B635EDD0EE40176D8BF04B7FD1C7BACF9AC7327DFAA8AA72D10DB3B8E70B2DDD811CB4196525EA386ACC33C0D9D4575916469C4E4F53E8E1C912CC618CB22DDE7C3568E90022E6BBA770202E4522A2DD623D180E215BD1D1507FE3DC90CA310D27B3EFCCD8F83DE3052CAD1E48938C68D095AAC91B5F37E28BB49EC7ED5970000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001EBFA0D5D06D8CE702DA3EAE890701D45E274C845000000000000000000000000000000000000000000",
            "0161120808A00000000406001030000248CB26FC830B43785B2BCE37C81ED334622F9622F4C89AAE641046B2353433883F307FB7C974162DA72F7A4EC75D9D657336865B8D3023D3D645667625C9A07A6B7A137CF0C64198AE38FC238006FB2603F41F4F3BB9DA1347270F2F5D8C606E420958C5F7D50A71DE30142F70DE468889B5E3A08695B938A50FC980393A9CBCE44AD2D64F630BB33AD3F5F5FD495D31F37818C1D94071342E07F1BEC2194F6035BA5DED3936500EB82DFDA6E8AFB655B1EF3D0D7EBF86B66DD9F29F6B1D324FE8B26CE38AB2013DD13F611E7A594D675C4432350EA244CC34F3873CBA06592987A1D7E852ADC22EF5A2EE28132031E48F74037E3B34AB747F1F910A1504D5FFB793D94F3B500765E1ABCAD72D9000000000000000000000000000000000000000000",
            "0161120809A000000388020010300002488C1CB1A139B95087455684F9AE2AA6FBA31BA504122DB7C194E8BE0B9AB9A9025DBB8FAFEA1AF9EE8A21CB66296611536863D43520407A57F01216CDB313679B47522821FF783C5B0BE31857EBFBA11A737940E3008D521EA019E17B559C93E21224B0B8651E7B6FD622BCC47FE62266261528CF3F130AF948B5C9087D6C6F28EEAAA73F0013EC1394C582A3F540F697443BA1E2F3D03378CB0C9122F7786821C76FF2D6FBFC827164CF609F0B26151F04C6B6A0675B3113225BD4FFB61FE230D2BBB2C335F5BDFCE30ECAA012AF180799FBC62BF8E02A17ABBB6BED510496529000ACF34EEB0F09AF438A9E7EE22FF16746DB2AD04CA7D11B9834C19CB6F71A8D234E47AB09BDB4E6A180E43000000000000000000000000000000000000000000",
            "0161120810A00000055701001030000176A7FEF3C1B8D3FA9FC12BFF691D855820C0C7700694843D692305D5700BD39894FC5977AB5BC60853FB6F840469C8734CE87BB4D458E51C8930AE3D910B7F6BB82AAE072BC0A6ECEA138BC128AE555860E17595B70C0DB4720B2929DBC51F75691A54922E768991E71044D045B2C03252B24B6613683B0CC6B35802FFAFE3D91E658A6DE46BED1D7BEF7ADE31F409C5FCC9F60FB456583E472EDBED8F6826E49F9A80D3BAFA40F053DDBCB43B6864AEF5000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000170FEABD4D15533FC478E981F71F1501C3B4FBA92000000000000000000000000000000000000000000",
            "0161120811A00000049401001030000144AAE2752517E82EFD0041A8CA6DA1C993D0961152E4F92E61448492D881D3AF055DF03CB87A4E9B72AF4D29A3FA2403E7D00BDC93CDD5746AD82BC7C4A7C9C1827578D3E391A6BEB5CA36A668BD20DB29360D479FC75CC84FDC78FE9A4C6BF754A26AB0C3153468DC87C8C244F54CC0650214835825BBE7B96985891DFA0804865C9ED562384DED56280B3B8E8176857B000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000017C3F72E081D76E5B9244843B3D012BD9D1840526000000000000000000000000000000000000000000",
            "0161120812A00000049402001030000176BE300EA002D9720E759152B2E843D0FB1F3BA2535B1CA3AA29131571D56AB4676ECEA552C31EA85257B4B6B146D6F9505311EA14B88F4034669A3E674B7C9D2E6D47C62CA65EB7709D287AF9E2E20AB5D0CBD73C56A55990EABFD871D79A3C20BD710C0FB005FABAE6503D5DB19B6747CFDEFDF739E4C217F1959DB97FD8501C7A5BDB71D8FC506BDBABB83ACF33F44F8D8B9CD72F50BE0FBB1B141927BF4C0618F93EC1A48B2583ADF1B01EAD23FD3900000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000016D5F471BC24E3C8E94A2FACB98682ADA18C670CA000000000000000000000000000000000000000000",
            "0161120813A00000049403001030000248DAF1A9708C22EBACE93B1067D0DDE5AF1948DA0D9785D7AB7A04B0FBA4089B96A2616C3A7AB3D9A97032EC12218B70576E54506B922DD373E37A47BD5FB72B9D10C5DD450DB6C92454672F20AAA1C8F0AC7B9ABC71D8A1E9D0DB7B4EDD7C01AD3479EAD0A2389B86B95B186A54F6CE1E463701FE675A2CB9639A03FA271812A2552C886D0A7CD9B3D8269AD63E61C98407E914C7FA5B85E952C1F9C3BDF6E1841F3015FD8D76D0463631C8B058D1A765AD6E25210F6BD8ACB70F5A3A262DEC513A07903A86704B6F44F4E28C850AC8F1DB0E29BF57FB68729E67FAAA453B46C484E5490B027965BC0DE6B538F8B0E261BEFD1BA89C06AB5112263AC2014951210F3CCEA263966ACDE1816D327000000000000000000000000000000000000000000",
            "0161120814A0000000250F001030000176C8D5AC27A5E1FB89978C7C6479AF993AB3800EB243996FBB2AE26B67B23AC482C4B746005A51AFA7D2D83E894F591A2357B30F85B85627FF15DA12290F70F05766552BA11AD34B7109FA49DE29DCB0109670875A17EA95549E92347B948AA1F045756DE56B707E3863E59A6CBE99C1272EF65FB66CBB4CFF070F36029DD76218B21242645B51CA752AF37E70BE1A84FF31079DC0048E928883EC4FADD497A719385C2BBBEBC5A66AA5E5655D18034EC50000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001A73472B3AB557493A9BC2179CC8014053B12BAB4000000000000000000000000000000000000000000",
            "0161120815A00000002510001030000248CF98DFEDB3D3727965EE7797723355E0751C81D2D3DF4D18EBAB9FB9D49F38C8C4A826B99DC9DEA3F01043D4BF22AC3550E2962A59639B1332156422F788B9C16D40135EFD1BA94147750575E636B6EBC618734C91C1D1BF3EDC2A46A43901668E0FFC136774080E888044F6A1E65DC9AAA8928DACBEB0DB55EA3514686C6A732CEF55EE27CF877F110652694A0E3484C855D882AE191674E25C296205BBB599455176FDD7BBC549F27BA5FE35336F7E29E68D783973199436633C67EE5A680F05160ED12D1665EC83D1997F10FD05BBDBF9433E8F797AEE3E9F02A34228ACE927ABE62B8B9281AD08D3DF5C7379685045D7BA5FCDE586371C729CF2FD262394ABC4CC173506502446AA9B9FD000000000000000000000000000000000000000000",
            "0161120816A00000055510003010001176C2C70441B08547562C3E311D6DAF742B160F4730BA5FBD304EA4FC6F03432758A16D97F96770690D0688AADB50A6AEF5B8372A91A9E7DC13B4AF1980C865EC879B5B4D8979EE48EA62A629D5E32586915BF174C43871ECD9CFD46EDCDA52BCF94E1B62012219492E0D817D9E39878A6F43DE54ED53E58BC900A4A0D14C419E517F1F0BC267E71C428AD73F9776283A6E31669167D079786CF13274E3F85D866F1885B53375A8D49B2ED96EB271FDB2790000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001BA9FBF1E563C25B858E4B77621BD3F1AC600A2AC000000000000000000000000000000000000000000",
            "0161120817A00000055511003010001248C2F56D084C2C28D96545B3FF747A6A1F1DFF5C0B1D87E4824B876F1B55178D58ED1DD00D22F7324761ADBA006E749F693B9B669D0F8F5E35E1DED8DA0689AA2C29A3E5734EACC7A05240CF668B494DF015D52F2D77065CAA593A71A421D6925DBCD688121C71E10A0E683C9385640B696B0169B7A9D593FEB32170E9B57CB8316F20E96AB674E64FC2B0575546E30C1184D9C2F43260DB8BEF2619B37301A90D106DFA562BA031B5A7370807C0A396A8D136C47BF3C83229F913E71AC7AFB2BBF4527D3C716AE9277CA51DD8E1D56CBDB075CF064C751698CF129791A00FB74E310827D862DE90E8DE4EC8E247325DD3EC7D28299F767C5B125408ABA7E43064F369731C4B8BBC1D4DDD89B39000000000000000000000000000000000000000000",
            "0161120818A00000055510003010001176C2C70441B08547562C3E311D6DAF742B160F4730BA5FBD304EA4FC6F03432758A16D97F96770690D0688AADB50A6AEF5B8372A91A9E7DC13B4AF1980C865EC879B5B4D8979EE48EA62A629D5E32586915BF174C43871ECD9CFD46EDCDA52BCF94E1B62012219492E0D817D9E39878A6F43DE54ED53E58BC900A4A0D14C419E517F1F0BC267E71C428AD73F9776283A6E31669167D079786CF13274E3F85D866F1885B53375A8D49B2ED96EB271FDB2790000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001BA9FBF1E563C25B858E4B77621BD3F1AC600A2AC000000000000000000000000000000000000000000",
            "0161120819A00000055511003010001248C2F56D084C2C28D96545B3FF747A6A1F1DFF5C0B1D87E4824B876F1B55178D58ED1DD00D22F7324761ADBA006E749F693B9B669D0F8F5E35E1DED8DA0689AA2C29A3E5734EACC7A05240CF668B494DF015D52F2D77065CAA593A71A421D6925DBCD688121C71E10A0E683C9385640B696B0169B7A9D593FEB32170E9B57CB8316F20E96AB674E64FC2B0575546E30C1184D9C2F43260DB8BEF2619B37301A90D106DFA562BA031B5A7370807C0A396A8D136C47BF3C83229F913E71AC7AFB2BBF4527D3C716AE9277CA51DD8E1D56CBDB075CF064C751698CF129791A00FB74E310827D862DE90E8DE4EC8E247325DD3EC7D28299F767C5B125408ABA7E43064F369731C4B8BBC1D4DDD89B39000000000000000000000000000000000000000000",
            "0161120820A00000065101001030000248B0F20A1EB23CAF2FCC53E5B5D05BCE2A9195781B9F225876E8CA237A33335AFC864384EFB152946F6164970CE6C77D93C1C0A58E1FDFBF869CE099FA7492C665205E0121761626C7C8B162BBE564D317712531B9D6F77C1694F1CB4B14C9FA95BDC10D250FD8FCABAB7B8115BD2621AD1B4CEB017F8A142C06F198DC18F3AAD95A19EA9DE9BEC22678F7E303A8D979F545982B12BEF99711429ABCD0A958000132C692DDD1551349558DF67B9CCB002B52EC57A90C01EFBE3A28C8C9FCE61C7087AFF59D11051A3410ED4DF66763564A340339162916ED0296E1C02D4F323F11EAC6CF63CBAD9677D1A304724167C614663988C4D9F79DBF148BE2B1445A042BC63D0FB994F4BB564087C54E2000000000000000000000000000000000000000000",
            "0161120821A00000045914003010010175E5F5BCE3C76E567E5DC9375BC7250D1CD4E0C30070F5DFDDA23AA614649281395D88F43770B8F3529D29C908EE35A182AB7FA3D9289ACAA7E2569BD23E18B9D61C8B74D1345A86027F18392366B1A2D76C6289D2B4AA424D12DFD28C2E23FFD1847A99C4F4CBAD0D7D64AD6DF9118D8D863F32280CC44A000633AD0AB612251C9CE343582E62BEB2A56947275BAE58741862A6486F7F97279FBA698373EBD4E13F5F7F0EBB35AD3F9736F3C085F55CD00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001893A15E5F667C08AFBD6C1AF244AF900BF5DB6B7000000000000000000000000000000000000000000"
        )
    }
}