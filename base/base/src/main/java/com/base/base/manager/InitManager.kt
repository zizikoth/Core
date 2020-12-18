package com.base.base.manager

import android.app.Application
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.base.base.BuildConfig
import com.base.base.R
import com.base.base.config.AppConfig
import com.base.base.ui.status.ServerErrorCallback
import com.base.base.ui.status.LoadCallback
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ProcessUtils
import com.blankj.utilcode.util.Utils
import com.frame.core.utils.extra.dimen
import com.load.status.core.LoadStatus
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * title:
 * describe:
 *
 * @author memo
 * @date 2020-12-18 10:38 PM
 * @email zhou_android@163.com
 *
 * Talk is cheap, Show me the code.
 */
object InitManager {

    /**
     * 在Application进行初始化
     */
    fun initInApp(app: Application) {
        if (ProcessUtils.isMainProcess()) {
            Utils.init(app)
            LogUtils.getConfig().setLogSwitch(AppConfig.isOpenLog)

            // ARouter
            if (BuildConfig.DEBUG) {
                ARouter.openLog()
                ARouter.openDebug()
            }
            ARouter.init(app)
        }
    }

    /**
     * 延迟初始化
     */
    fun initLater() {
        // LoadStatus
        LoadStatus.beginBuilder()
            .addCallback(LoadCallback())
            .addCallback(ServerErrorCallback())
            .setDefaultCallback(LoadCallback::class.java)
            .commit()

        // 初始化刷新框架
        SmartRefreshLayout.setDefaultRefreshInitializer { _, refreshLayout ->
            refreshLayout
                .setEnableAutoLoadMore(false)
                .setEnableOverScrollBounce(true)
                .setEnableOverScrollDrag(true)
                .setEnableLoadMoreWhenContentNotFull(false)
        }
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            val footer = BallPulseFooter(context)
                .setNormalColor(ContextCompat.getColor(context, R.color.color_666666))
                .setAnimatingColor(ContextCompat.getColor(context, R.color.color_666666))
            footer.minimumHeight = dimen(R.dimen.dp50).toInt()
            footer.minimumWidth = dimen(R.dimen.dp50).toInt()
            footer
        }

    }
}