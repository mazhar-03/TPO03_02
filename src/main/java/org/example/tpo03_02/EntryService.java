package org.example.tpo03_02;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntryService {
    private final  EntryRepository entryRepository;

    @Autowired
    public EntryService(final EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    public void loadInitialDataIfEmpty() {
        if (entryRepository.count() == 0) {
            entryRepository.save(new Entry("Dzien", "Day", "Tag"));
            entryRepository.save(new Entry("Niebiski", "Blue", "Blau"));

            System.out.println("Initial data inserted.");
        } else {
            System.out.println("DB already contains data, no inserting.");
        }
    }

    public void addEntry(Entry entry) {
        entryRepository.save(entry);
    }

    public Optional<Entry> findById(long id) {
        return entryRepository.findById(id);
    }

    public Optional<Entry> findByAnyLanguage(String anyLanguageWord) {
        return entryRepository.findByGerman(anyLanguageWord)
                .or(() -> entryRepository.findByEnglish(anyLanguageWord))
                .or(() -> entryRepository.findByPolish(anyLanguageWord));
    }
    public void deleteById(long id) {
        entryRepository.deleteById(id);
    }

    public List<Entry> findAll() {
        return (List<Entry>) entryRepository.findAll();
    }

    public Entry update(Entry entry) throws EntryNotFoundException {
        Entry dbEntry = entryRepository.findById(entry.getId())
                .orElseThrow(() -> new EntryNotFoundException("Entry not found with id: " + entry.getId()));

        dbEntry.setPolish(entry.getPolish());
        dbEntry.setEnglish(entry.getEnglish());
        dbEntry.setGerman(entry.getGerman());

        return entryRepository.save(dbEntry);
    }

    public void deleteByAnyLanguage(String anyLanguageWord) {
        entryRepository.delete(findByAnyLanguage(anyLanguageWord).orElse(null));
    }

    public List<Long> getAllIds() {
        return entryRepository.getAllIds();
    }

    public List<String> findOnlyEnglish(){
        return entryRepository.findAllByEnglish();
    }
    public List<String> findOnlyPolish(){
        return entryRepository.findAllByPolish();
    }
    public List<String> findOnlyGerman(){
        return entryRepository.findAllByGerman();
    }
    public void sortByIdAsc() {
        entryRepository.findAllByOrderByIdAsc().forEach(System.out::println);
    }
    public void sortByIdDesc() {
        entryRepository.findAllByOrderByIdDesc().forEach(System.out::println);
    }
    public void findAllByOrderByPolishAsc() {
        entryRepository.findAllByOrderByPolishAsc().forEach(System.out::println);
    }
    public void findAllByOrderByPolishDesc() {
        entryRepository.findAllByOrderByPolishDesc().forEach(System.out::println);
    }
    public void findAllByOrderByEnglishAsc() {
        entryRepository.findAllByOrderByEnglishAsc().forEach(System.out::println);
    }

    public void findAllByOrderByEnglishDesc() {
        entryRepository.findAllByOrderByEnglishDesc().forEach(System.out::println);
    }

    public void findAllByOrderByGermanAsc() {
        entryRepository.findAllByOrderByGermanAsc().forEach(System.out::println);
    }

    public void findAllByOrderByGermanDesc() {
        entryRepository.findAllByOrderByGermanDesc().forEach(System.out::println);
    }
    public long getCount(){
        return entryRepository.count();
    }
}