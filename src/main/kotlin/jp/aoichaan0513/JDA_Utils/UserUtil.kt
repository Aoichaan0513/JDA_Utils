package jp.aoichaan0513.JDA_Utils

import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.ClientType
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.utils.MarkdownUtil
import java.util.*

val User.activeClients
    get() = mutualGuilds.firstOrNull()?.getMember(this)?.activeClients ?: EnumSet.noneOf(ClientType::class.java)

val User.activities
    get() = mutualGuilds.firstOrNull()?.getMember(this)?.activities ?: emptyList()

val User.onlineStatus
    get() = mutualGuilds.firstOrNull()?.getMember(this)?.onlineStatus ?: OnlineStatus.UNKNOWN

val User.isOnline
    get() = onlineStatus == OnlineStatus.ONLINE || onlineStatus == OnlineStatus.IDLE || onlineStatus == OnlineStatus.DO_NOT_DISTURB

val User?.tag: String
    get() = getTag()


fun User?.getTag(isBold: Boolean = true, unknownText: String = "Unknown User"): String {
    return if (this != null) "${if (isBold) MarkdownUtil.bold(name) else name}#$discriminator" else unknownText
}