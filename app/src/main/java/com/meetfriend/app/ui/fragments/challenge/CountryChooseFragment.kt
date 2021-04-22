package com.meetfriend.app.ui.fragments.challenge

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.responseclasses.challenge.Result
import com.meetfriend.app.responseclasses.challenge.countryResponseClass
import com.meetfriend.app.ui.bottomsheet.CityChooseBottomSheet
import com.meetfriend.app.ui.bottomsheet.CountryChooseBottomSheet
import com.meetfriend.app.ui.bottomsheet.StateChooseBottomSheet
import com.meetfriend.app.viewmodal.ChallengeViewModal
import kotlinx.android.synthetic.main.fragment_create_challenge_1.*
import kotlinx.android.synthetic.main.fragment_live_challenge.*

class CountryChooseFragment : BaseFragment(), AdapterView.OnItemSelectedListener,
    CountryChooseBottomSheet.AdapterCallback, StateChooseBottomSheet.AdapterCallback,
    CityChooseBottomSheet.AdapterCallback {
    private var challengeViewModal: ChallengeViewModal? = null
    var languages = arrayOf("Everyone", "Friends", "Friends of Friends")
    private var countryList: ArrayList<Result>? =
        ArrayList()
    private var cntryDrpdownList: ArrayList<String>? = ArrayList()
    private var cntryIds: String? = ""
    private var stateIds: String? = ""
    private var cityIds: String? = ""
    private var seenById: Int? = null

    companion object {
        fun remove1() {

        }

        const val TAG_1 = "fragCreateChallenge2"
        const val CHALLENGE_SEEN_BY = "seenById"
        const val CHALLENGE_COUNTRY = "countryId"
        const val CHALLENGE_STATE = "stateID"
        const val CHALLENGE_CITY = "cityId"
    }

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_create_challenge_1, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_list, languages)
        adapter.setDropDownViewResource(R.layout.spinner_list)
        spnSee.adapter = adapter
        spnSee.onItemSelectedListener = this


        //click listener
        headerLoginBackButton?.setOnClickListener {
            findNavController().popBackStack()
        }
        spnCntry.setOnClickListener {
            openCntryBottomSheet();
        }
        spnState.setOnClickListener {
            openStateBottomSheet()
        }
        spnCity.setOnClickListener {
            opencityBottomSheet()
        }
        tvCreateChallengeNext.setOnClickListener {
            if (cntryIds.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Country is empty", Toast.LENGTH_SHORT).show()
            } else if (stateIds.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "State is empty", Toast.LENGTH_SHORT).show()
            } else if (cityIds.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "City is empty", Toast.LENGTH_SHORT).show()
            } else {
                activity?.supportFragmentManager?.popBackStack()
                val bundle = Bundle()
                bundle.putInt(CHALLENGE_SEEN_BY, seenById!!)
                bundle.putString(CHALLENGE_COUNTRY, cntryIds)
                bundle.putString(CHALLENGE_STATE, stateIds)
                bundle.putString(CHALLENGE_CITY, cityIds)
                val fragment = CreateChallenge()
                fragment.arguments = bundle
                activity?.supportFragmentManager?.beginTransaction()
                    ?.add(R.id.container, fragment)
                    ?.addToBackStack(TAG_1)
                    ?.commit()
            }
        }
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.e("spn", "item selected")
        when (position) {
            0 -> {
                seenById = 1
            }
            1 -> {
                seenById = 2
            }
            2 -> {
                seenById = 3
            }
        }
    }

    private fun setCountryAdapter(it: countryResponseClass) {
        countryList = it.result

        it.result.forEach {
            Log.e("id===", "" + it.name)
            cntryDrpdownList!!.add(it.name)
        }
    }

    private fun fetchState(id: Int) {
        val mHashMap = HashMap<String, Any>()
        mHashMap["country_id"] = id
        challengeViewModal?.state(mHashMap)
    }

    private fun openCntryBottomSheet() {
        val bottomSheet = CountryChooseBottomSheet(this)
        activity?.supportFragmentManager?.let { bottomSheet.show(it, "Tag") }
    }

    private fun openStateBottomSheet() {
        if (cntryIds.equals("1")) {

        } else
            if (cntryIds.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please choose country", Toast.LENGTH_SHORT).show()
            } else {
                val bottomSheet = cntryIds?.let { StateChooseBottomSheet(it, this) }
                activity?.supportFragmentManager?.let { bottomSheet!!.show(it, "Tag") }
            }
    }

    private fun opencityBottomSheet() {
        if (cntryIds.equals("1")||stateIds.equals("1")) {

        } else if (stateIds.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please choose state", Toast.LENGTH_SHORT).show()
        } else {
            val bottomSheet = stateIds?.let { CityChooseBottomSheet(it, this) }
            activity?.supportFragmentManager?.let { bottomSheet!!.show(it, "Tag") }
        }
    }


    override fun setCountry(
        item: ArrayList<Result>,
        isAllChecked: Boolean
    ) {
        Log.e("result contry:", "" + item)

        if (!isAllChecked) {
            cntryIds = item.joinToString { it ->
                "${it.id}"
            }
            val cntryNames = item.joinToString { it ->
                "${it.name}"
            }
            spnCntry.text = cntryNames
            stateIds = ""
            spnState.text = ""
            cityIds = ""
            spnCity.text = ""
        } else {
            cntryIds = "1"
            spnCntry.text = "All"
            stateIds = "1"
            spnState.text = "All"
            cityIds = "1"
            spnCity.text = "All"
        }
        Log.e("js:", "" + cntryIds)

    }

    override fun setState(item: ArrayList<Result>, checked: Boolean) {
        Log.e("result state:", "" + item)

        if (!checked) {
            stateIds = item.joinToString { it ->
                "${it.id}"
            }
            val statesNames = item.joinToString { it ->
                "${it.name}"
            }
            Log.e("js:", "" + stateIds)
            spnState.text = statesNames
            cityIds = ""
            spnCity.text = ""
        } else {
            stateIds = "1"
            spnState.text = "All"
            cityIds = "1"
            spnCity.text = "All"
        }


    }

    override fun setCity(item: ArrayList<Result>, checked: Boolean) {
        Log.e("result city:", "" + item)

        if (!checked) {

            val cityNames = item.joinToString { it ->
                "${it.name}"
            }
            cityIds = item.joinToString { it ->
                "${it.id}"
            }
            Log.e("js:", "" + cityIds)
            spnCity.text = cityNames
        } else {
            cityIds = "1"
            spnCity.text = "All"

        }
    }
}