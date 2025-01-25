package ovh.eukon05.swiftly.web.dto;

import java.util.List;

public record CountryDTO(String countryISO2, String countryName, List<BankDTO> swiftCodes) {
}
