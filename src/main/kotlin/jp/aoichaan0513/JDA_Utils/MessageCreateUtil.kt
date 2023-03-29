package jp.aoichaan0513.JDA_Utils

import club.minnced.discord.webhook.send.AllowedMentions
import club.minnced.discord.webhook.send.WebhookEmbedBuilder
import club.minnced.discord.webhook.send.WebhookMessageBuilder
import jp.aoichaan0513.JDA_Utils.Commons.MessageBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction
import net.dv8tion.jda.api.utils.SplitUtil
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder
import net.dv8tion.jda.api.utils.messages.MessageEditData


fun MessageChannel.send(
    content: MessageCreateData,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageCreateAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
    return sendMessage(content).setAllowedMentions(allowedMentions)
}

fun MessageChannel.send(
    content: CharSequence,
    allowedMentions: Collection<Message.MentionType>? = setOf()
) = send(MessageCreateData.fromContent(content.toString()), allowedMentions)

fun MessageChannel.send(
    content: Message,
    allowedMentions: Collection<Message.MentionType>? = setOf()
) = send(MessageCreateData.fromMessage(content), allowedMentions)

fun MessageChannel.send(
    content: MessageBuilder,
    allowedMentions: Collection<Message.MentionType>? = setOf()
) = send(content.buildCreateData(), allowedMentions)?.setNonce(content.nonce)

fun MessageChannel.send(
    content: MessageEmbed,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true
): MessageCreateAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
    return if (hasPermissionsByMember(Permission.MESSAGE_EMBED_LINKS)) {
        sendMessageEmbeds(content)
    } else {
        if (isEmbedToText) {
            sendMessage(content.toText())
        } else {
            null
        }
    }?.setAllowedMentions(allowedMentions)
}

fun MessageChannel.sendEmbeds(
    embeds: Collection<MessageEmbed>,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageCreateAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND, Permission.MESSAGE_EMBED_LINKS)) return null
    return sendMessageEmbeds(embeds).setAllowedMentions(allowedMentions)
}

fun MessageChannel.sendEmbeds(
    embeds: Iterable<MessageEmbed>,
    allowedMentions: Collection<Message.MentionType>? = setOf()
) = sendEmbeds(embeds.toList(), allowedMentions)

fun MessageChannel.sendEmbeds(
    vararg embeds: MessageEmbed,
    allowedMentions: Collection<Message.MentionType>? = setOf()
) = sendEmbeds(embeds.toList(), allowedMentions)

fun MessageChannel.sendComponents(
    content: MessageCreateData,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true,
    action: (MessageCreateAction) -> Unit = { it.queue() }
) {
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND)) return

    fun setAction(action: MessageCreateAction?) = action?.setAllowedMentions(allowedMentions)

    val data = setAction(
        if (content.embeds.isNotEmpty()) {
            if (hasPermissionsByMember(Permission.MESSAGE_EMBED_LINKS)) {
                sendMessage(content)
            } else {
                if (!isEmbedToText)
                    return

                val builder = MessageCreateBuilder.from(content)
                builder.setEmbeds(setOf())
                content.embeds.forEach {
                    builder.addContent("${StringUtil.ZERO_WIDTH_SPACE}${it.toText()}")
                }

                SplitUtil.split(
                    builder.content,
                    2000,
                    true,
                    SplitUtil.Strategy.onChar(StringUtil.ZERO_WIDTH_SPACE),
                    SplitUtil.Strategy.ANYWHERE
                ).mapNotNull { setAction(sendMessage(it.removePrefix(StringUtil.ZERO_WIDTH_SPACE.toString()))) }
                    .forEach(action)
                return
            }
        } else {
            sendMessage(content)
        }
    ) ?: return

    action(data)
}

fun MessageChannel.sendComponents(
    content: CharSequence,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true,
    action: (MessageCreateAction) -> Unit = { it.queue() }
) = sendComponents(
    MessageCreateData.fromContent(content.toString()),
    allowedMentions,
    isEmbedToText,
    action
)

fun MessageChannel.sendComponents(
    content: Message,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true,
    action: (MessageCreateAction) -> Unit = { it.queue() }
) = sendComponents(
    MessageCreateData.fromMessage(content),
    allowedMentions,
    isEmbedToText,
    action
)


fun MessageChannel.reply(
    reference: Message,
    content: MessageCreateData,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageCreateAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY)) return null
    return reference.reply(content).mentionRepliedUser(isRepliedMention).setAllowedMentions(allowedMentions)
}

fun MessageChannel.reply(
    reference: Message,
    content: CharSequence,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
) = reply(reference, MessageCreateData.fromContent(content.toString()), isRepliedMention, allowedMentions)

