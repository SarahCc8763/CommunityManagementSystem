package finalProj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class FinalProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinalProjectApplication.class, args);

		log.info(" FinalProject 啟動完成，服務已就緒！可開始接收請求。");
		log.info(" FinalProject 啟動完成，服務已就緒！可開始接收請求。");

	}

}
