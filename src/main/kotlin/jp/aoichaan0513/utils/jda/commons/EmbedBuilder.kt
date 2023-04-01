package jp.aoichaan0513.utils.jda.commons

import jp.aoichaan0513.utils.jda.StringUtil
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import java.awt.Color
import java.time.*
import java.time.temporal.TemporalAccessor

/**
 * Build new embed.
 *
 * @param builder Builder object
 *
 * @author Aoichaan0513
 */
fun buildEmbed(builder: EmbedBuilder.() -> Unit) = EmbedBuilder().apply(builder)

/**
 * Build new embed from [EmbedBuilder].
 *
 * @param embed [EmbedBuilder] object
 * @param builder Builder object
 *
 * @author Aoichaan0513
 */
fun buildEmbed(embed: EmbedBuilder, builder: EmbedBuilder.() -> Unit) = EmbedBuilder().apply {
    color = embed.color
    author {
        name = embed.author?.name
        url = embed.author?.url
        iconUrl = embed.author?.iconUrl
    }
    title = embed.title
    url = embed.url
    description = embed.description
    fields = embed.fields.map { EmbedBuilder.Field(it.name, it.value, it.inline) }.toMutableList()
    image = embed.image
    thumbnail = embed.thumbnail
    footer {
        text = embed.footer?.text
        iconUrl = embed.footer?.iconUrl
    }
    timestamp = embed.timestamp
}.apply(builder)

/**
 * Build new embed from [MessageEmbed].
 *
 * @param embed [MessageEmbed] object
 * @param builder Builder object
 *
 * @author Aoichaan0513
 */
fun buildEmbed(embed: MessageEmbed, builder: EmbedBuilder.() -> Unit) = EmbedBuilder().apply {
    color = embed.colorRaw
    author {
        name = embed.author?.name
        url = embed.author?.url
        iconUrl = embed.author?.iconUrl
    }
    title = embed.title
    url = embed.url
    description = embed.description
    fields = embed.fields.map { EmbedBuilder.Field(it.name, it.value, it.isInline) }.toMutableList()
    image = embed.image?.url
    thumbnail = embed.thumbnail?.url
    footer {
        text = embed.footer?.text
        iconUrl = embed.footer?.iconUrl
    }
    timestamp = embed.timestamp
}.apply(builder)

@DslContext
class EmbedBuilder {

    companion object {
        const val ZERO_WIDTH_SPACE = StringUtil.ZERO_WIDTH_SPACE.toString()
        var DEFAULT_COLOR = 0x1FFFFFFF
    }

    var color = DEFAULT_COLOR
    var author: Author? = null
    var title: String? = null
    var url: String? = null
    var description: String? = null
    var fields = mutableListOf<Field>()
    var image: String? = null
    var thumbnail: String? = null
    var footer: Footer? = null
    var timestamp: OffsetDateTime? = null

    /**
     * Set color.
     *
     * @param color [Color] (Nullable)
     *
     * @author Aoichaan0513
     */
    @DslContext
    fun color(color: Color?) {
        this.color = color?.rgb ?: DEFAULT_COLOR
    }

    /**
     * Set author.
     *
     * @param builder Builder object
     *
     * @author Aoichaan0513
     */
    @DslContext
    fun author(builder: Author.() -> Unit) {
        author = Author().also(builder)
    }

    /**
     * Set author from [User].
     *
     * @param user [User] object
     * @param name Name lambda (Optional)
     * @param avatarUrl Avatar url lambda (Optional)
     *
     * @author Aoichaan0513
     */
    @DslContext
    fun author(
        user: User,
        name: (User) -> String = { it.name },
        avatarUrl: (User) -> String = { it.effectiveAvatarUrl }
    ) {
        author {
            this.name = name(user)
            this.iconUrl = avatarUrl(user)
        }
    }

