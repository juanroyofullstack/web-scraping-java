package com.scraping;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;

public class WebScraper {
    private final String url;
    private Document doc;
    private Integer statusCode;

    public WebScraper(String url) {
        this.url = url;
        this.doc = null;
        this.statusCode = null;
    }

    public int checkConnection() {
        try {
            System.out.println("Connecting to " + url + "..., Please wait...");
            this.statusCode = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute().statusCode();
            System.out.println("Connection successful to: " + url);
            return this.statusCode;
        } catch(IOException e) {
            System.err.println("Failed to connect to: " + url);
        }

        return -1;
    }

    public void run() {
        try {
            if(this.statusCode == null || this.statusCode != 200) {
                System.err.println("No connection established. Please check the connection first.");
                return;
            }
            this.doc = Jsoup.connect(url).get();
            System.out.println("Scraping data from: " + doc.title());
        } catch(IOException e) {
            System.err.println("Error connecting to " + url);
        }
    }

    public void readAtags() {
        Elements As = doc.select("a[href]");
        for(var a : As) {
            System.out.println("Link: " + a.attr("abs:href") + " Text: " + a.text());
        }
        for (Element a : As) {
            System.out.println("Link text: " + a.text());
            System.out.println("Link href: " + a.absUrl("href"));
        }
    }

    static void main() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce la URL (ej. https://www.mediamarkt.es): ");
        String inputUrl = scanner.nextLine().trim();
        scanner.close();

        if (inputUrl.isEmpty()) {
            System.err.println("URL vacía. Saliendo.");
            return;
        }

        WebScraper scraper = new WebScraper(inputUrl);
        scraper.checkConnection();
        scraper.run();
        scraper.readAtags();
    }
}
