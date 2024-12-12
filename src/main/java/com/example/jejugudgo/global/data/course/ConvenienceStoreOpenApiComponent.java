package com.example.jejugudgo.global.data.course;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConvenienceStoreOpenApiComponent {
    private static final String BASE_URL = "https://safemap.go.kr/openApiService/data/getConvenienceStoreData.do";
    private static final String FCLTY_CD = "509010";
    private static final int NUM_OF_ROWS = 500;
    @Value("${open.api.key}")
    private static String SERVICE_KEY;

    public static void main(String[] args) {
        int page = 1;
        boolean hasMoreData = true;
        StringBuilder allData = new StringBuilder();

        while (hasMoreData) {
            System.out.println("Fetching data for page " + page + "...");

            try {
                String urlString = BASE_URL + "?pageNo=" + page + "&numOfRows=" + NUM_OF_ROWS + "&Fclty_Cd=" + FCLTY_CD + "&serviceKey=" + SERVICE_KEY + "&type=xml";
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() != 200) {
                    System.out.println("Error: Unable to fetch data. HTTP Response Code: " + conn.getResponseCode());
                    break;
                }

                Scanner scanner = new Scanner(conn.getInputStream(), "UTF-8");
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();

                // Remove BOM if present and trim whitespace
                String rawResponse = response.toString().replaceAll("\uFEFF", "").trim();

                // Extract valid XML content starting with <response>
                int startIndex = rawResponse.indexOf("<response>");
                int endIndex = rawResponse.lastIndexOf("</response>") + "</response>".length();

                if (startIndex == -1 || endIndex == -1) {
                    System.out.println("Error: Invalid XML structure. Skipping page " + page);
                    break;
                }

                String validXml = rawResponse.substring(startIndex, endIndex);

                // Clean malformed XML and replace < > around Korean with ( )
                String cleanedResponse = validXml.replaceAll("&(?!(amp;|lt;|gt;|apos;|quot;))", "&amp;")
                        .replaceAll("<([^>\s]+)/>", "<$1></$1>")
                        .replaceAll("<([가-힣]+)>", "($1)").replaceAll("</[가-힣]+>", "");

                // Save raw response for debugging
                try (BufferedWriter debugWriter = new BufferedWriter(new FileWriter("response_page_" + page + ".xml"))) {
                    debugWriter.write(cleanedResponse);
                }

                // Parse XML response
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                factory.setFeature("http://xml.org/sax/features/validation", false);
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new java.io.ByteArrayInputStream(cleanedResponse.getBytes("UTF-8")));

                NodeList items = doc.getElementsByTagName("item");
                if (items.getLength() == 0) {
                    System.out.println("No more data to fetch.");
                    hasMoreData = false;
                    break;
                }

                for (int i = 0; i < items.getLength(); i++) {
                    try {
                        Node item = items.item(i);
                        String OBJT_ID = getTagValue("OBJT_ID", item);
                        String FCLTY_TY = getTagValue("FCLTY_TY", item);
                        String FCLTY_CD = getTagValue("FCLTY_CD", item);
                        String FCLTY_NM = getTagValue("FCLTY_NM", item);
                        String ADRES = getTagValue("ADRES", item);
                        String RN_ADRES = getTagValue("RN_ADRES", item);
                        String TELNO = getTagValue("TELNO", item);
                        String CTPRVN_CD = getTagValue("CTPRVN_CD", item);
                        String SGG_CD = getTagValue("SGG_CD", item);
                        String EMD_CD = getTagValue("EMD_CD", item);
                        String X = getTagValue("X", item);
                        String Y = getTagValue("Y", item);
                        String DATA_YR = getTagValue("DATA_YR", item);

                        // 제주도 주소만 필터링
                        if (ADRES != null && ADRES.contains("제주")) {
                            allData.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                                    OBJT_ID, FCLTY_TY, FCLTY_CD, FCLTY_NM, ADRES, RN_ADRES, TELNO, CTPRVN_CD, SGG_CD, EMD_CD, X, Y, DATA_YR));
                        }
                    } catch (Exception e) {
                        System.out.println("Skipping malformed item: " + e.getMessage());
                    }
                }

            } catch (Exception e) {
                System.out.println("Error fetching data: " + e.getMessage());
                break;
            }

            page++;
        }

        // Save data to CSV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("convenience_store_data.csv"))) {
            writer.write("OBJT_ID,FCLTY_TY,FCLTY_CD,FCLTY_NM,ADRES,RN_ADRES,TELNO,CTPRVN_CD,SGG_CD,EMD_CD,X,Y,DATA_YR\n");
            writer.write(allData.toString());
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    private static String getTagValue(String tag, Node node) {
        try {
            NodeList children = ((org.w3c.dom.Element) node).getElementsByTagName(tag);
            return children.item(0).getTextContent();
        } catch (Exception e) {
            return "";
        }
    }
}