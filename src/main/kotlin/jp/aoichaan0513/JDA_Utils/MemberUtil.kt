package jp.aoichaan0513.JDA_Utils

import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role

val Member.isOnline
    get() = onlineStatus == OnlineStatus.ONLINE || onlineStatus == OnlineStatus.IDLE || onlineStatus == OnlineStatus.DO_NOT_DISTURB

val Member.isBot
    get() = user.isBot

val Member.name
    get() = user.name

val Member.discriminator
    get() = user.discriminator

val Member.defaultAvatarId
    get() = user.defaultAvatarId

val Member.defaultAvatarUrl
    get() = user.defaultAvatarUrl

val Member?.tag: String
    get() = getTag()


fun Member.hasRole(role: Role) = hasRole(role.idLong)

fun Member.hasRole(id: String) = id.toLongOrNull() != null && hasRole(id.toLong())

fun Member.hasRole(id: Long) = roles.any { it.idLong == id }

fun Member?.getTag(isBold: Boolean = true, unknownText: String = "Unknown Member"): String {
    return this?.user.getTag(isBold, unknownText)
}