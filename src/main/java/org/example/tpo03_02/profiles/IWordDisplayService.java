package org.example.tpo03_02.profiles;

import org.example.tpo03_02.Entry;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IWordDisplayService {
    void displayWords(List<Entry> entries);
}
