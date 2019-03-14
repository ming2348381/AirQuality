package com.example.airquality.Model;

import android.database.Cursor;
import com.example.airquality.Utils.DatabaseUtil;
import com.example.airquality.annotation.SqliteDataAnnotation;
import com.example.airquality.annotation.SqliteTableAnnotation;
import org.jsoup.nodes.Document;


@SqliteTableAnnotation(tableName = DailyQuote.TABLE_NAME)
@NetworkController.ApiRequest(path = "https://tw.appledaily.com/index/dailyquote/")
public class DailyQuote implements DatabaseStorable, DocumentParseable {
    protected static final String TABLE_NAME = "DailyQuote";
    private static final String QUOTE = "Quote";

    @SqliteDataAnnotation(columnName = QUOTE)
    private String Quote;

    @Override
    public void setObjectToDatabase() {
        DatabaseUtil.getInstance().setObjectToDatabase(TABLE_NAME, this, QUOTE + " != NULL AND " + QUOTE + " != '" + getQuote() + "'");
    }

    public static DailyQuote getDailyQuoteForDatabase() {
        DailyQuote dailyQuote = new DailyQuote();
        Cursor cursor = DatabaseUtil.getInstance().getAll(TABLE_NAME, null);
        while (cursor.moveToNext()) {
            dailyQuote = DatabaseUtil.getObjectByCursor(cursor, DailyQuote.class);
        }
        cursor.close();
        return dailyQuote;
    }

    public String getQuote() {
        return Quote;
    }

    @Override
    public void parseDocument(Document document) {
        Quote = document.select("meta[name=description]").first().attr("content");
    }
}
