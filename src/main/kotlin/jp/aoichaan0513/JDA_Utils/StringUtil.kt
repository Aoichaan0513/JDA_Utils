package jp.aoichaan0513.YudzukiBot.Utils.Object

import net.dv8tion.jda.api.utils.MarkdownUtil

fun String.bold() = MarkdownUtil.bold(this)
fun String.italic() = MarkdownUtil.italics(this)
fun String.underline() = MarkdownUtil.underline(this)
fun String.strikethrough() = MarkdownUtil.strike(this)
fun String.monospace() = MarkdownUtil.monospace(this)
fun String.codeblock(language: String? = null) = MarkdownUtil.codeblock(language, this)
fun String.quote() = MarkdownUtil.quote(this)
fun String.quoteBlock() = MarkdownUtil.quoteBlock(this)
fun String.spoiler() = MarkdownUtil.spoiler(this)
fun String.link(url: String) = MarkdownUtil.maskedLink(this, url)


fun fixCase(str: String) = buildString {
    for (i in str.indices) {
        val char = str[i]
        when {
            char == '_' -> append(" ")
            i == 0 || str[i - 1] == '_' -> append(char.toUpperCase())
            else -> append(char.toLowerCase())
        }
    }
}