fun MessageChannel.reply(
    reference: Message,
    content: Message,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
) = reply(reference, MessageCreateData.fromMessage(content), isRepliedMention, allowedMentions)

fun MessageChannel.reply(
    reference: Message,
    content: MessageBuilder,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
) = reply(reference, content.buildCreateData(), isRepliedMention, allowedMentions)?.setNonce(content.nonce)

fun MessageChannel.reply(
    reference: Message,
    content: MessageEmbed,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true
): MessageCreateAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY)) return null
    return if (hasPermissionsByMember(Permission.MESSAGE_EMBED_LINKS)) {
        reference.replyEmbeds(content)
    } else {
        if (isEmbedToText) {
            reference.reply(content.toText())
        } else {
            null
        }
    }?.mentionRepliedUser(isRepliedMention)?.setAllowedMentions(allowedMentions)
}

fun MessageChannel.replyEmbeds(
    reference: Message,
    embeds: Collection<MessageEmbed>,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageCreateAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY, Permission.MESSAGE_EMBED_LINKS))
        return null
    return reference.replyEmbeds(embeds).mentionRepliedUser(isRepliedMention).setAllowedMentions(allowedMentions)
}

fun MessageChannel.replyEmbeds(
    reference: Message,
    embeds: Iterable<MessageEmbed>,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
) = replyEmbeds(reference, embeds.toList(), isRepliedMention, allowedMentions)

fun MessageChannel.replyEmbeds(
    reference: Message,
    vararg embeds: MessageEmbed,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
) = replyEmbeds(reference, embeds.toList(), isRepliedMention, allowedMentions)

fun MessageChannel.replyComponents(
    reference: Message,
    content: MessageCreateData,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true,
    action: (MessageCreateAction) -> Unit = { it.queue() }
) {
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND)) return

    fun setAction(action: MessageCreateAction?) =
        action?.mentionRepliedUser(isRepliedMention)?.setAllowedMentions(allowedMentions)

    val data = setAction(
        if (content.embeds.isNotEmpty()) {
            if (hasPermissionsByMember(Permission.MESSAGE_EMBED_LINKS)) {
                reference.reply(content)
            } else {
                if (!isEmbedToText)
                    return

                val builder = MessageCreateBuilder.from(content)
                builder.setEmbeds(setOf())
                content.embeds.forEach {
                    builder.addContent("${StringUtil.ZERO_WIDTH_SPACE}${it.toText()}")
                }

                SplitUtil.split(
                    builder.content,
                    2000,
                    true,
                    SplitUtil.Strategy.onChar(StringUtil.ZERO_WIDTH_SPACE),
                    SplitUtil.Strategy.ANYWHERE
                ).mapNotNull { setAction(sendMessage(it.removePrefix(StringUtil.ZERO_WIDTH_SPACE.toString()))) }
                    .forEach(action)
                return
            }
        } else {
            reference.reply(content)
        }
    ) ?: return

    action(data)
}

fun MessageChannel.replyComponents(
    reference: Message,
    content: CharSequence,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true,
    action: (MessageCreateAction) -> Unit = { it.queue() }
) = replyComponents(
    reference,
    MessageCreateData.fromContent(content.toString()),
    isRepliedMention,
    allowedMentions,
    isEmbedToText,
    action
)

fun MessageChannel.replyComponents(
    reference: Message,
    content: Message,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true,
    action: (MessageCreateAction) -> Unit = { it.queue() }
) = replyComponents(
    reference,
    MessageCreateData.fromMessage(content),
    isRepliedMention,
    allowedMentions,
    isEmbedToText,
    action
)


fun MessageCreateBuilder.toEditBuilder() = MessageEditBuilder.fromCreateData(this.build())
fun MessageCreateData.toEditData() = MessageEditData.fromCreateData(this)


fun MessageCreateData.toWebhookMessageBuilder(): WebhookMessageBuilder {
    val builder = WebhookMessageBuilder()
    builder.setTTS(isTTS)
    builder.setContent(content)
    files.forEach { builder.addFile(it.name, it.data) }
    embeds.forEach { builder.addEmbeds(WebhookEmbedBuilder.fromJDA(it).build()) }

    val allowedMentions = AllowedMentions.none()
    val parse = this.allowedMentions
    allowedMentions.withUsers(mentionedUsers)
    allowedMentions.withRoles(mentionedRoles)
    allowedMentions.withParseUsers(parse.contains(Message.MentionType.USER))
    allowedMentions.withParseRoles(parse.contains(Message.MentionType.ROLE))
    allowedMentions.withParseEveryone(parse.contains(Message.MentionType.EVERYONE) || parse.contains(Message.MentionType.HERE))
    builder.setAllowedMentions(allowedMentions)

    return builder
}