package com.ahmed.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class SQLIteDatabase(context: Context?
) : SQLiteOpenHelper(context, "accounts.db", null, 1) {
    val id_column = "id"
    val name_column = "name"
    val email_column = "email"
    val balance_column = "balance"
    val accounts_table = "accounts_table"


    override fun onCreate(db: SQLiteDatabase?) {
        val createTable="create table $accounts_table ($id_column integer primary key, $name_column text, $email_column text, $balance_column integer)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $accounts_table");
        onCreate(db);
    }

    fun add_account(account:Account): Boolean{
        if(search_by_id(account.id).id!=-1){
            return true
        }

        val db: SQLiteDatabase = this.writableDatabase
        val cv = ContentValues()
        cv.put(id_column,account.id)
        cv.put(name_column,account.name)
        cv.put(email_column,account.email)
        cv.put(balance_column,account.balance)
        val insert = db.insert(accounts_table, null, cv)
        return(insert.toInt()!=-1)
    }

    fun get_all_accounts(): ArrayList<Account>{

        val accounts = ArrayList<Account>()
        val fetchQuery = "select * from $accounts_table"
        val db:SQLiteDatabase = this.readableDatabase
        val cursor: Cursor = db.rawQuery(fetchQuery,null)

        Log.d("1rrrrrrrrrrrrrrrrrrrrrrrrr",cursor.count.toString())
        if(cursor.moveToFirst()){
            do{
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val email = cursor.getString(2)
                val balance = cursor.getInt(3)

                val account = Account(id,name,email,balance)
                accounts.add(account)
            }while(cursor.moveToNext())
        }

        return accounts
    }

    fun search_by_id(id:Int): Account{

        val fetchQuery = "select * from $accounts_table where $id_column = ?"
        val db:SQLiteDatabase = this.readableDatabase
        val cursor: Cursor = db.rawQuery(fetchQuery, arrayOf(id.toString()))

        Log.d("rrrrrrrrrrrrrrrrrrrrrrrrr",cursor.count.toString())
        if(cursor.moveToFirst()){

                val name = cursor.getString(1)
                val email = cursor.getString(2)
                val balance = cursor.getInt(3)
                return Account(id,name,email,balance)
        }
        else{
            return Account(-1,"none","none",0)
        }


    }

    fun delete_account(account:Account): Boolean{
        val db = this.writableDatabase
        val delete_query = "delete from $accounts_table where $id_column = ${account.id}"
        val cursor:Cursor = db.rawQuery(delete_query,null)
        return(cursor.moveToFirst())
    }

    fun update_balance(sending_account:Account,receiving_account:Account,amount:Int){

        val db = this.writableDatabase
        val cv1 = ContentValues()
        cv1.put(id_column, sending_account.id)
        cv1.put(name_column, sending_account.name)
        cv1.put(email_column, sending_account.email)
        cv1.put(balance_column, sending_account.balance-amount)
        db.update(accounts_table, cv1, "id = ? ", arrayOf(Integer.toString(sending_account.id)))

        val cv2 = ContentValues()
        cv2.put(id_column, receiving_account.id)
        cv2.put(name_column, receiving_account.name)
        cv2.put(email_column, receiving_account.email)
        cv2.put(balance_column, receiving_account.balance+amount)
        db.update(accounts_table, cv2, "id = ? ", arrayOf(Integer.toString(receiving_account.id)))


    }

}