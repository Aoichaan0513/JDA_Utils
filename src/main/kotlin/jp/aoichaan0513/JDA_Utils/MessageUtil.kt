package jp.aoichaan0513.JDA_Utils

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.MessageReaction
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.entities.emoji.Emoji

private val MessageChannel.hasPermission
    get() = isGuildTextChannel && hasPermissionsByMember(Permission.MESSAGE_MANAGE)


fun Message.removeReaction(reaction: MessageReaction, user: User) {
    if (user.idLong != jda.selfUser.idLong) {
        if (channel.hasPermission)
            reaction.removeReaction(user).queue({}) {}
        return
    }
    reaction.removeReaction(user).queue({}) {}
}

fun Message.removeReactions(emoji: Emoji) {
    if (channel.hasPermission)
        clearReactions(emoji).queue({}) {}
}

fun Message.removeReactions(reaction: MessageReaction) {
    if (channel.hasPermission)
        reaction.clearReactions().queue({}) {}
}

fun Message.removeReactions() {
    if (channel.hasPermission)
        clearReactions().queue({}) {}
}


fun MessageEmbed.toText() = buildString {
    fun insertText(sourceText: String?, targetText: (String) -> String = { it }) = sourceText?.run {
        fun isLength(text: String) = (length + text.length) <= Message.MAX_CONTENT_LENGTH

        val text = targetText(this)
        if (isLength(text))
            append(text)
    }

    insertText(author?.name) { "${it.quote()}\n" }
    insertText(title) { "${it.bold().quote()}\n" }
    insertText(description) { "$it\n" }
    fields.filter { !it.name.isNullOrEmpty() || !it.value.isNullOrEmpty() }.forEach { field ->
        insertText("${field.name?.let { "${it.bold()} ― " }}${field.value}") { "$it\n" }
    }
    insertText(footer?.text) { it.quote() }
}.trim()