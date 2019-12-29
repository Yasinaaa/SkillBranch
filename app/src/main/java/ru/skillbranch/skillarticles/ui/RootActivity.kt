package ru.skillbranch.skillarticles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.layout_bottombar.*
import kotlinx.android.synthetic.main.activity_root.*
import kotlinx.android.synthetic.main.layout_submenu.*
import ru.skillbranch.skillarticles.extensions.dpToIntPx
import ru.skillbranch.skillarticles.viewmodels.ArticleState
import ru.skillbranch.skillarticles.viewmodels.ArticleViewModel
import ru.skillbranch.skillarticles.viewmodels.Notify
import ru.skillbranch.skillarticles.viewmodels.ViewModelFactory
import androidx.lifecycle.Observer

class RootActivity : AppCompatActivity() {

    private lateinit var viewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        setupToolbar()
        setupBottombar()
        setupSubmenu()

        val vmFactory = ViewModelFactory("0")
        viewModel = ViewModelProviders.of(this, vmFactory).get(ArticleViewModel::class.java)
        viewModel.observeState(this){
            renderUi(it)
        }
        viewModel.observeNotifications(this){
            renderNotification(it)
        }
    }

    private fun renderUi(data: ArticleState){
        btn_settings.isChecked = data.isShowMenu
        if (data.isShowMenu) submenu.open() else submenu.close()
        btn_like.isChecked = data.isLike
        btn_bookmark.isChecked = data.isBookmark
        switch_mode.isChecked = data.isDarkMode
        delegate.localNightMode =
            if (data.isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

        if (data.isBigText) {
            tv_text_content.textSize = 18f
            btn_text_up.isChecked = true
            btn_text_down.isChecked = false
        } else {
            tv_text_content.textSize = 14f
            btn_text_up.isChecked = false
            btn_text_down.isChecked = true
        }

        tv_text_content.text =
            if (data.isLoadingContent) "loading..." else data.content.first() as String
        toolbar.title = data.title ?: "loading"
        toolbar.subtitle = data.category ?: "loading"
        if (data.categoryIcon != null) toolbar.logo = getDrawable(data.categoryIcon as Int)
    }

    private fun setupToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val logo = if(toolbar.childCount>2) toolbar.getChildAt(2) as ImageView else null
        logo?.scaleType = ImageView.ScaleType.CENTER_CROP
        val lp = logo?.layoutParams as? Toolbar.LayoutParams
        lp?.let{
            it.width = this.dpToIntPx(40)
            it.height = this.dpToIntPx(40)
            it.marginEnd = this.dpToIntPx(16)
            logo.layoutParams = it
        }
    }

    private fun renderNotification(notify: Notify){
        val snackbar = Snackbar.make(coordinator_container, notify.message, Snackbar.LENGTH_LONG)
            .setAnchorView(bottombar)

        when(notify){
            is Notify.TextMessage -> { }

            is Notify.ActionMessage -> {
                snackbar.setActionTextColor(getColor(R.color.color_accent_dark))
                snackbar.setAction(notify.actionLabel){
                    notify.actionHandler?.invoke()
                }
            }

            is Notify.ErrorMessage -> {
                with(snackbar){
                    setBackgroundTint(getColor(R.color.design_default_color_error))
                    setTextColor(getColor(android.R.color.white))
                    snackbar.setActionTextColor(getColor(android.R.color.white))
                    snackbar.setAction(notify.errLabel){
                        notify?.errHandler?.invoke()
                    }
                }
            }
        }
        snackbar.show()
    }

    private fun setupSubmenu(){
        btn_text_up.setOnClickListener{viewModel.handleUpText()}
        btn_text_down.setOnClickListener{viewModel.handleDownText()}
        switch_mode.setOnClickListener{viewModel.handleNightMode()}
    }

    private fun setupBottombar(){
        btn_like.setOnClickListener{viewModel.handleLike()}
        btn_bookmark.setOnClickListener{viewModel.handleBookmark()}
        btn_share.setOnClickListener{viewModel.handleShare()}
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            viewModel.handleSearchMode(true)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        if (searchItem != null) {
            setupMenu(searchItem)
            viewModel.searchMode.observe(this, Observer {
                it.getContentIfNotHandled()?.let { pair ->
                    if(pair.first){
                        val searchView = searchItem.actionView as SearchView
                        searchItem.expandActionView()
                        searchView.setQuery(pair.second, false)
                        searchView.clearFocus()

                    }
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupMenu(searchItem: MenuItem) {

        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                viewModel.handleSearch(text)
                return true
            }

        })
        searchItem.setOnActionExpandListener(object :
            MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                viewModel.handleSearchMode(true)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                viewModel.handleSearchMode(false)
                return true
            }
        })

        val textView = searchView.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)
        textView.setTextColor(getColor(R.color.color_on_article_bar))
        textView.setHintTextColor(getColor(R.color.color_on_article_bar))
    }
}
