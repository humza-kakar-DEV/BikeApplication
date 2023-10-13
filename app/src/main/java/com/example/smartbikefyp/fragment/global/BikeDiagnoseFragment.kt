package com.example.smartbikefyp.fragment.global

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartbikefyp.R
import com.example.smartbikefyp.adapter.VideoRecyclerViewAdapter
import com.example.smartbikefyp.databinding.FragmentBikeDiagnoseBinding
import com.example.smartbikefyp.log
import com.example.smartbikefyp.model.VideoDetail
import com.example.smartbikefyp.util.ChangeFragment
import com.example.smartbikefyp.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BikeDiagnoseFragment : Fragment() {

    private lateinit var binding: FragmentBikeDiagnoseBinding

    private lateinit var videoListRecyclerViewAdapter: VideoRecyclerViewAdapter

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val videoList = listOf<VideoDetail>(
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fvideo_1_thumbnail.jpg?alt=media&token=73bdf93b-beb0-42ad-b13a-0da3094cdd56",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_1.mp4?alt=media&token=5580591a-466e-434b-b7f7-5d0f2ce0531e",
            title = "Honda CD-70 Top Speed | How Fast Would It Go | Gps tested | 2020",
            description = "Looking to see how fast a Honda CD-70 can go? Look no further! In this video, we've tested the CD-70's top speed using GPS technology, and we're excited to share our results with you. As a smaller bike, the CD-70 is not designed for high speeds, but we'll show you just how fast it can go. So sit back, relax, and join us for this thrilling ride. If you enjoy our content, please don't forget to subscribe for more exciting adventures and motorbike tests.",
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fvideo_2_thumbnail.jpg?alt=media&token=cfb64069-e1d6-4a35-8bfd-64600ce422dc",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_2.mp4?alt=media&token=d0b217c9-eaa4-4633-9aa4-62be3c3979fe",
            title = "HONDA CD 70 new\uD83E\uDEE3model 2024) new bike price)\uD83D\uDE08new 2024 model)",
            description = "The Honda CD 70 is a compact and efficient motorcycle designed for urban commuting and short-distance travel. Powered by a reliable four-stroke, single-cylinder engine with an approximate displacement of 72cc, it offers excellent fuel efficiency, making it a cost-effective choice. The design is simple and functional, featuring a classic look with a round headlamp and a comfortable seat. It typically includes a 4-speed constant mesh gearbox for smooth gear shifts and reliable drum brakes for effective stopping power. The CD 70 is known for its durability, affordability, and popularity, especially in regions where economical and reliable transportation is essential.",
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fvideo_3_thumbnail.jpg?alt=media&token=54c20355-5fc0-47aa-a0ed-385438198243",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_3.mp4?alt=media&token=05fb6792-4c8f-4cbf-8ef3-c89686232923",
            title = "Honda CD70",
            description = "The Honda CD 70 is a lightweight and fuel-efficient motorcycle ideal for urban commuting. Powered by a reliable four-stroke, single-cylinder engine with a displacement of around 72cc, it excels in delivering impressive fuel economy, translating to economical rides. Its design is characterized by simplicity and functionality, featuring a traditional round headlamp and a comfortable seat for the rider. Equipped with a 4-speed constant mesh gearbox, the CD 70 ensures smooth gear transitions and dependable drum brakes for effective stopping. Renowned for its durability and cost-effectiveness, the CD 70 remains a popular choice, especially in areas where reliable and economical transportation is a priority.",
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fvideo_4_thumbnail.jpg?alt=media&token=05745bbb-7609-4ffe-918a-18c2a618d8b1",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_4.mp4?alt=media&token=c1d5a845-a62b-444e-a888-e16fd914a785",
            title = "Top speed CD 70 2024 model || CD 70 new model 2024 || Top speed",
            description = "The Honda CD 70 epitomizes efficiency and practicality in urban mobility. With its compact frame and a robust yet economical four-stroke, single-cylinder engine boasting a displacement of approximately 72cc, it excels in fuel efficiency, presenting a frugal choice for daily commuting. The design is a testament to simplicity, featuring a classic round headlamp, a comfortable seat, and clear instrumentation. Its 4-speed constant mesh gearbox ensures smooth transitions, while the drum brakes provide reliable stopping power. Renowned for its reliability and affordability, the CD 70 holds enduring popularity, particularly in regions valuing reliable and cost-effective transportation solutions.",
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fvideo_5_thumbnail.jpg?alt=media&token=064c59aa-ff24-4889-8413-220f0ed99ca5",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_5.mp4?alt=media&token=5725aee5-d12c-4184-9d5d-977683e7b68f",
            title = "HONDA CD 200 MOTORCYCLE Vs HONDA CG 125 DRAG RACE NO 2",
            description = "The Honda CD 70 stands as a symbol of practicality and efficiency in urban transport. Powered by a sturdy four-stroke, single-cylinder engine with a displacement of around 72cc, it excels in fuel efficiency, making it an economical choice for everyday commuting. The design is straightforward, featuring a classic round headlamp, a comfortable seat, and clear analog instrumentation. Equipped with a 4-speed constant mesh gearbox, it ensures smooth gear shifts, complemented by reliable drum brakes for effective stopping. Renowned for its durability and affordability, the CD 70 enjoys enduring popularity, particularly in areas where reliable and cost-effective transportation is paramount.",
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fvideo_6_thumbnail.jpg?alt=media&token=7ffd062f-0035-4210-9413-90e6d272fa12",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_6.mp4?alt=media&token=427b99eb-f741-44ca-97a5-fb2d427af767",
            title = "FULL METER 120 Kmph HONDA CG 125 SE",
            description = "The Honda CD 70, a hallmark of efficiency and convenience in urban mobility, boasts a compact design and a fuel-sipping four-stroke, single-cylinder engine with a displacement of approximately 72cc. Its standout feature lies in remarkable fuel efficiency, ensuring a thrifty ride for daily commutes. The design is characterized by simplicity, sporting a timeless round headlamp, a rider-friendly seat, and clear analog instrumentation. Fitted with a smooth 4-speed constant mesh gearbox, it enables seamless gear shifts, while the dependable drum brakes offer secure stopping capabilities. Renowned for its reliability and affordability, the CD 70 continues to be a preferred choice, especially in regions prioritizing reliable and cost-effective transportation solutions.",
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fvideo_7_thumbnail.jpg?alt=media&token=05706cb0-a35f-46e7-941c-4692c63b038d",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_7.mp4?alt=media&token=2a29db16-61c3-464d-a0b4-06c28217d3cc",
            title = "History of Honda CG125 | National Bike | PakWheels",
            description = "The Honda CD 70 epitomizes efficiency and practicality in urban commuting. Powered by a fuel-efficient four-stroke, single-cylinder engine with a displacement of approximately 72cc, it strikes a balance between power and economy. Its design is straightforward and functional, featuring a classic round headlamp, a comfortable seat, and uncomplicated analog instrumentation. With a 4-speed constant mesh gearbox, it ensures smooth gear transitions, complemented by reliable drum brakes for effective stopping. Known for its durability and affordability, the CD 70 remains a popular choice, particularly in regions valuing reliable and cost-effective transportation solutions.",
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fvideo_8_thumbnail.jpg?alt=media&token=99b48049-a507-43fc-bb5e-20b9dae2f29f",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_8.mp4?alt=media&token=654c2c85-f759-4156-8bc7-94096e4c387e",
            title = "2023 Honda CG125 SE, Price Update & Expected Changes",
            description = "The Honda CG 125 is a legendary motorcycle renowned for its enduring power and reliability. Boasting a robust four-stroke, single-cylinder engine with a displacement of approximately 125cc, it offers a seamless blend of performance and fuel efficiency. Its design exudes a timeless and practical appeal, characterized by a sturdy frame, classic tank shape, and comfortable seating. The CG 125 is ideal for both city commuting and longer journeys, delivering a smooth and enjoyable riding experience. With its reputation for durability and a strong resale value, the Honda CG 125 remains a popular choice for motorcycle enthusiasts seeking a dependable and powerful ride.",
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fvideo_9_thumbnail.jpg?alt=media&token=198381c1-67c2-484e-920d-7d51ff4f36cc",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_9.mp4?alt=media&token=a9da43dd-56cc-4d65-9ad1-661b959d8a29",
            title = "Honda 125 new model 2024 Sound Test | honda 125 new model 2024 price in pakistan",
            description = "The Honda CG 125 is a true icon in the world of motorcycles, celebrated for its enduring power and reliability. Equipped with a robust four-stroke, single-cylinder engine boasting a displacement of approximately 125cc, it strikes a perfect balance between performance and fuel efficiency. Its design is a harmonious blend of classic and practical elements, featuring a sturdy frame, an iconic tank design, and a comfortable seat. Whether for city commuting or longer journeys, the CG 125 delivers a smooth and enjoyable riding experience. Renowned for its durability and strong resale value, the Honda CG 125 continues to capture the hearts of motorcycle enthusiasts seeking a dependable and powerful ride.",
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fvideo_10_thumbnail.jpg?alt=media&token=777e8f74-c916-4b38-96ad-79f7cd560deb",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_10.mp4?alt=media&token=40278e71-98b2-4fe8-af82-ce766aab95f5",
            title = "Honda Cg 125 new 2024 model || New model 125 || 2023 vs 2024 model big race today",
            description = "The Honda CG 125 stands as a testament to enduring power and reliability in the world of motorcycles. Housing a robust four-stroke, single-cylinder engine with a displacement of around 125cc, it perfectly balances performance and fuel efficiency. Its design embodies a timeless blend of classic and practical elements, featuring a sturdy frame, an iconic tank design, and a comfortable seat that caters to both utility and style. Whether for city commuting or longer journeys, the CG 125 promises a smooth and enjoyable riding experience. Respected for its durability and strong resale value, the Honda CG 125 remains a cherished choice among motorcycle enthusiasts seeking a dependable and potent ride.",
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fvideo_11_thumbnail.jpg?alt=media&token=8dca3f58-847e-45d6-82e8-263b9c30cb8b",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_11.mp4?alt=media&token=1b51a159-5e64-4a7e-8376-dc17b8d7c38a",
            title = "2024 Honda CD 70 Blue Color | PakWheels Bikes",
            description = "The Honda CD 70 is the epitome of practicality and efficiency in urban commuting. Powered by a compact and economical four-stroke, single-cylinder engine with a displacement of approximately 72cc, it excels in fuel efficiency, making it an economical choice for daily travel. Its design is simplistic and functional, featuring a classic round headlamp, a comfortable seat, and clear analog instrumentation. Equipped with a 4-speed constant mesh gearbox, it ensures smooth gear transitions, and reliable drum brakes offer effective stopping power. Known for its durability and affordability, the CD 70 remains a popular choice, particularly in regions valuing reliable and cost-effective transportation.",
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fvideo_12_thumbnail.jpg?alt=media&token=7cceb60b-967f-4a8b-900d-21667cddab6e",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_12.mp4?alt=media&token=3a3d90ed-20c7-4191-b9e5-daf6d82cc2d3",
            title = "HONDA NEW CD70 SOUND TEST 2024 MODEL",
            description = "The Honda CD 70 is a quintessential example of efficiency and practicality in urban commuting. Powered by a fuel-efficient four-stroke, single-cylinder engine with a displacement of around 72cc, it strikes a fine balance between power and economy. Its design is simple yet functional, featuring a classic round headlamp, a comfortable seat, and clear analog instrumentation. With a 4-speed constant mesh gearbox, it ensures smooth gear transitions, while reliable drum brakes provide efficient stopping power. Known for its durability and affordability, the CD 70 remains a popular choice, particularly in areas valuing reliable and cost-effective transportation solutions.",
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fvideo_13_thumbnail.jpg?alt=media&token=4ef84331-9a24-4172-aa92-594c6db1a4ad",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_13.mp4?alt=media&token=417c962d-7772-4a07-a995-94d0853a33c7",
            title = "Honda CD70 2024 Quick Walkaround!",
            description = "The Honda CD 70 is the epitome of practicality and efficiency in urban commuting. Powered by a compact and economical four-stroke, single-cylinder engine with a displacement of approximately 72cc, it excels in fuel efficiency, making it an economical choice for daily travel. Its design is simplistic and functional, featuring a classic round headlamp, a comfortable seat, and clear analog instrumentation. Equipped with a 4-speed constant mesh gearbox, it ensures smooth gear transitions, while reliable drum brakes offer effective stopping power. Known for its durability and affordability, the CD 70 remains a popular choice, particularly in regions valuing reliable and cost-effective transportation.",
        ),
        VideoDetail(
            thumbnailLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fthumbnails%2Fvideo_14_thumbnail.jpg?alt=media&token=bf79ac71-6b6f-42c7-ac86-e685ce6f3424",
            videoLink = "https://firebasestorage.googleapis.com/v0/b/smart-bike-fyp.appspot.com/o/app%20assets%2Fvideos%2Fvideo_14.mp4?alt=media&token=2bebdc9b-1d78-4c14-af0f-948cd37f7046",
            title = "After 500 km Sound Test | honda cd 70 new model 202",
            description = "The Honda CD 70 epitomizes efficiency and practicality in urban mobility. Powered by a compact and economical four-stroke, single-cylinder engine with a displacement of approximately 72cc, it strikes a balance between power and fuel efficiency. Its design is straightforward and functional, featuring a classic round headlamp, a comfortable seat, and clear analog instrumentation. Equipped with a 4-speed constant mesh gearbox, it ensures smooth gear transitions, while reliable drum brakes offer efficient stopping power. Renowned for its durability and affordability, the CD 70 continues to be a popular choice, especially in regions valuing reliable and cost-effective transportation solutions."
        ),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBikeDiagnoseBinding.inflate(layoutInflater, container, false)

        videoListRecyclerViewAdapter = VideoRecyclerViewAdapter(requireContext()) { position ->
            requireContext().log("video data: ${videoList[position]}")
            sharedViewModel.setVideoDetail(videoDetail = videoList[position])
            sharedViewModel.setChangeFragment(ChangeFragment.VIDEO_PLAYER_FRAGMENT)
        }

        videoListRecyclerViewAdapter.items = videoList

        binding.videosRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.videosRecyclerView.adapter = videoListRecyclerViewAdapter

        return binding.root
    }
}