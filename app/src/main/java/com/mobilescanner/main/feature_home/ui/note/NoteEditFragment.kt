package com.mobilescanner.main.feature_home.ui.note

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mobilescanner.main.R
import com.mobilescanner.main.feature_home.data.item.NoteItem
import com.mobilescanner.main.main.data.utils.FileUtils.logD
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class NoteEditFragment : Fragment() {

    companion object {
        fun newInstance() = NoteEditFragment()
    }

    private lateinit var viewModel: NoteEditViewModel
    private lateinit var mainView:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = view
        viewModel = ViewModelProvider( requireActivity() )[NoteEditViewModel::class.java]
        initTitle()
    }

    private fun initTitle() {
        viewModel.getNoteById(viewModel.noteItem.id)
        viewModel.queryLiveData.observe(
            viewLifecycleOwner, Observer { it ->
                if ( it == null ){
                    val id = (1..10000).random().toLong()
                    viewModel.noteItem = NoteItem( id = id )
                    viewModel.saveNote()
                }else{
                    viewModel.noteItem = it
                }
            }
        )
        val cancel = mainView.findViewById<ImageView>(R.id.cancel)
        cancel.setOnClickListener {
            findNavController().popBackStack()
        }
        val title = mainView.findViewById<EditText>(R.id.title)
        title.text = Editable.Factory.getInstance().newEditable(viewModel.noteItem.title)
        val body = mainView.findViewById<EditText>(R.id.body)
        body.text = Editable.Factory.getInstance().newEditable(viewModel.noteItem.body)

        val save = mainView.findViewById<ImageView>(R.id.save)
        save.setOnClickListener {
            val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            "id = ${viewModel.noteItem.id}".logD("NoteEdit")
            viewModel.noteItem = NoteItem(
                id = viewModel.noteItem.id,
                title = title.text.toString(),
                body = body.text.toString(),
                time = date
            )
            viewModel.updateNote()
            findNavController().popBackStack()
        }
    }
}