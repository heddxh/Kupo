package com.heddxh.kupo.util

/**
 * Currently, [versionNumber] only supports 3.0, 4.0, 5.0, 6.0, 2.x, 3.x, 3.y, 4.x, 5.x, 5.y, 6.x
 * TODO: 2.0 and side quests support will be added in the future.
 */
data class QuestsUtil(private val versionNumber: String) {
    //val isDotXVersion: Boolean = versionNumber.endsWith('x') or versionNumber.endsWith('y')
    val number: Int = when (versionNumber) {
        //"2.0" -> 215
        "3.0" -> 94
        "4.0" -> 122
        "5.0" -> 106
        "6.0" -> 108
        "2.x" -> 80
        "3.x" -> 25
        "3.y" -> 19
        "4.x" -> 40
        "5.x" -> 32
        "5.y" -> 19
        "6.x" -> 37
        else -> 0
    }
    val name: String = when (versionNumber) {
        //"2.0" -> "重生之境主线任务"
        "2.x" -> "第七星历主线任务"
        "3.0" -> "苍穹之禁城主线任务"
        "3.x" -> "龙诗战争终章主线任务"
        "3.y" -> "龙诗战争尾声主线任务"
        "4.0" -> "红莲之狂潮主线任务"
        "4.x" -> "解放战争战后主线任务"
        "5.0" -> "暗影之逆焰主线任务"
        "5.x" -> "拂晓回归主线任务"
        "5.y" -> "末日序曲主线任务"
        "6.0" -> "晓月之终途主线任务"
        "6.x" -> "崭新的冒险主线任务"
        else -> "库啵？"
    }

    companion object {

        fun isValidVersion(version: String): Boolean = validVersions().contains(version)

        fun validVersions(): List<String> {
            return listOf(
                //"2.0",
                "3.0",
                "4.0",
                "5.0",
                "6.0",
                "2.x",
                "3.x",
                "3.y",
                "4.x",
                "5.x",
                "5.y",
                "6.x",
            )
        }
    }
}

data class Quest(
    val id: Int = 282,
    val version: String = "5.0",
    val title: String = "地面冷若冰霜，天空遥不可及",
    val versionName: String = QuestsUtil(version).name,
)