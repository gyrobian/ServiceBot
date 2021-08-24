package nl.gyrobian.service_bot.command;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SlashCommandListener extends ListenerAdapter {
	private final Map<String, SlashCommandHandler> commandHandlers;

	public SlashCommandListener() {
		this.commandHandlers = new ConcurrentHashMap<>();
	}

	public void addCommandHandler(String command, SlashCommandHandler handler) {
		this.commandHandlers.put(command, handler);
	}

	@Override
	public void onSlashCommand(@Nonnull SlashCommandEvent event) {
		var handler = this.commandHandlers.get(event.getName());
		if (handler != null) {
			try {
				handler.handle(event).queue();
			} catch (Exception e) {
				e.printStackTrace();
				event.reply("An error occurred while handling the command: " + e.getMessage()).setEphemeral(true).queue();
			}
		} else {
			event.reply("The command you invoked does not have an associated handler.").setEphemeral(true).queue();
		}
	}
}
