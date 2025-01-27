package ovh.eukon05.swiftly.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ovh.eukon05.swiftly.util.Message;
import ovh.eukon05.swiftly.web.dto.BankDTO;
import ovh.eukon05.swiftly.web.dto.DeleteRequestDTO;
import ovh.eukon05.swiftly.web.dto.HeadquarterDTO;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class BankIntegrationTests {
    /*
    These tests will have access to the data from Interns_2025_SWIFT_CODES.xlsx,
    and as such, we don't have to populate the db here.
    I will be performing the tests against data sourced from the excel file, for example
    I will check if the number of banks in a country is equal to the number in the excel file
    */

    private static final String BASE_URL = "/v1/swift-codes";
    private static final String SWIFT_CODE_URL = BASE_URL + "/%s";
    private static final String COUNTRY_URL = BASE_URL + "/country/%s";

    private static final String deleteSwift = "AAISALTRXXX";
    private static final DeleteRequestDTO deleteBody = new DeleteRequestDTO("UNITED BANK OF ALBANIA SH.A", "AL");
    private static final String nonexistentSwift = "THISISNOTABANK";

    private static final BankDTO aliorHQ = new BankDTO("ALBPPLPWXXX", "ALIOR BANK SPOLKA AKCYJNA", "LOPUSZANSKA BUSINESS PARK LOPUSZANSKA 38 D WARSZAWA, MAZOWIECKIE, 02-232", "PL", "POLAND");
    private static final BankDTO aliorBranch = new BankDTO("ALBPPLPWCUS", "ALIOR BANK SPOLKA AKCYJNA", "LOPUSZANSKA BUSINESS PARK LOPUSZANSKA 38 D WARSZAWA, MAZOWIECKIE, 02-232", "PL", "POLAND");

    private static final BankDTO newHQ = new BankDTO("REMITLY1XXX", "Remitly Poland", "ul. Pawia 17, Kraków", "PL", "POLAND");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objMapper;

    @Test
    void should_get_branch() throws Exception {
        MvcResult res = mockMvc.perform(get(SWIFT_CODE_URL.formatted(aliorHQ.getSwiftCode())))
                .andExpect(status().isOk())
                .andReturn();

        BankDTO received = objMapper.readValue(res.getResponse().getContentAsString(), BankDTO.class);
        assertEquals(aliorHQ, received);
    }

    @Test
    void should_get_hq() throws Exception {
        MvcResult res = mockMvc.perform(get(SWIFT_CODE_URL.formatted(aliorHQ.getSwiftCode())))
                .andExpect(status().isOk())
                .andReturn();

        HeadquarterDTO hqDTO = objMapper.readValue(res.getResponse().getContentAsString(), HeadquarterDTO.class);

        assertEquals(aliorHQ.getSwiftCode(), hqDTO.getSwiftCode());
        assertEquals(aliorHQ.getBankName(), hqDTO.getBankName());
        assertEquals(aliorHQ.getCountryISO2(), hqDTO.getCountryISO2());
        assertEquals(aliorHQ.getCountryName(), hqDTO.getCountryName());
        assertEquals(aliorHQ.isHeadquarter(), hqDTO.isHeadquarter());
        assertEquals(aliorHQ.getAddress(), hqDTO.getAddress());

        assertEquals(aliorBranch, hqDTO.getBranches().getFirst());
    }

    @Test
    void should_not_get_nonexistent_bank() throws Exception {
        mockMvc.perform(get(SWIFT_CODE_URL.formatted(nonexistentSwift)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(Message.BANK_NOT_FOUND));
    }

    @Test
    void should_get_banks_by_country() throws Exception {
        mockMvc.perform(get(COUNTRY_URL.formatted("PL")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryISO2").value("PL"))
                .andExpect(jsonPath("$.countryName").value("POLAND"))
                .andExpect(jsonPath("$.swiftCodes.length()").value(459));
    }

    @Test
    void should_not_get_banks_by_nonexistent_country() throws Exception {
        mockMvc.perform(get(COUNTRY_URL.formatted("XX")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString(Message.INVALID_ISO2_FORMAT)));
    }

    @Test
    void should_delete_bank() throws Exception {
        mockMvc.perform(delete(SWIFT_CODE_URL.formatted(deleteSwift))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(deleteBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(Message.SUCCESS));

        mockMvc.perform(get(SWIFT_CODE_URL.formatted(deleteSwift)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(Message.BANK_NOT_FOUND));
    }

    @Test
    void should_not_delete_nonexistent_bank() throws Exception {
        mockMvc.perform(delete(SWIFT_CODE_URL.formatted(nonexistentSwift))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(deleteBody)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(Message.BANK_NOT_FOUND));
    }

    @Test
    void should_not_delete_mismatch() throws Exception {
        mockMvc.perform(delete(SWIFT_CODE_URL.formatted(aliorHQ.getSwiftCode()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(deleteBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(Message.DELETE_DATA_MISMATCH));
    }

    @Test
    void should_create_bank() throws Exception {
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objMapper.writeValueAsString(newHQ)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(Message.SUCCESS));

        MvcResult res = mockMvc.perform(get(SWIFT_CODE_URL.formatted(newHQ.getSwiftCode())))
                .andExpect(status().isOk())
                .andReturn();

        BankDTO hqDTO = objMapper.readValue(res.getResponse().getContentAsString(), HeadquarterDTO.class);
        assertEquals(newHQ.getSwiftCode(), hqDTO.getSwiftCode());
        assertEquals(newHQ.getBankName(), hqDTO.getBankName());
        assertEquals(newHQ.getCountryISO2(), hqDTO.getCountryISO2());
        assertEquals(newHQ.getCountryName(), hqDTO.getCountryName());
        assertEquals(newHQ.isHeadquarter(), hqDTO.isHeadquarter());
        assertEquals(newHQ.getAddress(), hqDTO.getAddress());
    }

    @Test
    void should_not_create_bank_with_overlapping_swift() throws Exception {
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objMapper.writeValueAsString(aliorHQ)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(Message.BANK_ALREADY_EXISTS));
    }
}
