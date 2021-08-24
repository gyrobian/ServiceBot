package nl.gyrobian.service_bot;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import nl.gyrobian.service_bot.command.SlashCommandListener;
import nl.gyrobian.service_bot.util.ReadyListener;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class ServiceBot {
	private static final Path PROPERTIES_FILE = Path.of("service-bot.properties");

	@Getter
	private final Properties properties;
	@Getter
	private final ScheduledExecutorService scheduledExecutorService;
	@Getter
	private final JDA jda;

	public ServiceBot() throws LoginException, InterruptedException, IOException {
		this.properties = new Properties();
		if (Files.notExists(PROPERTIES_FILE)) {
			this.properties.put("token", "INSERT TOKEN HERE");
			this.properties.put("tag", "TAG");
			this.properties.store(Files.newBufferedWriter(PROPERTIES_FILE), "ServiceBot Properties");
			log.warn("Properties file {} did not exist yet, so it was created with defaults. Edit properties and restart the bot to apply changes.", PROPERTIES_FILE);
		} else {
			this.properties.load(Files.newBufferedReader(PROPERTIES_FILE));
			log.info("Loaded properties from {}.", PROPERTIES_FILE);
		}
		this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		var slashCommandListener = new SlashCommandListener();
		this.jda = JDABuilder.createLight(this.properties.getProperty("token"))
				.setActivity(Activity.watching("services"))
				.addEventListeners(
						slashCommandListener,
						new ReadyListener(slashCommandListener, this)
				)
				.build();
	}

	public static void main(String[] args) {
		try {
			new ServiceBot();
		} catch (LoginException e) {
			log.error("Failed to log in with token.", e);
		} catch (InterruptedException e) {
			log.error("Interrupted while awaiting bot ready state.", e);
		} catch (IOException e) {
			log.error("Could not read or write bot properties.", e);
		}
	}
}
