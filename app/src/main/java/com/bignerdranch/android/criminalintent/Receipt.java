package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Receipt {

    private UUID mId;
    private String mTitle;
    private String mShop;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;

    public Receipt() {
        this(UUID.randomUUID());
    }

    public Receipt(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {

        return mId;
    }

    public String getTitle() {

        return mTitle;
    }

    public void setTitle(String title) {

        mTitle = title;
    }

    public String getShop() {

        return mShop;
    }

    public void setShop(String shop) {

        mShop = shop;
    }

    public Date getDate() {

        return mDate;
    }

    public void setDate(Date date) {

        mDate = date;
    }

    public boolean isSolved() {

        return mSolved;
    }

    public void setSolved(boolean solved) {

        mSolved = solved;
    }

    public String getSuspect() {

        return mSuspect;
    }

    public void setSuspect(String suspect) {

        mSuspect = suspect;
    }

    public String getPhotoFilename() {

        return "IMG_" + getId().toString() + ".jpg";
    }
}
