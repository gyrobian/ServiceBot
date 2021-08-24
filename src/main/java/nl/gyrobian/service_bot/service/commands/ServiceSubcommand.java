package nl.gyrobian.service_bot.service.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import nl.gyrobian.service_bot.command.SlashCommandHandler;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Abstract subcommand which is used for any service command that is invoked
 * on a single service.
 */
public abstract class ServiceSubcommand implements SlashCommandHandler {
	@Override
	public ReplyAction handle(SlashCommandEvent event) {
		String serviceName = Objects.requireNonNull(event.getOption("service")).getAsString();
		CompletableFuture.runAsync(() -> {
			try {
				this.runCommand(event, serviceName).queue();
			} catch (Exception e) {
				e.printStackTrace();
				event.getHook().sendMessage("An error occurred: " + e.getMessage()).queue();
			}
		});
		return event.deferReply();
	}

	protected abstract WebhookMessageAction<?> runCommand(SlashCommandEvent event, String serviceName) throws Exception;
}
