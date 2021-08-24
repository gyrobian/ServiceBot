package nl.gyrobian.service_bot.service;

public interface ServiceController {
	String status(String service) throws Exception;
	int start(String service) throws Exception;
	int restart(String service) throws Exception;
	int stop(String service) throws Exception;
}
