package de.juliando.app.android.ui.home.views

import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import de.juliando.app.android.ui.theme.CantineColors
import de.juliando.app.android.ui.theme.CantineTheme
import de.juliando.app.android.ui.theme.CantineTypography
import de.juliando.app.android.ui.theme.CantineTypography.Bodies.bodySmall
import de.juliando.app.android.ui.theme.CantineTypography.Bodies.bodySmallSelected
import de.juliando.app.models.objects.ui.Meal


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectBottomSheet(
    onDismiss: () -> Unit,
    meal: Meal
) {
    val modalSheetState = rememberModalBottomSheetState()
    val selectedList = remember { mutableStateMapOf<Int, Boolean>() }

    fun isSelected(index: Int): Boolean  {
       return (selectedList[index] ?: false)
    }

    fun update(index: Int) {
        val selected = isSelected(index)
        selectedList[index] = !selected
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = CantineColors.backgroundColor,
        sheetState = modalSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        meal.selections.forEach {

            // Headline of the selection
            Text(text = it.name, style = CantineTypography.Headlines.headlineMedium)


            // Sub-elements of the selection
            it.elements.forEachIndexed { index, selection ->
                val isSelected = isSelected(index)

                Row {
                    RadioButton(
                        selected = isSelected,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = CantineTheme.primaryColor,
                            unselectedColor = CantineTheme.grey2
                        ),
                        onClick = { update(index) }
                    )
                    Text(
                        text = selection.name,
                        style = if (isSelected) bodySmallSelected else bodySmall
                    )

                    if (isSelected) {
                        Text(
                            text = "${selection.price}",
                            style = bodySmall
                        )
                    }
                }


            }
        }
    }
}