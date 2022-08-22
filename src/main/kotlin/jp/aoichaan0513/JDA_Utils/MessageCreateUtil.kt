package jp.aoichaan0513.JDA_Utils

import jp.aoichaan0513.JDA_Utils.Commons.MessageBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction
import net.dv8tion.jda.api.utils.SplitUtil
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import net.dv8tion.jda.api.utils.messages.MessageCreateData


fun MessageChannel.send(
    content: CharSequence,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageCreateAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
    return sendMessage(content).setAllowedMentions(allowedMentions)
}

fun MessageChannel.send(
    content: Message,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageCreateAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
    return sendMessage(MessageCreateBuilder.fromMessage(content).build()).setAllowedMentions(allowedMentions)
}

fun MessageChannel.send(
    content: MessageBuilder,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageCreateAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
    return sendMessage(content.buildCreateData()).setNonce(content.nonce).setAllowedMentions(allowedMentions)
}

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
    content: Message,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true
): MessageCreateAction? {
    fun setAction(action: MessageCreateAction?) = action?.setAllowedMentions(allowedMentions)
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND)) return null

    return setAction(
        if (content.embeds.isNotEmpty()) {
            if (hasPermissionsByMember(Permission.MESSAGE_EMBED_LINKS)) {
                sendMessage(MessageCreateData.fromMessage(content))
            } else {
                if (isEmbedToText) {
                    val builder = MessageCreateBuilder.fromMessage(content)
                    builder.setEmbeds(setOf())
                    content.embeds.forEach {
                        builder.addContent("${ZERO_WIDTH_SPACE}${it.toText()}")
                    }

                    SplitUtil.split(
                        builder.content,
                        2000,
                        true,
                        SplitUtil.Strategy.onChar(ZERO_WIDTH_SPACE),
                        SplitUtil.Strategy.ANYWHERE
                    ).forEach {
                        setAction(sendMessage(it.removePrefix(ZERO_WIDTH_SPACE.toString())))?.queue()
                    }
                }

                null
            }
        } else {
            sendMessage(MessageCreateData.fromMessage(content))
        }
    )
}


fun MessageChannel.reply(
    reference: Message,
    content: CharSequence,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageCreateAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY)) return null
    return reference.reply(content).mentionRepliedUser(isRepliedMention).setAllowedMentions(allowedMentions)
}

fun MessageChannel.reply(
    reference: Message,
    content: Message,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageCreateAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY)) return null
    return reference.reply(MessageCreateData.fromMessage(content)).mentionRepliedUser(isRepliedMention)
        .setAllowedMentions(allowedMentions)
}


fun MessageChannel.reply(
    reference: Message,
    content: MessageBuilder,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageCreateAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY)) return null
    return reference.reply(content.buildCreateData()).setNonce(content.nonce).mentionRepliedUser(isRepliedMention)
        .setAllowedMentions(allowedMentions)
}

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
    content: Message,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true
): MessageCreateAction? {
    fun setAction(action: MessageCreateAction?) =
        action?.mentionRepliedUser(isRepliedMention)?.setAllowedMentions(allowedMentions)

    if (!hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
    return setAction(
        if (content.embeds.isNotEmpty()) {
            if (hasPermissionsByMember(Permission.MESSAGE_EMBED_LINKS)) {
                reference.reply(MessageCreateData.fromMessage(content))
            } else {
                if (isEmbedToText) {
                    val builder = MessageCreateBuilder.fromMessage(content)
                    builder.setEmbeds(setOf())
                    content.embeds.forEach {
                        builder.addContent("${ZERO_WIDTH_SPACE}${it.toText()}")
                    }

                    SplitUtil.split(
                        builder.content,
                        2000,
                        true,
                        SplitUtil.Strategy.onChar(ZERO_WIDTH_SPACE),
                        SplitUtil.Strategy.ANYWHERE
                    ).forEach {
                        setAction(sendMessage(it.removePrefix(ZERO_WIDTH_SPACE.toString())))?.queue()
                    }
                }

                null
            }
        } else {
            reference.reply(MessageCreateData.fromMessage(content))
        }
    )
}