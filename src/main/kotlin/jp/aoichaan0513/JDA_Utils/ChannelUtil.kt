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


fun MessageChannel.hasAccessByMember(member: Member? = null) =
    this !is GuildChannel || (member ?: guild.selfMember).hasAccess(this)

fun MessageChannel.hasAccessByRole(role: Role? = null) =
    this !is GuildChannel || (role ?: guild.publicRole).hasAccess(this)

fun GuildChannel.hasAccessByMember(member: Member? = null) =
    (member ?: guild.selfMember).hasAccess(this)

fun GuildChannel.hasAccessByRole(role: Role? = null) =
    (role ?: guild.publicRole).hasAccess(this)


private fun MessageChannel.hasPermissions(permissions: Collection<Permission?>, iPermissionHolder: IPermissionHolder) =
    this !is GuildChannel || iPermissionHolder.hasPermission(this, permissions.filterNotNull())

private fun MessageChannel.hasPermissions(iterable: Iterable<Permission?>, iPermissionHolder: IPermissionHolder) =
    hasPermissions(iterable.toSet(), iPermissionHolder)

private fun MessageChannel.hasPermissions(vararg array: Permission?, iPermissionHolder: IPermissionHolder) =
    hasPermissions(array.toSet(), iPermissionHolder)

fun MessageChannel.hasPermissionsByMember(permissions: Collection<Permission?>, member: Member? = null) =
    this !is GuildChannel || (this as GuildChannel).hasPermissions(
        permissions,
        (member ?: guild.selfMember) as IPermissionHolder
    )

fun MessageChannel.hasPermissionsByMember(iterable: Iterable<Permission?>, member: Member? = null) =
    hasPermissionsByMember(iterable.toSet(), member)

fun MessageChannel.hasPermissionsByMember(vararg array: Permission?, member: Member? = null) =
    hasPermissionsByMember(array.toSet(), member)

fun MessageChannel.hasPermissionsByRole(permissions: Collection<Permission?>, role: Role? = null) =
    this !is GuildChannel || (this as GuildChannel).hasPermissions(
        permissions,
        (role ?: guild.publicRole) as IPermissionHolder
    )

fun MessageChannel.hasPermissionsByRole(iterable: Iterable<Permission?>, role: Role? = null) =
    hasPermissionsByRole(iterable.toSet(), role)

fun MessageChannel.hasPermissionsByRole(vararg array: Permission?, role: Role? = null) =
    hasPermissionsByRole(array.toSet(), role)


private fun GuildChannel.hasPermissions(permissions: Collection<Permission?>, iPermissionHolder: IPermissionHolder) =
    iPermissionHolder.hasPermission(this, permissions.filterNotNull())

private fun GuildChannel.hasPermissions(iterable: Iterable<Permission?>, iPermissionHolder: IPermissionHolder) =
    hasPermissions(iterable.toSet(), iPermissionHolder)

private fun GuildChannel.hasPermissions(vararg array: Permission?, iPermissionHolder: IPermissionHolder) =
    hasPermissions(array.toSet(), iPermissionHolder)

fun GuildChannel.hasPermissionsByMember(permissions: Collection<Permission?>, member: Member? = null) =
    hasPermissions(permissions, (member ?: guild.selfMember) as IPermissionHolder)

fun GuildChannel.hasPermissionsByMember(iterable: Iterable<Permission?>, member: Member? = null) =
    hasPermissionsByMember(iterable.toSet(), member)

fun GuildChannel.hasPermissionsByMember(vararg array: Permission?, member: Member? = null) =
    hasPermissionsByMember(array.toSet(), member)

fun GuildChannel.hasPermissionsByRole(permissions: Collection<Permission?>, role: Role? = null) =
    hasPermissions(permissions, (role ?: guild.publicRole) as IPermissionHolder)

fun GuildChannel.hasPermissionsByRole(iterable: Iterable<Permission?>, role: Role? = null) =
    hasPermissionsByRole(iterable.toSet(), role)

