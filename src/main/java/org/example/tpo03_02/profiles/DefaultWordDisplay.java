package org.example.tpo03_02.profiles;

import org.example.tpo03_02.Entry;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("default")
public class DefaultWordDisplay implements IWordDisplayService {
    @Override
    public void displayWords(List<Entry> entries) {
        System.out.println("\nDefault Word Display");
        entries.forEach(System.out::println);
    }
}
