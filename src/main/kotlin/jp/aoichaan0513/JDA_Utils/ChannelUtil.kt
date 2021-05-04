package jp.aoichaan0513.JDA_Utils

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
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true
): MessageAction? {
    if (!hasPermissions(Permission.MESSAGE_WRITE)) return null
    return if (hasPermissions(Permission.MESSAGE_EMBED_LINKS)) {
        sendMessage(content)
    } else {
        if (isEmbedToText) {
            sendMessage(convertText(content))
        } else {
            null
        }
    }?.allowedMentions(allowedMentions)
}


fun MessageChannel.reply(
    reference: Message,
    content: CharSequence,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!hasPermissions(Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY)) return null
    return reference.reply(content).mentionRepliedUser(isRepliedMention).allowedMentions(allowedMentions)
}

fun MessageChannel.reply(
    reference: Message,
    content: Message,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!hasPermissions(Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY)) return null
    return reference.reply(content).mentionRepliedUser(isRepliedMention).allowedMentions(allowedMentions)
}

fun MessageChannel.reply(
    reference: Message,
    content: MessageEmbed,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true
): MessageAction? {
    if (!hasPermissions(Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY)) return null
    return if (hasPermissions(Permission.MESSAGE_EMBED_LINKS)) {
        reference.reply(content)
    } else {
        if (isEmbedToText) {
            reference.reply(convertText(content))
        } else {
            null
        }
    }?.mentionRepliedUser(isRepliedMention)?.allowedMentions(allowedMentions)
}


fun Message.edit(
    content: CharSequence,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!channel.hasPermissions(Permission.MESSAGE_WRITE)) return null
    return editMessage(content).mentionRepliedUser(isRepliedMention).allowedMentions(allowedMentions)
}

fun Message.edit(
    content: Message,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!channel.hasPermissions(Permission.MESSAGE_WRITE)) return null
    return editMessage(content).mentionRepliedUser(isRepliedMention).allowedMentions(allowedMentions)
}

fun Message.edit(
    content: MessageEmbed,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true
): MessageAction? {
    if (!channel.hasPermissions(Permission.MESSAGE_WRITE)) return null
    return if (channel.hasPermissions(Permission.MESSAGE_EMBED_LINKS)) {
        editMessage(content)
    } else {
        if (isEmbedToText) {
            editMessage(convertText(content))
        } else {
            null
        }
    }?.mentionRepliedUser(isRepliedMention)?.allowedMentions(allowedMentions)
}


private fun convertText(messageEmbed: MessageEmbed) = buildString {
    fun insertText(sourceText: String?, targetText: (String) -> String = { it }) = sourceText?.run {
        fun isLength(text: String) = (length + text.length) <= Message.MAX_CONTENT_LENGTH

        val text = targetText(this)
        if (isLength(text))
            append(text)
    }

    insertText(messageEmbed.author?.name) { "${it.quote()}\n" }
    insertText(messageEmbed.title) { "${it.bold().quote()}\n" }
    insertText(messageEmbed.description) { "$it\n" }
    messageEmbed.fields.filter { !it.name.isNullOrEmpty() || !it.value.isNullOrEmpty() }.forEach {
        insertText("${it.name?.let { "${it.bold()} ― " }}${it.value}") { "$it\n" }
    }
    insertText(messageEmbed.footer?.text) { it.quote() }
}.trim()