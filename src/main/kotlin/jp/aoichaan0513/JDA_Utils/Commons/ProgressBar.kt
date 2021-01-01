package jp.aoichaan0513.JDA_Utils.Commons

import net.dv8tion.jda.api.utils.MarkdownUtil
import java.text.DecimalFormat
import kotlin.math.ceil

class ProgressBar {
    companion object {

        private val decimalFormat = DecimalFormat("##.#")

        @JvmStatic
        @JvmOverloads
        fun build(
            v1: Double,
            v2: Double,
            progressType: ProgressType = ProgressType.DEFAULT,
            currentStatus: String? = null,
            maxStatus: String? = null,
            progressCount: Int = 40,
            char: Char = '#'
        ): String {
            val percentCount = 100.toDouble() / progressCount

            val percent = v1 / v2 * 100
            val count = ceil(percent / percentCount).toInt()

            val percentRangeText = char.toString().repeat(count.coerceAtLeast(0))
            val blankRangeText = " ".repeat((progressCount - count).coerceIn(0, 40))

            val progressText = "$percentRangeText$blankRangeText"
            val percentText = "${decimalFormat.format(percent.coerceAtLeast(0.0))}%"

            return MarkdownUtil.codeblock(
                progressType.language,
                if (currentStatus != null && maxStatus != null) {
                    """
                        [$currentStatus / $maxStatus]
                        [$progressText] [$percentText]
                    """.trimIndent()
                } else {
                    "[$progressText] [$percentText]"
                }
            )
        }
    }

    enum class ProgressType(val language: String) {
        DEFAULT("kotlin"),
        RED("css"),
        YELLOW("fix"),
        BLUE("ini")
    }
}