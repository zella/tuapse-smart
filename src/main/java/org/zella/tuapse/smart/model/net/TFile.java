package org.zella.tuapse.smart.model.net;

import org.apache.commons.io.FilenameUtils;

public class TFile {

    public String path;
    public long length;

    public TFile() {
    }

    public String fileName() {
        return FilenameUtils.getBaseName(path);
    }

    public static TFile create(int index, String path, long length) {
        var t = new TFile();
        t.path = path;
        t.length = length;
        return t;
    }
}
