package teamworks.server.service;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamworks.server.service.Parsing.KRuokaParseService;
import teamworks.server.service.Parsing.KRuokaParseServiceDeprecated;
import teamworks.server.service.Parsing.StoreParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class DataSearchService {

    @Autowired
    private StoreParser service;

//    public Map<String, List<KRuokaParseServiceDeprecated.FoundProductInfo>> search() throws IOException, ExecutionException, InterruptedException {
//        System.out.println("Read file");
//        String storesPage = readPageFromFile("C:\\Users\\User\\Desktop\\ProjectAndroidStudio\\Html pages\\StoresPage.html");
//
//        System.out.println("Parse file");
//        service.parseStoresUrls(storesPage);
//
//        System.out.println("Read file");
//        String categoryPage = readPageFromFile("C:\\Users\\User\\Desktop\\ProjectAndroidStudio\\Html pages\\Kauppasi verkossa – K-Ruoka.html");
//        System.out.println("Parse file");
//        service.parseProductCategories(categoryPage);
//
//        System.out.println("Parse all stores");
////        Map<String, List<KRuokaParseServiceDeprecated.FoundProductInfo>> stores = service.parseAllKRuokaStores();
//
////        for (Map.Entry<String, List<KRuokaParseServiceDeprecated.FoundProductInfo>> entry :stores.entrySet()) {
////            System.out.println(entry.toString());
////        }
//
//        return stores;
//    }

    public void search() {
        Map<String, String> urls = collectUrls();
        Map<String ,Future<String>> pages = new HashMap<>();

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

//        for (Map.Entry<String, Future<String>> page : pages.entrySet()) {
//            while (!page.getValue().isDone()){
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            try {
//                service.parseProducts(Jsoup.parse(page.getValue().get()), page.getKey());
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        }

    }

    private String readPageFromFile(String fileName) throws IOException {
        FileReader file = new FileReader(fileName);
        StringBuilder page = new StringBuilder();
        while(file.ready()) {
            page.append((char) file.read());
        }
        file.close();

        return page.toString();
    }

    private Map<String, String> collectUrls() {
        String storesPage = "";
        String categoriesPage = "";

        try {
            storesPage = readPageFromFile("C:\\Users\\User\\Desktop\\ProjectAndroidStudio\\Html pages\\StoresPage.html");
            categoriesPage = readPageFromFile("C:\\Users\\User\\Desktop\\ProjectAndroidStudio\\Html pages\\Kauppasi verkossa – K-Ruoka.html");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String> storesUrls = service.parseStoresLinks(Jsoup.parse(storesPage));
        List<String> categoriesUrls = service.parseProductCategories(Jsoup.parse(categoriesPage));

        return compileStoreAndCategoriesUrls(storesUrls, categoriesUrls);
    }

    private Map<String, String> compileStoreAndCategoriesUrls(Map<String, String> stores, List<String> categories) {
        // <url, storeName>
        Map<String, String> urls = new HashMap<>();
        for(Map.Entry<String, String> entry : stores.entrySet()) {
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

}
