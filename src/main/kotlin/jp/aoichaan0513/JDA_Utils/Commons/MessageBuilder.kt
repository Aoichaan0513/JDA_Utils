package jp.aoichaan0513.JDA_Utils.Commons

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.Message.MentionType
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.interactions.components.LayoutComponent
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder

fun buildMessage(builder: MessageBuilder.() -> Unit) = MessageBuilder().apply(builder)

/**
 * Build new message from [MessageBuilder].
 *
 * @param message [MessageBuilder] object
 * @param builder Builder object
 *
 * @author Aoichaan0513
 */
fun buildMessage(message: MessageBuilder, builder: MessageBuilder.() -> Unit) = MessageBuilder().apply {
    allowedMentions = message.allowedMentions
    nonce = message.nonce
    tts = message.tts
    content = message.content
    embeds = message.embeds
    components = message.components
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
    components = message.actionRows.toMutableList()
}.apply(builder)

@DslContext
class MessageBuilder {

    var allowedMentions = mutableSetOf<MentionType>()
    var nonce: String? = null
    var tts = false
    var content: String? = null
    var embeds = mutableListOf<MessageEmbed>()
    var components = mutableListOf<LayoutComponent>()

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
     * Add Layout component.
     *
     * @param component [LayoutComponent] object
     *
     * @author Aoichaan0513
     */
    @DslContext
    fun addComponent(component: LayoutComponent) {
        components.add(component)
    }

    /**
     * Add Layout component.
     *
     * @param index Index number
     * @param component [LayoutComponent] object
     *
     * @author Aoichaan0513
     */
    @DslContext
    fun addComponent(index: Int, component: LayoutComponent) {
        components.add(index, component)
    }

    /**
     * Build Create Data.
     *
     * @return [net.dv8tion.jda.api.utils.messages.MessageCreateData]
     *
     * @author Aoichaan0513
     */
    fun buildCreateData() = MessageCreateBuilder()
        .setAllowedMentions(allowedMentions)
        .setTTS(tts)
        .setContent(content?.ifBlank { null })
        .setEmbeds(embeds)
        .setComponents(components)
        .build()

    /**
     * Build Edit Data.
     *
     * @return [net.dv8tion.jda.api.utils.messages.MessageEditData]
     *
     * @author Aoichaan0513
     */
    fun buildEditData(replace: Boolean = false) = MessageEditBuilder()
        .setReplace(replace)
        .setAllowedMentions(allowedMentions)
        .setContent(content?.ifBlank { null })
        .setEmbeds(embeds)
        .setComponents(components)
        .build()
}