package nl.gyrobian.service_bot.service.commands;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageAction;
import nl.gyrobian.service_bot.service.ServiceController;

import java.time.Instant;

@RequiredArgsConstructor
public class StatusSubcommand extends ServiceSubcommand {
	private final ServiceController serviceController;
	@Override
	protected WebhookMessageAction<?> runCommand(SlashCommandEvent event, String serviceName) throws Exception {
		var s = this.serviceController.status(serviceName);
		EmbedBuilder embedBuilder = new EmbedBuilder()
				.setTimestamp(Instant.now())
				.setTitle("Status of " + serviceName)
				.setDescription("```\n" + s + "\n```");
		return event.getHook().sendMessageEmbeds(embedBuilder.build());
	}
}
