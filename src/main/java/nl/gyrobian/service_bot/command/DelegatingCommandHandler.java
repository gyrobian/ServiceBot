package nl.gyrobian.service_bot.command;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DelegatingCommandHandler implements SlashCommandHandler {
	private final Map<String, SlashCommandHandler> subcommands;

	public DelegatingCommandHandler(Map<String, SlashCommandHandler> subcommands) {
		this.subcommands = subcommands;
	}

	public DelegatingCommandHandler() {
		this.subcommands = new HashMap<>();
	}

	protected void addSubcommand(String name, SlashCommandHandler handler) {
		this.subcommands.put(name, handler);
	}

	public Map<String, SlashCommandHandler> getSubcommands() {
		return Collections.unmodifiableMap(this.subcommands);
	}

	@Override
	public ReplyAction handle(SlashCommandEvent event) throws Exception {
		if (event.getSubcommandName() == null) {
			return this.handleNonSubcommand(event);
		}
		var handler = this.getSubcommands().get(event.getSubcommandName());
		if (handler == null) {
			return event.reply("There is no registered handler for the subcommand `" + event.getSubcommandName() + "`.").setEphemeral(true);
		}
		return handler.handle(event);
	}

	protected ReplyAction handleNonSubcommand(SlashCommandEvent event) {
		return event.reply("The command you entered requires that you select a subcommand.").setEphemeral(true);
	}
}
