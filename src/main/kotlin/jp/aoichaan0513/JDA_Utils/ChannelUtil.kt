package jp.aoichaan0513.JDA_Utils

import jp.aoichaan0513.JDA_Utils.Commons.EmbedBuilder
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.requests.restaction.MessageAction


val MessageChannel.isPrivateChannel
    get() = this is PrivateChannel

val MessageChannel.isGuildChannel
    get() = this is GuildChannel

val Channel.isTextChannel
    get() = isGuildTextChannel || isPrivateTextChannel

val Channel.isGuildTextChannel
    get() = type == ChannelType.TEXT

val Channel.isPrivateTextChannel
    get() = type == ChannelType.PRIVATE || type == ChannelType.GROUP


val GuildChannel.parentCategory
    get() = (this as? ICategorizableChannel)?.parentCategory

val GuildChannel.parentCategoryId
    get() = (this as? ICategorizableChannel)?.parentCategoryId

val GuildChannel.parentCategoryIdLong
    get() = (this as? ICategorizableChannel)?.parentCategoryIdLong


val GuildChannel.position
    get() = (this as? IPositionableChannel)?.position

val GuildChannel.positionRaw
    get() = (this as? IPositionableChannel)?.positionRaw


val AudioChannel.userLimit
    get() = (this as? VoiceChannel)?.userLimit


fun Channel.hasAccessByMember(member: Member? = null) =
    this !is GuildChannel || (member ?: guild.selfMember).hasAccess(this)

fun Channel.hasAccessByRole(role: Role? = null) =
    this !is GuildChannel || (role ?: guild.publicRole).hasAccess(this)


private fun Channel.hasPermissions(permissions: Collection<Permission?>, holder: IPermissionHolder) =
    this !is GuildChannel || holder.hasPermission(this, permissions.filterNotNull())

private fun Channel.hasPermissions(iterable: Iterable<Permission?>, holder: IPermissionHolder) =
    hasPermissions(iterable.toSet(), holder)

private fun Channel.hasPermissions(vararg array: Permission?, holder: IPermissionHolder) =
    hasPermissions(array.toSet(), holder)

fun Channel.hasPermissionsByMember(permissions: Collection<Permission?>, member: Member? = null) =
    this !is GuildChannel || hasPermissions(
        permissions,
        (member ?: guild.selfMember) as IPermissionHolder
    )

fun Channel.hasPermissionsByMember(iterable: Iterable<Permission?>, member: Member? = null) =
    hasPermissionsByMember(iterable.toSet(), member)

fun Channel.hasPermissionsByMember(vararg array: Permission?, member: Member? = null) =
    hasPermissionsByMember(array.toSet(), member)

fun Channel.hasPermissionsByRole(permissions: Collection<Permission?>, role: Role? = null) =
    this !is GuildChannel || hasPermissions(
        permissions,
        (role ?: guild.publicRole) as IPermissionHolder
    )

fun Channel.hasPermissionsByRole(iterable: Iterable<Permission?>, role: Role? = null) =
    hasPermissionsByRole(iterable.toSet(), role)

fun Channel.hasPermissionsByRole(vararg array: Permission?, role: Role? = null) =
    hasPermissionsByRole(array.toSet(), role)


fun MessageChannel.send(
    content: CharSequence,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
    return sendMessage(content).allowedMentions(allowedMentions)
}

fun MessageChannel.send(content: Message, allowedMentions: Collection<Message.MentionType>? = setOf()): MessageAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
    return sendMessage(content).allowedMentions(allowedMentions)
}

fun MessageChannel.send(
    content: MessageEmbed,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true
): MessageAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
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
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND, Permission.MESSAGE_EMBED_LINKS)) return null
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

fun MessageChannel.sendComponents(
    content: Message,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true
): MessageAction? {
    fun setAction(action: MessageAction?) = action?.allowedMentions(allowedMentions)
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
    return setAction(
        if (content.embeds.isNotEmpty()) {
            if (hasPermissionsByMember(Permission.MESSAGE_EMBED_LINKS)) {
                sendMessage(content)
            } else {
                if (isEmbedToText) {
                    val builder = MessageBuilder(content)
                    builder.setEmbeds(setOf())
                    content.embeds.forEach {
                        builder.append("${EmbedBuilder.ZERO_WIDTH_SPACE}${convertText(it)}")
                    }

                    builder.buildAll(MessageBuilder.SplitPolicy.onChars(EmbedBuilder.ZERO_WIDTH_SPACE, true))
                        .map { setAction(sendMessage(it.contentRaw.removePrefix(EmbedBuilder.ZERO_WIDTH_SPACE)))?.queue() }
                }

                null
            }
        } else {
            sendMessage(content)
        }
    )
}


fun MessageChannel.reply(
    reference: Message,
    content: CharSequence,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY)) return null
    return reference.reply(content).mentionRepliedUser(isRepliedMention).allowedMentions(allowedMentions)
}

fun MessageChannel.reply(
    reference: Message,
    content: Message,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY)) return null
    return reference.reply(content).mentionRepliedUser(isRepliedMention).allowedMentions(allowedMentions)
}

fun MessageChannel.reply(
    reference: Message,
    content: MessageEmbed,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true
): MessageAction? {
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY)) return null
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
    if (!hasPermissionsByMember(Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY, Permission.MESSAGE_EMBED_LINKS))
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

fun MessageChannel.replyComponents(
    reference: Message,
    content: Message,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true
): MessageAction? {
    fun setAction(action: MessageAction?) =
        action?.mentionRepliedUser(isRepliedMention)?.allowedMentions(allowedMentions)

    if (!hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
    return setAction(
        if (content.embeds.isNotEmpty()) {
            if (hasPermissionsByMember(Permission.MESSAGE_EMBED_LINKS)) {
                reference.reply(content)
            } else {
                if (isEmbedToText) {
                    val builder = MessageBuilder(content)
                    builder.setEmbeds(setOf())
                    content.embeds.forEach {
                        builder.append("${EmbedBuilder.ZERO_WIDTH_SPACE}${convertText(it)}")
                    }

                    builder.buildAll(MessageBuilder.SplitPolicy.onChars(EmbedBuilder.ZERO_WIDTH_SPACE, true))
                        .map { setAction(reference.reply(it.contentRaw.removePrefix(EmbedBuilder.ZERO_WIDTH_SPACE)))?.queue() }
                }

                null
            }
        } else {
            reference.reply(content)
        }
    )
}


fun Message.edit(
    content: CharSequence,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!channel.hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
    return editMessage(content).mentionRepliedUser(isRepliedMention).allowedMentions(allowedMentions)
}

fun Message.edit(
    content: Message,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf()
): MessageAction? {
    if (!channel.hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
    return editMessage(content).mentionRepliedUser(isRepliedMention).allowedMentions(allowedMentions)
}

fun Message.edit(
    content: MessageEmbed,
    isRepliedMention: Boolean = false,
    allowedMentions: Collection<Message.MentionType>? = setOf(),
    isEmbedToText: Boolean = true
): MessageAction? {
    if (!channel.hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
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
    if (!channel.hasPermissionsByMember(Permission.MESSAGE_SEND)) return null
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