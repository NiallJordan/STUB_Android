package com.wit.stub

import android.accounts.Account
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
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
        bottom_nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> setMainFragment(home)
                R.id.settings -> setMainFragment(settings)
                R.id.account -> setMainFragment(account)
            }
            true
        }
    }

    private fun setMainFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.wrapper, fragment)
            info("Switched to $fragment" )
            commit()
        }
}