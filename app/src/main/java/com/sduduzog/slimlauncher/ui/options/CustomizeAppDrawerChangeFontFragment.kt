package com.sduduzog.slimlauncher.ui.options

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.Navigation
import com.sduduzog.slimlauncher.R
import com.sduduzog.slimlauncher.datasource.UnlauncherDataSource
import com.sduduzog.slimlauncher.utils.BaseFragment
import com.sduduzog.slimlauncher.utils.setTypeFace
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.customize_app_drawer_fragment.customize_app_drawer_change_font
import kotlinx.android.synthetic.main.customize_app_drawer_fragment.customize_app_drawer_fragment_visible_apps
import kotlinx.android.synthetic.main.customize_app_drawer_fragment_fonts.customize_app_drawer_fragment_change_font_fragment
import kotlinx.android.synthetic.main.customize_app_drawer_fragment_fonts.radio_group_available_fonts
import javax.inject.Inject

@AndroidEntryPoint
class CustomizeAppDrawerChangeFontFragment : BaseFragment() {

    @Inject
    lateinit var unlauncherDataSource: UnlauncherDataSource

    override fun getFragmentView(): ViewGroup = customize_app_drawer_fragment_change_font_fragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.customize_app_drawer_fragment_fonts, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefsRepo = unlauncherDataSource.corePreferencesRepo
        radio_group_available_fonts.setOnCheckedChangeListener { group, checkedId ->
            val tag = group.findViewById<RadioButton>(checkedId).tag
            prefsRepo.updateAppFont(tag as String)
            try {
                setTypeFace(requireContext().applicationContext, getFontFromTag(tag))
            } catch (e: Exception) {
                Log.e("Font", e.message!!)
            }

        }
        prefsRepo.liveData().observe(viewLifecycleOwner) {
            val view = radio_group_available_fonts.findViewWithTag<RadioButton>(it.chosenFont)
            // no chosen font found, setting to default
            val id =
                view?.id ?: radio_group_available_fonts.findViewWithTag<RadioButton>("ubuntu").id
            radio_group_available_fonts.check(id)
        }
    }

    @FontRes
    private fun getFontFromTag(tag: String): Int {
        return when (tag) {
            "ubuntu" -> R.font.ubuntu
            "lato" -> R.font.lato
            "carme" -> R.font.carme
            "roboto" -> R.font.roboto
            else -> R.font.ubuntu
        }
    }
}