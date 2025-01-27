package ovh.eukon05.swiftly.web.dto;

import jakarta.validation.constraints.NotBlank;
import ovh.eukon05.swiftly.annotation.ISO2Code;

public record DeleteRequestDTO(@NotBlank String bankName, @ISO2Code String countryISO2) {
}
