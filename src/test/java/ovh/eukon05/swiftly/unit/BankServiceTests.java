package ovh.eukon05.swiftly.unit;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ovh.eukon05.swiftly.database.BankEntity;
import ovh.eukon05.swiftly.database.BankRepository;
import ovh.eukon05.swiftly.exception.BankAlreadyExistsException;
import ovh.eukon05.swiftly.exception.BankNotFoundException;
import ovh.eukon05.swiftly.service.BankService;
import ovh.eukon05.swiftly.web.dto.BankDTO;
import ovh.eukon05.swiftly.web.dto.CountryDTO;
import ovh.eukon05.swiftly.web.dto.HeadquarterDTO;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BankServiceTests {
    private static final BankRepository repository = Mockito.mock(BankRepository.class);
    private static final BankService bankService = new BankService(repository);

    private static final BankEntity hqOne = new BankEntity("12345678XXX", "TestBank Polska", "Test Street 31, Warsaw", "PL", "POLAND");
    private static final BankEntity bankOne = new BankEntity("12345678KRA", "TestBank Polska oddział w Krakowie", "Mockito Road 10, Cracow", "PL", "POLAND");
    private static final BankEntity bankTwo = new BankEntity("12345678POZ", "TestBank Polska oddział w Poznaniu", "JUnit Avenue 5", "PL", "POLAND");

    private static final BankDTO newBankDTO = new BankDTO("34567890XXX", "NewBank", "New Street", "DE", "GERMANY");

    @Test
    void should_get_branch(){
        Mockito.when(repository.findById(bankOne.getSwiftCode())).thenReturn(Optional.of(bankOne));
        BankDTO dto = bankService.getBank(bankOne.getSwiftCode());
        assertFalse(dto instanceof HeadquarterDTO);
        assertEquals(bankOne.getSwiftCode(), dto.getSwiftCode());
    }

    @Test
    void should_get_hq(){
        Mockito.when(repository.findById(hqOne.getSwiftCode())).thenReturn(Optional.of(hqOne));
        Mockito.when(repository.findAllBySwiftCodeStartingWithAndHeadquarterFalse(hqOne.getSwiftCode().substring(0, 8))).thenReturn(List.of(bankOne, bankTwo));
        BankDTO result = bankService.getBank(hqOne.getSwiftCode());

        assertInstanceOf(HeadquarterDTO.class, result);
        HeadquarterDTO headquarterDTO = (HeadquarterDTO) result;
        assertEquals(2, headquarterDTO.getBranches().size());
    }

    @Test
    void should_delete_bank(){
        Mockito.when(repository.findById(bankOne.getSwiftCode())).thenReturn(Optional.of(bankOne));
        bankService.deleteBank(bankOne.getSwiftCode());
    }

    @Test
    void should_not_delete_non_existent_bank(){
        Mockito.when(repository.findById(bankOne.getSwiftCode())).thenReturn(Optional.empty());
        assertThrows(BankNotFoundException.class, () -> bankService.deleteBank(bankOne.getSwiftCode()));
    }

    @Test
    void should_create_bank(){
        Mockito.when(repository.existsById(newBankDTO.getSwiftCode())).thenReturn(false);
        bankService.createBank(newBankDTO);
    }

    @Test
    void should_not_create_duplicate_bank(){
        Mockito.when(repository.existsById(newBankDTO.getSwiftCode())).thenReturn(true);
        assertThrows(BankAlreadyExistsException.class, () -> bankService.createBank(newBankDTO));
    }


    // should_not_create_invalid_bank

    @Test
    void should_get_banks_by_country(){
        Mockito.when(repository.findAllByCountryISO2(hqOne.getCountryISO2())).thenReturn(List.of(hqOne, bankOne, bankTwo));
        CountryDTO dto = bankService.getBanksByCountry(hqOne.getCountryISO2());
        assertEquals(hqOne.getCountryISO2(), dto.countryISO2());
        assertEquals(3, dto.swiftCodes().size());
    }

    @Test
    void should_get_no_banks_by_country(){
        Mockito.when(repository.findAllByCountryISO2(hqOne.getCountryISO2())).thenReturn(Collections.emptyList());
        assertEquals(0, bankService.getBanksByCountry(hqOne.getCountryISO2()).swiftCodes().size());
    }
}
