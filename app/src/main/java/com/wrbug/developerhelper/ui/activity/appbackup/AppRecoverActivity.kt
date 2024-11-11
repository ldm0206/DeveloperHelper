package com.wrbug.developerhelper.ui.activity.appbackup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.recyclerview.widget.LinearLayoutManager
import com.wrbug.developerhelper.base.BaseActivity
import com.wrbug.developerhelper.base.ExtraKey
import com.wrbug.developerhelper.databinding.ActivityAppRecoverBinding
import com.wrbug.developerhelper.model.entity.BackupAppItemInfo
import com.wrbug.developerhelper.ui.adapter.ExMultiTypeAdapter

class AppRecoverActivity : BaseActivity() {

    companion object {
        fun start(context: Context, backupAppItemInfo: BackupAppItemInfo) {
            context.startActivity(Intent(context, AppRecoverActivity::class.java).apply {
                putExtra(ExtraKey.KEY_1, backupAppItemInfo as Parcelable)
            })
        }
    }

    private val backupAppItemInfo: BackupAppItemInfo? by lazy {
        intent?.getParcelableExtra(ExtraKey.KEY_1)
    }
    private val adapter by ExMultiTypeAdapter.get()

    private val binding by lazy {
        ActivityAppRecoverBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.cbApk.isEnabled = backupAppItemInfo?.backupApk == true
        binding.cbData.isEnabled = backupAppItemInfo?.backupData == true
        binding.cbAndroidData.isEnabled = backupAppItemInfo?.backupAndroidData == true
        binding.rvTimeLine.layoutManager = LinearLayoutManager(this)
        binding.rvTimeLine.adapter = adapter
    }
}