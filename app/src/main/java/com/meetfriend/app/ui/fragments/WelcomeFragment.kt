package com.meetfriend.app.ui.fragments

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.meetfriend.app.R
import kotlinx.android.synthetic.main.fragment_welcome.*


class WelcomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
        tvCreateAccount.setOnClickListener {
            //findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment)
            /*  showDialog(requireActivity(), "Register using", arrayOf("Ok"),
                  DialogInterface.OnClickListener { dialog, which ->
                      if (which == -1) Log.d("Neha", "On button click")
                      //Do your functionality here
                  })*/
            dialog()
        }
    }

    fun dialog() {
        val options = arrayOf("Email", "Phone")
        var selectedItem = 0
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Register using")
        builder.setSingleChoiceItems(options
            , 0, { dialogInterface: DialogInterface, item: Int ->
                selectedItem = item
            })
        builder.setPositiveButton("Proceed", { dialogInterface: DialogInterface, p1: Int ->
            // Toast.makeText(requireActivity(),
            //   "selected item = " + options[selectedItem], Toast.LENGTH_SHORT).show();
            dialogInterface.dismiss()
            if (selectedItem == 0) {
                val bundle = bundleOf(
                    "type" to "email"
                )
                findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment,bundle)
            } else {
                findNavController().navigate(R.id.action_welcomeFragment_to_verifyOtpFragment)
            }
        })
        /* builder.setNegativeButton("Cancel", { dialogInterface: DialogInterface, p1: Int ->
             dialogInterface.dismiss()
         })*/
        builder.create()
        builder.show();
    }

    fun showDialog(
        context: Context?, title: String?, btnText: Array<String?>,
        listener: DialogInterface.OnClickListener?
    ) {
        var listener = listener
        val items = arrayOf<CharSequence>("Email", "Phone")
        if (listener == null) listener =
            DialogInterface.OnClickListener { paramDialogInterface, paramInt -> paramDialogInterface.dismiss() }
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setSingleChoiceItems(items, -1,
            DialogInterface.OnClickListener { dialog, item -> })
        builder.setPositiveButton(btnText[0], listener)
        if (btnText.size != 1) {
            builder.setNegativeButton(btnText[1], listener)
        }
        builder.show()
    }
}