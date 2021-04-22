package com.meetfriend.app.ui.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseFragment
import com.meetfriend.app.responseclasses.photos.Data
import com.meetfriend.app.ui.activities.FullScreenActivity
import com.meetfriend.app.ui.activities.VideoPlayActivity
import com.meetfriend.app.ui.adapters.UserPhotosAdapter
import com.meetfriend.app.utilclasses.ApiFailureTypes
import com.meetfriend.app.utilclasses.GridItemDecoration
import com.meetfriend.app.viewmodal.ProfileViewModal
import contractorssmart.app.utilsclasses.CommonMethods
import kotlinx.android.synthetic.main.fragment_photos.*


class PhotosFragment(val otherUserId: String, type: String) : BaseFragment(), UserPhotosAdapter.AdapterCallback {
    private var profileViewModal: ProfileViewModal? = null
    private var userPhotosAdapter: UserPhotosAdapter? = null
    private var dataList: ArrayList<Data>? = ArrayList()
    private var baseUrl = ""
    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_photos, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModal = ViewModelProvider(this).get(ProfileViewModal::class.java)
        iniViews()
        initializeObservers()
        getPhotosData()

    }

    private fun iniViews() {
        recyclerViewUserPhotos.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            setEmptyView(tvNoPhotos)
            // Grid item spacing
            // Grid item spacing
            val itemDecoration = GridItemDecoration(
                requireActivity(),
                R.dimen.grid_item_spacing
            ) // R.dimen.grid_item_spacing is 2dp

            addItemDecoration(itemDecoration)
            //adapter = userPhotosAdapter
        }
    }

    private fun initializeObservers() {
        profileViewModal!!.userPhotosResponse.observe(requireActivity(), Observer {
            it.let {
                // CommonMethods.showToastMessageAtTop(requireActivity(), it.message)
                if (it.result != null) {
                    dataList = it.result.data
                    baseUrl = it.media_url
                    userPhotosAdapter =
                        UserPhotosAdapter(dataList!!, this, requireActivity(), baseUrl)
                    recyclerViewUserPhotos.adapter=userPhotosAdapter
                    userPhotosAdapter!!.notifyDataSetChanged()
                }
            }
        })
        profileViewModal!!.apiError.observe(requireActivity(), Observer {
            it.let {
                //CommonMethods.showToastMessageAtTop(requireActivity(), it)
            }
        })
        profileViewModal?.isLoading?.observe(requireActivity(), Observer {
            it?.let {
                showLoading(it)
            }
        })

        profileViewModal?.onFailure?.observe(requireActivity(), Observer {
            it?.let {
                CommonMethods.showToastMessageAtTop(
                    requireActivity(),
                    ApiFailureTypes().getFailureMessage(it, requireActivity())
                )
                /* CommonMethods.showSnackbarMessageWithoutColor(
                     requireActivity(),
                     ApiFailureTypes().getFailureMessage(it, requireActivity())
                 )*/
            }
        })
    }

    private fun setAdapter() {

    }

    private fun getPhotosData() {
        val mHashMap = HashMap<String, Any>()
        mHashMap["user_id"] = otherUserId
        profileViewModal?.userPhotos(mHashMap)
    }

    override fun onLikeItemClicked(position: Int) {
    }

    override fun onCancelItemClicked(position: Int) {
    }

    override fun ItemClicked(position: Int) {
        if (dataList!!.get(position).extension.equals(
                "mov",
                true
            ) ||dataList!!.get(position).extension.equals(
                "mp4",
                true
            ) || dataList!!.get(position).extension.equals(
                "3gp",
                true
            ) || dataList!!.get(position).extension.equals(
                "mpeg",
                true
            )
        ) {
            val intent = Intent(requireActivity(), VideoPlayActivity::class.java)
            intent.putExtra(
                "path",
                baseUrl + dataList!!.get(position).file_path)
            requireActivity().startActivity(intent)
        }
        else
        {
            val intent = Intent(requireActivity(), FullScreenActivity::class.java)
            intent.putExtra(
                "url",
                baseUrl + dataList!!.get(position).file_path
            )
            requireActivity().startActivity(intent)
        }
    }
}