package org.example.tpo03_02;

import org.example.tpo03_02.profiles.IWordDisplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

@Service
public class FlashcardsController {

    private final EntryService entryService;
    private final IWordDisplayService iWordDisplayService;
    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    public FlashcardsController(EntryService entryService, IWordDisplayService iWordDisplayService) {
        this.entryService = entryService;
        this.iWordDisplayService = iWordDisplayService;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\nMy app");
            System.out.println("1. Add new word");
            System.out.println("2. Show all words (with sorting)");
            System.out.println("3. Show only polish words");
            System.out.println("4. Show only english words");
            System.out.println("5. Show only german words");
            System.out.println("6. Take a quiz");
            System.out.println("7. Search for a word");
            System.out.println("8. Modify a word");
            System.out.println("9. Delete a word by its id");
            System.out.println("10. Delete a word by typing it in any language");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addNewWord();
                case 2 -> showSortedWords();
                case 3 -> showOnlyPolish();
                case 4 -> showOnlyEnglish();
                case 5 -> showOnlyGerman();
                case 6 -> startQuiz();
                case 7 -> searchWord();
                case 8 -> editWord();
                case 9 -> deleteWordById();
                case 10 -> deleteWordByAnyLanguage();
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void addNewWord() {
        System.out.print("Polish: ");
        String polish = scanner.nextLine();
        System.out.print("English: ");
        String english = scanner.nextLine();
        System.out.print("German: ");
        String german = scanner.nextLine();

        Entry newEntry = new Entry(polish, english, german);
        entryService.addEntry(newEntry);
        System.out.println("Word added.");
    }

    private void showSortedWords() {
        System.out.println("Sort by:\n1. Polish\n2. English\n3. German\n4. Id");
        int lang = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Order:\n1. Ascending\n2. Descending");
        int order = scanner.nextInt();
        scanner.nextLine();

        if (entryService.getCount() == 0) {
            System.out.println("No words found.");
            return;
        }

        switch (lang) {
            case 1 -> {
                if (order == 1)
                    entryService.findAllByOrderByPolishAsc();
                else
                    entryService.findAllByOrderByPolishDesc();
            }
            case 2 -> {
                if (order == 1)
                    entryService.findAllByOrderByEnglishAsc();
                else
                    entryService.findAllByOrderByEnglishDesc();
            }
            case 3 -> {
                if (order == 1)
                    entryService.findAllByOrderByGermanAsc();
                else
                    entryService.findAllByOrderByGermanDesc();
            }
            case 4 -> {
                if (order == 1)
                    entryService.sortByIdAsc();
                else
                    entryService.sortByIdDesc();
            }
            default -> System.out.println("Invalid choice.");
        }
    }

    private void startQuiz() {
        List<Entry> entries = entryService.findAll();

        if (entries.isEmpty()) {
            System.out.println("There is no words.");
            return;
        }

        Entry randomEntry = entries.get(new Random().nextInt(entries.size()));

        System.out.println("Write its translations: " + randomEntry.getPolish());
        System.out.print("English: ");
        String engAnswer = scanner.nextLine();
        System.out.print("German: ");
        String gerAnswer = scanner.nextLine();

        if (randomEntry.getEnglish().equalsIgnoreCase(engAnswer) &&
                randomEntry.getGerman().equalsIgnoreCase(gerAnswer)) {
            System.out.println("Correct!");
        } else {
            System.out.println("Incorrect. Correct answers: "
                    + randomEntry.getEnglish() + " / " + randomEntry.getGerman());
        }
    }

    private void searchWord() {
        System.out.print("Enter a word to search in any language: ");
        String word = scanner.nextLine();

        Optional<Entry> result = entryService.findByAnyLanguage(word);
        if (result.isPresent()) {
            System.out.println("Found: " + result.get());
        } else {
            System.out.println("Word not found.");
        }
    }

    private void editWord() {
        System.out.print("Enter the ID of the word to modify: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Entry> original = entryService.findById(id);

        if (original.isEmpty()) {
            System.out.println("Entry not found.");
            return;
        }

        System.out.print("New Polish word: ");
        String polish = scanner.nextLine();
        System.out.print("New English word: ");
        String english = scanner.nextLine();
        System.out.print("New German word: ");
        String german = scanner.nextLine();

        Entry updated = new Entry(id, polish, english, german);
        try {
            entryService.update(updated);
        } catch (EntryNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Entry updated.");
    }

    private void deleteWordById() {
        System.out.print("Enter the ID of the word to delete: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        if (entryService.getAllIds().contains(id)) {
            entryService.deleteById(id);
            System.out.println("Word deleted.");
        } else {
            System.out.println("Word not found.");
        }

    }

    private void deleteWordByAnyLanguage() {
        System.out.println("Write the word: ");
        String word = scanner.nextLine();

        boolean deleted = false;
        for (Entry e : entryService.findAll()) {
            if ((e.getPolish().equals(word) || e.getEnglish().equals(word) || e.getGerman().equals(word))) {
                entryService.deleteByAnyLanguage(word);
                System.out.println("Word deleted: " + word);
                deleted = true;
            }
        }
        if (!deleted) {
            System.out.println("Word not found.");
        }
    }

    private void showOnlyEnglish() {
        if (entryService.getCount() != 0) {
            List<String> englishWords = entryService.findOnlyEnglish();
            List<Entry> fakeEntries = englishWords.stream()
                    .map(e -> new Entry(null, "", e, ""))
                    .toList();

            iWordDisplayService.displayWords(fakeEntries);
        }
        else System.out.println("Word not found.");
    }

    private void showOnlyPolish() {
        if (entryService.getCount() != 0) {
            List<String> polishWords = entryService.findOnlyPolish();
            List<Entry> fakeEntries = polishWords.stream()
                    .map(e -> new Entry(null, e, "", ""))
                    .toList();

            iWordDisplayService.displayWords(fakeEntries);
        }

        else System.out.println("Word not found.");
    }

    private void showOnlyGerman() {
        if (entryService.getCount() != 0) {
            List<String> germanWords = entryService.findOnlyGerman();
            List<Entry> fakeEntries = germanWords.stream()
                    .map(e -> new Entry(null, "",  "", e))
                    .toList();

            iWordDisplayService.displayWords(fakeEntries);
        }
        else System.out.println("Word not found.");
    }
}
