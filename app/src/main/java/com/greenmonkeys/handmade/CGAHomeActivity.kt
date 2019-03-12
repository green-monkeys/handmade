package com.greenmonkeys.handmade

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.greenmonkeys.handmade.adapters.ArtisanListRecyclerAdapter
import com.greenmonkeys.handmade.persistence.AppDatabase
import com.greenmonkeys.handmade.persistence.DatabaseFactory
import com.greenmonkeys.handmade.persistence.Group
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception

const val ADD_ARTISAN_LABEL = "Add Artisan"
const val ADD_PAYOUT_LABEL = "Add Payout"
const val ADD_GROUP_LABEL = "Add Group"

const val ADD_ARTISAN_INTENT = 1
const val ADD_PAYOUT_INTENT = 2
const val ADD_GROUP_INTENT = 3

class CGAHomeActivity : AppCompatActivity(),
    RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener<Int>,
    ArtisanListFragment.OnFragmentInteractionListener,
    GroupListFragment.OnFragmentInteractionListener {

    private lateinit var rfaLayout: RapidFloatingActionLayout
    private lateinit var rfaButton: RapidFloatingActionButton
    private lateinit var rfabHelper: RapidFloatingActionHelper

    private lateinit var bottomNavigationBar: BottomNavigationView

    private lateinit var ft: FragmentTransaction

    private lateinit var artisanListFragment: ArtisanListFragment
    private lateinit var groupListFragment: GroupListFragment

    private lateinit var db: AppDatabase

    private lateinit var email: String
    private lateinit var cgaId: String

    private var editMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cga_home)

        cgaId = intent.getStringExtra("USER_ID")
        email = intent.getStringExtra("USER_EMAIL")

        db = DatabaseFactory.getDatabase(applicationContext)

        rfaLayout = findViewById(R.id.activity_cga_home_rfal)
        rfaButton = findViewById(R.id.activity_cga_home_rfab)

        bottomNavigationBar = findViewById(R.id.activity_cga_home_navigation)
        bottomNavigationBar.setOnNavigationItemSelectedListener { menuItem ->
            ft = supportFragmentManager.beginTransaction()
            when (menuItem.itemId) {
                R.id.cga_home_menu_groups -> {
                    ft.replace(R.id.activity_cga_home_list_fragment, groupListFragment)
                    ft.commit()
                    true
                }
                R.id.cga_home_menu_artisans -> {
                    ft.replace(R.id.activity_cga_home_list_fragment, artisanListFragment)
                    ft.commit()
                    true
                }
                else -> {
                    throw Exception("It looks like you created more menu items without adding them to the menu selection listener.")
                }
            }
        }

        val listFragmentArgs = Bundle()
        listFragmentArgs.putString("cgaId", cgaId)
        listFragmentArgs.putString("email", email)

        artisanListFragment = ArtisanListFragment()
        artisanListFragment.arguments = listFragmentArgs

        groupListFragment = GroupListFragment()
        groupListFragment.arguments = listFragmentArgs

        ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.activity_cga_home_list_fragment, artisanListFragment)
        ft.commit()

        buildRapidFloatingActionList()
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun buildRapidFloatingActionList() {
        val rfaContent = RapidFloatingActionContentLabelList(this)
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this)
        val personDrawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_person_24px)
        val moneyDrawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_attach_money_24px)
        val groupDrawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_baseline_group_add_24px)
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
                .setWrapper(0),
            RFACLabelItem<Int>()
                .setLabel(ADD_GROUP_LABEL)
                .setDrawable(groupDrawable)
                .setIconNormalColor(Color.parseColor("#9BE564"))
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
                intent.putExtra("USER_EMAIL", email)
                startActivityForResult(intent, intentCode)
            }
            ADD_PAYOUT_LABEL -> {
                intent = Intent(this, InitiatePayoutActivity::class.java)
                intentCode = ADD_PAYOUT_INTENT
                intent.putExtra("USER_EMAIL", email)
                startActivityForResult(intent, intentCode)
            }
            ADD_GROUP_LABEL -> {
                rfaLayout.collapseContent()
                if (bottomNavigationBar.selectedItemId == R.id.cga_home_menu_artisans) {
                    toggleEditMode()
                }
            }
            else ->
                throw Exception("New Button Type Detected. Make sure you add a response for when new items are clicked.")
        }
    }

    fun onSaveButtonClicked(view: View?) {
        artisanListFragment.onSaveButtonClicked(null)
        if (artisanListFragment.canSaveWithCurrentGroupName()) {
            toggleEditMode()
        }
    }

    fun onCancelButtonClicked(view: View?) {
        toggleEditMode()
    }

    private fun toggleEditMode() {
        editMode = !editMode
        if (editMode) {
            artisanListFragment.toggleEditMode(true)
            rfaButton.visibility = View.GONE
        } else {
            artisanListFragment.toggleEditMode(false)
            rfaButton.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_ARTISAN_INTENT) {
            doAsync {
                val cga = db.cgaDao().getCGAByEmail(email)
                val artisans = db.cgaDao().getArtisansForCGA(cga.email)
                uiThread {
                    artisanListFragment.updateValues(artisans)
                }
            }
        }
        rfaLayout.collapseContent()
    }

    override fun onBackPressed() {
        return
    }
}

