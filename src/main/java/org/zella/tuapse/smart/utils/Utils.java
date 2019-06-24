package org.zella.tuapse.smart.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Utils {
    public static int recursiveDeleteFilesAcessedOlderThanNDays(int days, Path dirPath) throws IOException {
        AtomicInteger count = new AtomicInteger(0);
        long cutOff = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000);
        Files.list(dirPath)
                .forEach(path -> {
                    if (Files.isDirectory(path)) {
                        try {
                            recursiveDeleteFilesAcessedOlderThanNDays(days, path);
                        } catch (IOException e) {
                            // log here and move on
                        }
                    } else {
                        try {
                            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
                            FileTime time = attrs.lastAccessTime();

                            if (time.toMillis() < cutOff) {
                                Files.delete(path);
                                count.incrementAndGet();
                            }
                        } catch (IOException ex) {
                            // log here and move on
                        }
                    }
                });
        return count.get();
    }

    /**
     *
     * @param textRaw
     * @return lower case words
     */
    public static List<String> wordsLowerCase(String textRaw) {
        String text = textRaw.toLowerCase().trim();
        List<String> words = new ArrayList<>();
        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(text);
        int lastIndex = breakIterator.first();
        while (BreakIterator.DONE != lastIndex) {
            int firstIndex = lastIndex;
            lastIndex = breakIterator.next();
            if (lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(text.charAt(firstIndex))) {
                words.add(text.substring(firstIndex, lastIndex));
            }
        }
        return words;
    }
}
