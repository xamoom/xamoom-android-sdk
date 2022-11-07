package com.xamoom.android.xamoomsdk

import com.xamoom.android.xamoomsdk.Enums.ContentReason
import com.xamoom.android.xamoomsdk.Resource.Content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BeaconContentApiLoader(
    private val majorId: Int,
    private val api: EnduserApi
) {
    val contents: List<Content>
        get() = beaconContentCache.values.toList()

    private val beaconContentCache = HashMap<Int, Content>()

    data class LoadResponse(val minor: Int, val content: Content)

    fun loadBeaconContents(minors: List<Int>, callback: (content: List<LoadResponse>) -> Unit) =
        GlobalScope.launch(Dispatchers.IO) {
            val results = mutableListOf<LoadResponse>()
            minors.forEach {
                load(it, ContentReason.BEACON)?.let { content ->
                    results.add(LoadResponse(it, content))
                }
            }

            GlobalScope.launch(Dispatchers.Main) { callback(results) }
        }

    private suspend fun load(minor: Int, reason: ContentReason): Content? =
        suspendCoroutine { cont ->
            api.getContentByBeacon(
                majorId,
                minor,
                null,
                null,
                reason,
                null,
                object : APIPasswordCallback<Content, List<at.rags.morpheus.Error>> {
                    override fun finished(result: Content) {
                        beaconContentCache[minor] = result
                        cont.resume(result)
                    }

                    override fun error(error: List<at.rags.morpheus.Error>) {
                        cont.resume(null)
                    }

                    override fun passwordRequested() {
                        cont.resume(null)
                    }
                }
            )
        }

    fun loadBeaconContent(minor: Int, callback: (content: Content?) -> Unit) = GlobalScope.launch {
        beaconContentCache[minor]?.let {
            callback(it)
            return@launch
        }

        val content = load(minor, ContentReason.BEACON)

        GlobalScope.launch(Dispatchers.Main) { callback(content) }
    }
}