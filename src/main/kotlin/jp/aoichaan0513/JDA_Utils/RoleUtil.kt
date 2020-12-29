package jp.aoichaan0513.YudzukiBot.Utils.Object

import net.dv8tion.jda.api.entities.Role

val Role.members
    get() = guild.getMembersWithRoles(this).toSet()