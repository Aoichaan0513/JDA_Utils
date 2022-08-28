package jp.aoichaan0513.JDA_Utils

import club.minnced.discord.webhook.WebhookClientBuilder
import club.minnced.discord.webhook.receive.ReadonlyMessage
import club.minnced.discord.webhook.send.AllowedMentions
import club.minnced.discord.webhook.send.WebhookEmbedBuilder
import club.minnced.discord.webhook.send.WebhookMessageBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.Message.MentionType
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.Webhook
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import net.dv8tion.jda.api.utils.messages.MessageEditData


fun Webhook.send(content: String): ReadonlyMessage {
    val webhookClient = WebhookClientBuilder.fromJDA(this).buildJDA()
    val completableFuture = webhookClient.send(content)
    webhookClient.close()
    return completableFuture.get()
}

fun Webhook.send(msg: Message): ReadonlyMessage {
    val webhookClient = WebhookClientBuilder.fromJDA(this).buildJDA()
    val completableFuture = webhookClient.send(msg)
    webhookClient.close()
    return completableFuture.get()
}

fun Webhook.send(embed: MessageEmbed): ReadonlyMessage {
    val webhookClient = WebhookClientBuilder.fromJDA(this).buildJDA()
    val completableFuture = webhookClient.send(embed)
    webhookClient.close()
    return completableFuture.get()
}

fun Webhook.send(collection: Collection<MessageEmbed>): ReadonlyMessage {
    val webhookClient = WebhookClientBuilder.fromJDA(this).buildJDA()
    val completableFuture = webhookClient.send(collection.map { WebhookEmbedBuilder.fromJDA(it).build() })
    webhookClient.close()
    return completableFuture.get()
}

fun Webhook.send(iterable: Iterable<MessageEmbed>) = send(iterable.toList())

fun Webhook.send(vararg embed: MessageEmbed) = send(embed.toList())


fun Webhook.edit(readonlyMessage: ReadonlyMessage, content: String) = edit(readonlyMessage.id, content)

fun Webhook.edit(id: Long, content: String): ReadonlyMessage {
    val webhookClient = WebhookClientBuilder.fromJDA(this).buildJDA()
    val completableFuture = webhookClient.edit(id, content)
    webhookClient.close()
    return completableFuture.get()
}

fun Webhook.edit(readonlyMessage: ReadonlyMessage, msg: Message) = edit(readonlyMessage.id, msg)

fun Webhook.edit(id: Long, msg: Message): ReadonlyMessage {
    val webhookClient = WebhookClientBuilder.fromJDA(this).buildJDA()
    val completableFuture = webhookClient.edit(id, msg)
    webhookClient.close()
    return completableFuture.get()
}

fun Webhook.edit(readonlyMessage: ReadonlyMessage, embed: MessageEmbed) = edit(readonlyMessage.id, embed)

fun Webhook.edit(id: Long, embed: MessageEmbed): ReadonlyMessage {
    val webhookClient = WebhookClientBuilder.fromJDA(this).buildJDA()
    val completableFuture = webhookClient.edit(id, embed)
    webhookClient.close()
    return completableFuture.get()
}

fun Webhook.edit(readonlyMessage: ReadonlyMessage, collection: Collection<MessageEmbed>) =
    edit(readonlyMessage.id, collection)

fun Webhook.edit(id: Long, collection: Collection<MessageEmbed>): ReadonlyMessage {
    val webhookClient = WebhookClientBuilder.fromJDA(this).buildJDA()
    val completableFuture = webhookClient.edit(id, collection.map { WebhookEmbedBuilder.fromJDA(it).build() })
    webhookClient.close()
    return completableFuture.get()
}

fun Webhook.edit(readonlyMessage: ReadonlyMessage, iterable: Iterable<MessageEmbed>) =
    edit(readonlyMessage.id, iterable.toList())

fun Webhook.edit(id: Long, iterable: Iterable<MessageEmbed>) = edit(id, iterable.toList())

fun Webhook.edit(readonlyMessage: ReadonlyMessage, vararg embed: MessageEmbed) =
    edit(readonlyMessage.id, embed.toList())

fun Webhook.edit(id: Long, vararg embed: MessageEmbed) = edit(id, embed.toList())


fun WebhookMessageBuilder.fromJDA(data: MessageCreateData): WebhookMessageBuilder {
    val builder = WebhookMessageBuilder()
    builder.setTTS(data.isTTS)
    builder.setContent(data.content)
    data.files.forEach { builder.addFile(it.name, it.data) }
    data.embeds.forEach { builder.addEmbeds(WebhookEmbedBuilder.fromJDA(it).build()) }

    val allowedMentions = AllowedMentions.none()
    val parse = data.allowedMentions
    allowedMentions.withUsers(data.mentionedUsers)
    allowedMentions.withRoles(data.mentionedRoles)
    allowedMentions.withParseUsers(parse.contains(MentionType.USER))
    allowedMentions.withParseRoles(parse.contains(MentionType.ROLE))
    allowedMentions.withParseEveryone(parse.contains(MentionType.EVERYONE) || parse.contains(MentionType.HERE))
    builder.setAllowedMentions(allowedMentions)

    return builder
}

fun WebhookMessageBuilder.fromJDA(data: MessageEditData): WebhookMessageBuilder {
    val builder = WebhookMessageBuilder()
    builder.setContent(data.content)
    data.files.forEach { builder.addFile(it.name, it.data) }
    data.embeds.forEach { builder.addEmbeds(WebhookEmbedBuilder.fromJDA(it).build()) }

    val allowedMentions = AllowedMentions.none()
    val parse = data.allowedMentions
    allowedMentions.withUsers(data.mentionedUsers)
    allowedMentions.withRoles(data.mentionedRoles)
    allowedMentions.withParseUsers(parse.contains(MentionType.USER))
    allowedMentions.withParseRoles(parse.contains(MentionType.ROLE))
    allowedMentions.withParseEveryone(parse.contains(MentionType.EVERYONE) || parse.contains(MentionType.HERE))
    builder.setAllowedMentions(allowedMentions)

    return builder
}