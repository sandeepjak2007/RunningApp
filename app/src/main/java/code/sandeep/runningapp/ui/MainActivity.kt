package code.sandeep.runningapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import code.sandeep.runningapp.R
import code.sandeep.runningapp.databinding.ActivityMainBinding
import code.sandeep.runningapp.other.ACTION_SHOW_TRACKING_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.rootView)
        setSupportActionBar(binding.toolbar)

        binding.bottomNavigationView.setupWithNavController(binding.flFragment.getChildAt(0).findNavController())
        binding.bottomNavigationView.setOnNavigationItemReselectedListener { /** NO OP */ }
        binding.flFragment.getChildAt(0).findNavController().addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.settingsFragment, R.id.runFragment, R.id.statisticsFragment ->
                    binding.bottomNavigationView.visibility =
                    View.VISIBLE
                else -> binding.bottomNavigationView.visibility = View.GONE
            }

        }
        navigateToTrackingFragmentIfNeeded(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_TRACKING_FRAGMENT) {
            binding.flFragment.getChildAt(0).findNavController().navigate(R.id.action_global_trackingFragment)
        }
    }
}