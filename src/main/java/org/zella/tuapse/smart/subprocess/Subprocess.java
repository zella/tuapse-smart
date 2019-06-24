package org.zella.tuapse.smart.subprocess;

import com.github.davidmoten.rx2.Strings;
import com.github.zella.rxprocess2.Exit;
import com.github.zella.rxprocess2.RxNuProcessBuilder;
import io.reactivex.*;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Subprocess {

    private static final String TTSExec = (System.getenv().getOrDefault("TTS_EXEC", "engine/gtts.py"));
    private static final String TTSLang = (System.getenv().getOrDefault("TTS_LANG", "ru"));


    private static final String HotwordExec = (System.getenv().getOrDefault("HOTWORD_EXEC", "engine/hotword.py"));
    private static final Path HotwordCredentials = Paths.get(System.getenv().getOrDefault("HOTWORD_CREDENTIALS", "/hotword/credentials.json"));
    private static final Path HotwordDeviceId = Paths.get(System.getenv().get("HOTWORD_DEVICE_ID"));


    private static final String RecognizeExec = (System.getenv().getOrDefault("RECOGNIZE_EXEC", "engine/recognize.py"));
    private static final String RecognizeKey = (System.getenv().get("RECOGNIZE_KEY"));
    private static final String RecognizeLang = (System.getenv().getOrDefault("RECOGNIZE_LANG", "ru-RU"));


    public static Flowable<String> hotword() {
        return RxNuProcessBuilder
                .fromCommand(List.of("python3", HotwordExec,
                        "--credentials", HotwordCredentials.toString(),
                        "--device-model-id", "'" + HotwordDeviceId + "'"))
                .asStdOut().toFlowable(BackpressureStrategy.BUFFER)
                .compose(src -> Strings.decode(src, Charset.defaultCharset()))
                .compose(src -> Strings.split(src, System.lineSeparator()))
                .filter(s -> s.startsWith("[trigger]"));
    }


    public static Single<String> recognize() {
        return RxNuProcessBuilder
                .fromCommand(List.of("python3", RecognizeExec, RecognizeKey, RecognizeLang))
                .asStdOutSingle()
                .map(String::new)
                .map(s -> Arrays.stream(s.split(System.lineSeparator()))
                        .filter(ss -> ss.startsWith("[recognized]"))
                        .map(sss -> sss.replace("[recognized]", "")).collect(Collectors.joining()));

    }

    public static Completable playGtts(String text) {
        return RxNuProcessBuilder
                .fromCommand(List.of("python3", TTSExec, text, TTSLang))
                .asWaitDone()
                .ignoreElement();


    }

}
