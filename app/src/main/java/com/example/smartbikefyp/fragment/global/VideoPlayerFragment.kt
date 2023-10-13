package com.example.smartbikefyp.fragment.global

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.smartbikefyp.R
import com.example.smartbikefyp.databinding.FragmentVideoPlayerBinding
import com.example.smartbikefyp.log
import com.example.smartbikefyp.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VideoPlayerFragment : Fragment() {

    private lateinit var binding: FragmentVideoPlayerBinding

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var player: ExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoPlayerBinding.inflate(layoutInflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                sharedViewModel.observeVideoDetail.collect { videoDetail ->
                    videoDetail?.apply {
                        requireContext().log("VIDEO COROUTINE SCOPE!!!")
                        player = ExoPlayer.Builder(requireContext()).build().apply {
                            addMediaItem(
                                MediaItem.fromUri(videoDetail.videoLink)
                            )
                            prepare()
                            playWhenReady = true
                        }
                        binding.playerView.player = player
                        binding.titleTextView.text = videoDetail.title
                        binding.descriptionTextView.text = videoDetail.description
                    }
                }
            }
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        player?.stop()
    }

}