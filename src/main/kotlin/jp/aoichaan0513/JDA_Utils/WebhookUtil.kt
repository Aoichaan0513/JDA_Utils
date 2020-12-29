package jp.aoichaan0513.YudzukiBot.Utils.Object

import club.minnced.discord.webhook.WebhookClientBuilder
import club.minnced.discord.webhook.receive.ReadonlyMessage
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.Webhook

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

fun Webhook.send(messageEmbed: MessageEmbed): ReadonlyMessage {
    val webhookClient = WebhookClientBuilder.fromJDA(this).buildJDA()
    val completableFuture = webhookClient.send(messageEmbed)
    webhookClient.close()
    return completableFuture.get()
}


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

fun Webhook.edit(readonlyMessage: ReadonlyMessage, messageEmbed: MessageEmbed) = edit(readonlyMessage.id, messageEmbed)

fun Webhook.edit(id: Long, messageEmbed: MessageEmbed): ReadonlyMessage {
    val webhookClient = WebhookClientBuilder.fromJDA(this).buildJDA()
    val completableFuture = webhookClient.edit(id, messageEmbed)
    webhookClient.close()
    return completableFuture.get()
}