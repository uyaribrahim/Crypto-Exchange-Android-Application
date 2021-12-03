package com.ibrhm.cryptoexchange.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ibrhm.cryptoexchange.R
import com.ibrhm.cryptoexchange.viewmodel.LoggedInViewModel
import kotlinx.android.synthetic.main.fragment_user.*

class UserFragment : Fragment() {


    private lateinit var loggedInViewModel: LoggedInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loggedInViewModel = ViewModelProvider(requireActivity()).get(LoggedInViewModel::class.java)

        btnLogout.setOnClickListener {
            loggedInViewModel.logOut()
        }
    }

}