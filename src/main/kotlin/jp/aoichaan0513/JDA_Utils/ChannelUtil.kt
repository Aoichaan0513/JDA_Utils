package jp.aoichaan0513.JDA_Utils

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.IPermissionHolder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.channel.Channel
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.entities.channel.attribute.ICategorizableChannel
import net.dv8tion.jda.api.entities.channel.attribute.IPositionableChannel
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel


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


fun Channel.hasAccessByMember(member: Member? = null) =
    this !is GuildChannel || (member ?: guild.selfMember).hasAccess(this)

fun Channel.hasAccessByRole(role: Role? = null) =
    this !is GuildChannel || (role ?: guild.publicRole).hasAccess(this)


private fun GuildChannel.hasPermissions(holder: IPermissionHolder, permissions: Collection<Permission?>) =
    holder.hasPermission(this, permissions.filterNotNull())

fun Channel.hasPermissionsByMember(member: Member, permissions: Collection<Permission?>) =
    this !is GuildChannel || hasPermissions(member, permissions)

fun Channel.hasPermissionsByMember(member: Member, iterable: Iterable<Permission?>) =
    hasPermissionsByMember(member, iterable.toSet())

fun Channel.hasPermissionsByMember(member: Member, vararg array: Permission?) =
    hasPermissionsByMember(member, array.toSet())

fun Channel.hasPermissionsByMember(permissions: Collection<Permission?>) =
    this !is GuildChannel || hasPermissions(guild.selfMember, permissions)

fun Channel.hasPermissionsByMember(iterable: Iterable<Permission?>) =
    hasPermissionsByMember(iterable.toSet())

fun Channel.hasPermissionsByMember(vararg array: Permission?) =
    hasPermissionsByMember(array.toSet())

fun Channel.hasPermissionsByRole(role: Role, permissions: Collection<Permission?>) =
    this !is GuildChannel || hasPermissions(role, permissions)

fun Channel.hasPermissionsByRole(role: Role, iterable: Iterable<Permission?>) =
    hasPermissionsByRole(role, iterable.toSet())

fun Channel.hasPermissionsByRole(role: Role, vararg array: Permission?) =
    hasPermissionsByRole(role, array.toSet())

fun Channel.hasPermissionsByRole(permissions: Collection<Permission?>) =
    this !is GuildChannel || hasPermissions(guild.publicRole, permissions)

fun Channel.hasPermissionsByRole(iterable: Iterable<Permission?>) =
    hasPermissionsByRole(iterable.toSet())

fun Channel.hasPermissionsByRole(vararg array: Permission?) =
    hasPermissionsByRole(array.toSet())