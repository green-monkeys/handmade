package com.greenmonkeys.handmade

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greenmonkeys.handmade.adapters.ArtisanListRecyclerAdapter
import com.greenmonkeys.handmade.persistence.AppDatabase
import com.greenmonkeys.handmade.persistence.DatabaseFactory
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception

const val ADD_ARTISAN_LABEL = "Add Artisan"
const val ADD_PAYOUT_LABEL = "Add Payout"

const val ADD_ARTISAN_INTENT = 1
const val ADD_PAYOUT_INTENT = 2

class CGAHomeActivity : AppCompatActivity(),
    RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener<Int> {
    private lateinit var rfaLayout: RapidFloatingActionLayout
    private lateinit var rfaButton: RapidFloatingActionButton
    private lateinit var rfabHelper: RapidFloatingActionHelper

    private lateinit var artisanListRecycler: RecyclerView
    private lateinit var cgaInfoTextView: TextView

    private lateinit var db: AppDatabase

    private lateinit var email: String
    private lateinit var cgaId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cga_home)

        db = DatabaseFactory.getDatabase(applicationContext)
        artisanListRecycler = findViewById(R.id.activity_cga_home_artisan_recycler_view)
        artisanListRecycler.layoutManager = LinearLayoutManager(this)
        cgaInfoTextView = findViewById(R.id.activity_cga_home_user_information_text_view)

        rfaLayout = findViewById(R.id.activity_cga_home_rfal)
        rfaButton = findViewById(R.id.activity_cga_home_rfab)

        buildRapidFloatingActionList()

        cgaId = intent.getStringExtra("USER_ID")
        cgaInfoTextView.text = "${intent.getStringExtra("USER_NAME")} (${intent.getStringExtra("USER_EMAIL")})"
        email = intent.getStringExtra("USER_EMAIL")
        doAsync {
            val cga = db.cgaDao().getCGAByEmail(email)
            val artisans = db.cgaDao().getArtisansForCGA(cga.email)
            uiThread {
                artisanListRecycler.adapter = ArtisanListRecyclerAdapter(applicationContext)
                (artisanListRecycler.adapter as ArtisanListRecyclerAdapter).updateValues(artisans)
            }
        }

    }

    private fun buildRapidFloatingActionList() {
        val rfaContent = RapidFloatingActionContentLabelList(this)
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this)
        val personDrawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_person_24px)
        val moneyDrawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_attach_money_24px)
        val items = arrayListOf(
            RFACLabelItem<Int>()
                .setLabel(ADD_ARTISAN_LABEL)
                .setDrawable(personDrawable)
                .setIconNormalColor(Color.parseColor("#D00000"))
                .setWrapper(0),
            RFACLabelItem<Int>()
                .setLabel(ADD_PAYOUT_LABEL)
                .setDrawable(moneyDrawable)
                .setIconNormalColor(Color.parseColor("#FFBA08"))
                .setWrapper(0)
        ).toList()
        rfaContent.items = items
        rfabHelper = RapidFloatingActionHelper(applicationContext, rfaLayout, rfaButton, rfaContent).build()
    }

    override fun onRFACItemIconClick(position: Int, item: RFACLabelItem<Int>?) {
        onRFACItemClick(item)
    }

    override fun onRFACItemLabelClick(position: Int, item: RFACLabelItem<Int>?) {
        onRFACItemClick(item)
    }

    private fun onRFACItemClick(item: RFACLabelItem<Int>?) {
        val intent: Intent
        val intentCode: Int
        when (item?.label) {
            ADD_ARTISAN_LABEL -> {
                intent = Intent(this, AddArtisanActivity::class.java)
                intentCode = ADD_ARTISAN_INTENT
            }
            ADD_PAYOUT_LABEL -> {
                intent = Intent(this, InitiatePayoutActivity::class.java)
                intentCode = ADD_PAYOUT_INTENT
            }
            else ->
                throw Exception("New Button Type Detected. Make sure you add a response for when new items are clicked.")
        }
        intent.putExtra("USER_EMAIL", email)
        startActivityForResult(intent, intentCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_ARTISAN_INTENT) {
            doAsync {
                val cga = db.cgaDao().getCGAByEmail(email)
                val artisans = db.cgaDao().getArtisansForCGA(cga.email)
                uiThread {
                    (artisanListRecycler.adapter as ArtisanListRecyclerAdapter).updateValues(artisans)
                }
            }
        }
        rfaLayout.collapseContent()
    }

    override fun onBackPressed() {
        return
    }
}

