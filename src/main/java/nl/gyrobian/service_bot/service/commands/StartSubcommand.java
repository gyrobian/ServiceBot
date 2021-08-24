package nl.gyrobian.service_bot.service.commands;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageAction;
import nl.gyrobian.service_bot.service.ServiceController;

@RequiredArgsConstructor
public class StartSubcommand extends ServiceSubcommand {
	private final ServiceController serviceController;
	@Override
	protected WebhookMessageAction<?> runCommand(SlashCommandEvent event, String serviceName) throws Exception {
		int exitCode = this.serviceController.start(serviceName);
		if (exitCode == 0) {
			return event.getHook().sendMessage("Started service successfully.");
		} else {
			return event.getHook().sendMessage("Non-zero exit code returned: " + exitCode);
		}
	}
}