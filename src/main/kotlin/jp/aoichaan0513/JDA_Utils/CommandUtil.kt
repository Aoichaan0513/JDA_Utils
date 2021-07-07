package jp.aoichaan0513.JDA_Utils

import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.api.interactions.commands.OptionType

val OptionMapping.asInteger: Int
    get() {
        check(type == OptionType.INTEGER) { "Cannot convert option of type $type to integer" }
        return asString.toInt()
    }