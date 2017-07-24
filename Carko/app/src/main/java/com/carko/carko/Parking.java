package com.carko.carko;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

class Parking implements Parcelable {

    private String TAG = Parking.class.getSimpleName();

    private int id;
    private int customerId;
    private LatLng latLng;
    private String address;
    private String description;
    private String photoUrl;
    private ArrayList<String> photoUrls;
    private float price;
    private float totalRevenue;
    private AvailabilityInfo availabilityInfo;
    private boolean isAvailable;
    private boolean isComplete;
    private boolean isDeleted;

    Parking(JSONObject jsonObject) {
        id = JSONHelper.INSTANCE.getInt(jsonObject, "id");
        customerId = JSONHelper.INSTANCE.getInt(jsonObject,"customer_id");
        latLng = new LatLng(
                JSONHelper.INSTANCE.getDouble(jsonObject,"latitude"),
                JSONHelper.INSTANCE.getDouble(jsonObject,"longitude"));
        address = JSONHelper.INSTANCE.getString(jsonObject,"address");
        description = JSONHelper.INSTANCE.getString(jsonObject, "description");
        price = JSONHelper.INSTANCE.getFloat(jsonObject, "price");
        totalRevenue = JSONHelper.INSTANCE.getFloat(jsonObject, "total_revenue");
        availabilityInfo = new AvailabilityInfo(
                JSONHelper.INSTANCE.getJSONObject(jsonObject, "availability_info"));
        isAvailable = JSONHelper.INSTANCE.getBoolean(jsonObject, "is_available");
        isComplete = JSONHelper.INSTANCE.getBoolean(jsonObject, "is_complete");
        isDeleted = JSONHelper.INSTANCE.getBoolean(jsonObject, "is_deleted");
        photoUrl = JSONHelper.INSTANCE.getString(jsonObject, "photo_url");
        photoUrls = JSONHelper.INSTANCE.getArray(jsonObject, "multiple_photo_urls");
    }

    long getId(){ return id; }
    LatLng getLatLng(){ return latLng; }
    String getAvailability() { return ""; }
    String getAddress() { return address; }
    String getDescription() { return description; }
    int getDrawable() {
        return 0;
    }
    float getPrice() { return price; }
    String getPhotoUrl() { return photoUrl; }
    ArrayList<String> getPhotoUrls() { return photoUrls; }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags){
        int[] ints = {this.id, this.customerId, this.photoUrls.size()+1};
        double[] doubles = {this.latLng.latitude, this.latLng.longitude};
        ArrayList<String> strings = new ArrayList<>();
        strings.add(this.address);
        strings.add(this.description);
        strings.add(this.photoUrl);
        strings.addAll(this.photoUrls);
        float[] floats = {this.price, this.totalRevenue};
        boolean[] booleans = {this.isAvailable, this.isComplete, this.isDeleted};

        String[] stringsArray = new String[strings.size()];
        strings.toArray(stringsArray);

        out.writeIntArray(ints);
        out.writeDoubleArray(doubles);
        out.writeStringArray(stringsArray);
        out.writeFloatArray(floats);
        out.writeBooleanArray(booleans);
    }

    public static final Parcelable.Creator<Parking> CREATOR
            = new Parcelable.Creator<Parking>(){
        public Parking createFromParcel(Parcel in){
            return new Parking(in);
        }

        public Parking[] newArray(int size) {
            return new Parking[size];
        }
    };

    private Parking(Parcel in){
        int[] ints = new int[3];
        in.readIntArray(ints);
        this.id = ints[0];
        this.customerId = ints[1];
        int photosSize = ints[2];

        double[] doubles = new double[2];
        String[] strings = new String[2+photosSize];
        float[] floats = new float[2];
        boolean[] booleans = new boolean[3];
        in.readDoubleArray(doubles);
        in.readStringArray(strings);
        in.readFloatArray(floats);
        in.readBooleanArray(booleans);

        this.latLng = new LatLng(doubles[0], doubles[1]);
        this.address = strings[0];
        this.description = strings[1];
        this.photoUrl = strings[2];
        this.photoUrls = new ArrayList<>(Arrays.asList(strings));
        for (int i=0; i < 3; i++) {
            // Remove address, description and first photoURL
            this.photoUrls.remove(0);
        }
        this.price = floats[0];
        this.totalRevenue = floats[1];
        this.isAvailable = booleans[0];
        this.isComplete = booleans[1];
        this.isDeleted = booleans[2];
    }

}
