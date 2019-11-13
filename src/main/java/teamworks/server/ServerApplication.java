package teamworks.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import teamworks.server.repository.ProductsRepository;
import teamworks.server.repository.StoresRepository;
import teamworks.server.service.DataSearchService;

import java.util.concurrent.Executor;

@SpringBootApplication

public class ServerApplication implements CommandLineRunner {

	@Autowired
	DataSearchService dataSearchService;

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}



	@Override
	public void run(String... args) throws Exception {
//		dataSearchService.search();
	}
}
