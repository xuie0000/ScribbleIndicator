package com.xuie0000.scribbleindicator

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import kotlin.math.absoluteValue

// ACTUAL OFFSET
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.offsetForPage(page: Int): Float = (currentPage - page) + currentPageOffsetFraction

// OFFSET ONLY FROM THE LEFT
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.startOffsetForPage(page: Int): Float {
    return offsetForPage(page).coerceAtLeast(0f)
}

// OFFSET ONLY FROM THE RIGHT
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.endOffsetForPage(page: Int): Float {
    return offsetForPage(page).coerceAtMost(0f)
}

// NEW FUNCTION FOR INDICATORS
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.indicatorOffsetForPage(page: Int) =
    1f - offsetForPage(page).coerceIn(-1f, 1f).absoluteValue
