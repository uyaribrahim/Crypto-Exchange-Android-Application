package com.ibrhm.cryptoexchange.model

data class CoinModel(var name: String? = null, var symbol: String? = null, var price: Double? = null,
                     var volume_24h: Long? = null, var percent_change_24h: Double? = null)
