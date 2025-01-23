package ovh.eukon05.swiftly;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import ovh.eukon05.swiftly.excel.ExcelService;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
@OpenAPIDefinition(info = @Info(title = "Swiftly", description = "An API for managing bank information", version = "1.0"))
public class SwiftlyApplication {
	@Value(value = "${swiftly.excelfile}")
	private String excelFile;

	private final ExcelService excelService;

	public static void main(String[] args) {
		SpringApplication.run(SwiftlyApplication.class, args);
	}

	@EventListener(ContextRefreshedEvent.class)
	public void onApplicationEvent() {
		if(excelFile != null && !excelFile.isBlank())
			excelService.parseExcel(excelFile);
	}
}
