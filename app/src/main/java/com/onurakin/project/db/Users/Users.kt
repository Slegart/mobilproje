package com.onurakin.project.db.Products


import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sefikonurakin_hw2.util.Constants

@Entity(tableName = Constants.TABLENAME)
class Products(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var ProductName: String,
    var ProductType: String,
    @ColumnInfo(name = "date") var Date: String,
    var Price: Int,
    var InCart: Boolean = false,
    var IsPurchased: Boolean = false
) : Parcelable {
    // Parcelable constructor
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(ProductName)
        parcel.writeString(ProductType)
        parcel.writeString(Date)
        parcel.writeInt(Price)
        parcel.writeByte(if (InCart) 1 else 0)
        parcel.writeByte(if (IsPurchased) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Products> {
        override fun createFromParcel(parcel: Parcel): Products {
            return Products(parcel)
        }
        override fun newArray(size: Int): Array<Products?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "Tasks{" +
                "id=" + id +
                ", TaskName='" + ProductName + '\'' +
                ", ProductType='" + ProductType + '\'' +
                ", Date='" + Date + '\'' +
                ", Price=" + Price +
                ", InCart=" + InCart +
                ", IsPurchased=" + IsPurchased +
                '}'
    }
}
