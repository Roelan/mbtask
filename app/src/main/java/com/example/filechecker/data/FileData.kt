package com.example.filechecker.data

import android.os.Parcel
import android.os.Parcelable

data class FileData(
    var fileName: String,
    var filePath: String,
    var fileSize: String,
    var lastModified: String,
    var fileExtension: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fileName)
        parcel.writeString(filePath)
        parcel.writeString(fileSize)
        parcel.writeString(lastModified)
        parcel.writeString(fileExtension)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FileData> {
        override fun createFromParcel(parcel: Parcel): FileData {
            return FileData(parcel)
        }

        override fun newArray(size: Int): Array<FileData?> {
            return arrayOfNulls(size)
        }
    }
}