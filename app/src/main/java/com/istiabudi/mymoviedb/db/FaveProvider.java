package com.istiabudi.mymoviedb.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.istiabudi.mymoviedb.db.DatabaseContract.AUTHORITY;

public class FaveProvider extends ContentProvider {

    private static final int FAVE = 1;
    private static final int FAVE_ID = 2;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {

         //content://com.istiabudi.mymoviedb/fave
        uriMatcher.addURI(
                AUTHORITY,
                DatabaseContract.TABLE_NAME,
                FAVE);

        // content://com.istiabudi.mymoviedb/fave/id
        uriMatcher.addURI(AUTHORITY,
                DatabaseContract.TABLE_NAME + "/#",
                FAVE_ID);

    }

    private FaveHelper faveHelper;
    private Context context;


    @Override
    public boolean onCreate() {
        this.faveHelper = new FaveHelper(this.getContext());
        this.faveHelper.open();
        this.context = getContext();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case FAVE:
                cursor = this.faveHelper.queryProvider();
                break;
            case FAVE_ID:
                cursor = this.faveHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        if(cursor != null)
            cursor.setNotificationUri(this.context.getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long numAdded;
        switch (uriMatcher.match(uri)) {
            case FAVE:
                numAdded = this.faveHelper.insertProvider(values);
                break;
            default:
                numAdded = 0;
                break;
        }
        if(numAdded > 0)
            this.context.getContentResolver().notifyChange(uri, null);

        return Uri.parse(DatabaseContract.contentUri() + "/" + numAdded);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numDeleted;
        switch (uriMatcher.match(uri)) {
            case FAVE_ID:
                numDeleted = faveHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                numDeleted = 0;
                break;
        }

        if(numDeleted > 0)
            this.context.getContentResolver().notifyChange(uri, null);

        return numDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int numUpdated;
        switch (uriMatcher.match(uri)) {
            case FAVE_ID:
                numUpdated = faveHelper.updateProvider(uri.getLastPathSegment(), values);
                break;
            default:
                numUpdated = 0;
                break;
        }

        if(numUpdated > 0)
            this.context.getContentResolver().notifyChange(uri, null);

        return numUpdated;

    }
}
