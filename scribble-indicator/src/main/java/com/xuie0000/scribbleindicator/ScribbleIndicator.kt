package com.xuie0000.scribbleindicator

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.floor

/**
 * https://www.sinasamaki.com/custom-tabrow-indicator-in-jetpack-compose/
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> ScribbleIndicator(
    list: List<T>,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState { list.size },
    title: @Composable ColumnScope.(item: T) -> Unit,
    content: @Composable (pagerState: PagerState) -> Unit,
) {
    Column(modifier = modifier) {
        val scope = rememberCoroutineScope()

        val sizeList = remember { mutableStateMapOf<Int, Pair<Float, Float>>() }

        val progressFromFirstPage by remember {
            derivedStateOf {
                pagerState.offsetForPage(0)
            }
        }

        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            edgePadding = 20.dp,
            containerColor = Color.Transparent,
            contentColor = Color(0xFF362C28),
            divider = {},
            indicator = {
                Box(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .fillMaxSize()
                        .drawBehind {
                            val ribbonSectionsLengths = mutableMapOf<Int, Float>()
                            var currentRibbonLength = 0f

                            var currentOrigin = 0f
                            val path = Path()

                            sizeList.keys
                                .sorted()
                                .mapNotNull { sizeList[it] }
                                .forEachIndexed { index, (width, height) ->

                                    val bottom = height - 10f
                                    val top = 10f

                                    if (index == 0) path.moveTo(0f, top)

                                    path.quadraticBezierTo(
                                        currentOrigin + width,
                                        top,
                                        currentOrigin + width,
                                        height / 2,
                                    )

                                    path.quadraticBezierTo(
                                        currentOrigin + width,
                                        bottom,
                                        currentOrigin + (width / 2),
                                        bottom,
                                    )

                                    path.quadraticBezierTo(
                                        currentOrigin + 0f,
                                        bottom,
                                        currentOrigin + 0f,
                                        height / 2,
                                    )

                                    path.quadraticBezierTo(
                                        currentOrigin,
                                        top,
                                        currentOrigin + width,
                                        top,
                                    )

                                    currentOrigin += width

                                    val measure = PathMeasure()
                                    measure.setPath(path, false)

                                    val length = measure.length
                                    ribbonSectionsLengths[index] = length - currentRibbonLength
                                    currentRibbonLength = length
                                }

                            val progress = progressFromFirstPage - floor(progressFromFirstPage)
                            val start = floor(progressFromFirstPage)
                                .toInt()
                                .coerceIn(0, ribbonSectionsLengths.size - 1)
                            val end = ceil(progressFromFirstPage)
                                .toInt()
                                .coerceIn(0, ribbonSectionsLengths.size - 1)

                            val ribbonLength =
                                ribbonSectionsLengths[start]!! + ((ribbonSectionsLengths[end]!! - ribbonSectionsLengths[start]!!) * progress)

                            val lengthUntilStart = ribbonSectionsLengths
                                .keys
                                .sorted()
                                .map { ribbonSectionsLengths[it] ?: 0f }
                                .take(start)
                                .fold(0f) { acc, it -> acc - it }

                            val lengthUntilEnd = ribbonSectionsLengths
                                .keys
                                .sorted()
                                .map { ribbonSectionsLengths[it] ?: 0f }
                                .take(end)
                                .fold(0f) { acc, it -> acc - it }

                            val phaseOffset =
                                lengthUntilStart + ((lengthUntilEnd - lengthUntilStart) * progress)

                            drawPath(
                                path = path,
                                color = Color(0xFF7A6151),
                                style = Stroke(
                                    width = 20f,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round,
                                    pathEffect = PathEffect.dashPathEffect(
                                        intervals = floatArrayOf(
                                            ribbonLength,
                                            currentRibbonLength,
                                        ),
                                        phase = phaseOffset,
                                    ),
                                ),
                            )
                        },
                )
            },
        ) {
            for (index in list.indices) {
                Tab(
                    selected = index == pagerState.currentPage,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    modifier = Modifier
                        .onSizeChanged {
                            sizeList[index] = Pair(it.width.toFloat(), it.height.toFloat())
                        },
                    interactionSource = remember { NoInteraction() },
                ) {
                    this.title(list[index])
                }
            }
        }

        content.invoke(pagerState)
    }
}

@Stable
private class NoInteraction : MutableInteractionSource {
    override val interactions: Flow<Interaction> = MutableSharedFlow()
    override suspend fun emit(interaction: Interaction) {}
    override fun tryEmit(interaction: Interaction) = false
}
