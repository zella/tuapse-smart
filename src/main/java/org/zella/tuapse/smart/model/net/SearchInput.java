package org.zella.tuapse.smart.model.net;

import java.util.List;
import java.util.Optional;

public class SearchInput {

    public String text;
    public Optional<List<String>> extensions;

    public SearchInput(String text, Optional<List<String>> extensions) {
        this.text = text;
        this.extensions = extensions;
    }

}