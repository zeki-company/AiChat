package com.cdsy.aichat.ui.login

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.cdsy.aichat.R
import com.cdsy.aichat.databinding.FragmentSignInOrUpBinding
import com.cdsy.aichat.manager.api.GoogleSignInService
import com.cdsy.aichat.ui.base.BaseFragment
import com.cdsy.aichat.ui.main.MainActivity

class SignInOrUpFragment : BaseFragment<FragmentSignInOrUpBinding, LoginViewModel>(
    LoginViewModel::class.java,
    R.layout.fragment_sign_in_or_up
) {

    override fun initView() {
        binding.viewModel = viewModel

        // AppsFlyer 初始化等待：转圈由 Base 层 progressDialog 统一控制
        viewModel.waitAppsFlyerReady()

        binding.videoView.apply {
            setOnPreparedListener {
                it.isLooping = true
                it.setVolume(0f, 0f)
            }
            setVideoURI(Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.raw.pv_home_1))
            start()
        }

        binding.btnContinueWithGoogle.setOnClickListener {
            viewModel.startGoogleSignIn()
        }

        binding.btnContinueWithEmail.setOnClickListener {
            findNavController().navigate(R.id.action_SignInOrUpFragment_to_SignWithEmailFragment)
        }
        binding.btnContinueWithPhone.setOnClickListener {
            Toast.makeText(
                requireContext(),
                R.string.login_phone_not_supported,
                Toast.LENGTH_SHORT
            ).show()
        }

        viewModel.googleSignInIntentEvent.observeNonNull {
            it.handleIfNot { intent ->
                startActivityForResult(
                    intent,
                    GoogleSignInService.RC_SIGN_IN,
                    null
                )
            }
        }

        viewModel.googleSignInSuccessEvent.observeNonNull {
            it.handleIfNot { success ->
                if (success) onGoogleSignInSuccess()
            }
        }

        viewModel.googleSignInErrorEvent.observeNonNull {
            it.handleIfNot { e ->
                onGoogleSignInError(e)
            }
        }

        setupTermsPrivacy()
    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
        if (!binding.videoView.isPlaying) {
            binding.videoView.start()
        }
    }

    override fun onPause() {
        super.onPause()
        binding.videoView.pause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GoogleSignInService.RC_SIGN_IN) {
            viewModel.handleGoogleSignInResult(data)
        }
    }

    private fun onGoogleSignInSuccess() {
        requireActivity().finish()
        startActivity(Intent(requireContext(), MainActivity::class.java))
    }

    private fun onGoogleSignInError(errorMessage: String) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }


    /**
     * 条款富文本：Terms / Privacy 可点击
     */
    private fun setupTermsPrivacy() {
        val fullText = getString(R.string.login_main_comment_bottom)
        val termsText = getString(R.string.hiyak_pro_purchase_terms_click)
        val privacyText = getString(R.string.hiyak_pro_purchase_policy_click)

        val spannable = SpannableString(fullText)
        val firstPos = fullText.indexOf(termsText).coerceAtLeast(0)
        val secondPos = fullText.indexOf(privacyText).coerceAtLeast(0)

        if (firstPos >= 0) {
            val span = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    Toast.makeText(
                        requireContext(),
                        "Open Terms of Use",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = Color.WHITE
                    ds.isUnderlineText = false
                }
            }
            spannable.setSpan(
                span,
                firstPos,
                firstPos + termsText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                StyleSpan(android.graphics.Typeface.BOLD),
                firstPos,
                firstPos + termsText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        if (secondPos >= 0) {
            val span = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    Toast.makeText(
                        requireContext(),
                        "Open Privacy Policy",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = Color.WHITE
                    ds.isUnderlineText = false
                }
            }
            spannable.setSpan(
                span,
                secondPos,
                secondPos + privacyText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                StyleSpan(android.graphics.Typeface.BOLD),
                secondPos,
                secondPos + privacyText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        binding.tvStatement.apply {
            highlightColor = Color.TRANSPARENT
            movementMethod = LinkMovementMethod.getInstance()
            text = spannable
        }
    }
}

