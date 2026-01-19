package com.iregados.deckarr.feature.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.iregados.deckarr.feature.search.SearchType

@Composable
fun SearchTopSelector(
    searchType: SearchType,
    onSelect: (searchType: SearchType) -> Unit,
) {
    val iS1 = remember { MutableInteractionSource() }
    val iS2 = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable(
                    interactionSource = iS1,
                    indication = null
                ) { onSelect(SearchType.Movies) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = searchType == SearchType.Movies,
                onClick = { onSelect(SearchType.Movies) },
                interactionSource = iS1
            )
            Text(
                text = "Movies",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .clickable(
                    interactionSource = iS2,
                    indication = null
                ) { onSelect(SearchType.Series) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = searchType == SearchType.Series,
                onClick = { onSelect(SearchType.Series) },
                interactionSource = iS2
            )
            Text(
                text = "Series",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}