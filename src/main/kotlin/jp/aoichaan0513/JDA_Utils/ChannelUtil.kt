package jp.aoichaan0513.YudzukiBot.Utils.Object

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.requests.restaction.MessageAction


val MessageChannel.isPrivateChannel
    get() = this is PrivateChannel

val MessageChannel.isGuildChannel
    get() = this is GuildChannel

val MessageChannel.isTextChannel
    get() = isGuildTextChannel || isPrivateTextChannel

val GuildChannel.isTextChannel
    get() = isGuildTextChannel || isPrivateTextChannel

val MessageChannel.isGuildTextChannel
    get() = type == ChannelType.TEXT

val GuildChannel.isGuildTextChannel
    get() = type == ChannelType.TEXT

val MessageChannel.isPrivateTextChannel
    get() = type == ChannelType.PRIVATE || type == ChannelType.GROUP

val GuildChannel.isPrivateTextChannel
    get() = type == ChannelType.PRIVATE || type == ChannelType.GROUP


fun MessageChannel.hasPermissions(permissions: Collection<Permission?>, member: Member? = null) =
    isPrivateChannel || this is GuildChannel && (member ?: guild.selfMember).hasPermission(
        this,
        permissions.filterNotNull()
    )

fun MessageChannel.hasPermissions(iterable: Iterable<Permission?>, member: Member? = null) =
    hasPermissions(iterable.toSet(), member)

fun MessageChannel.hasPermissions(vararg array: Permission?, member: Member? = null) =
    hasPermissions(array.toSet(), member)

fun GuildChannel.hasPermissions(permissions: Collection<Permission?>, member: Member? = null) =
    (member ?: guild.selfMember).hasPermission(this, permissions.filterNotNull())

fun GuildChannel.hasPermissions(iterable: Iterable<Permission?>, member: Member? = null) =
    hasPermissions(iterable.toSet(), member)

fun GuildChannel.hasPermissions(vararg array: Permission?, member: Member? = null) =
    hasPermissions(array.toSet(), member)


fun MessageChannel.send(
    content: CharSequence,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!hasPermissions(Permission.MESSAGE_WRITE)) return null
    return sendMessage(content).allowedMentions(allowedMentions)
}

fun MessageChannel.send(content: Message, allowedMentions: Collection<Message.MentionType>? = setOf()): MessageAction? {
    if (!hasPermissions(Permission.MESSAGE_WRITE)) return null
    return sendMessage(content).allowedMentions(allowedMentions)
}

fun MessageChannel.send(
    content: MessageEmbed,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!hasPermissions(Permission.MESSAGE_WRITE)) return null
    return sendMessage(content).allowedMentions(allowedMentions)
}


fun MessageChannel.reply(
    reference: Message,
    content: CharSequence,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!hasPermissions(Permission.MESSAGE_WRITE)) return null
    return reference.reply(content).mentionRepliedUser(isRepliedMention).allowedMentions(allowedMentions)
}

fun MessageChannel.reply(
    reference: Message,
    content: Message,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!hasPermissions(Permission.MESSAGE_WRITE)) return null
    return reference.reply(content).mentionRepliedUser(isRepliedMention).allowedMentions(allowedMentions)
}

fun MessageChannel.reply(
    reference: Message,
    content: MessageEmbed,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!hasPermissions(Permission.MESSAGE_WRITE)) return null
    return reference.reply(content).mentionRepliedUser(isRepliedMention).allowedMentions(allowedMentions)
}