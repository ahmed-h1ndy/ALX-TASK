package com.ahmed.myapplication

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    lateinit var account_details: Dialog
    lateinit var acc1_ui: View
    lateinit var acc2_ui: View
    lateinit var acc3_ui: View
    lateinit var acc4_ui: View
    lateinit var acc5_ui: View

    lateinit var acc1_data: Account
    lateinit var acc2_data: Account
    lateinit var acc3_data: Account
    lateinit var acc4_data: Account
    lateinit var acc5_data: Account

    lateinit var db_helper: SQLIteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db_helper = SQLIteDatabase(this)

        add_accounts()
        show_accounts()

    }

    private fun show_accounts() {
        account_details = Dialog(this)
        acc1_ui = findViewById(R.id.account1)
        acc2_ui = findViewById(R.id.account2)
        acc3_ui = findViewById(R.id.account3)
        acc4_ui = findViewById(R.id.account4)
        acc5_ui = findViewById(R.id.account5)

        acc1_data = db_helper.search_by_id(1)
        acc2_data = db_helper.search_by_id(2)
        acc3_data = db_helper.search_by_id(3)
        acc4_data = db_helper.search_by_id(4)
        acc5_data = db_helper.search_by_id(5)

        link_ui_data(acc1_ui,acc1_data)
        link_ui_data(acc2_ui,acc2_data)
        link_ui_data(acc3_ui,acc3_data)
        link_ui_data(acc4_ui,acc4_data)
        link_ui_data(acc5_ui,acc5_data)

        set_photos()

        acc1_ui.setOnClickListener {
            showDetails(acc1_data)
        }
        acc2_ui.setOnClickListener {
            showDetails(acc2_data)
        }
        acc3_ui.setOnClickListener {
            showDetails(acc3_data)
        }
        acc4_ui.setOnClickListener {
            showDetails(acc4_data)
        }
        acc5_ui.setOnClickListener {
            showDetails(acc5_data)
        }
    }
    private fun link_ui_data(acc_ui:View,acc_data:Account){
        acc_ui.findViewById<TextView>(R.id.account_name).setText(acc_data.name)
        acc_ui.findViewById<TextView>(R.id.account_email).setText(acc_data.email)
        acc_ui.findViewById<TextView>(R.id.account_balance).setText("$"+acc_data.balance.toString()+"M")
    }
    private fun set_photos(){
        acc1_ui.findViewById<ImageView>(R.id.account_photo).setImageResource(R.drawable.icons8_arsenal_fc)
        acc2_ui.findViewById<ImageView>(R.id.account_photo).setImageResource(R.drawable.icons8_chelsea_fc)
        acc3_ui.findViewById<ImageView>(R.id.account_photo).setImageResource(R.drawable.liverpool_f_c__logo_vector)
        acc4_ui.findViewById<ImageView>(R.id.account_photo).setImageResource(R.drawable.icons8_manchester_united)
        acc5_ui.findViewById<ImageView>(R.id.account_photo).setImageResource(R.drawable.icons8_barcelona_fc_144)
    }

    private fun showDetails(account:Account){

        account_details.setContentView(R.layout.activity_account_details)
        account_details.findViewById<TextView>(R.id.popup_name).setText(account.name)
        account_details.findViewById<TextView>(R.id.popup_email_val).setText(account.email)
        account_details.findViewById<TextView>(R.id.popup_balance_val).setText("$"+account.balance.toString()+"M")

        set_popup_photo(account_details.findViewById(R.id.popup_photo),account)

        val transfer_button: Button = account_details.findViewById(R.id.transfer_button)
        transfer_button.setOnClickListener{
            var amount = 0
            var amount_text = account_details.findViewById<EditText>(R.id.popup_amount).text.toString()
            if(!amount_text.isEmpty()){
                amount = amount_text.toInt()}
            chooseClub(account,amount)
            account_details.dismiss()
        }

        account_details.window?.setWindowAnimations(R.style.dialogAnimation)
        account_details.show()
    }

    private fun set_popup_photo(img: ImageView?, account: Account) {
        if(account.id==1){
            img?.setImageResource(R.drawable.icons8_arsenal_fc)
        }
        else if(account.id==2){
            img?.setImageResource(R.drawable.icons8_chelsea_fc)
        }
        else if(account.id==3){
            img?.setImageResource(R.drawable.liverpool_f_c__logo_vector)
        }
        else if(account.id==4){
            img?.setImageResource(R.drawable.icons8_manchester_united)
        }
        else{
            img?.setImageResource(R.drawable.icons8_barcelona_fc_144)
        }

    }

    private fun chooseClub(account:Account,amount:Int){

        val clubs_dialog = Dialog(this)
        clubs_dialog.setContentView(R.layout.clubs)

        val img_views:ArrayList<ImageView> = ArrayList()
        img_views.add(clubs_dialog.findViewById(R.id.first))
        img_views.add(clubs_dialog.findViewById(R.id.second))
        img_views.add(clubs_dialog.findViewById(R.id.third))
        img_views.add(clubs_dialog.findViewById(R.id.fourth))

        val images:ArrayList<Int> = ArrayList()
        images.add(R.drawable.icons8_arsenal_fc)
        images.add(R.drawable.icons8_chelsea_fc)
        images.add(R.drawable.liverpool_f_c__logo_vector)
        images.add(R.drawable.icons8_manchester_united)
        images.add(R.drawable.icons8_barcelona_fc_144)

        var counter = 0
        for(i in 1..5){

            if(i!=account.id)
            img_views[counter++].setImageResource(images[i-1])
        }

        img_views[0].setOnClickListener {
            if(account.id==1){
                //chelsea is clicked
                db_helper.update_balance(account,
                    db_helper.search_by_id(2),
                    amount)
            }
            else{
                //arsenal is clicked
                db_helper.update_balance(account,
                    db_helper.search_by_id(1),
                    amount)
            }
            show_accounts()
            clubs_dialog.dismiss()
        }
        img_views[1].setOnClickListener {
            if(account.id<=2){
                //liverpool is clicked
                db_helper.update_balance(account,
                    db_helper.search_by_id(3),
                    amount)
            }
            else{
                //chelsea is clicked
                db_helper.update_balance(account,
                    db_helper.search_by_id(2),
                    amount)
            }
            show_accounts()
            clubs_dialog.dismiss()
        }
        img_views[2].setOnClickListener {
            if(account.id<=3){
                //manu is clicked
                db_helper.update_balance(account,
                    db_helper.search_by_id(4),
                    amount)
            }
            else{
                //liverpool is clicked
                db_helper.update_balance(account,
                    db_helper.search_by_id(3),
                    amount)
            }
            show_accounts()
            clubs_dialog.dismiss()
        }
        img_views[3].setOnClickListener {
            if(account.id==5){
                //barcelona is clicked
                db_helper.update_balance(account,
                    db_helper.search_by_id(4),
                    amount)
            }
            else{
                //manu is clicked
                db_helper.update_balance(account,
                    db_helper.search_by_id(5),
                    amount)
            }
            show_accounts()
            clubs_dialog.dismiss()
        }
        clubs_dialog.window?.setWindowAnimations(R.style.dialogAnimation)
        clubs_dialog.show()
    }

    private fun add_accounts(){
        db_helper.add_account(Account(1,"arsenal","a@gmail.com",50))
        db_helper.add_account(Account(2,"chelsea","c@gmail.com",100))
        db_helper.add_account(Account(3,"liverpool","l@gmail.com",150))
        db_helper.add_account(Account(4,"manu","m@gmail.com",200))
        db_helper.add_account(Account(5,"barcelona","b@gmail.com",500))
    }


}


