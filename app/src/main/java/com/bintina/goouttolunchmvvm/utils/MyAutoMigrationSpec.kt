package com.bintina.goouttolunchmvvm.utils
import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec

/*@RenameColumn.Entries(
    RenameColumn(
        tableName = "LocalRestaurant",
        fromColumnName = "attending",
        toColumnName = "attendingList"
    )
)
@DeleteColumn.Entries(
    DeleteColumn(
        tableName = "LocalRestaurant",
        columnName = "attending"
    )
)*/
class MyAutoMigrationSpec : AutoMigrationSpec