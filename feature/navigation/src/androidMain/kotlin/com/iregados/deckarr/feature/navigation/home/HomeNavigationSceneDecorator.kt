package com.iregados.deckarr.feature.navigation.home

import androidx.compose.animation.EnterExitState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneDecoratorStrategy
import androidx.navigation3.scene.SceneDecoratorStrategyScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.iregados.deckarr.core.util.dto.Notification
import com.iregados.deckarr.feature.navigation.BottomNavTab
import kotlinx.coroutines.flow.SharedFlow

object TopLevelChromeKey : NavMetadataKey<BottomNavTab>

fun topLevelChrome(tab: BottomNavTab): Map<String, Any> = metadata {
    put(TopLevelChromeKey, tab)
}

@Composable
fun rememberHomeNavigationSceneDecoratorStrategy(
    onTabSelect: (BottomNavTab) -> Unit,
    onNavigate: (NavKey) -> Unit,
    notificationFlow: SharedFlow<Notification>,
    onRefresh: (BottomNavTab) -> Unit,
): SceneDecoratorStrategy<NavKey> {
    val currentOnTabSelect by rememberUpdatedState(onTabSelect)
    val currentOnNavigate by rememberUpdatedState(onNavigate)
    val currentOnRefresh by rememberUpdatedState(onRefresh)

    return remember(notificationFlow) {
        HomeNavigationSceneDecoratorStrategy(
            onTabSelect = { currentOnTabSelect(it) },
            onNavigate = { currentOnNavigate(it) },
            notificationFlow = notificationFlow,
            onRefresh = { currentOnRefresh(it) },
        )
    }
}

private class HomeNavigationSceneDecoratorStrategy(
    private val onTabSelect: (BottomNavTab) -> Unit,
    private val onNavigate: (NavKey) -> Unit,
    private val notificationFlow: SharedFlow<Notification>,
    private val onRefresh: (BottomNavTab) -> Unit,
) : SceneDecoratorStrategy<NavKey> {
    override fun SceneDecoratorStrategyScope<NavKey>.decorateScene(
        scene: Scene<NavKey>,
    ): Scene<NavKey> {
        val sceneTab = scene.entries.lastOrNull()?.metadata?.get(TopLevelChromeKey)
            ?: return scene

        return HomeChromeScene(
            scene = scene,
            sceneTab = sceneTab,
            onTabSelect = onTabSelect,
            onNavigate = onNavigate,
            notificationFlow = notificationFlow,
            onRefresh = onRefresh,
        )
    }
}

private class HomeChromeScene<T : Any>(
    private val scene: Scene<T>,
    private val sceneTab: BottomNavTab,
    private val onTabSelect: (BottomNavTab) -> Unit,
    private val onNavigate: (NavKey) -> Unit,
    private val notificationFlow: SharedFlow<Notification>,
    private val onRefresh: (BottomNavTab) -> Unit,
) : Scene<T> by scene {
    override val key: Any = scene::class to scene.key

    override val content: @Composable () -> Unit = {
        val animatedContentScope = LocalNavAnimatedContentScope.current
        val isTarget = animatedContentScope.transition.targetState == EnterExitState.Visible

        HomeNavigationRoot(
            activeTab = sceneTab,
            showSnackbar = isTarget,
            onTabSelect = onTabSelect,
            onNavigate = onNavigate,
            notificationFlow = notificationFlow,
            onRefresh = { onRefresh(sceneTab) },
            content = { scene.content() },
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HomeChromeScene<*>) return false
        return key == other.key &&
                sceneTab == other.sceneTab &&
                entries == other.entries &&
                previousEntries == other.previousEntries
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + sceneTab.hashCode()
        result = 31 * result + entries.hashCode()
        result = 31 * result + previousEntries.hashCode()
        return result
    }
}