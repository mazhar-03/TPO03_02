package org.example.tpo03_02;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntryRepository extends CrudRepository<Entry, Long> {
    Optional<Entry> findById(long id);

    @Query("SELECT e.id FROM Entry e")
    List<Long> getAllIds();

    List<Entry> findAllByOrderByPolishAsc();
    List<Entry> findAllByOrderByPolishDesc();
    List<Entry> findAllByOrderByEnglishAsc();
    List<Entry> findAllByOrderByEnglishDesc();
    List<Entry> findAllByOrderByGermanAsc();
    List<Entry> findAllByOrderByGermanDesc();

    @Query("SELECT e.english FROM Entry e")
    List<String> findAllByEnglish();

    @Query("SELECT e.polish FROM Entry e")
    List<String> findAllByPolish();

    @Query("SELECT e.german FROM Entry e")
    List<String> findAllByGerman();

    Optional<Entry> findByEnglish(String english);
    Optional<Entry> findByPolish(String polish);
    Optional<Entry> findByGerman(String german);


    List<Entry> findAllByOrderByIdAsc();
    List<Entry> findAllByOrderByIdDesc();

}
