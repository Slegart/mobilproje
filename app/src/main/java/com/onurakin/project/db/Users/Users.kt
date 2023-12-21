package com.onurakin.project.db.Users


import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sefikonurakin_hw2.util.Constants

@Entity(tableName = Constants.TABLEUSERS)
class Users(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var UserName: String,
    var Gender: String,
    @ColumnInfo(name = "date") var JoinDate: String,
    var Money: Int,
    var Password: String = ""

) : Parcelable {
    // Parcelable constructor
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(UserName)
        parcel.writeString(Gender)
        parcel.writeString(JoinDate)
        parcel.writeInt(Money)
        parcel.writeString(Password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Users> {
        override fun createFromParcel(parcel: Parcel): Users {
            return Users(parcel)
        }
        override fun newArray(size: Int): Array<Users?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "Tasks{" +
                "id=" + id +
                ", TaskName='" + UserName + '\'' +
                ", ProductType='" + Gender + '\'' +
                ", Date='" + JoinDate + '\'' +
                ", Price=" + Money +
                '}'
    }
}
