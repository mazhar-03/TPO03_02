package org.example.tpo03_02.profiles;

import org.example.tpo03_02.Entry;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("lowercase")  // Runs when profile is "lowercase"
public class LowercaseWordDisplay implements IWordDisplayService {
    @Override
    public void displayWords(List<Entry> entries) {
        System.out.println("\nlowercase word display");
        entries.forEach(entry ->
                System.out.println(entry.getPolish().toLowerCase() + " - " +
                        entry.getEnglish().toLowerCase() + " - " +
                        entry.getGerman().toLowerCase()));
    }
}

