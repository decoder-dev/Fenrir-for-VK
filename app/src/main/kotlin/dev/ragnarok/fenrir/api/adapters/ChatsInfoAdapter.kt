package dev.ragnarok.fenrir.api.adapters

import dev.ragnarok.fenrir.api.model.VKApiChat
import dev.ragnarok.fenrir.api.model.response.ChatsInfoResponse
import dev.ragnarok.fenrir.kJson
import dev.ragnarok.fenrir.util.serializeble.json.JsonElement
import dev.ragnarok.fenrir.util.serializeble.json.decodeFromJsonElement

class ChatsInfoAdapter : AbsAdapter<ChatsInfoResponse>("ChatsInfoResponse") {
    @Throws(Exception::class)
    override fun deserialize(
        json: JsonElement
    ): ChatsInfoResponse {
        val chats: List<VKApiChat>? = when {
            checkObject(json) -> {
                listOf(
                    kJson.decodeFromJsonElement(
                        json
                    )
                )
            }
            checkArray(json) -> {
                val array = json.asJsonArray
                parseArray(
                    array,
                    emptyList()
                )
            }
            else -> {
                emptyList()
            }
        }
        val response = ChatsInfoResponse()
        response.chats = chats
        return response
    }
}