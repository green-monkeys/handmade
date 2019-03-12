package com.greenmonkeys.handmade

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.greenmonkeys.handmade.adapters.ArtisanListRecyclerAdapter
import com.greenmonkeys.handmade.persistence.AppDatabase
import com.greenmonkeys.handmade.persistence.Artisan
import com.greenmonkeys.handmade.persistence.DatabaseFactory
import com.greenmonkeys.handmade.persistence.Group
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class ArtisanListFragment : Fragment() {
    private lateinit var cancelButton: ImageButton

    private lateinit var artisanListRecycler: RecyclerView
    private lateinit var selectionSaveButton: FloatingActionButton
    private lateinit var groupNameField: EditText

    private lateinit var db: AppDatabase
    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = DatabaseFactory.getDatabase(view.context)

        cancelButton = view.findViewById(R.id.fragment_artisan_list_cancel_button)

        artisanListRecycler = view.findViewById(R.id.fragment_artisan_list_recycler_view)
        artisanListRecycler.layoutManager = LinearLayoutManager(view.context)
        artisanListRecycler.adapter = ArtisanListRecyclerAdapter(view.context)

        selectionSaveButton = view.findViewById(R.id.fragment_artisan_list_selection_save_button)

        groupNameField = view.findViewById(R.id.fragment_artisan_list_group_name_field)

        doAsync {
            val email = arguments!!["email"] as String
            val cga = db.cgaDao().getCGAByEmail(email)

            val artisans = if (arguments!!.containsKey("group")) {
                db.groupDao().getMembersOfGroup(arguments!!["group"] as String, cga.email)
            } else {
                db.cgaDao().getArtisansForCGA(cga.email)
            }
            uiThread {
                (artisanListRecycler.adapter as ArtisanListRecyclerAdapter).updateValues(artisans)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artisan_list, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun canSaveWithCurrentGroupName(): Boolean {
        return groupNameField.text.toString().isNotBlank()
    }

    fun onSaveButtonClicked(view: View?) {
        val groupName = groupNameField.text.toString()
        if (groupName.isBlank()) {
            Toast.makeText(context, "Please Enter a Group Name", Toast.LENGTH_LONG).show()
            return
        }
        val selectedArtisans = (artisanListRecycler.adapter as ArtisanListRecyclerAdapter).getSelected()
        selectedArtisans.forEach {
            val group = Group(groupName, it.cgaId, it.email)
            doAsync {
                db.groupDao().addGroupMember(group)
            }
        }
    }

    fun updateValues(artisans: List<Artisan>) {
        (artisanListRecycler.adapter as ArtisanListRecyclerAdapter).updateValues(artisans)
    }

    fun toggleEditMode(enable: Boolean) {
        if (enable) {
            (artisanListRecycler.adapter as ArtisanListRecyclerAdapter).setSelectable(true)
            selectionSaveButton.visibility = View.VISIBLE
            groupNameField.visibility = View.VISIBLE
            cancelButton.visibility = View.VISIBLE
        } else {
            (artisanListRecycler.adapter as ArtisanListRecyclerAdapter).setSelectable(false)
            selectionSaveButton.visibility = View.GONE
            groupNameField.text.clear()
            groupNameField.visibility = View.GONE
            cancelButton.visibility = View.GONE
            groupNameField.isSelected = false
        }
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(cgaId: String, email: String) =
            ArtisanListFragment().apply {
                arguments = Bundle().apply {
                    putString("cgaId", cgaId)
                    putString("email", email)
                }
            }
    }
}
