package com.greenmonkeys.handmade

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class GroupInformationActivity : AppCompatActivity(), ArtisanListFragment.OnFragmentInteractionListener {
    lateinit var groupNameField: TextView
    lateinit var sendMessageButton: Button
    lateinit var renameGroupButton: Button
    lateinit var artisanListFragment: ArtisanListFragment

    lateinit var groupName: String
    lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_information)

        groupName = intent.getStringExtra("GROUP_NAME")
        email = intent.getStringExtra("CGA_EMAIL")

        groupNameField = findViewById(R.id.activity_group_information_group_name)
        sendMessageButton = findViewById(R.id.activity_group_information_send_message_button)
        renameGroupButton = findViewById(R.id.activity_group_information_rename_group_button)

        groupNameField.text = groupName

        val listFragmentArgs = Bundle()
        listFragmentArgs.putString("email", email)
        listFragmentArgs.putString("group", groupName)

        artisanListFragment = ArtisanListFragment()
        artisanListFragment.arguments = listFragmentArgs

        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.activity_group_information_artisan_list, artisanListFragment)
        ft.commit()
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
