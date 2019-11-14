package teamworks.server.service;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import teamworks.server.service.Parsing.KRuokaParseService;
import teamworks.server.service.Parsing.KRuokaParseServiceDeprecated;
import teamworks.server.service.Parsing.StoreParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
@Profile("search")
public class DataSearchService implements teamworks.server.service.Service {

    @Autowired
    private StoreParser service;

    public void search() throws InterruptedException, ExecutionException, IOException {
        Map<String, String> urls = collectUrls();
        Map<String, Future<String>> pages = new HashMap<>();

        for (Map.Entry<String, String> url : urls.entrySet()) {
            try {
                Future<String> page = StoreParser.downloadPage(url.getKey());

                while (!page.isDone()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                pages.put(url.getValue(), page);
                service.parseProducts(Jsoup.parse(page.get()), url.getValue());
            } catch (IOException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }

    }

    private String readPageFromFile(String fileName) throws IOException {
        FileReader file = new FileReader(fileName);
        StringBuilder page = new StringBuilder();
        while (file.ready()) {
            page.append((char) file.read());
        }
        file.close();

        return page.toString();
    }

    private String readPageFromResource(String resource) throws IOException {
        FileReader file = new FileReader(getClass().getClassLoader().getResource(resource).getFile());
        StringBuilder page = new StringBuilder();
        while (file.ready()) {
            page.append((char) file.read());
        }
        file.close();

        return page.toString();
    }

    private Map<String, String> collectUrls() throws IOException, ExecutionException, InterruptedException {
//        Future<String> storesPage;
//        Future<String> categoriesPage;

        String storesPage;
        String categoriesPage;

        Map<String, String> storesUrls;
        List<String> categoriesUrls;

//        storesPage = StoreParser.downloadPage("https://www.k-ruoka.fi/kauppa/k-citymarket-helsinki-easton?kaikki-kaupat");
//        categoriesPage = StoreParser.downloadPage("https://www.k-ruoka.fi/kauppa/tarjoushaku");

//        while (!storesPage.isDone() && !categoriesPage.isDone()) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        storesPage = readPageFromResource("pages/StoresPage.html");
        categoriesPage = readPageFromResource("pages/CategoryPage.html");


        storesUrls = service.parseStoresLinks(Jsoup.parse(storesPage));
        categoriesUrls = service.parseProductCategories(Jsoup.parse(categoriesPage));

        return compileStoreAndCategoriesUrls(storesUrls, categoriesUrls);
    }

    private Map<String, String> compileStoreAndCategoriesUrls(Map<String, String> stores, List<String> categories) {
        // <url, storeName>
        Map<String, String> urls = new HashMap<>();
        for (Map.Entry<String, String> entry : stores.entrySet()) {
            for (String categoryPath : categories) {
//                System.out.println(entry.getValue() + " : " + categoryPath);
                String[] pathParts = entry.getKey().split("[?]");
                String url = pathParts[0].concat("/tuotehaku/")
                        .concat(categoryPath)
                        .concat("?")
                        .concat(pathParts[1]);
                urls.put(url, entry.getValue());
            }
        }
        return urls;
    }

    @Override
    public void run() {
        try {
            search();
        } catch (InterruptedException | IOException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
