package br.edu.scl.ifsp.sharedlist.model

import android.os.Parcelable
import androidx.annotation.NonNull

class Task(
    @PrimaryKey(autoGenerate = true) val id: Int? = -1,
    @NonNull var name: String = "",
    @NonNull var address: String = "",
    @NonNull var phone: String = "",
    @NonNull var email: String = "",
): Parcelable
}