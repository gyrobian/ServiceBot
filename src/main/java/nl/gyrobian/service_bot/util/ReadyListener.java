package nl.gyrobian.service_bot.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nl.gyrobian.service_bot.ServiceBot;
import nl.gyrobian.service_bot.command.Commands;
import nl.gyrobian.service_bot.command.SlashCommandListener;
import nl.gyrobian.service_bot.service.SystemdServiceController;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Slf4j
public class ReadyListener extends ListenerAdapter {
	private final SlashCommandListener slashCommandListener;
	private final ServiceBot serviceBot;

	@Override
	public void onReady(@NotNull ReadyEvent event) {
		var commands = Commands.get(new SystemdServiceController());
		var botTag = this.serviceBot.getProperties().getProperty("tag");
		for (var entry : commands.entrySet()) {
			entry.getKey().setName(entry.getKey().getName() + "-" + botTag);
			this.slashCommandListener.addCommandHandler(entry.getKey().getName(), entry.getValue());
		}
		for (Guild guild : event.getJDA().getGuilds()) {
			guild.updateCommands()
					.addCommands(commands.keySet())
					.queue(commandsResult -> {
						log.info("Initialized slash commands for guild {}.", guild.getName());
					});
		}
	}
}
