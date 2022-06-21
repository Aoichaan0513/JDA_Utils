package jp.aoichaan0513.JDA_Utils.Commons

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.Message.MentionType
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.interactions.components.ActionRow

fun buildMessage(builder: MessageBuilder.() -> Unit) = MessageBuilder().apply(builder)

/**
 * Build new message from [MessageBuilder].
 *
 * @param message [MessageBuilder] object
 * @param builder Builder object
 *
 * @author Aoichaan0513
 */
fun buildEmbed(message: MessageBuilder, builder: MessageBuilder.() -> Unit) = MessageBuilder().apply {
    allowedMentions = message.allowedMentions
    nonce = message.nonce
    tts = message.tts
    content = message.content
    embeds = message.embeds
    actionRows = message.actionRows
}.apply(builder)

/**
 * Build new message from [Message].
 *
 * @param message [Message] object
 * @param builder [MessageBuilder] object
 *
 * @author Aoichaan0513
 */
fun buildMessage(message: Message, builder: MessageBuilder.() -> Unit) = MessageBuilder().apply {
    nonce = message.nonce
    tts = message.isTTS
    content = message.contentRaw
    embeds = message.embeds
    actionRows = message.actionRows
}.apply(builder)

@DslContext
class MessageBuilder {

    var allowedMentions: MutableSet<MentionType> = mutableSetOf()
    var nonce: String? = null
    var tts: Boolean = false
    var content: String? = null
    var embeds: MutableList<MessageEmbed> = mutableListOf()
    var actionRows: MutableList<ActionRow> = mutableListOf()

    /**
     * Add embed.
     *
     * @param embed [MessageEmbed] object
     *
     * @author Aoichaan0513
     */
    @DslContext
    fun addEmbed(embed: MessageEmbed) {
        embeds.add(embed)
    }

    /**
     * Add embed.
     *
     * @param builder [EmbedBuilder] object
     *
     * @author Aoichaan0513
     */
    @DslContext
    fun addEmbed(builder: EmbedBuilder.() -> Unit) {
        addEmbed(buildEmbed(builder).buildEmbed())
    }

    /**
     * Add embed.
     *
     * @param index Index number
     * @param embed [MessageEmbed] object
     *
     * @author Aoichaan0513
     */
    @DslContext
    fun addEmbed(index: Int, embed: MessageEmbed) {
        embeds.add(index, embed)
    }

    /**
     * Add embed.
     *
     * @param index Index number
     * @param builder [EmbedBuilder] object
     *
     * @author Aoichaan0513
     */
    @DslContext
    fun addEmbed(index: Int, builder: EmbedBuilder.() -> Unit) {
        addEmbed(index, buildEmbed(builder).buildEmbed())
    }

    /**
     * Build message.
     *
     * @return [net.dv8tion.jda.api.MessageBuilder]
     *
     * @author Aoichaan0513
     */
    fun build() = net.dv8tion.jda.api.MessageBuilder()
        .setAllowedMentions(allowedMentions)
        .setNonce(nonce)
        .setTTS(tts)
        .setContent(content?.ifBlank { null })
        .setEmbeds(embeds)
        .setActionRows(actionRows)

    /**
     * Build mesage.
     *
     * @return [Message]
     *
     * @author Aoichaan0513
     */
    fun buildEmbed() = build().build()
}