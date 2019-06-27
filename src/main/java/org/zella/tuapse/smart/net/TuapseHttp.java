package org.zella.tuapse.smart.net;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zella.tuapse.smart.RunnerMain;
import org.zella.tuapse.smart.model.net.PlayInput;
import org.zella.tuapse.smart.model.net.SearchInput;
import org.zella.tuapse.smart.model.net.TFileWithMeta;
import org.zella.tuapse.smart.utils.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class TuapseHttp {

    private static final Logger logger = LoggerFactory.getLogger(TuapseHttp.class);


    private static final String TuapseOrigin = (System.getenv().get("TUAPSE_ORIGIN"));
    private static final int TuapseTimeout = Integer.parseInt(System.getenv().getOrDefault("TUAPSE_TIMEOUT_SEC", "60"));

    private static final String TuapsePlayOrigin = (System.getenv().get("TUAPSE_PLAY_ORIGIN"));
    private static final int TuapsePlayTimeout = Integer.parseInt(System.getenv().getOrDefault("TUAPSE_PLAY_TIMEOUT_SEC", "60"));

    private AsyncHttpClient client = asyncHttpClient();

    public Single<Integer> play(PlayInput input) {
        return Single.defer(() -> Single.fromFuture(client.preparePost(TuapsePlayOrigin + "/api/v1/play")
                .setBody(Json.mapper.writeValueAsString(input))
                .execute(), TuapsePlayTimeout, TimeUnit.SECONDS, Schedulers.io())
                .map(Response::getStatusCode));
    }

    public Single<SearchResult> searchFile(SearchInput input){

        var params = new HashMap<String, List<String>>();
        params.put("text", List.of(input.text));
        var extsArr = new ArrayList<String>();
        input.extensions.ifPresent(extsArr::addAll);
        if (!extsArr.isEmpty())
            params.put("ext", extsArr);

        return Single.fromFuture(client.prepareGet(TuapseOrigin + "/api/v1/search_file")
                .setQueryParams(params)
                .execute(), TuapseTimeout, TimeUnit.SECONDS, Schedulers.io())
                .map(r -> Json.mapper.readValue(r.getResponseBody(), TFileWithMeta.class))
                .map(t -> new SearchResult(Optional.of(t)))
                .doOnError(e -> logger.error("Http error", e))
                .onErrorReturnItem(new SearchResult(Optional.empty()));
    }

    public static class SearchResult {
        public final Optional<TFileWithMeta> found;

        SearchResult(Optional<TFileWithMeta> found) {
            this.found = found;
        }
    }

}
