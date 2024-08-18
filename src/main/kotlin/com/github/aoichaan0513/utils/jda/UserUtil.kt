package com.github.aoichaan0513.utils.jda

import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.ClientType
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User
import java.util.*

val User.activeClients: EnumSet<ClientType>
    get() = member?.activeClients ?: EnumSet.noneOf(ClientType::class.java)

val User.activities
    get() = member?.activities ?: emptyList()

val User.onlineStatus
    get() = member?.onlineStatus ?: OnlineStatus.UNKNOWN

val User.isOnline
    get() = onlineStatus == OnlineStatus.ONLINE || onlineStatus == OnlineStatus.IDLE || onlineStatus == OnlineStatus.DO_NOT_DISTURB

val User?.tag: String
    get() = getTag()


private val User.member: Member?
    get() = mutualGuilds.firstOrNull()?.getMember(this)


fun User?.getTag(isBold: Boolean = true, unknownText: String = "Unknown User") =
    this?.let { "${if (isBold) name.bold() else name}#$discriminator" } ?: unknownText