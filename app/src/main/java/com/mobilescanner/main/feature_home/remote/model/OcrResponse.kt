package com.mobilescanner.main.feature_home.remote.model

import com.google.gson.annotations.SerializedName

data class OcrGeneralBasicResponse(
    @SerializedName("log_id") val logId: Long,
    @SerializedName("words_result_num") val wordsResultNum: Int,
    @SerializedName("words_result") val wordsResult: List<WordsResult>
) {
    data class WordsResult(
        val words: String
    )
}