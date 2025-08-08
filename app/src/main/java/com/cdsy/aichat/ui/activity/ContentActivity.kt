package com.cdsy.aichat.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.ActivitySingleBinding
import com.cdsy.aichat.ui.base.BaseActivity
import com.cdsy.aichat.util.setStatusBarColor
import java.io.Serializable

/**
 * @author Zeki
 * @version 1.0
 * @date 2019/10/11 18:10
 */

class ContentActivity : BaseActivity() {
    private lateinit var binding: ActivitySingleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(this, Color.TRANSPARENT)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_single)
        intent?.extras?.let {
            (it.getSerializable(INTENT_EXTRA_KEY_EXTRA_DES) as? Destination)?.let { des ->
                findNavController(R.id.nav_host_fragment).navigate(
                    des.id,
                    it.getBundle(INTENT_EXTRA_KEY_BUNDLE),
                    navOptions {
                        popUpTo(R.id.bridge_fragment) {
                            inclusive = true
                        }
                    }
                )
            }

            (it.getInt(INTENT_EXTRA_KEY_DES_ID)).let { desId ->
                if (desId == 0) return@let
                findNavController(R.id.nav_host_fragment).navigate(
                    desId,
                    null,
                    navOptions {
                        popUpTo(R.id.bridge_fragment) {
                            inclusive = true
                        }
                    })
            }
        }


        /*val lp = binding.clRoot.layoutParams as FrameLayout.LayoutParams
        lp.setMargins(0, 0, 0, 0)
        binding.clRoot.requestLayout()

        if (checkDeviceHasNavigationBar(this)) {
            val h = getNavigationBarHeight(this)
            Timber.d("!!", binding.clRoot.top.toString())
            Timber.d("@@", h.toString())
            if(h>50){
                val lp = binding.clRoot.layoutParams as FrameLayout.LayoutParams
                lp.setMargins(0, 0, 0, getNavigationBarHeight(this))
                binding.clRoot.requestLayout()
            }
        }*/
    }

    companion object {
        const val INTENT_EXTRA_KEY_EXTRA_DES = "extra_des"
        private const val INTENT_EXTRA_KEY_DES_ID = "extra_des_id"
        private const val INTENT_EXTRA_KEY_BUNDLE = "extra_args_bundle"
        private const val BUNDLE_KEY = "extra_args"
        const val RESULT_CODE_REFRESH_MEDIAS = 10001

        fun createIntent(
            context: Context,
            des: Destination,
            s: Serializable? = null,
            bundle: Bundle? = null
        ): Intent {
            return Intent(context, ContentActivity::class.java)
                .putExtra(INTENT_EXTRA_KEY_EXTRA_DES, des)
                .putExtra(
                    INTENT_EXTRA_KEY_BUNDLE,
                    bundle ?: bundleOf(BUNDLE_KEY to s)
                )
        }

        fun needRefreshMedias(resultCode: Int): Boolean {
            return resultCode == RESULT_CODE_REFRESH_MEDIAS
        }

        // desId may not contains in the navGraph
        fun createIntentUnsafe(context: Context, @IdRes desId: Int): Intent {
            return Intent(context, ContentActivity::class.java)
                .putExtra(INTENT_EXTRA_KEY_DES_ID, desId)
        }
    }

    enum class Destination(@IdRes val id: Int) {
        //PersonRecordList(R.id.action_bridge_fragment_to_person_record_list_fragment),
        //RoomJoin(R.id.action_bridge_fragment_to_RoomJoinFragment)
        Search(R.id.action_bridge_fragment_to_search_fragment),
        Setting(R.id.action_bridge_fragment_to_setting_fragment),
        CharacterDetail(R.id.action_bridge_fragment_to_character_detail_fragment),
        Chat(R.id.action_bridge_fragment_to_chat_fragment),
        AccountActivity(R.id.action_bridge_fragment_to_account_activity_fragment),
    }
}
