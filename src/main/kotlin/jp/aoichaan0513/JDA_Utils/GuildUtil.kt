package jp.aoichaan0513.JDA_Utils

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role

fun Guild.addRolesToMember(member: Member, collection: Collection<Role>) {
    collection.forEach { addRoleToMember(member, it).queue {} }
}

fun Guild.addRolesToMember(member: Member, iterable: Iterable<Role>) {
    addRolesToMember(member, iterable.toSet())
}

fun Guild.addRolesToMember(member: Member, vararg array: Role) {
    addRolesToMember(member, array.toSet())
}

fun Guild.addRolesToMember(id: Long, collection: Collection<Role>) {
    collection.forEach { addRoleToMember(id, it).queue {} }
}

fun Guild.addRolesToMember(id: Long, iterable: Iterable<Role>) {
    addRolesToMember(id, iterable.toSet())
}

fun Guild.addRolesToMember(id: Long, vararg array: Role) {
    addRolesToMember(id, array.toSet())
}

fun Guild.addRolesToMember(id: String, collection: Collection<Role>) {
    collection.forEach { addRoleToMember(id, it).queue {} }
}

fun Guild.addRolesToMember(id: String, iterable: Iterable<Role>) {
    addRolesToMember(id, iterable.toSet())
}

fun Guild.addRolesToMember(id: String, vararg array: Role) {
    addRolesToMember(id, array.toSet())
}


fun Guild.removeRolesFromMember(member: Member, collection: Collection<Role>) {
    collection.forEach { removeRoleFromMember(member, it).queue {} }
}

fun Guild.removeRolesFromMember(member: Member, iterable: Iterable<Role>) {
    removeRolesFromMember(member, iterable.toSet())
}

fun Guild.removeRolesFromMember(member: Member, vararg array: Role) {
    removeRolesFromMember(member, array.toSet())
}

fun Guild.removeRolesFromMember(id: Long, collection: Collection<Role>) {
    collection.forEach { removeRoleFromMember(id, it).queue {} }
}

fun Guild.removeRolesFromMember(id: Long, iterable: Iterable<Role>) {
    removeRolesFromMember(id, iterable.toSet())
}

fun Guild.removeRolesFromMember(id: Long, vararg array: Role) {
    removeRolesFromMember(id, array.toSet())
}

fun Guild.removeRolesFromMember(id: String, collection: Collection<Role>) {
    collection.forEach { removeRoleFromMember(id, it).queue {} }
}

fun Guild.removeRolesFromMember(id: String, iterable: Iterable<Role>) {
    removeRolesFromMember(id, iterable.toSet())
}

fun Guild.removeRolesFromMember(id: String, vararg array: Role) {
    removeRolesFromMember(id, array.toSet())
}