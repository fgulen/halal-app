package com.example.ui.screens

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.data.model.AppLanguage
import com.example.data.model.AppStrings
import com.example.ui.theme.HalalKontrolTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class BottomNavTest {

    @get:Rule val composeTestRule = createComposeRule()

    // The E-codes sheet opens over the home screen, so the E-codes tab must be the selected one while
    // it is open, and the last real screen must be selected again once it closes.
    @Test
    fun eCodesTabIsSelectedWhileSheetIsOpen() {
        assertEquals(E_CODES_NAV_INDEX, bottomNavSelectedIndex(isEAdditivesOpen = true, selectedScreenIndex = 0))
        assertEquals(E_CODES_NAV_INDEX, bottomNavSelectedIndex(isEAdditivesOpen = true, selectedScreenIndex = 1))
    }

    @Test
    fun lastRealScreenIsSelectedWhenSheetIsClosed() {
        assertEquals(0, bottomNavSelectedIndex(isEAdditivesOpen = false, selectedScreenIndex = 0))
        assertEquals(1, bottomNavSelectedIndex(isEAdditivesOpen = false, selectedScreenIndex = 1))
    }

    @Test
    fun tapNotifiesTheTappedIndexAndShowsSelection() {
        var tapped = -1
        composeTestRule.setContent {
            HalalKontrolTheme {
                NaturalBottomNavigation(
                    selectedIndex = 0,
                    language = AppLanguage.EN,
                    onSelectIndex = { tapped = it }
                )
            }
        }

        composeTestRule.onNodeWithText(AppStrings.getNavHome(AppLanguage.EN)).assertIsSelected()
        composeTestRule.onNodeWithText(AppStrings.getNavECodes(AppLanguage.EN)).assertIsNotSelected()

        composeTestRule.onNodeWithText(AppStrings.getNavECodes(AppLanguage.EN)).performClick()
        assertEquals(E_CODES_NAV_INDEX, tapped)
    }
}