    /**
     * Set author from [Member].
     *
     * @param member [Member] object
     * @param name Name lambda (Optional)
     * @param avatarUrl Avatar url lambda (Optional)
     *
     * @author Aoichaan0513
     */
    @DslContext
    fun author(
        member: Member,
        name: (Member) -> String = { it.effectiveName },
        avatarUrl: (Member) -> String = { it.effectiveAvatarUrl }
    ) {
        author {
            this.name = name(member)
            this.iconUrl = avatarUrl(member)
        }
    }

    /**
     * Add field.
     *
     * @param field [Field] object
     *
     * @author Aoichaan0513
     */
    @DslContext
    fun addField(field: Field) {
        fields.add(field)
    }

    /**
     * Add field.
     *
     * @param builder Builder object
     *
     * @author Aoichaan0513
     */
    @DslContext
    fun addField(builder: Field.() -> Unit) {
        addField(Field().also(builder))
    }

    /**
     * Add field.
     *
     * @param index Index number
     * @param field [Field] object
     *
     * @author Aoichaan0513
     */
    @DslContext
    fun addField(index: Int, field: Field) {
        fields.add(index, field)
    }

    /**
     * Add field.
     *
     * @param index Index number
     * @param builder Builder object
     *
     * @author Aoichaan0513
     */
    @DslContext
    fun addField(index: Int, builder: Field.() -> Unit) {
        addField(index, Field().also(builder))
    }

    /**
     * Set footer.
     *
     * @param builder Builder object
     *
     * @author Aoichaan0513
     */
    @DslContext
    fun footer(builder: Footer.() -> Unit) {
        footer = Footer().also(builder)
    }

    /**
     * Set timestamp.
     *
     * @param temporal Datetime object ([TemporalAccessor])
     *
     * @author Aoichaan0513
     */
    @DslContext
    fun timestamp(temporal: TemporalAccessor?) {
        timestamp = when (temporal) {
            null -> null
            is OffsetDateTime -> temporal

            else -> {
                val offset = try {
                    ZoneOffset.from(temporal)
                } catch (ignore: DateTimeException) {
                    ZoneOffset.UTC
                }

                try {
                    val localDateTime = LocalDateTime.from(temporal)
                    OffsetDateTime.of(localDateTime, offset)
                } catch (ignore: DateTimeException) {
                    try {
                        val instant = Instant.from(temporal)
                        OffsetDateTime.ofInstant(instant, offset)
                    } catch (err: DateTimeException) {
                        throw DateTimeException(
                            "Unable to obtain OffsetDateTime from TemporalAccessor: $temporal of type ${temporal.javaClass.name}",
                            err
                        )
                    }
                }
            }
        }
    }


    /**
     * Build embed.
     *
     * @return [net.dv8tion.jda.api.EmbedBuilder]
     *
     * @author Aoichaan0513
     */
    fun build(): net.dv8tion.jda.api.EmbedBuilder {
        val embedBuilder = net.dv8tion.jda.api.EmbedBuilder()
            .setColor(color)
            .setAuthor(author?.name, author?.url, author?.iconUrl)
            .setTitle(title, url)
            .setDescription(description)
            .setImage(image)
            .setThumbnail(thumbnail)
            .setFooter(footer?.text, footer?.iconUrl)
            .setTimestamp(timestamp)
        fields.forEach { embedBuilder.addField(it.name ?: ZERO_WIDTH_SPACE, it.value ?: ZERO_WIDTH_SPACE, it.inline) }
        return embedBuilder
    }

    /**
     * Build embed.
     *
     * @return [MessageEmbed]
     *
     * @author Aoichaan0513
     */
    fun buildEmbed() = build().build()


    @DslContext
    class Author(var name: String? = null, var url: String? = null, var iconUrl: String? = null)

    @DslContext
    class Field(
        var name: String? = ZERO_WIDTH_SPACE,
        var value: String? = ZERO_WIDTH_SPACE,
        var inline: Boolean = false
    )

    @DslContext
    class Footer(var text: String? = null, var iconUrl: String? = null)
}

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPEALIAS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
annotation class DslContext