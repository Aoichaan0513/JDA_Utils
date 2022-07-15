package jp.aoichaan0513.JDA_Utils

import jp.aoichaan0513.Kotlin_Utils.getFormattedDateTime
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.ocpsoft.prettytime.PrettyTime
import java.time.*
import java.util.*

fun DateTime.getElapsedFormat(
    locale: Locale,
    zoneId: ZoneId = DateTimeUtil.DEFAULT_ZONE_ID,
    dateTime: DateTime = DateTime.now()
) =
    withZone(DateTimeZone.forID(zoneId.id)).toGregorianCalendar().toZonedDateTime().getElapsedFormat(
        locale,
        zoneId,
        dateTime.withZone(DateTimeZone.forID(zoneId.id)).toGregorianCalendar().toZonedDateTime(),
    )

fun LocalDate.getElapsedFormat(
    locale: Locale,
    zoneId: ZoneId = DateTimeUtil.DEFAULT_ZONE_ID,
    localDate: LocalDate = LocalDate.now(),
) = atStartOfDay(zoneId).getElapsedFormat(locale, zoneId, localDate.atStartOfDay(zoneId))

fun LocalDateTime.getElapsedFormat(
    locale: Locale,
    zoneId: ZoneId = DateTimeUtil.DEFAULT_ZONE_ID,
    localDateTime: LocalDateTime = LocalDateTime.now()
) = atZone(zoneId).getElapsedFormat(locale, zoneId, localDateTime.atZone(zoneId))

fun OffsetDateTime.getElapsedFormat(
    locale: Locale,
    zoneId: ZoneId = DateTimeUtil.DEFAULT_ZONE_ID,
    offsetDateTime: OffsetDateTime = OffsetDateTime.now()
) = atZoneSameInstant(zoneId).getElapsedFormat(locale, zoneId, offsetDateTime.atZoneSameInstant(zoneId))

fun ZonedDateTime.getElapsedFormat(
    locale: Locale,
    zoneId: ZoneId = DateTimeUtil.DEFAULT_ZONE_ID,
    zonedDateTime: ZonedDateTime = ZonedDateTime.now()
): String {
    val prettyTime = PrettyTime(locale)
    prettyTime.reference = zonedDateTime.withZoneSameInstant(zoneId).toInstant()
    return prettyTime.format(withZoneSameInstant(zoneId))
}


fun DateTime.getFormattedTimestamp(
    locale: Locale,
    zoneId: ZoneId = DateTimeUtil.DEFAULT_ZONE_ID,
    pattern: String = DateTimeUtil.DEFAULT_PATTERN,
    dateTime: DateTime = DateTime.now()
) = "${getFormattedDateTime(zoneId, pattern).bold()} (${getElapsedFormat(locale, zoneId, dateTime)})"

fun LocalDate.getFormattedTimestamp(
    locale: Locale,
    zoneId: ZoneId = DateTimeUtil.DEFAULT_ZONE_ID,
    pattern: String = DateTimeUtil.DEFAULT_PATTERN,
    localDate: LocalDate = LocalDate.now()
) = "${getFormattedDateTime(zoneId, pattern).bold()} (${getElapsedFormat(locale, zoneId, localDate)})"

fun LocalDateTime.getFormattedTimestamp(
    locale: Locale,
    zoneId: ZoneId = DateTimeUtil.DEFAULT_ZONE_ID,
    pattern: String = DateTimeUtil.DEFAULT_PATTERN,
    localDateTime: LocalDateTime = LocalDateTime.now()
) = "${getFormattedDateTime(zoneId, pattern).bold()} (${getElapsedFormat(locale, zoneId, localDateTime)})"

fun OffsetDateTime.getFormattedTimestamp(
    locale: Locale,
    zoneId: ZoneId = DateTimeUtil.DEFAULT_ZONE_ID,
    pattern: String = DateTimeUtil.DEFAULT_PATTERN,
    offsetDateTime: OffsetDateTime = OffsetDateTime.now()
) = "${getFormattedDateTime(zoneId, pattern).bold()} (${getElapsedFormat(locale, zoneId, offsetDateTime)})"

fun ZonedDateTime.getFormattedTimestamp(
    locale: Locale,
    zoneId: ZoneId = DateTimeUtil.DEFAULT_ZONE_ID,
    pattern: String = DateTimeUtil.DEFAULT_PATTERN,
    zonedDateTime: ZonedDateTime = ZonedDateTime.now()
) = "${getFormattedDateTime(zoneId, pattern).bold()} (${getElapsedFormat(locale, zoneId, zonedDateTime)})"

object DateTimeUtil {

    val DEFAULT_ZONE_ID = jp.aoichaan0513.Kotlin_Utils.DateTimeUtil.DEFAULT_ZONE_ID
    var DEFAULT_PATTERN = jp.aoichaan0513.Kotlin_Utils.DateTimeUtil.DEFAULT_PATTERN
}