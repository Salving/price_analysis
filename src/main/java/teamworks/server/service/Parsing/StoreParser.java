package teamworks.server.service.Parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import teamworks.server.domain.Store;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Service
public interface StoreParser {

    @Async("taskExecutor")
    static Future<String> downloadPage(String url) throws IOException {
        System.out.println(Thread.currentThread().toString() + " Downloading page " + url);
        return new AsyncResult<>(Jsoup.connect(url).userAgent("Mozilla").timeout(60000).get().html());
    }

    @Async
    void parseProducts(Document doc, String store);

    Map<String, String> parseStoresLinks(Document doc);

    List<String> parseProductCategories(Document doc);
}
