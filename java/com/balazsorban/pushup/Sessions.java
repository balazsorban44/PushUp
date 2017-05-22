package com.balazsorban.pushup;

public class Sessions {

    private int _id,
                _pushups;
    private long _date;

    Sessions(int _pushups, long _date) {
        this._pushups = _pushups;
        this._date = _date;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    int get_pushups() {
        return _pushups;
    }

    public void set_pushups(int _pushups) {
        this._pushups = _pushups;
    }

    long get_date() {
        return _date;
    }

    public void set_date(long _date) {
        this._date = _date;
    }
}
