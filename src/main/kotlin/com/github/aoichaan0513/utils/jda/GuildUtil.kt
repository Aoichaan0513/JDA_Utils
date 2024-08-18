package com.github.aoichaan0513.utils.jda

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role

fun Guild.addRolesToMember(member: Member, collection: Collection<Role>) =
    modifyMemberRoles(member, collection, null)

fun Guild.addRolesToMember(member: Member, iterable: Iterable<Role>) =
    addRolesToMember(member, iterable.toSet())

fun Guild.addRolesToMember(member: Member, vararg array: Role) =
    addRolesToMember(member, array.toSet())

fun Guild.removeRolesFromMember(member: Member, collection: Collection<Role>) =
    modifyMemberRoles(member, null, collection)

fun Guild.removeRolesFromMember(member: Member, iterable: Iterable<Role>) =
    removeRolesFromMember(member, iterable.toSet())

fun Guild.removeRolesFromMember(member: Member, vararg array: Role) =
    removeRolesFromMember(member, array.toSet())