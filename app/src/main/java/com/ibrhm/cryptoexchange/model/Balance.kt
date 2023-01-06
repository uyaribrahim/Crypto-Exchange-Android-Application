package com.ibrhm.cryptoexchange.model

import java.io.Serializable

data class Balance(var coin_symbol: String? = null,
                   var coin_image: String? = null,
                   var coin_balance: Double? = null,
                   var coin_usd_value: Double? = null
)