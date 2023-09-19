package com.example.myrecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Item> list = new ArrayList<>();
    List<String> listOfItemName = new ArrayList<>();
    List<Integer> listOfItemId = new ArrayList<>();

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parseXML();
        recyclerView = findViewById(R.id.recyclerview);
        MyAdapter adapter = new MyAdapter(listOfItemName,listOfItemId);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1)) ;
    }
    private void parseXML() {
        XmlPullParserFactory parserFactory;
        try {
            XmlPullParser parser = getResources().getXml(R.xml.data);
            processParsing(parser);


        } catch (XmlPullParserException | IOException ignored) {
        }
    }
    private void processParsing(XmlPullParser parser) throws IOException,XmlPullParserException{
        int eventType = parser.getEventType();
        Item currentItem = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String eltName = null;

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();

                    if ("item".equals(eltName)) {
                        currentItem = new Item();
                        list.add(currentItem);
                    } else if (currentItem != null) {
                        if ("image".equals(eltName)) {
                            currentItem.setImage(parser.nextText());
                            listOfItemId.add(getImageResourceId(this,currentItem.getImage()));
                        } else if ("text".equals(eltName)) {
                            currentItem.setName(parser.nextText());
                            listOfItemName.add(currentItem.getName());
                        }
                    }
                    break;
            }
            eventType = parser.next();
        }
    }
    private static int getImageResourceId(Context context, String imageFileName) {
        // Chuyển đổi tên tệp hình ảnh thành id hình ảnh
        int imageResourceId = context.getResources().getIdentifier(
                imageFileName, "drawable", context.getPackageName());
        return imageResourceId;
    }
}