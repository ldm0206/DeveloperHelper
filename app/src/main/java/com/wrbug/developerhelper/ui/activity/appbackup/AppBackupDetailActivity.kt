package com.wrbug.developerhelper.ui.activity.appbackup

import android.app.ActionBar.LayoutParams
import android.app.backup.BackupManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.IntentCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.wrbug.developerhelper.R
import com.wrbug.developerhelper.base.BaseActivity
import com.wrbug.developerhelper.base.ExtraKey
import com.wrbug.developerhelper.commonutil.dpInt
import com.wrbug.developerhelper.databinding.ActivityAppBackupDetailBinding
import com.wrbug.developerhelper.model.entity.BackupAppData
import com.wrbug.developerhelper.model.entity.BackupAppItemInfo
import com.wrbug.developerhelper.ui.adapter.ExMultiTypeAdapter
import com.wrbug.developerhelper.ui.decoration.SpaceItemDecoration
import com.wrbug.developerhelper.util.BackupUtils
import com.wrbug.developerhelper.util.loadImage
import com.yanzhenjie.recyclerview.SwipeMenuItem

class AppBackupDetailActivity : BaseActivity() {

    companion object {
        fun start(context: Context, info: BackupAppData) {
            context.startActivity(Intent(context, AppBackupDetailActivity::class.java).apply {
                putExtra(ExtraKey.DATA, info)
            })
        }
    }


    private val adapter by ExMultiTypeAdapter.get()

    private val info by lazy {
        IntentCompat.getSerializableExtra(intent, ExtraKey.DATA, BackupAppData::class.java)
    }
    private val binding by lazy {
        ActivityAppBackupDetailBinding.inflate(layoutInflater)
    }
    private var listCache = arrayListOf<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        loadData(info?.backupMap)
    }

    private fun loadData(map: Map<String, BackupAppItemInfo>?) {
        val list = map?.values?.sortedByDescending { it.time } ?: emptyList()
        listCache.clear()
        listCache.addAll(list)
        setupList()
    }

    private fun setupList() {
        if (listCache.isEmpty()) {
            adapter.showEmpty()
        } else {
            adapter.loadData(listCache)
        }
    }

    private fun initView() {
        binding.appBar.setSubTitle(info?.appName)
        binding.rvAppBackupList.layoutManager = LinearLayoutManager(this)
        binding.rvAppBackupList.addItemDecoration(SpaceItemDecoration.standard)
        adapter.register(BackupDetailDelegate(info?.appName.orEmpty()))
        binding.rvAppBackupList.setSwipeMenuCreator { leftMenu, rightMenu, position ->
            rightMenu.addMenuItem(SwipeMenuItem(this).apply {
                text = getString(R.string.item_swipe_menu_meme)
                width = 56.dpInt()
                height = LayoutParams.MATCH_PARENT
                setTextColorResource(R.color.material_text_color_white_text)
                setBackgroundColorResource(R.color.colorAccent)
            })
            rightMenu.addMenuItem(SwipeMenuItem(this).apply {
                text = getString(R.string.item_swipe_menu_delete)
                width = 56.dpInt()
                height = LayoutParams.MATCH_PARENT
                setTextColorResource(R.color.material_text_color_white_text)
                setBackgroundColorResource(R.color.material_color_red_600)
            })
        }
        binding.rvAppBackupList.setOnItemMenuClickListener { menuBridge, adapterPosition ->
            menuBridge.closeMenu()
            when (menuBridge.position) {
                0 -> {

                }

                1 -> {
                    showDialog(
                        R.string.notice,
                        R.string.delete_backup_notice,
                        R.string.ok,
                        R.string.cancel,
                        {
                            deleteBackup(adapter.items[adapterPosition] as? BackupAppItemInfo)
                            dismiss()
                        },
                        {
                            dismiss()
                        })
                }

                else -> {}
            }
        }
        binding.rvAppBackupList.adapter = adapter
    }

    private fun deleteBackup(backupAppItemInfo: BackupAppItemInfo?) {
        backupAppItemInfo ?: return
        BackupUtils.deleteBackupItem(backupAppItemInfo.packageName, backupAppItemInfo.backupFile)
            .subscribe({
                loadData(it.backupMap)
            }, {
                showSnack(getString(R.string.delete_backup_failed_retry))
            })
    }
}