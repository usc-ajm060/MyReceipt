package com.bignerdranch.android.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.criminalintent.Receipt;
import com.bignerdranch.android.criminalintent.database.ReceiptDbSchema.ReceiptTable;

import java.util.Date;
import java.util.UUID;

import static com.bignerdranch.android.criminalintent.database.ReceiptDbSchema.ReceiptTable.*;

public class ReceiptCursorWrapper extends CursorWrapper {

    public ReceiptCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Receipt getReceipt() {
        String uuidString = getString(getColumnIndex(Cols.UUID));
        String title = getString(getColumnIndex(Cols.TITLE));
        String shop = getString(getColumnIndex(Cols.SHOP));
        String comment = getString(getColumnIndex(Cols.COMMENT));
        long date = getLong(getColumnIndex(Cols.DATE));
        int isSolved = getInt(getColumnIndex(Cols.SOLVED));
        String contact = getString(getColumnIndex(Cols.CONTACT));

        Receipt receipt = new Receipt(UUID.fromString(uuidString));
        receipt.setTitle(title);
        receipt.setShop(shop);
        receipt.setComment(comment);
        receipt.setDate(new Date(date));
        receipt.setSolved(isSolved != 0);
        receipt.setContact(contact);

        return receipt;
    }
}
