package com.wit.stub.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.wit.stub.R
import com.wit.stub.fragments.AccountFragment
import com.wit.stub.fragments.HomeFragment
import com.wit.stub.fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainActivity : AppCompatActivity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val home = HomeFragment()
        val account = AccountFragment()
        val settings = SettingsFragment()

        setMainFragment(home)

        //Listener for bottom nav bar
        top_nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> setMainFragment(home)
                R.id.settings -> setMainFragment(settings)
                R.id.account -> setMainFragment(account)
            }
            true
        }
    }

    /**
     * Set a main fragment by replacing what is in the wrapper Frame Layout.
     * Logs this event in logcat
     */
    private fun setMainFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.wrapper, fragment)
            info("Switched to $fragment" )
            commit()
        }
}