package com.greenmonkeys.handmade

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.greenmonkeys.handmade.adapters.GroupListRecyclerAdapter
import com.greenmonkeys.handmade.persistence.AppDatabase
import com.greenmonkeys.handmade.persistence.DatabaseFactory
import com.greenmonkeys.handmade.persistence.Group
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class GroupListFragment : Fragment() {
    private lateinit var groupListRecycler: RecyclerView
    private lateinit var selectionSaveButton: FloatingActionButton
    private lateinit var groupNameField: EditText

    private lateinit var db: AppDatabase
    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = DatabaseFactory.getDatabase(view.context)

        groupListRecycler = view.findViewById(R.id.fragment_group_list_recycler_view)
        groupListRecycler.layoutManager = LinearLayoutManager(view.context)
        groupListRecycler.adapter = GroupListRecyclerAdapter(view.context)

        selectionSaveButton = view.findViewById(R.id.fragment_group_list_selection_save_button)

        groupNameField = view.findViewById(R.id.fragment_group_list_group_name_field)

        doAsync {
            val email = arguments!!["email"] as String
            val cga = db.cgaDao().getCGAByEmail(email)
            val groups = db.groupDao().getGroupsForCGA(cga.email)
            uiThread {
                (groupListRecycler.adapter as GroupListRecyclerAdapter).updateValues(groups)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_list, container, false)
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
    }

    fun updateValues(groups: List<Group>) {
        (groupListRecycler.adapter as GroupListRecyclerAdapter).updateValues(groups)
    }

    fun toggleEditMode(enable: Boolean) {
        if (enable) {
            (groupListRecycler.adapter as GroupListRecyclerAdapter).setSelectable(true)
            selectionSaveButton.visibility = View.VISIBLE
            groupNameField.visibility = View.VISIBLE
        } else {
            (groupListRecycler.adapter as GroupListRecyclerAdapter).setSelectable(false)
            selectionSaveButton.visibility = View.GONE
            groupNameField.text.clear()
            groupNameField.visibility = View.GONE
            groupNameField.isSelected = false
        }
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(cgaId: String, email: String) =
            GroupListFragment().apply {
                arguments = Bundle().apply {
                    putString("cgaId", cgaId)
                    putString("email", email)
                }
            }
    }
}
