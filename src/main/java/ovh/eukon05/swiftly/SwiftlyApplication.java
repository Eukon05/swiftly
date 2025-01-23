package ovh.eukon05.swiftly;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import ovh.eukon05.swiftly.excel.ExcelService;

@SpringBootApplication
@RequiredArgsConstructor
public class SwiftlyApplication {
	private final ExcelService excelService;

	public static void main(String[] args) {
		SpringApplication.run(SwiftlyApplication.class, args);
	}

	@EventListener(ContextRefreshedEvent.class)
	public void onApplicationEvent() {
		excelService.parseExcel("src/main/resources/FILE.xlsx");
	}
}
