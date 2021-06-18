package com.e.eleos.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DonateFood implements Parcelable {
    private String username;
    private String phno;
    private String address;
    private double longitude;
    private double latitude;
    private String email;
    private List<String> list;
    private String quantity;
    private String time;
    private Boolean reheat;
    private Boolean pickup;
    public Timestamp deliveredOn;
    public Boolean isdelivered;
    public Timestamp bookedOn;
    public String documentId;


    public DonateFood(){

    }

    protected DonateFood(Parcel in) {
        username = in.readString();
        phno = in.readString();
        address = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        email = in.readString();
        if (in.readByte() == 0x01) {
            list = new ArrayList<String>();
            in.readList(list, String.class.getClassLoader());
        } else {
            list = null;
        }
        quantity = in.readString();
        time = in.readString();
        byte reheatVal = in.readByte();
        reheat = reheatVal == 0x02 ? null : reheatVal != 0x00;
        byte pickupVal = in.readByte();
        pickup = pickupVal == 0x02 ? null : pickupVal != 0x00;
        deliveredOn = (Timestamp) in.readValue(Timestamp.class.getClassLoader());
        byte isdeliveredVal = in.readByte();
        isdelivered = isdeliveredVal == 0x02 ? null : isdeliveredVal != 0x00;
        bookedOn = (Timestamp) in.readValue(Timestamp.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(phno);
        dest.writeString(address);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeString(email);
        if (list == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(list);
        }
        dest.writeString(quantity);
        dest.writeString(time);
        if (reheat == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (reheat ? 0x01 : 0x00));
        }
        if (pickup == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (pickup ? 0x01 : 0x00));
        }
        dest.writeValue(deliveredOn);
        if (isdelivered == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isdelivered ? 0x01 : 0x00));
        }
        dest.writeValue(bookedOn);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DonateFood> CREATOR = new Parcelable.Creator<DonateFood>() {
        @Override
        public DonateFood createFromParcel(Parcel in) {
            return new DonateFood(in);
        }

        @Override
        public DonateFood[] newArray(int size) {
            return new DonateFood[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getReheat() {
        return reheat;
    }

    public void setReheat(Boolean reheat) {
        this.reheat = reheat;
    }

    public Boolean getPickup() {
        return pickup;
    }

    public void setPickup(Boolean pickup) {
        this.pickup = pickup;
    }

    public Timestamp getDeliveredOn() {
        return deliveredOn;
    }

    public void setDeliveredOn(Timestamp deliveredOn) {
        this.deliveredOn = deliveredOn;
    }

    public Boolean getIsdelivered() {
        return isdelivered;
    }

    public void setIsdelivered(Boolean isdelivered) {
        this.isdelivered = isdelivered;
    }

    public Timestamp getBookedOn() {
        return bookedOn;
    }

    public void setBookedOn(Timestamp bookedOn) {
        this.bookedOn = bookedOn;
    }

    public static Creator<DonateFood> getCREATOR() {
        return CREATOR;
    }
}