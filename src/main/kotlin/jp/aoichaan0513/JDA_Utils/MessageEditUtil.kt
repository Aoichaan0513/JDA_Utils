package jp.aoichaan0513.JDA_Utils

import club.minnced.discord.webhook.send.AllowedMentions
import club.minnced.discord.webhook.send.WebhookEmbedBuilder
import club.minnced.discord.webhook.send.WebhookMessageBuilder
import jp.aoichaan0513.JDA_Utils.Commons.MessageBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.requests.restaction.MessageEditAction
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder
import net.dv8tion.jda.api.utils.messages.MessageEditData


fun Message.edit(
    content: MessageEditData,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageEditAction? {
    if (!channel.hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
    return editMessage(content).mentionRepliedUser(isRepliedMention).setAllowedMentions(allowedMentions)
}

fun Message.edit(
    content: CharSequence,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
) = edit(MessageEditData.fromContent(content.toString()), isRepliedMention, allowedMentions)

fun Message.edit(
    content: Message,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
) = edit(MessageEditData.fromMessage(content), isRepliedMention, allowedMentions)

fun Message.edit(
    content: MessageBuilder,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
) = edit(content.buildEditData(), isRepliedMention, allowedMentions)

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


fun MessageEditBuilder.toCreateBuilder() = MessageCreateBuilder.fromEditData(this.build())
fun MessageEditData.toCreateData() = MessageCreateData.fromEditData(this)


fun MessageEditData.toWebhookMessageBuilder(): WebhookMessageBuilder {
    val builder = WebhookMessageBuilder()
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