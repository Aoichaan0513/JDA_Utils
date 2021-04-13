package jp.aoichaan0513.JDA_Utils

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.*

private val MessageChannel.hasPermission
    get() = isGuildTextChannel && hasPermissions(Permission.MESSAGE_MANAGE)


fun Message.removeReaction(reaction: MessageReaction, user: User) {
    if (user.idLong != jda.selfUser.idLong) {
        if (channel.hasPermission)
            reaction.removeReaction(user).queue({}) {}
        return
    }
    reaction.removeReaction(user).queue({}) {}
}

fun Message.removeReactions(str: String) {
    if (channel.hasPermission)
        clearReactions(str).queue({}) {}
}

fun Message.removeReactions(emote: Emote) {
    if (channel.hasPermission)
        clearReactions(emote).queue({}) {}
}

fun Message.removeReactions(reaction: MessageReaction) {
    if (channel.hasPermission)
        reaction.clearReactions().queue({}) {}
}

fun Message.removeReactions() {
    if (channel.hasPermission)
        clearReactions().queue({}) {}
}