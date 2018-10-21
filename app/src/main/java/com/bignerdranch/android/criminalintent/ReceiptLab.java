package com.bignerdranch.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.criminalintent.database.ReceiptBaseHelper;
import com.bignerdranch.android.criminalintent.database.ReceiptCursorWrapper;
import com.bignerdranch.android.criminalintent.database.ReceiptDbSchema.ReceiptTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bignerdranch.android.criminalintent.database.ReceiptDbSchema.ReceiptTable.Cols.*;

public class ReceiptLab {
    private static ReceiptLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ReceiptLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new ReceiptLab(context);
        }

        return sCrimeLab;
    }

    private ReceiptLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ReceiptBaseHelper(mContext)
                .getWritableDatabase();

    }

    public void addCrime(Receipt c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(ReceiptTable.NAME, null, values);
    }

    public List<Receipt> getReceipts() {
        List<Receipt> crimes = new ArrayList<>();
        ReceiptCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getReceipt());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }

    public Receipt getReceipt(UUID id) {
        ReceiptCursorWrapper cursor = queryCrimes(
                ReceiptTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getReceipt();
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Receipt crime) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, crime.getPhotoFilename());
    }

    public void updateReceipt(Receipt crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(ReceiptTable.NAME, values,
                ReceiptTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    private ReceiptCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ReceiptTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new ReceiptCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Receipt crime) {
        ContentValues values = new ContentValues();
        values.put(UUID, crime.getId().toString());
        values.put(TITLE, crime.getTitle());
        values.put(SHOP, crime.getShop());
        values.put(DATE, crime.getDate().getTime());
        values.put(SOLVED, crime.isSolved() ? 1 : 0);
        values.put(ReceiptTable.Cols.SUSPECT, crime.getSuspect());

        return values;
    }
}
