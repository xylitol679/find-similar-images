import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.ocbg.xyz.apps.common.ext.dp2px

/**
 * @description: 两个Tab之间间距为0，左右边距可自定义
 * @param:
 * @return:
 */
fun TabLayout.setCustomSpacing(leftMarginDp: Int, rightMarginDp: Int) {
    val tabStrip = this.getChildAt(0) as ViewGroup
    for (i in 0 until tabStrip.childCount) {
        val tab = tabStrip.getChildAt(i)
        tab.setPadding(0, 0, 0, 0) // 设置每个 Tab 的 padding 为 0
        val layoutParams = tab.layoutParams as ViewGroup.MarginLayoutParams

        // 为第一个 Tab 设置左边距为指定的 dp 值
        if (i == 0) {
            layoutParams.leftMargin = leftMarginDp.dp2px.toInt()
        } else {
            layoutParams.leftMargin = 0
        }

        // 为最后一个 Tab 设置右边距为指定的 dp 值
        if (i == tabStrip.childCount - 1) {
            layoutParams.rightMargin = rightMarginDp.dp2px.toInt()
        } else {
            layoutParams.rightMargin = 0
        }

        tab.layoutParams = layoutParams
        tab.requestLayout()
    }
}