package dpm.project.b.b_project.api;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemData implements Parcelable {

    public int id;
    public String name;
    public String url;
    public int price;

    protected ItemData(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.url = in.readString();
        this.price = in.readInt();
    }

    public static final Creator<ItemData> CREATOR = new Creator<ItemData>() {
        @Override
        public ItemData createFromParcel(Parcel in) {
            return new ItemData(in);
        }

        @Override
        public ItemData[] newArray(int size) {
            return new ItemData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.url);
        parcel.writeInt(this.price);
    }
}
