package jp.aoichaan0513.JDA_Utils

import jp.aoichaan0513.JDA_Utils.Commons.MessageBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.requests.restaction.MessageEditAction
import net.dv8tion.jda.api.utils.messages.MessageEditData


fun Message.edit(
    content: CharSequence,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageEditAction? {
    if (!channel.hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
    return editMessage(content).mentionRepliedUser(isRepliedMention).setAllowedMentions(allowedMentions)
}

fun Message.edit(
    content: Message,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageEditAction? {
    if (!channel.hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
    return editMessage(MessageEditData.fromMessage(content)).mentionRepliedUser(isRepliedMention)
        .setAllowedMentions(allowedMentions)
}

fun Message.edit(
    content: MessageBuilder,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageEditAction? {
    if (!channel.hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
    return editMessage(content.buildEditData()).mentionRepliedUser(isRepliedMention).setAllowedMentions(allowedMentions)
}

fun Message.edit(
    content: MessageEmbed,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true
): MessageEditAction? {
    if (!channel.hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
    return if (channel.hasPermissionsByMember(Permission.MESSAGE_EMBED_LINKS)) {
        editMessageEmbeds(content)
    } else {
        if (isEmbedToText) {
            editMessage(content.toText())
        } else {
            null
        }
    }?.mentionRepliedUser(isRepliedMention)?.setAllowedMentions(allowedMentions)
}

fun Message.editEmbeds(
    embeds: Collection<MessageEmbed>,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageEditAction? {
    if (!channel.hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
    return editMessageEmbeds(embeds).mentionRepliedUser(isRepliedMention).setAllowedMentions(allowedMentions)
}

fun Message.editEmbeds(
    embeds: Iterable<MessageEmbed>,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
) = editEmbeds(embeds.toList(), isRepliedMention, allowedMentions)

fun Message.editEmbeds(
    vararg embeds: MessageEmbed,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
) = editEmbeds(embeds.toList(), isRepliedMention, allowedMentions)