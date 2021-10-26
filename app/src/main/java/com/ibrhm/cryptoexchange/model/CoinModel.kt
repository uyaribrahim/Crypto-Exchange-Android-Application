package com.ibrhm.cryptoexchange.model

data class CoinModel(var cmc_rank: Int? = null,
                     var name: String? = null,
                     var percent_change_24h: Double? = null,
                     var price: Double? = null,
                     var symbol: String? = null,
                     var volume_24h: Long? = null)
