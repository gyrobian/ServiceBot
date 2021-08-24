package nl.gyrobian.service_bot.command;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import nl.gyrobian.service_bot.service.ServiceController;
import nl.gyrobian.service_bot.service.commands.RestartSubcommand;
import nl.gyrobian.service_bot.service.commands.StartSubcommand;
import nl.gyrobian.service_bot.service.commands.StatusSubcommand;
import nl.gyrobian.service_bot.service.commands.StopSubcommand;

import java.util.HashMap;
import java.util.Map;

public final class Commands {
	public static Map<CommandData, SlashCommandHandler> get(
			ServiceController serviceController
	) {
		Map<CommandData, SlashCommandHandler> commands = new HashMap<>();

		// Simple utility commands.
		commands.put(
				new CommandData("help", "Show help information for this bot.")
						.addOptions(
								new OptionData(OptionType.STRING, "command", "Get help for the specified command.", false)
										.addChoice("service", "service")
						),
				event -> {
					return event.reply("yes");
				}
		);


		// Service control commands.
		var serviceOption = new OptionData(OptionType.STRING, "service", "The name of a service.", true);
		commands.put(
				new CommandData("service", "Get information and control services.")
						.addSubcommands(
								new SubcommandData("status", "Get the status of a service.").addOptions(serviceOption),
								new SubcommandData("start", "Start a service.").addOptions(serviceOption),
								new SubcommandData("restart", "Restarts the given service.").addOptions(serviceOption),
								new SubcommandData("stop", "Stop a service.").addOptions(serviceOption)
						),
				new DelegatingCommandHandler(Map.of(
						"status", new StatusSubcommand(serviceController),
						"start", new StartSubcommand(serviceController),
						"restart", new RestartSubcommand(serviceController),
						"stop", new StopSubcommand(serviceController)
				))
		);
		return commands;
	}
}
