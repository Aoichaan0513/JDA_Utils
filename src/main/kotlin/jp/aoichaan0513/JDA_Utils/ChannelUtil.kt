package jp.aoichaan0513.JDA_Utils

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.*

val ZERO_WIDTH_SPACE = '\u200E'


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