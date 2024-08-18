package com.github.aoichaan0513.utils.jda

import net.dv8tion.jda.api.entities.Role

val Role.members
    get() = guild.getMembersWithRoles(this).toSet()