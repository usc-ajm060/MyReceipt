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

    public Receipt getCrime() {
        String uuidString = getString(getColumnIndex(Cols.UUID));
        String title = getString(getColumnIndex(Cols.TITLE));
        long date = getLong(getColumnIndex(Cols.DATE));
        int isSolved = getInt(getColumnIndex(Cols.SOLVED));
        String suspect = getString(getColumnIndex(ReceiptTable.Cols.SUSPECT));

        Receipt crime = new Receipt(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);
        crime.setSuspect(suspect);

        return crime;
    }
}
