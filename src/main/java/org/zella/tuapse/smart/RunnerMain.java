package org.zella.tuapse.smart;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zella.tuapse.smart.commands.CommandDef;
import org.zella.tuapse.smart.model.net.PlayInput;
import org.zella.tuapse.smart.model.net.SearchInput;
import org.zella.tuapse.smart.net.TuapseHttp;
import org.zella.tuapse.smart.subprocess.Subprocess;
import scala.compat.java8.OptionConverters;
import scala.compat.java8.OptionConverters.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class RunnerMain {

    private static final Logger logger = LoggerFactory.getLogger(RunnerMain.class);

    private static List<String> SupportedExtensions = Arrays.asList((System.getenv().getOrDefault("PLAY_EXT", "mp3,flac")).split(","));

    private static final String StreamingDevice = (System.getenv().get("STREAMING_DEVICE"));

    private static final String AnswerNotFound = (System.getenv().get("ANSWER_NOT_FOUND"));

    private static final String AnswerStartPlaying = (System.getenv().get("ANSWER_PLAYING"));

    public static void main(String[] args) {
        logger.info("Starting hotword detection...");

        var tuapseHttp = new TuapseHttp();

        //TODO check threads
        Subprocess.hotword().subscribeOn(Schedulers.io())
                .doOnNext(event -> logger.info("Hotword detected"))
                .flatMapSingle(event -> Subprocess.recognize().subscribeOn(Schedulers.io()).doOnSuccess(text -> logger.info("Recognized:" + text)))
                //parse command
                .flatMap(text -> OptionConverters.toJava(CommandDef.extractMediaSubject(text)).map(Flowable::just).orElse(Flowable.empty()))
                .flatMapCompletable(media ->
                        tuapseHttp.searchFile(new SearchInput(media.text(), Optional.of(SupportedExtensions)))
                                .flatMapCompletable(searchResult -> searchResult.found
                                        .map(t -> {
                                            logger.info("Found: " + t.file.path);
                                            return tuapseHttp.play(new PlayInput(t.hash, t.file.path, StreamingDevice))
                                                    .flatMapCompletable(statusCode -> Subprocess.playGtts(AnswerStartPlaying + " " + t.file.fileName()));
                                        })
                                        .orElse(Subprocess.playGtts(AnswerNotFound)))
                )
                .retryWhen(throwables -> throwables.delay(2, TimeUnit.SECONDS))
                .subscribeOn(Schedulers.io())
                .subscribe();


    }

}
