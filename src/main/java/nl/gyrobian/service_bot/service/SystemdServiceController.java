package nl.gyrobian.service_bot.service;

import java.io.IOException;

public class SystemdServiceController implements ServiceController {
	@Override
	public String status(String service) throws Exception {
		var p = new ProcessBuilder("systemctl", "status", service, "--lines=0").start();
		int exitCode = p.waitFor();
		if (exitCode != 0) throw new IOException("Non-zero exit code from status.");
		return new String(p.getInputStream().readAllBytes());
	}

	@Override
	public int start(String service) throws Exception {
		var p = new ProcessBuilder("systemctl", "start", service).start();
		return p.waitFor();
	}

	@Override
	public int restart(String service) throws Exception {
		var p = new ProcessBuilder("systemctl", "restart", service).start();
		return p.waitFor();
	}

	@Override
	public int stop(String service) throws Exception {
		var p = new ProcessBuilder("systemctl", "stop", service).start();
		return p.waitFor();
	}
}
