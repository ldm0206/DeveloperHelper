package com.wrbug.developerhelper.ui.activity.appbackup

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.wrbug.developerhelper.R
import com.wrbug.developerhelper.databinding.ItemBackupDetailInfoBinding
import com.wrbug.developerhelper.model.entity.BackupAppItemInfo
import com.wrbug.developerhelper.ui.adapter.delegate.BaseItemViewBindingDelegate
import com.wrbug.developerhelper.util.format
import com.wrbug.developerhelper.util.getColor
import com.wrbug.developerhelper.util.getString

class BackupDetailDelegate(private val appName: String) :
    BaseItemViewBindingDelegate<BackupAppItemInfo, ItemBackupDetailInfoBinding>() {
    override fun onBindViewHolder(binding: ItemBackupDetailInfoBinding, item: BackupAppItemInfo) {
        binding.tvTime.text = item.time.format()
        binding.tvTitle.text = item.memo.ifEmpty {
            binding.root.context.getString(
                R.string.backup_default_meme,
                appName
            )
        }
        binding.tvVersion.text = "${item.versionName}(${item.versionCode})"
        binding.tvBackupApk.setStatusColor(item.backupApk)
        binding.tvBackupAndroidData.setStatusColor(item.backupAndroidData)
        binding.tvBackupData.setStatusColor(item.backupData)
    }

    private fun TextView.setStatusColor(enable: Boolean) {
        val color = if (enable) {
            R.color.material_color_green_500.getColor()
        } else {
            R.color.material_color_red_500.getColor()
        }
        setTextColor(color)
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemBackupDetailInfoBinding {
        return ItemBackupDetailInfoBinding.inflate(inflater, parent, false)
    }
}