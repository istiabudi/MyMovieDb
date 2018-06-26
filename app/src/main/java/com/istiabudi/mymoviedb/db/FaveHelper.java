package com.istiabudi.mymoviedb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static com.istiabudi.mymoviedb.db.DatabaseContract.TABLE_NAME;

public class FaveHelper {
    private static String DATABASE_TABLE = TABLE_NAME;
    private Context context;
    private DatabaseHelper databaseHelper;

    private SQLiteDatabase database;

    public FaveHelper(Context context) {
        this.context = context;
    }

    public void open() throws SQLException {
        this.databaseHelper = new DatabaseHelper(this.context);
        this.database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        this.databaseHelper.close();
    }




    public Cursor queryByIdProvider(String id) {
        return this.database.query(DATABASE_TABLE, null,
                DatabaseContract.FaveColumns.TMDBID + " = ?",
                new String[] {id},
                null,
                null,
                null,
                null
        );
    }

    public Cursor queryProvider() {
        return this.database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                DatabaseContract.FaveColumns.ID + " DESC"
        );
    }

    public long insertProvider(ContentValues values) {
        return this.database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return this.database.update(DATABASE_TABLE,
                values,
                DatabaseContract.FaveColumns.TMDBID + " = ?",
                new String[] {id});
    }

    public int deleteProvider(String id) {
        return this.database.delete(DATABASE_TABLE,
                DatabaseContract.FaveColumns.TMDBID + " = ?",
                new String[]{id});
    }
}
