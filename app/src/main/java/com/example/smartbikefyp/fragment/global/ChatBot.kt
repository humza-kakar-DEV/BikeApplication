package com.example.smartbikefyp.fragment.global

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartbikefyp.adapter.RecyclerViewChatBotAdapter
import com.example.smartbikefyp.databinding.FragmentChatBotBinding
import com.example.smartbikefyp.hideKeyboard
import com.example.smartbikefyp.model.ChatBotRecyclerViewItem
import com.example.smartbikefyp.toast
import com.example.smartbikefyp.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatBot : Fragment() {

    private lateinit var binding: FragmentChatBotBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val recyclerViewChatBotAdapter = RecyclerViewChatBotAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBotBinding.inflate(layoutInflater, container, false)

        binding.chatBotRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = recyclerViewChatBotAdapter
        }

        recyclerViewChatBotAdapter.items = listOf(
            ChatBotRecyclerViewItem.BotChat(
                "test 123",
                "Hi! i am alice how can i help you 😊?"
            )
        )

        binding.sendButton.setOnClickListener {
            binding.yourQueryEditText.editText?.setText("")
            it.hideKeyboard()
        }

        return binding.root
    }
}
































