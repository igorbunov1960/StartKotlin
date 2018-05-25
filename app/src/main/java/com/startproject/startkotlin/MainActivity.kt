package com.startproject.startkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val MENU_ALPHA_ID = 1
    private val MENU_SCALE_ID = 2
    private val MENU_TRANSLATE_ID = 3
    private val MENU_ROTATE_ID = 4
    private val MENU_COMBO_ID = 5

    private val textView by lazy { tv }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerForContextMenu(textView)
        textView.setOnClickListener { openContextMenu(textView) }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        if (v.id == textView.id) {
            menu.add(0, MENU_ALPHA_ID, 0, "alpha")
            menu.add(0, MENU_SCALE_ID, 0, "scale")
            menu.add(0, MENU_TRANSLATE_ID, 0, "translate")
            menu.add(0, MENU_ROTATE_ID, 0, "rotate")
            menu.add(0, MENU_COMBO_ID, 0, "combo")
        }
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        var anim: Animation? = null
        when(item.itemId) {
            MENU_ALPHA_ID -> anim = AnimationUtils.loadAnimation(this, R.anim.myalpha)
            MENU_COMBO_ID -> anim = AnimationUtils.loadAnimation(this, R.anim.mycombo)
            MENU_ROTATE_ID -> anim = AnimationUtils.loadAnimation(this, R.anim.myrotate)
            MENU_SCALE_ID -> anim = AnimationUtils.loadAnimation(this, R.anim.myscale)
            MENU_TRANSLATE_ID -> anim = AnimationUtils.loadAnimation(this, R.anim.mytrans)
        }
        textView.startAnimation(anim)
        return super.onContextItemSelected(item)
    }
}
