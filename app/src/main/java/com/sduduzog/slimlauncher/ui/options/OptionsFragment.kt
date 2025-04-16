package com.sduduzog.slimlauncher.ui.options

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.navigation.Navigation
import com.jkuester.unlauncher.datasource.DataRepository
import com.jkuester.unlauncher.datasource.setKeepDeviceWallpaper
import com.jkuester.unlauncher.datasource.toggleHideStatusBar
import com.jkuester.unlauncher.datastore.proto.CorePreferences
import com.jkuester.unlauncher.dialog.AlignmentFormatDialog
import com.jkuester.unlauncher.dialog.ClockTypeDialog
import com.jkuester.unlauncher.dialog.ThemeDialog
import com.jkuester.unlauncher.dialog.TimeFormatDialog
import com.jkuester.unlauncher.fragment.WithFragmentLifecycle
import com.sduduzog.slimlauncher.R
import com.sduduzog.slimlauncher.databinding.OptionsFragmentBinding
import com.sduduzog.slimlauncher.utils.BaseFragment
import com.sduduzog.slimlauncher.utils.capitalize
import com.sduduzog.slimlauncher.utils.createTitleAndSubtitleText
import com.sduduzog.slimlauncher.utils.getStringArray
import com.sduduzog.slimlauncher.utils.isDefaultLauncher
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OptionsFragment : BaseFragment() {
    @Inject
    lateinit var iActivity: ComponentActivity
    @Inject @WithFragmentLifecycle
    lateinit var corePreferencesRepo: DataRepository<CorePreferences>

    private lateinit var changeThemeTextView: TextView

    private lateinit var timeFormatTextView: TextView
    private lateinit var timeFormatString: String

    private lateinit var clockTypeTextView: TextView
    private lateinit var clockTypeString: TextView

    private lateinit var alignmentTextView: TextView
    private lateinit var toggleStatusBarTextView: TextView
    private lateinit var customizeAppDrawerTextView: TextView

    override fun getFragmentView(): ViewGroup = OptionsFragmentBinding.bind(
        requireView()
    ).optionsFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.options_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val optionsFragment = OptionsFragmentBinding.bind(requireView())

        optionsFragment.optionsFragmentDeviceSettings.setOnClickListener {
            val intent = Intent(Settings.ACTION_SETTINGS)
            launchActivity(it, intent)
        }
        optionsFragment.optionsFragmentBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        optionsFragment.optionsFragmentDeviceSettings.setOnLongClickListener {
            val intent = Intent(Settings.ACTION_HOME_SETTINGS)
            launchActivity(it, intent)
            true
        }
        changeThemeTextView = optionsFragment.optionsFragmentChangeTheme
        changeThemeTextView.text = createTitleAndSubtitleText(
            requireContext(), changeThemeTextView.text,
            requireContext().getStringArray(R.array.themes_array)[corePreferencesRepo.get().themeValue])
        changeThemeTextView.setOnClickListener {
            ThemeDialog().showNow(childFragmentManager, null)
        }

        timeFormatTextView = optionsFragment.optionsFragmentChooseTimeFormat
        timeFormatString = requireContext().getStringArray(
            R.array.time_format_array
        )[corePreferencesRepo.get().timeFormatValue]
        timeFormatTextView.text = createTitleAndSubtitleText(
            requireContext(),
            getString(R.string.options_fragment_choose_time_format),
            timeFormatString
        )
        timeFormatTextView.setOnClickListener {
            TimeFormatDialog().showNow(childFragmentManager, null)
        }

        clockTypeTextView = optionsFragment.optionsFragmentChooseClockType
        clockTypeTextView.text = createTitleAndSubtitleText(requireContext(),
            getString(R.string.options_fragment_choose_clock_type),
            requireContext().getStringArray(R.array.clock_type_array)[corePreferencesRepo.get().clockTypeValue])
        clockTypeTextView.setOnClickListener {
            ClockTypeDialog().showNow(childFragmentManager, "CLOCK_TYPE_CHOOSER")
        }

        alignmentTextView = optionsFragment.optionsFragmentChooseAlignment
        alignmentTextView.text = createTitleAndSubtitleText(requireContext(),
            getString(R.string.options_fragment_choose_alignment),
            requireContext().getStringArray(R.array.alignment_format_array)
                [corePreferencesRepo.get().alignmentFormatValue].capitalize())
        alignmentTextView.setOnClickListener {
            AlignmentFormatDialog().showNow(childFragmentManager, "ALIGNMENT_CHOOSER")
        }

        toggleStatusBarTextView = optionsFragment.optionsFragmentToggleStatusBar
        toggleStatusBarTextView.text = createTitleAndSubtitleText(requireContext(),
            getString(R.string.options_fragment_toggle_status_bar),
            if (corePreferencesRepo.get().hideStatusBar) {
                getString(R.string.hidden)
            } else {
                getString(R.string.shown)
            }.capitalize()
        )
        toggleStatusBarTextView.setOnClickListener {
            corePreferencesRepo.updateAsync(toggleHideStatusBar())
        }
        optionsFragment.optionsFragmentCustomizeQuickButtons.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_optionsFragment_to_customiseQuickButtonsFragment
            )
        )
        optionsFragment.optionsFragmentCustomizeAppDrawer.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_optionsFragment_to_customiseAppDrawerFragment
            )
        )
    }

    override fun onStart() {
        super.onStart()
        // setting up the switch text, since changing the default launcher re-starts the activity
        // this should able to adapt to it.
        setupAutomaticDeviceWallpaperSwitch()
        corePreferencesRepo.observe {
            changeThemeTextView.text = createTitleAndSubtitleText(
                requireContext(),
                getString(R.string.options_fragment_change_theme),
                requireContext().getStringArray(R.array.themes_array)[it.themeValue])
            timeFormatTextView.text = createTitleAndSubtitleText(
                requireContext(),
                getString(R.string.options_fragment_choose_time_format),
                requireContext().getStringArray(R.array.time_format_array)[it.timeFormatValue]
            )
            clockTypeTextView.text = createTitleAndSubtitleText(requireContext(),
                getString(R.string.options_fragment_choose_clock_type),
                requireContext().getStringArray(R.array.clock_type_array)[it.clockTypeValue])
            alignmentTextView.text = createTitleAndSubtitleText(requireContext(),
                getString(R.string.options_fragment_choose_alignment),
                requireContext().getStringArray(R.array.alignment_format_array)
                    [corePreferencesRepo.get().alignmentFormatValue].capitalize())
            toggleStatusBarTextView.text = createTitleAndSubtitleText(requireContext(),
                getString(R.string.options_fragment_toggle_status_bar),
                if (corePreferencesRepo.get().hideStatusBar) {
                    getString(R.string.hidden)
                } else {
                    getString(R.string.shown)
                }.capitalize()
            )
        }
    }

    private fun setupAutomaticDeviceWallpaperSwitch() {
        val appIsDefaultLauncher = isDefaultLauncher(iActivity)
        val optionsFragment = OptionsFragmentBinding.bind(requireView())
        setupDeviceWallpaperSwitchText(optionsFragment, appIsDefaultLauncher)
        optionsFragment.optionsFragmentAutoDeviceThemeWallpaper.isEnabled = appIsDefaultLauncher

        corePreferencesRepo.observe {
            // always uncheck once app isn't default launcher
            optionsFragment.optionsFragmentAutoDeviceThemeWallpaper
                .isChecked = appIsDefaultLauncher && !it.keepDeviceWallpaper
        }
        optionsFragment.optionsFragmentAutoDeviceThemeWallpaper
            .setOnCheckedChangeListener { _, checked ->
                corePreferencesRepo.updateAsync(setKeepDeviceWallpaper(!checked))
            }
    }

    /**
     * Adds a hint text underneath the default text when app is not the default launcher.
     */
    private fun setupDeviceWallpaperSwitchText(optionsFragment: OptionsFragmentBinding, appIsDefaultLauncher: Boolean) {
        val text = if (appIsDefaultLauncher) {
            getText(R.string.customize_app_drawer_fragment_auto_theme_wallpaper_text)
        } else {
            buildSwitchTextWithHint()
        }
        optionsFragment.optionsFragmentAutoDeviceThemeWallpaper.text = text
    }

    private fun buildSwitchTextWithHint(): CharSequence {
        val titleText = getText(R.string.customize_app_drawer_fragment_auto_theme_wallpaper_text)
        // have a title text and a subtitle text to indicate that adapting the
        // wallpaper can only be done when app it the default launcher
        val subTitleText = getText(
            R.string.customize_app_drawer_fragment_auto_theme_wallpaper_subtext_no_default_launcher
        )
        return createTitleAndSubtitleText(requireContext(), titleText, subTitleText)
    }
}
