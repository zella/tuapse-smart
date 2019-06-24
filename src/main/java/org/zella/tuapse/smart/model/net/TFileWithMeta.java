package org.zella.tuapse.smart.model.net;

public class TFileWithMeta {

    public TFile file;
    public String hash;

    public TFileWithMeta() {
    }

    public TFileWithMeta(TFile file, String hash) {
        this.file = file;
        this.hash = hash;
    }
}
