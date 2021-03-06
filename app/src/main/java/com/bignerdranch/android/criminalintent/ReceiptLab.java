package com.bignerdranch.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.criminalintent.database.ReceiptBaseHelper;
import com.bignerdranch.android.criminalintent.database.ReceiptCursorWrapper;
import com.bignerdranch.android.criminalintent.database.ReceiptDbSchema;
import com.bignerdranch.android.criminalintent.database.ReceiptDbSchema.ReceiptTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bignerdranch.android.criminalintent.database.ReceiptDbSchema.ReceiptTable.Cols.*;

public class ReceiptLab {
    private static ReceiptLab sReceiptLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ReceiptLab get(Context context) {
        if (sReceiptLab == null) {
            sReceiptLab = new ReceiptLab(context);
        }

        return sReceiptLab;
    }

    private ReceiptLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ReceiptBaseHelper(mContext)
                .getWritableDatabase();

    }

    public void addReceipt(Receipt c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(ReceiptTable.NAME, null, values);
    }

//    public void deleteReceipt (UUID receiptID)
//    {
//        String uuidString = receiptID.toString();
//        mDatabase.delete(ReceiptTable.NAME, ReceiptTable.Cols.UUID + " = ?", new String[] (uuidString));
//    }

    public int deleteReceipt (UUID receiptID) {
        String uuidString = receiptID.toString();
        return (mDatabase.delete(ReceiptTable.NAME, ReceiptDbSchema.ReceiptTable.Cols.UUID + "= ?", new String[] {uuidString}));
    }

    public List<Receipt> getReceipts() {
        List<Receipt> receipts = new ArrayList<>();
        ReceiptCursorWrapper cursor = queryReceipts(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                receipts.add(cursor.getReceipt());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return receipts;
    }

    public Receipt getReceipt(UUID id) {
        ReceiptCursorWrapper cursor = queryReceipts(
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

    public File getPhotoFile(Receipt receipt) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, receipt.getPhotoFilename());
    }

    public void updateReceipt(Receipt receipt) {
        String uuidString = receipt.getId().toString();
        ContentValues values = getContentValues(receipt);
        mDatabase.update(ReceiptTable.NAME, values,
                ReceiptTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    private ReceiptCursorWrapper queryReceipts(String whereClause, String[] whereArgs) {
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

    private static ContentValues getContentValues(Receipt receipt) {
        ContentValues values = new ContentValues();
        values.put(UUID, receipt.getId().toString());
        values.put(TITLE, receipt.getTitle());
        values.put(SHOP, receipt.getShop());
        values.put(COMMENT, receipt.getComment());
        values.put(DATE, receipt.getDate().getTime());
        values.put(SOLVED, receipt.isSolved() ? 1 : 0);
        values.put(ReceiptTable.Cols.CONTACT, receipt.getContact());

        return values;
    }
}
