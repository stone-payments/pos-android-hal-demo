package br.com.stone.posandroid.hal.demo.util

import br.com.stone.posandroid.hal.demo.bc.base.AutoLoadTableTest



const val REGEX_CARD_CHIP = "^0300108\\d{4}\\s{76}"
const val REGEX_CARD_MAG = "^0000000007"
const val DEFAULT_GOC_COMMAND = "00000000010000000000000001031600000000000000000000000000000000000000000000000000000000003000003000"
const val DEFAULT_GCR_INPUT =
    "0801000000023850020904164230${AutoLoadTableTest.TABLE_STUB_TIMESTAMP}000"
const val VISA_TESTCARD01_OUTPUT =
    "03001080500                                                                            344761739001010119=22122011758928889   000                                                                                                        164761739001010119   01                   VISA ACQUIRER TEST/CARD 0122123100                           840000"

const val DEFAULT_LENGTH_CARD_MAG = 342