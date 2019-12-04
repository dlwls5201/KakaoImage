package com.tistory.blackjin.kakaoimage.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tistory.blackjin.kakaoimage.R
import com.tistory.blackjin.kakaoimage.databinding.ActivityDetailBinding
import com.tistory.blackjin.kakaoimage.ui.base.BaseActivity

class DetailActivity : BaseActivity<ActivityDetailBinding>(R.layout.activity_detail) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {

        intent.getStringExtra(KEY_IMAGE_URL)?.let { imageUrl ->

            val aspect = intent.getFloatExtra(KEY_IMAGE_ASPECT, 1f)

            with(binding.itemImageView.sdvItemImage) {
                aspectRatio = aspect
                setImageURI(imageUrl)
            }

        } ?: error("must be image url")
    }

    companion object {

        private const val KEY_IMAGE_URL = "image_url"

        private const val KEY_IMAGE_ASPECT = "image_aspect"

        fun startDetailActivity(context: Context, imageUrl: String, aspect: Float) {
            context.startActivity(
                Intent(context, DetailActivity::class.java).apply {
                    putExtra(KEY_IMAGE_URL, imageUrl)
                    putExtra(KEY_IMAGE_ASPECT, aspect)
                }
            )
        }
    }
}
