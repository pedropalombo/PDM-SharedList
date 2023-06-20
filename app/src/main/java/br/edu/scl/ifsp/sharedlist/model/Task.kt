package br.edu.scl.ifsp.sharedlist.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int? = -1,
    @NonNull var title: String = "",
    @NonNull var description: String = "",
    @NonNull var dateOfConclusion: String = "",
    @NonNull var creatorEmail: String = "",
    @NonNull var dateOfCreation: String = "",
): Parcelable