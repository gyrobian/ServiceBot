package nl.gyrobian.service_bot.command;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

public interface SlashCommandHandler {
	ReplyAction handle(SlashCommandEvent event) throws Exception;
}
