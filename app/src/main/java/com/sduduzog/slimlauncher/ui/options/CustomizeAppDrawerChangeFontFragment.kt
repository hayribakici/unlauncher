package com.sduduzog.slimlauncher.ui.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.navigation.Navigation
import com.sduduzog.slimlauncher.R
import com.sduduzog.slimlauncher.datasource.UnlauncherDataSource
import com.sduduzog.slimlauncher.utils.BaseFragment
import kotlinx.android.synthetic.main.customize_app_drawer_fragment.customize_app_drawer_change_font
import kotlinx.android.synthetic.main.customize_app_drawer_fragment.customize_app_drawer_fragment_visible_apps
import kotlinx.android.synthetic.main.customize_app_drawer_fragment_fonts.customize_app_drawer_fragment_change_font_fragment
import kotlinx.android.synthetic.main.customize_app_drawer_fragment_fonts.radio_group_available_fonts
import javax.inject.Inject

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

        radio_group_available_fonts.setOnCheckedChangeListener { group, checkedId -> {
            val prefsRepo = unlauncherDataSource.corePreferencesRepo
            val tag = group.findViewById<RadioButton>(checkedId).tag

//            prefsRepo.
        } }
    }
}