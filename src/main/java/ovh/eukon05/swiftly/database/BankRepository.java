package ovh.eukon05.swiftly.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankRepository extends JpaRepository<BankEntity, String> {
    List<BankEntity> findAllBySwiftCodeStartingWithAndHeadquarterFalse(String swiftStart);
    List<BankEntity> findAllByCountryISO2(String countryISO2);
}
