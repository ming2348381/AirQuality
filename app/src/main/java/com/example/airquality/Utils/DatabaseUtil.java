package com.example.airquality.Utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.airquality.Model.AirQuality;
import com.example.airquality.View.MainApplication;
import com.example.airquality.annotation.SqliteDataAnnotation;
import com.example.airquality.annotation.SqliteTableAnnotation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DatabaseUtil extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database_name";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseUtil mInstance;

    private Class<?>[] mListClass = new Class<?>[]{AirQuality.class};

    private DatabaseUtil() {
        super(MainApplication.getAppContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseUtil getInstance() {
        if (mInstance == null) {
            synchronized (DatabaseUtil.class) {
                if (mInstance == null) {
                    mInstance = new DatabaseUtil();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        bootstrapDB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        dropTables(sqLiteDatabase);
        bootstrapDB(sqLiteDatabase);
    }

    private static List<String> getSqlStringCreateTable(Class<?>[] mClassList) {
        ArrayList<String> mStringList = new ArrayList<>();
        for (Class<?> mClass : mClassList) {
            SqliteTableAnnotation sqliteTableAnnotation = mClass.getAnnotation(SqliteTableAnnotation.class);
            if (sqliteTableAnnotation != null) {
                List<String> tableNames = new ArrayList<>();
                for (String tableName : sqliteTableAnnotation.tableName()) {
                    if (tableName != null && tableName.isEmpty() == false) {
                        tableNames.add(tableName);
                    }
                }
                tableNames = new ArrayList(new HashSet(tableNames));
                if (tableNames.size() > 0) {
                    String mStringSql = "(";
                    mStringSql = getSqlStringColumn(mStringSql, mClass);
                    if (mStringSql.length() > 1) {
                        mStringSql = mStringSql.substring(0, mStringSql.length() - 1);
                    }
                    mStringSql += ")";
                    if (mStringSql != "()") {
                        for (String tablename : tableNames) {
                            mStringList.add("CREATE TABLE IF NOT EXISTS " + tablename + mStringSql);
                        }
                    }
                }
            }
        }
        return mStringList;
    }

    private static String getSqlStringColumn(String sqlString, Class<?> className) {
        Field[] fields = className.getDeclaredFields();
        for (Field field : fields) {
            SqliteDataAnnotation sqliteDataAnnotation = field.getAnnotation(SqliteDataAnnotation.class);
            if (sqliteDataAnnotation != null) {
                field.setAccessible(true);
                Class<?> type = field.getType();
                String columnName = sqliteDataAnnotation.columnName();
                if (columnName == null || columnName.isEmpty() == true) {
                    columnName = field.getName();
                }
                if (type == String.class) {
                    sqlString += columnName + " TEXT,";
                }
            }
        }
        return sqlString;
    }


    public static <T> T getObjectByCursor(Cursor cursor, Class<T> className) {
        T object = null;
        try {
            object = className.newInstance();
            Field[] fields = className.getDeclaredFields();
            for (Field field : fields) {
                SqliteDataAnnotation sqliteDataAnnotation = field.getAnnotation(SqliteDataAnnotation.class);
                if (sqliteDataAnnotation != null) {
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    String columnName = sqliteDataAnnotation.columnName();
                    if (columnName == null || columnName.isEmpty() == true) {
                        columnName = field.getName();
                    }
                    if (type == String.class) {
                        field.set(object, cursor.getString(cursor.getColumnIndex(columnName)));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    private static <T> void setObjectToDatabase(final ContentValues data, final T obj) {
        Class<?> className = obj.getClass();
        Field[] fields = className.getDeclaredFields();
        for (Field field : fields) {
            try {
                SqliteDataAnnotation sqliteDataAnnotation = field.getAnnotation(SqliteDataAnnotation.class);
                if (sqliteDataAnnotation != null) {
                    field.setAccessible(true);
                    Object columnObj = field.get(obj);
                    String columnName = sqliteDataAnnotation.columnName();
                    if (columnName == null || columnName.isEmpty()) {
                        columnName = field.getName();
                    }
                    if (columnObj == null) {
                        data.putNull(columnName);
                    } else {
                        Class<?> type = field.getType();
                        if (type == String.class) {
                            data.put(columnName, columnObj.toString());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public Cursor getAll(String tableName, String where) {
        return getWritableDatabase().query(tableName, null, where, null, null, null, null);
    }

    public Cursor getAllAndOrder(String tableName, String where, String orderBy) {
        return getWritableDatabase().query(tableName, null, where, null, null, null, orderBy);
    }

    public void update(String tableName, ContentValues values, String where) {
        getWritableDatabase().update(tableName, values, where, null);
    }

    public void delete(String tableName, String where) {
        getWritableDatabase().delete(tableName, where, null);
    }

    private void bootstrapDB(SQLiteDatabase sqLiteDatabase) {
        createCustomTables(sqLiteDatabase);
    }

    private void createCustomTables(SQLiteDatabase sqLiteDatabase) {
        try {
            for (String sqlString : getSqlStringCreateTable(mListClass)) {
                sqLiteDatabase.execSQL(sqlString);
            }
        } catch (Exception e) {
        }
    }

    private void dropTables(SQLiteDatabase sqLiteDatabase) {
        for (Class<?> className : mListClass) {
            SqliteTableAnnotation sqliteTableAnnotation = className.getAnnotation(SqliteTableAnnotation.class);
            if (sqliteTableAnnotation != null) {
                List<String> tableNames = new ArrayList<>();
                for (String tableName : sqliteTableAnnotation.tableName()) {
                    if (tableName != null && tableName.isEmpty() == false) {
                        tableNames.add(tableName);
                    }
                }
                tableNames = new ArrayList(new HashSet(tableNames));
                for (int i = 0; i < tableNames.size(); i++) {
                    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tableNames.get(i));
                }
            }
        }
    }

    public long insert(String tableName, ContentValues value) {
        return getWritableDatabase().insert(tableName, null, value);
    }

    public <T> void setObjectToDatabase(final String tableName, final T obj, final String where) {
        ContentValues data = new ContentValues();
        setObjectToDatabase(data, obj);

        if (where != null) {
            Cursor cursor = getAll(tableName, where);
            if (cursor.moveToNext()) {
                update(tableName, data, where);
                cursor.close();
                return;
            }
            cursor.close();
        }
        insert(tableName, data);
    }
}