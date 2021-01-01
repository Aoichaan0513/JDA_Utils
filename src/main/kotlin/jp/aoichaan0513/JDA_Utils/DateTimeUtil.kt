package jp.aoichaan0513.JDA_Utils

import jp.aoichaan0513.Kotlin_Utils.getDifference
import jp.aoichaan0513.Kotlin_Utils.getFormattedDateTime
import org.joda.time.DateTime
import java.time.*
import kotlin.math.abs

val DEFAULT_ZONE_ID
    get() = ZoneId.systemDefault()
val DEFAULT_PATTERN = "yyyy/MM/dd HH:mm:ss"

fun DateTime.getFormattedTimestamp(
    zoneId: ZoneId = DEFAULT_ZONE_ID,
    pattern: String = DEFAULT_PATTERN,
    dateTime: DateTime = DateTime.now()
): String {
    val difference = getDifference(dateTime)
    return "${getFormattedDateTime(zoneId, pattern).bold()} (${if (difference < 0) "+" else "-"}${abs(difference)})"
}

fun LocalDate.getFormattedTimestamp(
    zoneId: ZoneId = DEFAULT_ZONE_ID,
    pattern: String = DEFAULT_PATTERN,
    localDate: LocalDate = LocalDate.now()
): String {
    val difference = getDifference(localDate)
    return "${getFormattedDateTime(zoneId, pattern).bold()} (${if (difference < 0) "+" else "-"}${abs(difference)})"
}

fun LocalDateTime.getFormattedTimestamp(
    zoneId: ZoneId = DEFAULT_ZONE_ID,
    pattern: String = DEFAULT_PATTERN,
    localDateTime: LocalDateTime = LocalDateTime.now()
): String {
    val difference = getDifference(localDateTime)
    return "${getFormattedDateTime(zoneId, pattern).bold()} (${if (difference < 0) "+" else "-"}${abs(difference)})"
}

fun OffsetDateTime.getFormattedTimestamp(
    zoneId: ZoneId = DEFAULT_ZONE_ID,
    pattern: String = DEFAULT_PATTERN,
    offsetDateTime: OffsetDateTime = OffsetDateTime.now()
): String {
    val difference = getDifference(offsetDateTime)
    return "${getFormattedDateTime(zoneId, pattern).bold()} (${if (difference < 0) "+" else "-"}${abs(difference)})"
}

fun ZonedDateTime.getFormattedTimestamp(
    zoneId: ZoneId = DEFAULT_ZONE_ID,
    pattern: String = DEFAULT_PATTERN,
    zonedDateTime: ZonedDateTime = ZonedDateTime.now()
): String {
    val difference = getDifference(zonedDateTime)
    return "${getFormattedDateTime(zoneId, pattern).bold()} (${if (difference < 0) "+" else "-"}${abs(difference)})"
}