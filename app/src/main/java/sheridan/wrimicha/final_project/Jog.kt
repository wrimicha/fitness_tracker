package sheridan.wrimicha.final_project

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jogData")
data class Jog(
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo(name = "kms")
    val value: String,

    val year: Int,
    val month: Int,
    val day: Int

)