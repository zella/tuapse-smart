package org.zella.tuapse.smart.model.net;

public class PlayInput {

    public String hash;
    public String path;
    public String streaming;

    public PlayInput() {
    }

    public PlayInput(String hash, String path, String streaming) {
        this.hash = hash;
        this.path = path;
        this.streaming = streaming;
    }


}