fun GuildChannel.hasPermissionsByRole(vararg array: Permission?, role: Role? = null) =
    hasPermissionsByRole(array.toSet(), role)


fun MessageChannel.send(
    content: CharSequence,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_WRITE)) return null
    return sendMessage(content).allowedMentions(allowedMentions)
}

fun MessageChannel.send(content: Message, allowedMentions: Collection<Message.MentionType>? = setOf()): MessageAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_WRITE)) return null
    return sendMessage(content).allowedMentions(allowedMentions)
}

fun MessageChannel.send(
    content: MessageEmbed,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true
): MessageAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_WRITE)) return null
    return if (hasPermissionsByMember(Permission.MESSAGE_EMBED_LINKS)) {
        sendMessageEmbeds(content)
    } else {
        if (isEmbedToText) {
            sendMessage(convertText(content))
        } else {
            null
        }
    }?.allowedMentions(allowedMentions)
}

fun MessageChannel.sendEmbeds(
    embeds: Collection<MessageEmbed>,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS)) return null
    return sendMessageEmbeds(embeds).allowedMentions(allowedMentions)
}

fun MessageChannel.sendEmbeds(
    embeds: Iterable<MessageEmbed>,
    allowedMentions: Collection<Message.MentionType>? = setOf()
) = sendEmbeds(embeds.toList(), allowedMentions)

fun MessageChannel.sendEmbeds(
    vararg embeds: MessageEmbed,
    allowedMentions: Collection<Message.MentionType>? = setOf()
) = sendEmbeds(embeds.toList(), allowedMentions)


fun MessageChannel.reply(
    reference: Message,
    content: CharSequence,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY)) return null
    return reference.reply(content).mentionRepliedUser(isRepliedMention).allowedMentions(allowedMentions)
}

fun MessageChannel.reply(
    reference: Message,
    content: Message,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY)) return null
    return reference.reply(content).mentionRepliedUser(isRepliedMention).allowedMentions(allowedMentions)
}

fun MessageChannel.reply(
    reference: Message,
    content: MessageEmbed,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true
): MessageAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY)) return null
    return if (hasPermissionsByMember(Permission.MESSAGE_EMBED_LINKS)) {
        reference.replyEmbeds(content)
    } else {
        if (isEmbedToText) {
            reference.reply(convertText(content))
        } else {
            null
        }
    }?.mentionRepliedUser(isRepliedMention)?.allowedMentions(allowedMentions)
}

fun MessageChannel.replyEmbeds(
    reference: Message,
    embeds: Collection<MessageEmbed>,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY, Permission.MESSAGE_EMBED_LINKS))
        return null
    return reference.replyEmbeds(embeds).mentionRepliedUser(isRepliedMention).allowedMentions(allowedMentions)
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


fun Message.edit(
    content: CharSequence,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!channel.hasPermissionsByMember(Permission.MESSAGE_WRITE)) return null
    return editMessage(content).mentionRepliedUser(isRepliedMention).allowedMentions(allowedMentions)
}

fun Message.edit(
    content: Message,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!channel.hasPermissionsByMember(Permission.MESSAGE_WRITE)) return null
    return editMessage(content).mentionRepliedUser(isRepliedMention).allowedMentions(allowedMentions)
}

fun Message.edit(
    content: MessageEmbed,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true
): MessageAction? {
    if (!channel.hasPermissionsByMember(Permission.MESSAGE_WRITE)) return null
    return if (channel.hasPermissionsByMember(Permission.MESSAGE_EMBED_LINKS)) {
        editMessageEmbeds(content)
    } else {
        if (isEmbedToText) {
            editMessage(convertText(content))
        } else {
            null
        }
    }?.mentionRepliedUser(isRepliedMention)?.allowedMentions(allowedMentions)
}

fun Message.editEmbeds(
    embeds: Collection<MessageEmbed>,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!channel.hasPermissionsByMember(Permission.MESSAGE_WRITE)) return null
    return editMessageEmbeds(embeds).mentionRepliedUser(isRepliedMention).allowedMentions(allowedMentions)
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