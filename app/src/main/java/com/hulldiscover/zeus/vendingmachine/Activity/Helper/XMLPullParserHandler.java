package com.hulldiscover.zeus.vendingmachine.Activity.Helper;

import com.hulldiscover.zeus.vendingmachine.Activity.Model.VendingItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zeus on 07/05/16.
 */
public class XMLPullParserHandler {

    List<VendingItem> vendingItemsStock;

    private VendingItem vendingItem;
    private String text;

    public XMLPullParserHandler () {
        vendingItemsStock = new ArrayList<VendingItem>();
    }

    public List<VendingItem> getVendingItemsStock() {
        return vendingItemsStock;
    }

    public List<VendingItem> parse(InputStream inputStream) {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;

        try {
            factory  = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            parser = factory.newPullParser();
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase("item")) {
                            vendingItem = new VendingItem();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase("item")) {
                            vendingItemsStock.add(vendingItem);
                        }
                        else if (tagName.equalsIgnoreCase("id")) {
                            vendingItem.setDescription(text);
                        }
                        else if (tagName.equalsIgnoreCase("item_img")) {
                            vendingItem.setImage(text);
                        }
                        else if (tagName.equalsIgnoreCase("price")) {
                            vendingItem.setPrice(text);
                        }
                        else if (tagName.equalsIgnoreCase("quantity")) {
                            vendingItem.setQauntity(Integer.parseInt(text));
                        }
                        break;

                    default:
                        break;
                }
                    eventType = parser.next();

                }
        } catch (Exception e){
            e.printStackTrace();
        }

        return vendingItemsStock;
    }

}
