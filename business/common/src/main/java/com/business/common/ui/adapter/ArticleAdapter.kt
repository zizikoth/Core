package com.business.common.ui.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.base.entity.remote.*
import com.base.base.utils.IconHelper
import com.base.base.utils.onItemClickListener
import com.base.base.widget.recyclerview.StartSnapHelper
import com.business.common.R
import com.chad.library.adapter.base.BaseProviderMultiAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.frame.core.utils.extra.*

/**
 * title:文章列表适配器
 * describe:
 *
 * @author memo
 * @date 2021-01-30 4:10 PM
 * @email zhou_android@163.com
 *
 * Talk is cheap, Show me the code.
 */

class ArticleAdapter : BaseProviderMultiAdapter<Article>() {

    init {
        addItemProvider(GridProvider())
        addItemProvider(TitleProvider())
        addItemProvider(NewArticleProvider())
        addItemProvider(NormalArticleProvider())
    }

    var onNewArticleClick: (title: String, url: String) -> Unit = { _, _ -> }

    override fun getItemType(data: List<Article>, position: Int): Int {
        return data[position].itemType
    }
}

private class GridProvider(
    override val itemViewType: Int = HOME_TYPE_SYSTEM_GRID,
    override val layoutId: Int = R.layout.layout_item_system_grid) : BaseItemProvider<Article>() {
    override fun convert(helper: BaseViewHolder, item: Article) {
        helper.setText(R.id.mTvTitle, item.title)
        helper.setImageResource(R.id.mIvLogo, IconHelper.randomIcon(item.id))
    }
}

private class TitleProvider(
    override val itemViewType: Int = HOME_TYPE_TITLE,
    override val layoutId: Int = R.layout.layout_item_title) : BaseItemProvider<Article>() {
    override fun convert(helper: BaseViewHolder, item: Article) {
        helper.run {
            setText(R.id.mTvTitle, item.title)
            setVisible(R.id.mTvMore, item.hasMore)
            getView<TextView>(R.id.mTvMore).round(color(R.color.color_shape), 12.5f.dp2px)
        }
    }
}

private class NewArticleProvider(
    override val itemViewType: Int = HOME_TYPE_NEW_ARTICLE,
    override val layoutId: Int = R.layout.layout_item_new_article) : BaseItemProvider<Article>() {

    private val mAdapter = NewArticleAdapter().apply {
        onItemClickListener { (super.getAdapter() as ArticleAdapter).onNewArticleClick.invoke(it.title, it.link) }
    }

    override fun convert(helper: BaseViewHolder, item: Article) {
        helper.getView<RecyclerView>(R.id.mRvArticle).run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.onFlingListener = null
            StartSnapHelper().attachToRecyclerView(this)
            mAdapter.setList(item.newArticles)
            adapter = mAdapter
        }
    }
}

private class NormalArticleProvider(
    override val itemViewType: Int = HOME_TYPE_NORMAL_ARTICLE,
    override val layoutId: Int = R.layout.layout_item_article) : BaseItemProvider<Article>() {
    override fun convert(helper: BaseViewHolder, item: Article) {
        helper.run {
            getView<ImageView>(R.id.mIvIcon).loadCircle(IconHelper.randomAvatar(item.userId))

            setText(R.id.mTvName, item.getCurrentAuthor())

            setText(R.id.mTvTitle, item.title.fromHtml())

            setText(R.id.mTvDesc, item.desc.fromHtml())

            setText(R.id.mTvChapter,
                if (item.superChapterName.isNotEmpty()) "${item.superChapterName} · ${item.chapterName}"
                else item.chapterName)

            setText(R.id.mTvTime, item.niceDate)

            setGone(R.id.mIvPic, item.envelopePic.isEmpty())
            getView<ImageView>(R.id.mIvPic).loadRound(item.envelopePic, 5.dp2px)

        }
    }
}

private class NewArticleAdapter() : BaseQuickAdapter<Article, BaseViewHolder>(R.layout.layout_item_new_article_item_article) {
    override fun convert(holder: BaseViewHolder, item: Article) {
        holder.run {
            setGone(R.id.mItemBg, item.envelopePic.isEmpty())
            setGone(R.id.mItemCover, item.envelopePic.isEmpty())
            if (item.envelopePic.isNotEmpty()) {
                getView<ImageView>(R.id.mItemBg).loadRound(item.envelopePic, 8.dp2px)
                getView<ImageView>(R.id.mItemCover).loadRound(item.envelopePic, 8.dp2px)
            }
            setText(R.id.mItemTitle, item.title)

            setText(R.id.mItemDesc,
                when {
                    item.desc.isNotEmpty() -> item.desc.fromHtml()
                    item.superChapterName.isNotEmpty() -> "${item.superChapterName} · ${item.chapterName}"
                    else -> item.chapterName
                })

        }
    }
}


