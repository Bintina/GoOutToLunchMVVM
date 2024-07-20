package com.bintina.goouttolunchmvvm.model.database.repositories

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
)
*/
@DeleteColumn.Entries(
    DeleteColumn(
        tableName = "LocalRestaurant",
        columnName = "attendingList"
    ),
    DeleteColumn(
        tableName = "LocalUser",
        columnName = "attending_string"
    ),
    DeleteColumn(
        tableName = "LocalRestaurant",
        columnName = "currentUserName"
    ),
    DeleteColumn(
        tableName = "LocalRestaurant",
        columnName = "currentUserUid"
    ),
    DeleteColumn(
        tableName = "LocalRestaurant",
        columnName = "currentUserAttendingBoolean"
    )
)
class MyAutoMigrationSpec : AutoMigrationSpec