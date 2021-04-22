package com.meetfriend.app.ui.bottomsheet

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.meetfriend.app.R
import com.meetfriend.app.responseclasses.challenge.Result
import com.meetfriend.app.responseclasses.challenge.countryResponseClass
import com.meetfriend.app.ui.adapters.CountryListAdapter
import com.meetfriend.app.ui.fragments.challenge.CountryChooseFragment
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.utilclasses.CallProgressWheel
import com.meetfriend.app.viewmodal.ChallengeViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.bottomsheet_country_choose.*
import kotlinx.android.synthetic.main.bottomsheet_country_choose.headerLoginBackButton
import kotlinx.android.synthetic.main.fragment_live_challenge.*


class CountryChooseBottomSheet(var callBack: AdapterCallback) : DialogFragment(),
    CountryListAdapter.AdapterCallback {

    private var challengeViewModal: ChallengeViewModal? = null
    private var cntryAdapter: CountryListAdapter? = null

    interface AdapterCallback {
        fun setCountry(
            item: ArrayList<Result>,
            checked: Boolean
        )
    }

    companion object {
        const val TAG = "bottomSheetCountry"
        fun newInstance() {

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_country_choose, container, false)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        );
        return dialog;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        challengeViewModal = ViewModelProvider(this).get(ChallengeViewModal::class.java)
        headerLoginBackButton.visibility=View.GONE
        initializeObservers()
        challengeViewModal?.country()
        headerLoginBackButton?.setOnClickListener {
            dismiss()
        }
        btnDone.setOnClickListener {
            Log.e("btn", "click working")
            if (cntryAdapter?.listisEmpty() == false) {
                CommonMethods.showToastMessageAtTop(requireActivity(), "Choose Country")
            } else {

                cntryAdapter?.getItem()?.let { it1 -> callBack.setCountry(it1, checkBox.isChecked) }
                dismiss()
            }
        }
    }

    private fun initializeObservers() {
        challengeViewModal?.countryResponse!!.observe(requireActivity(), Observer {
            it.let {
                headerLoginBackButton.visibility=View.VISIBLE

                if (it.status) {
                    // CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                    setCountryAdapter(it)

                } else {
                    /* if (it.status_code.equals(AppConstants.httpcodes.STATUS_VALIDATION_ERROR)) {

                     }
                     if (it.status_code.equals(AppConstants.httpcodes.STATUS_SESSION_EXPIRED)) {

                     }*/
                    CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                }
            }
        })
        challengeViewModal!!.apiError.observe(requireActivity(), Observer {
            it.let {
                headerLoginBackButton.visibility=View.VISIBLE
                CommonMethods.showToastMessageAtTop(requireActivity(), it)
            }
        })
        challengeViewModal?.isLoading?.observe(requireActivity(), Observer {
            it?.let {
                showLoading(it)
            }
        })

        challengeViewModal?.onFailure?.observe(requireActivity(), Observer {
            it?.let {
                headerLoginBackButton.visibility=View.VISIBLE
                CommonMethods.showToastMessageAtTop(
                    requireActivity(),
                    ApiFailureTypes().getFailureMessage(it, requireActivity())
                )
            }
        })
    }

    private fun setCountryAdapter(it: countryResponseClass?) {
        if (it != null) {

            cntryAdapter = CountryListAdapter(it.result, this, requireActivity())
        }

        recyclerviewCntry.layoutManager = LinearLayoutManager(activity)
        recyclerviewCntry.adapter = cntryAdapter

        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cntryAdapter!!.checkedAll()
                cntryAdapter!!.notifyDataSetChanged()
            } else {
                cntryAdapter!!.unCheckedAll()
                cntryAdapter!!.notifyDataSetChanged()
            }
        }
        search_badge.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                cntryAdapter?.filter?.filter(newText)

                return false
            }
        })
    }

    fun showLoading(show: Boolean?) {
        if (show!!) showLoading() else hideLoading()
    }

    protected fun showLoading() {
        CallProgressWheel.showLoadingDialog(requireActivity())
    }

    protected fun hideLoading() {
        CallProgressWheel.dismissLoadingDialog()
    }

    override fun itemCheckedAll(checked: Boolean) {
        Log.e("item checke:", "" + checked)
        if (checked) {
            cntryAdapter!!.checkedAll()
            cntryAdapter!!.notifyDataSetChanged()
        } else {
            cntryAdapter!!.unCheckedAll()
            cntryAdapter!!.notifyDataSetChanged()
        }
    }

    override fun itemClick(result: Result) {

    }
}